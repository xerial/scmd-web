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
		super("SCMDServer.config","SCMDServer.config���ǂ߂邩�ǂ����̃e�X�g");
	}

	public boolean test() {

        File configFile = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT) + "config/SCMDServer.config");

        //	�t�@�C�������݂��邩����
        if(!configFile.exists()) {
			setReport(configFile.getAbsoluteFile()+" is not exist.");
			return false;
        }
        //	�t�@�C�����ǂ������ׂ�
        if(!configFile.isFile()) {
			setReport(configFile.getAbsoluteFile()+" is not file.");
			return false;
        }
	
		//	��v�Ȑݒ肪�ǂ߂Ă��邩�ǂ�������
		if(SCMDConfiguration.getProperty(SCMDConfiguration.IMAGEMAGICK_CONVERT) == "") {
			setReport("IMAGEMAGICK_CONVERT�ɒl�������Ă��܂���");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.TABLEQUERY) == "") {
			setReport("TABLEQUERY�ɒl�������Ă��܂���");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.TEARDROP_URI) == "") {
			setReport("TEARDROP_URI�ɒl�������Ă��܂���");
			return false;
		}
		
		if(SCMDConfiguration.getProperty(SCMDConfiguration.VALUEQUERY) == "") {
			setReport("VALUEQUERY�ɒl�������Ă��܂���");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.XMLQUERY) == "") {
			setReport("XMLQUERY�ɒl�������Ă��܂���");
			return false;
		}

		if(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_PHOTO_URL) == "") {
			setReport("SCMD_PHOTO_URL�ɒl�������Ă��܂���");
			return false;
		}
		return true;
	}

}


//	-------------------------
//	$log: $
//	-------------------------