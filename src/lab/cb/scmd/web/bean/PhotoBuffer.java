//--------------------------------------
// SCMDServer
//
// PhotoBuffer.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

//import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PhotoBuffer {

    /**
     *  
     */
    public PhotoBuffer() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PhotoBuffer(String orf, int photoID, int stainType, int photoType) {
        try {
            Photo photo = new Photo(orf, photoID, stainType, photoType);
            //FileInputStream fin = new FileInputStream(new File(photo.getPhotoFilePath()));
            URL imageURL = photo.getPhotoURL();
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageURL.openStream());
            // Get jpeg image.
            imageBuffer = decoder.decodeAsBufferedImage();
            //imageBuffer = ImageIO.read(new File(photo.getPhotoFilePath()));
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getClipedImage(int x1, int x2, int y1, int y2)
    {
        return imageBuffer.getSubimage(x1, y1, x2-x1, y2-y1);
    }

    /**
     * @return Returns the imageBuffer.
     */
    public BufferedImage getImageBuffer() {
        return imageBuffer;
    }

    /**
     * @param imageBuffer
     *            The imageBuffer to set.
     */
    public void setImageBuffer(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    BufferedImage imageBuffer = null;

}

//--------------------------------------
// $Log: PhotoBuffer.java,v $
// Revision 1.3  2004/08/14 11:09:08  leo
// Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
// Revision 1.2  2004/08/12 19:20:48  leo
// 結果のサイズが０の時の対処を追加
//
// Revision 1.1  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
//--------------------------------------
