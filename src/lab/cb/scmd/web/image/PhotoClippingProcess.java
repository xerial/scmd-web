/**
 * 
 */
package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.viewer.Photo;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

/**
 * @author mattun
 *
 */
public class PhotoClippingProcess implements Runnable
{	
    TreeMap<String, BufferedImage> photoStorage = new TreeMap<String, BufferedImage>();
    TreeSet<String> unavailablePhotoIDSet = new TreeSet<String>();
    ImageCache imageCache = null;
    Collection<Cell> cellList = null;
    int photoType = PhotoType.ANALYZED_PHOTO;
    int stainTypeList[] = new int[] {}; 
                                  
    
    public PhotoClippingProcess(ImageCache imageCache, Collection<Cell> cellList, int photoType, int[] stainTypeList)
    {
        this.imageCache = imageCache;
        this.cellList = cellList;
        this.photoType = photoType;
        this.stainTypeList = stainTypeList;
    }
    
    /**
     * 
     */
    public void process()
    {
        Thread thread = new Thread(this);
        thread.start();
    }
        
    
    public void run()
    {
        for(Cell cell : cellList)
        {
            Photo photo = cell.getPhoto();
            for(int stainType : stainTypeList)
            {
                String photoID = photo.getImageID(photoType, stainType);
                BufferedImage photoImage = null;
                if(photoStorage.containsKey(photoID))
                {
                    photoImage = photoStorage.get(photoID);
                }
                else
                {
                    if(!unavailablePhotoIDSet.contains(photoID))
                    {
                        try
                        {
                            // load image
                            URL imageURL = photo.getPhotoURL(photoType, stainType);
                            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageURL.openStream());
                            // Get jpeg image.
                            photoImage = decoder.decodeAsBufferedImage();
                            photoStorage.put(photoID, photoImage);
                        }
                        catch (InvalidParameterException e)
                        {
                            imageCache.setAsNotAvailable(photoID);
                            continue;
                        }
                        catch (MalformedURLException e)
                        {
                            imageCache.setAsNotAvailable(photoID);
                            continue;
                        }
                        catch(IOException e)
                        {
                            imageCache.setAsNotAvailable(photoID);
                            continue;
                        }                                
                    }
                    else
                        continue; // skip the cell in this photooo
                }

                // clip the cell 
                BoundingRectangle br = cell.getBoundingRectangle();
                int x1 = br.getX1();
                int x2 = br.getX2();
                int y1 = br.getY1();
                int y2 = br.getY2();
                int borderSize = 2;
                int xRange = x2 - x1 + borderSize * 2;
                int yRange = y2 - y1 + borderSize * 2;
                int xBegin = x1 < borderSize ? 0 : x1 - borderSize;
                int yBegin = y1 < borderSize ? 0 : y1 - borderSize;

                BufferedImage cellImage = photoImage.getSubimage(xBegin, yBegin, xRange, yRange);                          
                imageCache.addImage(cell.getImageID(photoType, stainType), cellImage);                             
            } // for stainType
        } // for cell
        
        
        
        // ŒãŽn––
        photoStorage.clear();
        unavailablePhotoIDSet.clear();
    }
}