package lab.cb.scmd.web.test;

import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class WebTestRunner {
	Vector<WebTestCase> list = new Vector<WebTestCase>(); 

	/**
	 * �e�X�g���������̂�ǉ�����
	 * @param test
	 */
	public void addTest(WebTestCase test) {
		list.add(test);
	}

	public Vector<WebTestCase> getTestCaseList() {
		return list;
	}

	public void run() {
		for(WebTestCase testcase : list) {
			try{
				testcase.init();
				testcase.setIsError(!testcase.test());
				testcase.destory();
			} catch(Exception e) {
				//	�G���[���N���Ă���������悤��Exception���L���b�`���Ă���
				testcase.setIsError(true);
				testcase.setReport(e);
			}
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------