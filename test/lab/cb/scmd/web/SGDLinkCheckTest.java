package lab.cb.scmd.web;

import junit.framework.TestCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * SGDからのリンクがきちんと動いているかどうかのテスト
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class SGDLinkCheckTest extends TestCase {
	String TestORFList[] = {"YOR202W","YAL002w","YAL004w"};
	String SGDUrl = "http://scmd.gi.k.u-tokyo.ac.jp/viewer/photo/viewer.cgi?gid=";
	String SCMDUrl = "http://scmd.gi.k.u-tokyo.ac.jp/datamine/ViewPhoto.do?orf=";

	protected void setUp() throws Exception {
		
	}

	public void testSGDLinkCehck() throws Exception {
		WebConversation wc = new WebConversation();		

		for(String orf : TestORFList) {
			WebRequest request  = new GetMethodWebRequest(SGDUrl+orf);		
			WebResponse response = wc.getResource(request);
	
			System.out.println("code:"+response.getResponseCode());
			System.out.println("mes :"+response.getLinkWith("here").getURLString());
			assertEquals(SGDUrl+orf,response.getLinkWith("here").getURLString(),SCMDUrl+orf);
		}
	}
	
}


//	-------------------------
//	$log: $
//	-------------------------