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
import java.util.LinkedList;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Imageを保存しておくクラス
 * @author leo
 *
 */
public class ImageCache 
{
    public final static String IMAGA_CACHE = "imageCache"; 
    
    private enum ImageStatus { not_ready, ready, not_available, removable} 
    private TreeMap<String, BufferedImage> cache = new TreeMap<String, BufferedImage>();
    private TreeMap<String, ImageStatus> imageRegistory = new TreeMap<String, ImageStatus>();
    
    public ImageCache() 
    {
        super();
    }
    
    synchronized public void addImage(String imageID, BufferedImage image)
    {
        cache.put(imageID, image);
        imageRegistory.put(imageID, ImageStatus.ready);
        notifyAll();
    }
    synchronized public void registerImage(String imageID)
    {
        imageRegistory.put(imageID, ImageStatus.not_ready);
        notifyAll();
    }
    synchronized public void setAsNotAvailable(String imageID)
    {
        imageRegistory.put(imageID, ImageStatus.not_available);
        notifyAll();
    }
    
    synchronized public BufferedImage getImage(String imageID)
    {
        BufferedImage image = null;
        
        if(imageRegistory.containsKey(imageID))
        {
            switch(imageRegistory.get(imageID))
            {
            case not_ready:
                try
                {
                    int loopTimes = 0;
                    while(imageRegistory.get(imageID) == ImageStatus.not_ready && imageRegistory.get(imageID) != null)
                    {
                        wait(10 * 1000); // timeout 10秒
                    }
                    return getImage(imageID);
                }
                catch(InterruptedException e)
                {
                    // image is not available
                }
                return null;
            case removable:
                return cache.get(imageID);
            case ready:
                image = cache.get(imageID);
            case not_available:
                break;
            }
        }

        setAsRemovable(imageID);
        notifyAll();
        return image;
    }
    
    private void setAsRemovable(String imageID)
    {
        //cache.remove(imageID);
        imageRegistory.put(imageID, ImageStatus.removable);
    }
    
    synchronized void recallImages()
    {
        LinkedList<String> removeList = new LinkedList<String>();
        
        // 単純にすべてクリアするようにしてみた
        imageRegistory.clear();
        cache.clear();
        
        /*
        for(String imageID : imageRegistory.keySet())
        {
            ImageStatus status = imageRegistory.get(imageID);
            switch(status)
            {
            case not_available:
            case removable:
                removeList.add(imageID);
            default:
                continue;
            }
        }
        for(String imageID : removeList)
        {
            imageRegistory.remove(imageID);
            cache.remove(imageID);
        }
        */
    }
    
    /**
     * 現在のrequestに応じたImageCacheを提供する
     * @param request
     * @return
     */
    static public ImageCache getImageCache(HttpServletRequest request)
    {
        HttpSession session = request.getSession(true);
        
        ImageCache imageCache = (ImageCache) session.getAttribute(IMAGA_CACHE);
        if(imageCache == null)
        {
            imageCache = new ImageCache();
            session.setAttribute(IMAGA_CACHE, imageCache);
        }
                
        return imageCache;
    }
    
    /** 現在のrequestに応じたImageCacheに画像を追加する
     * @param request
     * @param imageID
     * @param image
     */
    static public void addImage(HttpServletRequest request, String imageID, BufferedImage image)
    {
        ImageCache imageCache = getImageCache(request);
        imageCache.addImage(imageID, image);
    }

    
    static public class ImageRecallProcess implements Runnable
    {
        private ImageCache imageCache;
        private int sec = 30; 
        
        public ImageRecallProcess(ImageCache imageCache, int sec)
        {
            this.imageCache = imageCache;
            this.sec = sec;
        }
        
        // @see java.lang.Runnable#run()
        public void run()
        {
            synchronized(this)
            {
                try
                {
                    wait(sec * 1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                imageCache.recallImages();
            }
            System.gc();
        }
    }
}


