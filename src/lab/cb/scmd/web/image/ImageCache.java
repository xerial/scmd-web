//--------------------------------------
// SCMDWeb Project
//
// ImageCache.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.db.common.HttpSessionDB;

/**
 * Imageを保存しておくクラス
 * 
 * @author leo
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 * 
 */
public class ImageCache implements Serializable {
	private enum ImageStatus {
		//	準備中, 準備完了, 利用できない, 削除できる画像
		NOT_AVAILABLE, NOT_READY, READY, REMOVABLE
	}

	private TreeMap<String, BufferedImage> cache = new TreeMap<String, BufferedImage>();
	private TreeMap<String, ImageStatus> imageRegistory = new TreeMap<String, ImageStatus>();

	public final static String IMAGA_CACHE = "imageCache";


	/**
	 * 現在のrequestに応じたImageCacheに画像を追加する
	 * 
	 * @param request
	 * @param imageID
	 * @param image
	 */
	static public void addImage(HttpServletRequest request, String imageID,
			BufferedImage image) {
		ImageCache imageCache = getImageCache(request);
		imageCache.addImage(imageID, image);
	}

	/**
	 * HttpSessionからImageCacheを取り出す。 ない場合は生成してHttpSessionに保存する
	 * 
	 * @param request
	 * @return
	 */
	static public ImageCache getImageCache(HttpServletRequest request) {
		// HttpSession session = request.getSession(true);
		HttpSession session = new HttpSessionDB(request);

		ImageCache imageCache = (ImageCache) session.getAttribute(IMAGA_CACHE);
		if (imageCache == null) {
			imageCache = new ImageCache();
			session.setAttribute(IMAGA_CACHE, imageCache);
		}

		return imageCache;
	}

	public ImageCache() {
		super();
	}

	/**
	 * imageIDをキーとしてimageを保存する
	 * 
	 * @param imageID
	 * @param image
	 */
	synchronized public void addImage(String imageID, BufferedImage image) {
		cache.put(imageID, image);
		imageRegistory.put(imageID, ImageStatus.READY);
		notifyAll();
	}

	/**
	 * imageIDキーに登録されているBufferedImageを取り出す
	 * 
	 * @param imageID
	 * @return
	 */
	synchronized public BufferedImage getImage(String imageID) {
		BufferedImage image = null;

		// キーが存在するか見る
		if (imageRegistory.containsKey(imageID)) {
			switch (imageRegistory.get(imageID)) {
			//	準備中の場合は状態が変化するまで待つ
			case NOT_READY:
				try {
					int loopTimes = 0;
					// NOT_READYな間はずっとループ
					while (imageRegistory.get(imageID) == ImageStatus.NOT_READY
							&& imageRegistory.get(imageID) != null) {
						wait(10 * 1000); // timeout 10秒
					}
					return getImage(imageID);
				} catch (InterruptedException e) {
					// image is not available
				}
				return null;
			case REMOVABLE:
				return cache.get(imageID);
			case READY:
				image = cache.get(imageID);
			case NOT_AVAILABLE:
				break;
			}
		}

		setAsRemovable(imageID);
		notifyAll();
		return image;
	}

	/**
	 * すべてを初期化
	 */
	synchronized void recallImages() {
		LinkedList<String> removeList = new LinkedList<String>();

		// 単純にすべてクリアするようにしてみた
		imageRegistory.clear();
		cache.clear();

		/*
		 * for(String imageID : imageRegistory.keySet()) { ImageStatus status =
		 * imageRegistory.get(imageID); switch(status) { case not_available:
		 * case removable: removeList.add(imageID); default: continue; } }
		 * for(String imageID : removeList) { imageRegistory.remove(imageID);
		 * cache.remove(imageID); }
		 */
	}

	/**
	 * imageIDに登録されている状態をNOT_READYにする
	 * 
	 * @param imageID
	 */
	synchronized public void registerImage(String imageID) {
		imageRegistory.put(imageID, ImageStatus.NOT_READY);
		notifyAll();
	}

	/**
	 * imageIDに登録されている状態をNOT_AVAILABLEにする
	 * 
	 * @param imageID
	 */
	synchronized public void setAsNotAvailable(String imageID) {
		imageRegistory.put(imageID, ImageStatus.NOT_AVAILABLE);
		notifyAll();
	}

	/**
	 * imageIDに登録されている状態をREMOVABLEにする
	 * 
	 * @param imageID
	 */
	private void setAsRemovable(String imageID) {
		// cache.remove(imageID);
		imageRegistory.put(imageID, ImageStatus.REMOVABLE);
	}
	
	/**
	 * 監視して指定秒後にrecallImagesを呼ぶ
	 * 
	 * @author mattun
	 * @version $Revision$ $LastChangedDate$ $LastChangedBy$
	 */
	static public class ImageRecallProcess implements Runnable {
		private ImageCache imageCache;

		private int sec = 30;

		public ImageRecallProcess(ImageCache imageCache, int sec) {
			this.imageCache = imageCache;
			this.sec = sec;
		}

		// @see java.lang.Runnable#run()
		public void run() {
			synchronized (this) {
				try {
					wait(sec * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				imageCache.recallImages();
			}
			System.out.println("[ImageRecallProcess]: invoke gc");
			System.gc();
		}
	}
}
