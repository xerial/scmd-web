package lab.cb.scmd.web.test;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.db.common.HttpSessionDB;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class HttpSessionDBWebTest extends WebTestCase {
	HttpServletRequest request = null;
	HttpSession session = null;
	public HttpSessionDBWebTest(HttpServletRequest request) {
		super("HttpSessionDB","HttpSessionDB���g���Ă��邩�e�X�g����");
		this.request = request;
	}
	
	/* (non-Javadoc)
	 * @see lab.cb.scmd.web.test.WebTestCase#init()
	 */
	@Override
	public void init() {
		//	HttpSessionDB�̏���
		session = new HttpSessionDB(request,true);
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.web.test.WebTestCase#test()
	 */
	@Override
	public boolean test() {
		Random rnd = new Random(new Date().getTime());
		String rand = String.valueOf(rnd.nextInt()); 
		session.setAttribute("HttpSessionDBWebTest",rand);
		String string = (String)session.getAttribute("HttpSessionDBWebTest");

		if(string == null || !string.equals(rand)) {
			setReport("setAttribute�Ɏ��s���܂���");
			return false;
		}

		return true;
	}

}


//	-------------------------
//	$log: $
//	-------------------------