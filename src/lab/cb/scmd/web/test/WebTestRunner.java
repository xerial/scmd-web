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
	 * テストしたいものを追加する
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
				//	エラーが起きても続けられるようにExceptionをキャッチしておく
				testcase.setIsError(true);
				testcase.setReport(e);
			}
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------