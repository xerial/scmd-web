/**
 * 
 */
package lab.cb.scmd.db.connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lab.cb.scmd.db.sql.SqlManager;
import lab.cb.scmd.db.sql.SqlQuery;
import lab.cb.scmd.util.table.AppendableTable;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.log.ErrorLog;
import lab.cb.scmd.web.log.SCMDLogging;
import lab.cb.scmd.web.table.Table;
import net.jp.peace.db.ConnectionPool;

/**
 * コネクションプールに外部ライブラリーを使用
 * @author mattun
 * @version $Revision: 810 $ $LastChangedDate: 2005-11-10 16:52:52 +0900 $
 * $LastChangedBy: matsumiya $
 */
public class DBManager implements SCMDManagerModel{
	ConnectionPool pooling;
	private SqlManager sqlManager = null;
	
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.connect.SCMDManagerModel#initialize()
	 */
	public boolean initialize() {
		String uri = "";

		//	データベース接続の準備
		String driver = SCMDConfiguration.getProperty("DB_DRIVER").trim();
		String database = SCMDConfiguration.getProperty("DB_TYPE").trim();
		String host = SCMDConfiguration.getProperty("DB_HOST", "localhost").trim();
		int port = Integer.parseInt(SCMDConfiguration.getProperty("DB_PORT", "5432").trim());
		String db = SCMDConfiguration.getProperty("DB_NAME", "scmd").trim();
		String user = SCMDConfiguration.getProperty("DB_USER", "").trim();
		String pass = SCMDConfiguration.getProperty("DB_PASSWORD", "").trim(); 
		int maxconnection = Integer.parseInt(SCMDConfiguration.getProperty("DB_MAXCONNECTION","10").trim());

        //	種類を変えてJDBC-URIを作成
		if(database.equals("pgsql")) {
			uri = ConnectionPool.createUrlPgSQL(host,port,db);
		} else if(database.equals("mysql")) {
			uri = ConnectionPool.createUrlMysql(host,port,db);
		} else {
			//	エラー処理をする
		}

		//	TODO このExceptionはinitializeでスルーさせるようにする
		//	データベースプーリングを作成する
		try{
			pooling = new ConnectionPool(driver,uri,user,pass,maxconnection);
		} catch(ClassNotFoundException e) {
			ErrorLog.insert("JDBCドライバーが存在しません",e.getMessage(),e);
		}
		//	SQLマネージャーを作成する
		try{
			System.out.println(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("DB_SQLCONFIGPATH"));
			sqlManager = new SqlManager(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("DB_SQLCONFIGPATH").trim(),"");
		} catch( Exception e) {
			return false;
		}
		return true;
	}

	public boolean restart() {
		destory();
		return initialize();
	}
	
	public void destory() {
		try{
			pooling.close();
			pooling = null;
		} catch(SQLException sqle) {
			
		}
	}
/*
	public int update(String name,HashMap<String,String> map ) throws SQLException{
		SqlQuery query = sqlManager.getSqlQuery(name);
		return query.update(name,map);
	}
	*/
    /**
	 * 
	 * SQLManagerを使用してSQLを発行する
	 * 戻り値が必要ないINSERTやUPDATEの場合に使用する
	 * @param name
	 * @param objs
	 */
	public int executeUpdate(String sql) throws SQLException{
		Connection con = pooling.getPoolConnection();
		int updateline = 0;
		try{
			Statement stmt = con.createStatement();
			updateline = stmt.executeUpdate(sql);
	    	SCMDLogging.file(sql,java.util.logging.Level.INFO);
	    } catch(SQLException sqe) {
			sqlErrorLog(sqe);
		} finally {
			con.close();
		}
		return updateline;
	}
	/**
	 * PreparedStatementを使用するので？が使える
	 * @param sql　ＳＱＬ文章
	 * @param param ？に対するパラメータ
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate(String sql,Object ...params) throws SQLException{
		Connection con = pooling.getPoolConnection();
		int updateline = 0;
		try{
			sqlManager.executeUpdate(con,sql,params);
	    } catch(SQLException sqe) {
			sqlErrorLog(sqe);
		} finally {
			con.close();
		}
		return updateline;
	}
    /**
	 * 
	 * SQLManagerを使用してSQLを発行する
	 * 戻り値が必要ないINSERTやUPDATEの場合に使用する
	 * @param name
	 * @param objs
	 */
	public int update(String name,HashMap<String,String> map) throws SQLException{
		Connection con = pooling.getPoolConnection();
		SqlQuery query = sqlManager.getSqlQuery(name,con) ;
		int updateline = 0;
		try{
			//	SQL文章を生成
			updateline = query.update(map);
	    } catch(SQLException sqe) {
			sqlErrorLog(sqe);
		} finally {
			con.close();
		}
		return updateline;
	}
	public int update(String name,Object bean) throws SQLException{
		Connection con = pooling.getPoolConnection();
		SqlQuery query = sqlManager.getSqlQuery(name,con) ;
		int updateline = 0;
		try{
			//	SQL文章を生成
			updateline = query.update(bean);
	    } catch(SQLException sqe) {
			sqlErrorLog(sqe);	    	
		} finally {
			con.close();
		}
		return updateline;
	}

	/**
	 * StatementのexecuteQueryと同じ動作をする
	 * 直接SQL文章を書きたい場合に使う
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet queryExecute(String sql) throws SQLException {
		Connection con = pooling.getPoolConnection();
		Statement stmt = con.createStatement();
		try{
			return stmt.executeQuery(sql);
		} finally {
			con.close();
		}
	}
	
    /**
     * @param <T>
     * @param name SQLXMLに記述された名前
     * @param map 置換する変数
     * @param cast クラスを指定されるとそこのSetterにデータを入れてListで返す
     * @return　１つのクラス
     * @throws SQLException
     */
    public <T> T query(String name,HashMap<String,String> map,Class<T> cast) throws SQLException{
    	Connection con = pooling.getPoolConnection();

    	List<T> result = null;
    	try{
	    	SqlQuery query = sqlManager.getSqlQuery(name,con);
	    	result = query.queryResults(map,cast);
    	} catch(SQLException sqe) {
			sqlErrorLog(sqe);
    		throw sqe;
		} finally {
			con.close();
		}

    	return result.get(0);
    }

    /**
     * 指定したカラム名のオブジェクトが返る
     * DBでINTEGER型だと JAVAではINTEGER型
     * DBでTEXT型だと JAVAではString型となる
     * @param name
     * @param map
     * @param column
     * @return
     * @throws SQLException
     */
    public Object queryScalar(String name,HashMap<String,String> map,int column) throws SQLException{
    	Connection con = pooling.getPoolConnection();
    	try{
	    	SqlQuery query = sqlManager.getSqlQuery(name,con);
	    	return query.queryScalar(map,column);
    	} catch(SQLException sqe) {
			sqlErrorLog(sqe);
    		throw sqe;
		} finally {
			con.close();
		}
		//return null;
    }
    public Object queryScalar(String name,HashMap<String,String> map,String column) throws SQLException{
    	Connection con = pooling.getPoolConnection();
    	try{
	    	SqlQuery query = sqlManager.getSqlQuery(name,con);
	    	return query.queryScalar(map,column);
    	} catch(SQLException sqe) {
			sqlErrorLog(sqe);
    		throw sqe;
		} finally {
			con.close();
		}
		//return null;
    }
    /**
     * @param <T> キャスト
     * @param name SQLXMLに記述された名前
     * @param map 置換する変数
     * @param cast クラスを指定されるとそこのSetterにデータを入れてListで返す
     * @return キャストされたList
     * @throws SQLException
     */
	public <T> List<T> queryResults(String name,HashMap<String,String> map,Class<T> cast) throws SQLException{
		Connection con = pooling.getPoolConnection();
		List<T> result = null;
		try{
			SqlQuery query = sqlManager.getSqlQuery(name,con);
			result = query.queryResults(map,cast);
		} catch(SQLException sqe) {
			sqlErrorLog(sqe);
			throw sqe;
		} finally {
			con.close();
		}
		return result;
	}

	/**
	 * QUERYXMLファイルからSQLを発行しTableで帰ってくる
	 * @param name
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public Table queryTable(String name,HashMap<String,String> map) throws SQLException{
		Connection con = pooling.getPoolConnection();
		Table table = null;
		try{
			SqlQuery query = sqlManager.getSqlQuery(name,con);
			String sql = query.createSQLMarge(map);
			table = convertTable(con.createStatement().executeQuery(sql));
			//result = query.queryBean(map,cast);
		} catch(SQLException sqe) {
			sqlErrorLog(sqe);
			throw sqe;
		} finally {
			con.close();
		}
		return table;
	}

	/**
	 * queryTableと同じでBasicTableが帰ってくる
	 * @param name
	 * @param map
	 * @param keyColumnName
	 * @return
	 * @throws SQLException
	 */
	public BasicTable queryBasicTable(String name,HashMap<String,String> map,String keyColumnName) throws SQLException{
		Connection con = pooling.getPoolConnection();
		BasicTable table = null;
		try{
			SqlQuery query = sqlManager.getSqlQuery(name,con);
			String sql = query.createSQLMarge(map);
			table = convertBasicTable(con.createStatement().executeQuery(sql),keyColumnName);
			//result = query.queryBean(map,cast);
		} catch(SQLException sqe) {
			sqlErrorLog(sqe);
			throw sqe;
		} finally {
			con.close();
		}
		return table;
	}
	/**
	 * 直接SQLでQueryを発行しBasicTableが帰ってくる
	 * @param sql
	 * @param keyColumnName
	 * @return
	 * @throws SQLException
	 */
	public BasicTable queryBasicTable(String sql,String keyColumnName) throws SQLException{
		Connection con = pooling.getPoolConnection();
		BasicTable table = null;
		try{
			table = convertBasicTable(con.createStatement().executeQuery(sql),keyColumnName);
			//result = query.queryBean(map,cast);
		} catch(SQLException sqe) {
			sqlErrorLog(sqe);
			throw sqe;
		} finally {
			con.close();
		}
		return table;
	}
	/**
	 * ResultSetからデータを取り出しTableクラスに変換する
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static Table convertTable(ResultSet rs) throws SQLException{
        Table table = new Table();
        
        ResultSetMetaData metadata = rs.getMetaData();
        Vector<String> columnLabel = new Vector<String>(metadata.getColumnCount());
        for(int i=1; i<=metadata.getColumnCount(); i++)
        {
            String columnName = metadata.getColumnName(i);
            columnLabel.add(columnName);
        }
        table.addRow(columnLabel);
        
        while(rs.next())
        {
            Vector<String> row = new Vector<String>(metadata.getColumnCount());
            for(int i=1; i<=metadata.getColumnCount(); i++)
            {
                row.add(rs.getString(i));
            }
            table.addRow(row);
        }
        return table;
	}

	/**
	 * ResultSetからデータを取り出しBasicTableクラスに変換する
	 * @param rs
	 * @param keyColumnName
	 * @return
	 * @throws SQLException
	 */
	protected static BasicTable convertBasicTable(ResultSet rs,String keyColumnName) throws SQLException{
		AppendableTable at = null;            
		ResultSetMetaData metaData = rs.getMetaData();
		int numberOfColumns =  metaData.getColumnCount();
		String[] columnName = new String [numberOfColumns];
		int keyColumn = -1;
		for( int i = 0; i < numberOfColumns; i++ ) {
			columnName[i] = metaData.getColumnLabel(i + 1);
			if( keyColumnName.equals(metaData.getColumnLabel(i + 1))) {
				keyColumn = i;
			}
		}
		int curcol = 0;
		int collength = 0;
		if( keyColumn < 0 ) {
			collength = numberOfColumns;
		} else {
			collength = numberOfColumns + 1;
			curcol = 1;
		}

		if( keyColumn < 0)
			at = new AppendableTable("SQL Result", columnName); // without
		// rowlabel
		else
			at = new AppendableTable("SQL Result", columnName, true); // with
		// rowlabel

		while( rs.next()){
			String[] row = new String[collength];
			if( keyColumn >= 0 )
				row[0] = rs.getString(columnName[keyColumn]);
			for( int i = 0; i < numberOfColumns; i++ ) {
				row[i + curcol] = rs.getString(columnName[i]);
			}
			at.append(row);
		}
		return at;
	}
	
	private void sqlErrorLog(SQLException e) {
    	ErrorLog.insert(e.getMessage(),"データーベースエラー",e);
	}
}


//	-------------------------
//	$log: $
//	-------------------------