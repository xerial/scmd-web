package lab.cb.scmd.web.servlet.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.SCMDDBConnect;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.table.Table;

/**
 * @todo Photo画像のキャッシュを持っておく
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class PhotoClipImageServer extends HttpServlet{
	//	HashMapよりFastHashMapを使用したほうがいい？
	protected static LinkedHashMap<String,BufferedImage> cache = new LinkedHashMap<String,BufferedImage>();
	protected static SCMDDBConnect scmdconnect = new SCMDDBConnect();
	protected static int CACHE_SIZE = 1000;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String encoding = request.getParameter("encoding");
		String imageID = request.getParameter("imageID");

		if(cache.containsKey(imageID)) {
//			System.out.println("キャッシュから画像を出力します");
		} else {
			String split[] = imageID.split("_");
			String strainname = null;
			int photo_type = -1;
			int stain_type = -1;
			int image_number = -1;
			int cell_local_id = -1;
	
			strainname = split[1];
			photo_type = Integer.parseInt(split[2]);
			stain_type = Integer.parseInt(split[3]);
			image_number = Integer.parseInt(split[4]);
			cell_local_id = Integer.parseInt(split[5]);
			BufferedImage img = null;
			try{
				img = ImageIO.read(getPhotoURL(strainname,stain_type,image_number,photo_type,cell_local_id));
			} catch(IIOException e) {
		//		e.printStackTrace();
			}
			if(img == null) {
				System.out.println("Not found " + getPhotoURL(strainname,stain_type,image_number,photo_type,cell_local_id));
				response.setStatus(404);
				return;
			} else {
				//	キャッシュサイズがいっぱいの場合は最後にキャッシュしたデータを消す
				if(cache.size() > CACHE_SIZE) {
					cache.remove(cache.keySet().iterator().next());
				}
				//	キャッシュに保存する
				cache.put(imageID,img);
			}
		}
		ImageIO.write(cache.get(imageID),encoding,response.getOutputStream());
	}
	static URL getPhotoURL(String strainname, int stainType, int image_number,
			int photoType,int cell_local_id) throws MalformedURLException {

		String params[] = {"x1","y1","x2","y2"};

		Table table = null;
		//	SQL発行
		try{
			table = scmdconnect.getDataSheet(strainname.toUpperCase(), image_number, cell_local_id,params);
		} catch(InvalidSQLException e) {
			
		}
		if (table.getRowSize() == 2 ) {
			URL photoDIRURL = new URL(SCMDConfiguration
					.getProperty("SCMD_PHOTO_DIR_URL"));
			String photoPath;
			if (photoType == PhotoType.ORIGINAL_PHOTO) {
				List list = table.getRowList(1);

				StringBuilder path = new StringBuilder();
				path.append("/");
				path.append("half_photo_clips");
				path.append("/");
				path.append(strainname);
				path.append("/");
				path.append(strainname);
				path.append("-");
				path.append(StainType.STAIN_TYPE[stainType]);
				path.append(image_number);
				path.append("_");
				path.append(list.get(0));
				path.append("_");
				path.append(list.get(1));
				path.append("_");
				path.append(list.get(2));
				path.append("_");
				path.append(list.get(3));
				path.append(".jpg");
				photoPath = path.toString();
			} else {
				List list = table.getRowList(1);

				StringBuilder path = new StringBuilder();
				path.append("/");
				path.append("analyzed_photo_clips");
				path.append("/");
				path.append(strainname);
				path.append("/");
				path.append(strainname);
				path.append("-");
				path.append(StainType.IMAGE_NAME[stainType]);
				path.append(image_number);
				path.append("_");
				path.append(list.get(0));
				path.append("_");
				path.append(list.get(1));
				path.append("_");
				path.append(list.get(2));
				path.append("_");
				path.append(list.get(3));
				path.append(".jpg");
				photoPath = path.toString();
//				photoPath = "analyzed_photo_clips" + "/" + strainname + "/" + strainname + "-"
//						+ StainType.IMAGE_NAME[stainType] + image_number + ".jpg";
			}
			return new URL(photoDIRURL+photoPath);
		} else return null;
	}
}


//	-------------------------
//	$log: $
//	-------------------------