package lab.cb.scmd.db.connect;
import java.sql.Connection;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;
import net.jp.peace.db.ConnectionPool;

/**
 *　外部ライブラリーのコネクションプーリングをテストする
 * これは接続するだけのテストなのでSQLテストしたい場合はSQLManagerTestへ
 * 
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class DBPoolingTest extends TestCase {
	private static ConnectionPool pool = null;
	int MAX_CONNECTION = 30;	//	最大コネクション
	int THREAD_SIZE = 10;		//	THREAD_SIZE分の同時接続テスト
	int ACCESS_SIZE = 10;		//	１スレッドが繰り返す量
	String error = null;

	protected void setUp() throws Exception {
		//	これってSCMDServer.configが見えないとなぁ
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
	 * コネクションプーリングのテスト
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
			System.out.println("開始します=["+Thread.currentThread().getName()+"]");
			Random rnd = new Random(new Date().getTime());
			try{
				//	0-1000nsの待ち時間を作って重なるようにする
				sleep(Math.abs(rnd.nextInt())%1000);
				for(int i=0;i<ACCESS_SIZE;i++){
					Connection con = pool.getPoolConnection();

					if(con.isClosed()) {
						error = "コネクションが切れたか。コネクションが取れていないようです";
						return;
					}
					con.close();
				}
			} catch(Exception e) {
				error = e.getMessage();
				return;
			}
			System.out.println("終了します=["+Thread.currentThread().getName()+"]");
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------