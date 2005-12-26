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
	 * ���b�N�ƊJ���ɓ����I�u�W�F�N�g���K�v
	 * @param lockobj
	 */
	public Mutex(int max,int timeout) {
		maxsize = max;
		count = 0;
		this.timeout = timeout;
	}
	
	/**
	 * �I�[�o�[���[�N����
	 * ����maxsize�ȏ�̏���������ꍇ�͈ꎞ��~����
	 */
	public void lock() {
		//	����MAX�T�C�Y�ȏ㒴���Ă����ꍇ��wait������
		synchronized (this) {
			if(count > maxsize) {
				try {
					if(this.timeout != 0) {
						System.out.println("�I�[�o�[���[�N�̂��߈ꎞ��~");
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
	 * ���ԂɃ��b�N����������
	 */
	public synchronized void unlock() {
		notify();
		count--;
	}
}


//	-------------------------
//	$log: $
//	-------------------------