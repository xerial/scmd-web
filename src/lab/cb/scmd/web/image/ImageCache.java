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
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ImageÇï€ë∂ÇµÇƒÇ®Ç≠ÉNÉâÉX
 * @author leo
 *
 */
public class ImageCache 
{
    public final static String IMAGA_CACHE = "imageCache"; 
    
    private enum ImageStatus { not_ready, ready, not_available } 
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
                    while(imageRegistory.get(imageID) == ImageStatus.not_ready)
                    {
                        wait(30000); // timeout 30ïb
                    }
                    if(imageRegistory.get(imageID) == ImageStatus.ready)
                        image = cache.get(imageID);
                    else
                    {
                        // not available
                    }
                }
                catch(InterruptedException e)
                {
                    // image is not available
                }
                break;
            case ready:
                image = cache.get(imageID);
                break;
            case not_available:
                break;        
            }
        }

        removeEntry(imageID);
        return image;
    }
    
    private void removeEntry(String imageID)
    {
        cache.remove(imageID);
        imageRegistory.remove(imageID);
    }
    
    /**
     * åªç›ÇÃrequestÇ…âûÇ∂ÇΩImageCacheÇíÒãüÇ∑ÇÈ
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
    
    /** åªç›ÇÃrequestÇ…âûÇ∂ÇΩImageCacheÇ…âÊëúÇí«â¡Ç∑ÇÈ
     * @param request
     * @param imageID
     * @param image
     */
    static public void addImage(HttpServletRequest request, String imageID, BufferedImage image)
    {
        ImageCache imageCache = getImageCache(request);
        imageCache.addImage(imageID, image);
    }
    


}


