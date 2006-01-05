package lab.cb.scmd.web;

import java.io.IOException;

import junit.framework.TestCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class PageSpeedTest extends TestCase {
	final int ERROR_CODES[] = {403,404,500,503};
	int MAX_RESPONSETIME = 30;	//	もし30秒の間に取得できない場合は通知

	static String SERVERURL="http://localhost:8080/scmd-server";
	static String CheckURLS[] = {
			"/ViewORFList.do",
			"/ParameterHelp.do",
			"/ViewPhoto.do?orf=YAL004w",
			"/ViewDataSheet.do?orf=YAL004w",
			"/ViewGroupBySheet.do?stainType=0&orf=YAL004w",
			"/ViewGroupBySheet.do?stainType=1&orf=YAL004w",
			"/SelectShape.do",
			"/ORFTeardrop.do?orf=YAL004w",
			"/View2DPlot.do?orfType=current",
			"/ViewStats.do?orf=YAL004w",
			"/ViewGroupByTearDrop.do?stainType=0&orf=YAL004w",
			"/ViewGroupByTearDrop.do?stainType=1&orf=YAL004w",
			"/ViewGroupByTearDrop.do?stainType=2&orf=YAL004w",
			"/ViewSelection.do",
			"/CustomizeView.do",
			"/Search.do?keyword=rad52",
			"/photo.img?encoding=jpeg&imageID=C_yal004w_1_0_1_0",
			"/teardrop.png?encoding=png&paramid=221",
			"/ViewSelection.do",
			"/CustomizeView.do",
			"/ParameterHelp.do",
			"/publication.jsp",
			"/about.jsp",
			"/sitemap.jsp",
			"/download.jsp",
			"/calmorph/"
	};

	public void testSpeed() {
		try{
			WebConversation wc = new WebConversation();
	
			for(String url : CheckURLS) {
				WebRequest request  = new GetMethodWebRequest(SERVERURL+url);
				long startTime = System.currentTimeMillis();
				WebResponse response = wc.getResource(request);
				long stopTime = System.currentTimeMillis();

				System.out.println( "ResponseCode:" + response.getResponseCode() + " URL: " +response.getURL().toString() +" "+ (stopTime - startTime) + "ns");

				//	ページの表示でエラーが出ているかチェック
				assertEquals(url,response.getResponseCode(),200);
				assertTrue(url,(stopTime - startTime) < MAX_RESPONSETIME*1000);
			}
		} catch(IOException e) {
			assertTrue("IOException",true);
		}

	}
}


//	-------------------------
//	$log: $
//	-------------------------