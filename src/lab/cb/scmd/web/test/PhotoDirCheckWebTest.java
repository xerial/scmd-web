package lab.cb.scmd.web.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import lab.cb.scmd.db.connect.SCMDManager;
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
		super("PhotoDirCheck","写真のディレクトリーとファイルが存在するか確認");
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
				setReport("Teadropの画像ファイルが見つかりませんでした");
				return false;
			}
		} catch(MalformedURLException e) {
			setReport("TEARDROP_URIのアドレスがおかしいです。");
			return false;
		} catch(IOException e) {
			setReport(e);
			return false;
		}

		//	PARAMFIG_URIをチェック
		try{
			URL url = new URL(SCMDConfiguration.getProperty("PARAMFIG_URI")+"/A101.png");
			BufferedImage img = ImageIO.read(url);
			if(img == null) {
				setReport("Paramfigの画像ファイルが見つかりませんでした");
				return false;
			}
		} catch(MalformedURLException e) {
			setReport("PARAMFIG_URIのアドレスがおかしいです。");
			return false;
		} catch(IOException e) {
			setReport(e);
			return false;
		}
		
		try {
			URL url = new URL(SCMDConfiguration.getProperty("SCMD_PHOTO_DIR_URL"));
			File file = new File(url.getFile());
			if(!file.isDirectory()) {
				setReport("SCMD_PHOTO_DIR_URLのアドレスにディレクトリが存在しません");
				return false;
			}
			//	analyzed_photoをチェック
			if(!checkDir(file.getAbsolutePath(),"analyzed_photo")) {
				return false; 
			}
			//	analyzed_photo_clipsをチェック
			if(!checkDir(file.getAbsolutePath(),"analyzed_photo_clips")) {
				return false; 
			}
			//	データベースからORF名をすべて取り出す
			try{
				ResultSet rs = SCMDManager.getDBManager().queryExecute("SELECT systematicname FROM (SELECT systematicname,primaryname,aliasname,annotation FROM genename_20040719) AS genetable INNER JOIN analysisdata_20050131 ON genetable.systematicname = analysisdata_20050131.strainname ORDER BY systematicname");
				if(rs == null) {
					setReport("SQLの発行に失敗しました");
					return false;
				}
				while(rs.next()) {
					if(!checkDir(file.getAbsolutePath()+"/analyzed_photo",rs.getString("systematicname").toLowerCase())) {
						return false; 
					}
					if(!checkDir(file.getAbsolutePath()+"/half_photo",rs.getString("systematicname").toLowerCase())) {
						return false; 
					}
					if(!checkDir(file.getAbsolutePath()+"/analyzed_photo_clips",rs.getString("systematicname").toLowerCase())) {
						return false; 
					}
					if(!checkDir(file.getAbsolutePath()+"/half_photo_clips",rs.getString("systematicname").toLowerCase())) {
						return false; 
					}
				}
			} catch(SQLException e) {
				
			}
		} catch(MalformedURLException e) {
			setReport("SCMD_PHOTO_DIR_URLのアドレスがおかしいです。");
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
			setReport(dir+"/"+foldername+"のディレクトリが存在しません");
			return false;
		}
		return true;
	}

}


//	-------------------------
//	$log: $
//	-------------------------