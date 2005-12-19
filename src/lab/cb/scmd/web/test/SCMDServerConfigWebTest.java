package lab.cb.scmd.web.test;

import java.io.File;
import java.util.Set;

import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class SCMDServerConfigWebTest extends WebTestCase {
	public SCMDServerConfigWebTest() {
		super("SCMDServer.config","SCMDServer.configが読めるかどうかのテスト");
	}

	public boolean test() {

        File configFile = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT) + "config/SCMDServer.config");

        //	ファイルが存在するか見る
        if(!configFile.exists()) {
			setReport(configFile.getAbsoluteFile()+" is not exist.");
			return false;
        }
        //	ファイルかどうか調べる
        if(!configFile.isFile()) {
			setReport(configFile.getAbsoluteFile()+" is not file.");
			return false;
        }
	
		//	主要な設定が読めているかどうか見る
		if(SCMDConfiguration.getProperty(SCMDConfiguration.IMAGEMAGICK_CONVERT) == "") {
			setReport("IMAGEMAGICK_CONVERTに値が入っていません");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.TABLEQUERY) == "") {
			setReport("TABLEQUERYに値が入っていません");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.TEARDROP_URI) == "") {
			setReport("TEARDROP_URIに値が入っていません");
			return false;
		}
		
		if(SCMDConfiguration.getProperty(SCMDConfiguration.VALUEQUERY) == "") {
			setReport("VALUEQUERYに値が入っていません");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.XMLQUERY) == "") {
			setReport("XMLQUERYに値が入っていません");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_PHOTO_URL) == "") {
			setReport("SCMD_PHOTO_URLに値が入っていません");
			return false;
		}
		return true;
	}

}


//	-------------------------
//	$log: $
//	-------------------------