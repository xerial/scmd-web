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
		super("DataBaseTest","�f�[�^�x�[�X�ڑ��̃e�X�g");
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

		//	�f�[�^�x�[�X�ڑ��̏���
		String driver = SCMDConfiguration.getProperty("DB_DRIVER").trim();
		String database = SCMDConfiguration.getProperty("DB_TYPE").trim();
		String host = SCMDConfiguration.getProperty("DB_HOST", "localhost").trim();
		int port = Integer.parseInt(SCMDConfiguration.getProperty("DB_PORT", "5432").trim());
		String db = SCMDConfiguration.getProperty("DB_NAME", "scmd").trim();
		String user = SCMDConfiguration.getProperty("DB_USER", "").trim();
		String pass = SCMDConfiguration.getProperty("DB_PASSWORD", "").trim(); 
		int maxconnection = Integer.parseInt(SCMDConfiguration.getProperty("DB_MAXCONNECTION","10").trim());

        //	��ނ�ς���JDBC-URI���쐬
		if(database.equals("pgsql")) {
			uri = ConnectionPool.createUrlPgSQL(host,port,db);
		} else if(database.equals("mysql")) {
			uri = ConnectionPool.createUrlMysql(host,port,db);
		} else {
			//	�G���[����������
			setReport("DB_TYPE��pgsql,mysql�ȊO�����͂���Ă��܂�");
			return false;
		}


		//	�f�[�^�x�[�X�v�[�����O�쐬�e�X�g
		try{
			pooling = new ConnectionPool(driver,uri,user,pass,maxconnection);
		} catch(ClassNotFoundException e) {
			setReport("JDBC�h���C�o�[�����݂��܂���");
			return false;
		}

		try{
			pooling.getConnection();
			pooling.getPoolConnection();
		} catch(SQLException e) {
			setReport("�f�[�^�x�[�X�֐ڑ����ł��Ă��Ȃ��悤�ł�");
			return false;
		}

		File scmdsqlFile = new File(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("DB_SQLCONFIGPATH"));
		if(!scmdsqlFile.isFile()) {
			setReport(scmdsqlFile.isAbsolute()+"�t�@�C�������݂��܂���");
			return false;
		}

		//	SQL�}�l�[�W���[���쐬����
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