package lab.cb.scmd.web;

import junit.framework.TestCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class SearchKeyTest extends TestCase{
    String serverURL = "http://localhost:8080/scmd-server/Search.do?keyword=";
    String testKeywords[] = {"rad52","YAL014c","FUN3"};

	protected void setUp() throws Exception {
	}

	/**
	 * http://localhost:8080/scmd-server/Search.doのテスト
	 * @throws Exception
	 */
	public void testSearchKey() throws Exception {
		WebConversation wc = new WebConversation();		

		for(String url : testKeywords) {
			url = serverURL+url;
			WebRequest request  = new GetMethodWebRequest(url);
			WebResponse response = wc.getResource(request);
			//	レスポンスが200番を返すかどうかとHTMLであるかどうか
	        assertTrue(url,response.isHTML());
	        assertEquals(url,response.getResponseCode(),200);
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------