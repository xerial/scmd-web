//--------------------------------------
// SCMDWeb Project
//
// ConnectionServer.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.xerial.util.Pair;

import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.web.common.ConfigObserver;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.Table;

/**
 * JDBCのコネクションをsemarphoreの形で複数保持し、供給するクラス。Singleton。
 * 使用前には必ずConnectionServer.initialize()を実行しておくこと
 * 
 * 使い方は、
 * <pre>
 *    ConnectionServer.query("select * from ... ", new BeanHandler(SomeBean.class)); 
 * </pre>
 * など。ResultSetHandlerの部分は、Jakarta Commons のDBUtilsで定義されているもの。
 * @author leo
 *
 */
public class ConnectionServer implements ConfigObserver
{
    static private ConnectionServer _instance = null;
    private int _maxConnection = 10;
    private LinkedList<Connection> _connectionHolder = new LinkedList<Connection>();
    private QueryRunner _queryRunner = new QueryRunner();

    /** SQLを実行して結果をTableに格納して返す
     * @param sql $1, $2, ...という変数を含むＳＱＬ文
     * @param args 引数に代入する値
     * @return lab.cb.web.table.Table クラスのインスタンス
     * @throws SQLException
     */
    static public Table retrieveTable(String sql, Object... args) throws SQLException
    {
        return retrieveTable(new SQLExpression(sql).assign(args));
    }
    /** SQLを実行して結果をTableに格納して返す
     * @param sql ＳＱＬ文
     * @return lab.cb.web.table.Table クラスのインスタンス
     * @throws SQLException
     */
    static public Table retrieveTable(String sql) throws SQLException
    {
        return (Table) _instance.execQuery(sql, new TableConverter()); 
    }
    
    static private class TableConverter implements ResultSetHandler
    {
        public Object handle(ResultSet resultSet) throws SQLException
        {
            Table table = new Table();
            
            ResultSetMetaData metadata = resultSet.getMetaData();
            Vector<String> columnLabel = new Vector<String>(metadata.getColumnCount());
            for(int i=1; i<=metadata.getColumnCount(); i++)
            {
                String columnName = metadata.getColumnName(i);
                columnLabel.add(columnName);
            }
            table.addRow(columnLabel);
            
            while(resultSet.next())
            {
                Vector<String> row = new Vector<String>(metadata.getColumnCount());
                for(int i=1; i<=metadata.getColumnCount(); i++)
                {
                    row.add(resultSet.getString(i));
                }
                table.addRow(row);
            }
            return table;
        }
        
    }
    
    
    /**
     * DBへの問い合わせを行う
     * @param sql SQL文
     * @param rsh ResultSetHandler
     * @return ResultSetHandlerによって加工されたObject
     * @throws SQLException
     */
    static public Object query(String sql, ResultSetHandler rsh) throws SQLException
    {
        return _instance.execQuery(sql, rsh);
    }
    /**
     * DBへの問い合わせを行う
     * @param rsh ResultSetHandler
     * @param sqlWithVariable SQL文の一部が、$1, $2, ...という変数に置き換えられたもの
     * @param sqlArg $1, $2, ...に代入する値
     * @return ResultSetHandlerによって加工されたObject
     * @throws SQLException
     */
    static public Object query(ResultSetHandler rsh, String sqlWithVariable, Object... sqlArg) throws SQLException
    {
        return _instance.execQuery(new SQLExpression(sqlWithVariable).assign(sqlArg), rsh);
    }
    
    protected Object execQuery(String sql, ResultSetHandler rsh) throws SQLException
    {
        try
        {
            Connection connection = null;
            // achieve a connection
            synchronized(this)
            {
                while(_connectionHolder.isEmpty())
                {
                    wait(60000);  // timeout １分
                }
                connection = _connectionHolder.poll();
            }
            Object result = _queryRunner.query(connection, sql, rsh);
            // release connection
            synchronized(this)
            {
                _connectionHolder.add(connection);
                notify();
            }
            return result;
        }
        catch(InterruptedException e)
        {
            // connectionの取得待ちが中断された場合
            return null;
        }
    }
    
    
    
    
    /**
     * ConnectionServerを初期化する。プログラムで使用前に一度だけ実行する
     */
    static public void initialize() throws SQLException
    {
        if(_instance != null)
            _instance.closeAll(); //  後始末
        
        _instance = new ConnectionServer();
        SCMDConfiguration.addObserver(_instance);
    }
    
    static public void dispose() 
    {
        if(_instance != null)
            _instance.closeAll();
    }
    
    /**
     * 
     */
    private ConnectionServer() throws SQLException
    {
       prepareDataSource(); 
    }
    
    
    // @see java.lang.Object#finalize()
    protected void finalize() throws Throwable
    {
        closeAll();
        super.finalize();
    }
    
    protected void prepareDataSource() throws SQLException
    {
        closeAll();

        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace(System.out);
            return;
        }

        
        String jdbcConnectionURI = "jdbc:postgresql://";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_IP", "localhost").trim() + ":";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_PORT", "5432").trim() + "/";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_DBNAME", "scmd").trim();
        
        String user = SCMDConfiguration.getProperty("POSTGRESQL_USER", "postgres");
        String pass = SCMDConfiguration.getProperty("POSTGRESQL_PASSWORD", ""); 
        for(int i=0; i<_maxConnection; i++)
        {
            Connection con = DriverManager.getConnection(jdbcConnectionURI, user, pass);
            _connectionHolder.add(con);
        }
    }
    
    // @see lab.cb.scmd.web.common.ConfigObserver#reloaded()
    public void reloaded()
    {
        // SCMDConfigurationのreloadを検知
        closeAll();
        try
        {
            prepareDataSource();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    protected void closeAll()
    {
        for(Connection con : _connectionHolder)
        {
            try
            {
                con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        _connectionHolder.clear();
    }
}




