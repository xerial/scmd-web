package lab.cb.scmd.web.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class PhotoDirCheckWebTest extends WebTestCase {
	/**
	 * 
	 */
	public PhotoDirCheckWebTest() {
		super("PhotoDirCheck","�ʐ^�̃f�B���N�g���[�ƃt�@�C�������݂��邩�m�F");
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.web.test.WebTestCase#test()
	 */
	@Override
	public boolean test() {

		try{
			URL url = new URL(SCMDConfiguration.getProperty(SCMDConfiguration.TEARDROP_URI)+"/td_111_10.png");
			BufferedImage img = ImageIO.read(url);
			if(img == null) {
				setReport("Teadrop�̉摜�t�@�C����������܂���ł���");
				return false;
			}
		} catch(MalformedURLException e) {
			setReport("TEARDROP_URI�̃A�h���X�����������ł��B");
			return false;
		} catch(IOException e) {
			setReport(e);
			return false;
		}

		//	PARAMFIG_URI���`�F�b�N
		try{
			URL url = new URL(SCMDConfiguration.getProperty("PARAMFIG_URI")+"/A101.png");
			BufferedImage img = ImageIO.read(url);
			if(img == null) {
				setReport("Paramfig�̉摜�t�@�C����������܂���ł���");
				return false;
			}
		} catch(MalformedURLException e) {
			setReport("PARAMFIG_URI�̃A�h���X�����������ł��B");
			return false;
		} catch(IOException e) {
			setReport(e);
			return false;
		}
		
		try {
			URL url = new URL(SCMDConfiguration.getProperty("SCMD_PHOTO_DIR_URL"));
			File file = new File(url.getFile());
			if(!file.isDirectory()) {
				setReport("SCMD_PHOTO_DIR_URL�̃A�h���X�Ƀf�B���N�g�������݂��܂���");
				return false;
			}
			//	analyzed_photo���`�F�b�N
			if(!checkDir(file.getAbsolutePath(),"analyzed_photo")) {
				return false; 
			}
			//	analyzed_photo_clips���`�F�b�N
			if(!checkDir(file.getAbsolutePath(),"analyzed_photo_clips")) {
				return false; 
			}
		} catch(MalformedURLException e) {
			setReport("SCMD_PHOTO_DIR_URL�̃A�h���X�����������ł��B");
			return false;
		} catch(IOException e) {
			setReport(e);
			return false;
		}
		
		return true;
	}
	
	public boolean checkDir(String dir,String foldername) {
		File file  = new File(dir+"/"+foldername);
		if(!file.isDirectory()) {
			setReport(dir+"/"+foldername+"�̃f�B���N�g�������݂��܂���");
			return false;
		}
		return true;
	}

}


//	-------------------------
//	$log: $
//	-------------------------