package lab.cb.scmd.web.util;

import java.util.Vector;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class Mutex {
	Vector list = new Vector();
	int maxsize;
	int count;
	int timeout;

	/**
	 * ロックと開放に同じオブジェクトが必要
	 * @param lockobj
	 */
	public Mutex(int max,int timeout) {
		maxsize = max;
		count = 0;
		this.timeout = timeout;
	}
	
	/**
	 * オーバーワーク検査
	 * もしmaxsize以上の処理をする場合は一時停止する
	 */
	public void lock() {
		//	もしMAXサイズ以上超えていた場合はwaitかける
		synchronized (this) {
			if(count > maxsize) {
				try {
					if(this.timeout != 0) {
						System.out.println("オーバーワークのため一時停止");
						wait(this.timeout);
					}
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			count++;
		}
	}
	
	/**
	 * 順番にロックを解除する
	 */
	public synchronized void unlock() {
		notify();
		count--;
	}
}


//	-------------------------
//	$log: $
//	-------------------------