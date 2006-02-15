package lab.cb.scmd.web;

import java.io.IOException;

import junit.framework.TestCase;
import lab.cb.scmd.GlobalConfigure;

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
public class PhotoCheckTest extends TestCase {
	String checkPhotoList[] = {
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=0&stainType=0",
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=0&stainType=1",
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=0&stainType=2",
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=1&stainType=0",
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=1&stainType=1",
			"/DisplayPhoto.do?magnification=50&orf=YAL002w&photoNum=1&photoType=1&stainType=2",
			"/ViewPhoto.do?orf=yal002w",
			"/photo.img?encoding=jpeg&imageID=C_yal002w_0_0_1_0",
			"/photo.img?encoding=jpeg&imageID=C_yal002w_1_0_1_0",
			"/DrawTeardrop.do?groupID=1&orientation=horizontal&paramID=31&plotTargetORF=true&value=",
			"/teardrop.png?encoding=png&paramid=221",
	};

	public void testPhotoCheck() {
		try{
			WebConversation wc = new WebConversation();
	
			for(String url : checkPhotoList) {
				WebRequest request  = new GetMethodWebRequest(GlobalConfigure.SERVER_URL+url);
				long startTime = System.currentTimeMillis();
				WebResponse response = wc.getResource(request);
				long stopTime = System.currentTimeMillis();

				System.out.println( "ResponseCode:" + response.getResponseCode() + " URL: " +response.getURL().toString() +" "+ (stopTime - startTime) + "ns");

				//	ページの表示でエラーが出ているかチェック
				assertEquals(url,response.getResponseCode(),200);
			}
		} catch(IOException e) {
			assertTrue("IOException",true);
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------