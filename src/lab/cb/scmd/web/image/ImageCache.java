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
 * Image��ۑ����Ă����N���X
 * 
 * @author leo
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 * 
 */
public class ImageCache implements Serializable {
	private enum ImageStatus {
		//	������, ��������, ���p�ł��Ȃ�, �폜�ł���摜
		NOT_AVAILABLE, NOT_READY, READY, REMOVABLE
	}

	private TreeMap<String, BufferedImage> cache = new TreeMap<String, BufferedImage>();
	private TreeMap<String, ImageStatus> imageRegistory = new TreeMap<String, ImageStatus>();

	public final static String IMAGA_CACHE = "imageCache";


	/**
	 * ���݂�request�ɉ�����ImageCache�ɉ摜��ǉ�����
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
	 * HttpSession����ImageCache�����o���B �Ȃ��ꍇ�͐�������HttpSession�ɕۑ�����
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
	 * imageID���L�[�Ƃ���image��ۑ�����
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
	 * imageID�L�[�ɓo�^����Ă���BufferedImage�����o��
	 * 
	 * @param imageID
	 * @return
	 */
	synchronized public BufferedImage getImage(String imageID) {
		BufferedImage image = null;

		// �L�[�����݂��邩����
		if (imageRegistory.containsKey(imageID)) {
			switch (imageRegistory.get(imageID)) {
			//	�������̏ꍇ�͏�Ԃ��ω�����܂ő҂�
			case NOT_READY:
				try {
					int loopTimes = 0;
					// NOT_READY�ȊԂ͂����ƃ��[�v
					while (imageRegistory.get(imageID) == ImageStatus.NOT_READY
							&& imageRegistory.get(imageID) != null) {
						wait(10 * 1000); // timeout 10�b
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
	 * ���ׂĂ�������
	 */
	synchronized void recallImages() {
		LinkedList<String> removeList = new LinkedList<String>();

		// �P���ɂ��ׂăN���A����悤�ɂ��Ă݂�
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
	 * imageID�ɓo�^����Ă����Ԃ�NOT_READY�ɂ���
	 * 
	 * @param imageID
	 */
	synchronized public void registerImage(String imageID) {
		imageRegistory.put(imageID, ImageStatus.NOT_READY);
		notifyAll();
	}

	/**
	 * imageID�ɓo�^����Ă����Ԃ�NOT_AVAILABLE�ɂ���
	 * 
	 * @param imageID
	 */
	synchronized public void setAsNotAvailable(String imageID) {
		imageRegistory.put(imageID, ImageStatus.NOT_AVAILABLE);
		notifyAll();
	}

	/**
	 * imageID�ɓo�^����Ă����Ԃ�REMOVABLE�ɂ���
	 * 
	 * @param imageID
	 */
	private void setAsRemovable(String imageID) {
		// cache.remove(imageID);
		imageRegistory.put(imageID, ImageStatus.REMOVABLE);
	}
	
	/**
	 * �Ď����Ďw��b���recallImages���Ă�
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
