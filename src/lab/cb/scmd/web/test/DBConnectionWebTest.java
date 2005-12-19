package lab.cb.scmd.web.test;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import lab.cb.scmd.db.sql.SqlManager;
import lab.cb.scmd.web.common.SCMDConfiguration;
import net.jp.peace.db.ConnectionPool;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class DBConnectionWebTest extends WebTestCase{
	/**
	 * 
	 */
	public DBConnectionWebTest() {
		super("DataBaseTest","データベース接続のテスト");
	}

	ConnectionPool pooling = null;

	public void destory() {
		try {
			if(pooling != null)
				pooling.close();
		} catch(SQLException e) {
			
		}
	}

	public boolean test() {
		String uri="";

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
			setReport("DB_TYPEにpgsql,mysql以外が入力されています");
			return false;
		}


		//	データベースプーリング作成テスト
		try{
			pooling = new ConnectionPool(driver,uri,user,pass,maxconnection);
		} catch(ClassNotFoundException e) {
			setReport("JDBCドライバーが存在しません");
			return false;
		}

		try{
			pooling.getConnection();
			pooling.getPoolConnection();
		} catch(SQLException e) {
			setReport("データベースへ接続ができていないようです");
			return false;
		}

		File scmdsqlFile = new File(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("DB_SQLCONFIGPATH"));
		if(!scmdsqlFile.isFile()) {
			setReport(scmdsqlFile.isAbsolute()+"ファイルが存在しません");
			return false;
		}

		//	SQLマネージャーを作成する
		try{
			SqlManager sqlManager = new SqlManager(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("DB_SQLCONFIGPATH").trim(),"");
		} catch( Exception e) {
			setReport(e);
			return false;
		}
		return true;
	}
}


//	-------------------------
//	$log: $
//	-------------------------