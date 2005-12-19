//--------------------------------------
// SCMDServer
// 
// CellImageServer.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.servlet.image;

//import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

//import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.web.bean.SingleCell;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 *  
 */
public class CellImageServer extends ImageServer 
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SingleCell  cell = (SingleCell) request.getAttribute("cell");
        if(cell == null)
        {
            throw new ServletException("SingleCell (cell) is not found in the request");
        }
        OutputStream out = response.getOutputStream();

        try
		{
            Photo photo = new Photo(cell.getOrf(), cell.getPhotoNum(), cell.getStainType(), cell.getPhotoType());
            URL photoURL = photo.getPhotoURL();

          	ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
            _imageConverter.clipImage(new PrintStream(response.getOutputStream()), photoURL,  cell.getBoundingRectangle(), 100, 2);
			response.setContentType("image/jpeg");
			out.write(bufferOut.toByteArray());

			/*
            BufferedImage origPhoto = ImageIO.read(photoURL);
            BoundingRectangle rect = cell.getBoundingRectangle();
            int[] subImageRange = rect.getBox(2);
            BufferedImage clippedImage = origPhoto.getSubimage(subImageRange[0], subImageRange[1], subImageRange[2], subImageRange[3]);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
            encoder.encode(clippedImage);
            */
		}
        catch(IOException e)
		{
        	log(e.getMessage());
        	printNAimage(request, response);
		}
        catch(SCMDException e)
        {
            e.what(System.out);
            printNAimage(request, response);
        }
        
    }
    
    protected void printNAimage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
    	File photoPath = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT), "/image/notavailable.png");
    	response.setContentType("image/png");
    	request.getRequestDispatcher("/png/na_small.png").forward(request, response);
    }
}

//--------------------------------------
// $Log: CellImageServer.java,v $
// Revision 1.9  2004/09/09 02:19:07  leo
// clippingはimagemagickの方が高速のよう
//
// Revision 1.8  2004/09/09 02:15:10  leo
// bufferedimageで、clippingをするようにしてみた
//
// Revision 1.7  2004/08/24 06:57:43  leo
// commentの削除
//
// Revision 1.6  2004/08/14 12:34:50  leo
// cellの表示を修正
//
// Revision 1.5  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.4  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
//--------------------------------------
