package lab.cb.scmd.db.connect;
import java.sql.Connection;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;
import net.jp.peace.db.ConnectionPool;

/**
 *�@�O�����C�u�����[�̃R�l�N�V�����v�[�����O���e�X�g����
 * ����͐ڑ����邾���̃e�X�g�Ȃ̂�SQL�e�X�g�������ꍇ��SQLManagerTest��
 * 
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class DBPoolingTest extends TestCase {
	private static ConnectionPool pool = null;
	int MAX_CONNECTION = 30;	//	�ő�R�l�N�V����
	int THREAD_SIZE = 10;		//	THREAD_SIZE���̓����ڑ��e�X�g
	int ACCESS_SIZE = 10;		//	�P�X���b�h���J��Ԃ���
	String error = null;

	protected void setUp() throws Exception {
		//	�������SCMDServer.config�������Ȃ��ƂȂ�
		String user = "postgres";
		String pass = "";
		String database = "scmd";
		String host = "localhost";

		if(pool!=null)
			pool.close();

		pool = null;
		pool = new ConnectionPool("org.postgresql.Driver",ConnectionPool.createUrlPgSQL(host,5432,database),user,pass,MAX_CONNECTION);
	}
	
	/**
	 * �R�l�N�V�����v�[�����O�̃e�X�g
	 */
	public void testPooling() {
		PoolThread pt = new PoolThread();
		String e = pt.Start();
		assertNull(e,e);
	}

	protected void tearDown() throws Exception {
		pool.close();
	}

	class PoolThread extends Thread {
		public synchronized String Start() {
			try {
				PoolThread m[] = new PoolThread[THREAD_SIZE];
				for(int i=0;i<m.length;i++){
					m[i] = new PoolThread();
					m[i].start();
				}
				for(int i=0;i<m.length;i++){
					m[i].join();
				}
		    } catch(Exception ex) {
		    	ex.printStackTrace();
		    }
			try{
				pool.close();
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
			return error;
		}
		
		public void run() {
			System.out.println("�J�n���܂�=["+Thread.currentThread().getName()+"]");
			Random rnd = new Random(new Date().getTime());
			try{
				//	0-1000ns�̑҂����Ԃ�����ďd�Ȃ�悤�ɂ���
				sleep(Math.abs(rnd.nextInt())%1000);
				for(int i=0;i<ACCESS_SIZE;i++){
					Connection con = pool.getPoolConnection();

					if(con.isClosed()) {
						error = "�R�l�N�V�������؂ꂽ���B�R�l�N�V���������Ă��Ȃ��悤�ł�";
						return;
					}
					con.close();
				}
			} catch(Exception e) {
				error = e.getMessage();
				return;
			}
			System.out.println("�I�����܂�=["+Thread.currentThread().getName()+"]");
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------