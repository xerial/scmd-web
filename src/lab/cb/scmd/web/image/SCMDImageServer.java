//--------------------------------------
// SCMDWeb Project
//
// SCMDImageServer.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 画像をあらかじめimageCacheというsessionオブジェクトに計算して蓄えておき、
 * sessionのowner(ページを見てる人）にのみ提供するクラス
 * imageIDと、encodingをセットする必要がある
 * @author leo
 *
 */
public class SCMDImageServer extends HttpServlet
{    
    /**
     * 
     */
    public SCMDImageServer()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ImageCache imageCache = ImageCache.getImageCache(request);
        
        String imageID = request.getParameter("imageID");
        if(imageID == null)
            return; 
        
        String imageEncoding = request.getParameter("encoding");
        if(imageEncoding == null)
            imageEncoding = "jpeg";
        else
            if(!(imageEncoding.equals("jpeg") || imageEncoding.equals("png")))
                return;
        
        BufferedImage image = imageCache.getImage(imageID);
        if(image == null)
        {
            System.err.println("[scmd-server] cannot get " + imageID);
            return;
        }
        
        try
        {
            response.setContentType("image/" + imageEncoding);
            if(imageEncoding.equals("jpeg"))
            {
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
                JPEGEncodeParam encodeParam = encoder.getDefaultJPEGEncodeParam(image);
                //encodeParam.setQuality(1.0f, false);
                encoder.encode(image, encodeParam);
            }
            else if(imageEncoding.equals("png"))
            {
                ImageIO.write(image, "png", response.getOutputStream());                
            }
            response.getOutputStream().flush();
        }
        catch(IOException e)
        {
            log(e.getMessage());
        }
        

    }
    
}

 