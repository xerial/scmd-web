//--------------------------------------
// SCMDServer
//
// BufferedCellImageServer.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.PhotoBuffer;

/**
 * @author leo
 *
 */
public class BufferedCellImageServer extends HttpServlet
{

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        
        HttpSession session = request.getSession(true);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
        {
            // TODO return black image
            throw new ServletException("unknown view");
        }
        
        PhotoBuffer[] photoBuffer = view.getPhotoBuffer();
        
        int x1 = Integer.parseInt((String) request.getParameter("x1"));
        int x2 = Integer.parseInt((String) request.getParameter("x2"));
        int y1 = Integer.parseInt((String) request.getParameter("y1"));
        int y2 = Integer.parseInt((String) request.getParameter("y2"));
        int borderSize = 2;
        x1 = x1 < borderSize ? 0 : x1 - borderSize; 
        x2 += borderSize; 
        y1 = y1 < borderSize ? 0 : y1 - borderSize; 
        y2 += borderSize; 

        int stainType = Integer.parseInt((String) request.getParameter("stainType"));
        
        BufferedImage clippedImage = photoBuffer[stainType].getClipedImage(x1, x2, y1, y2);
        
        try
        {
        	response.setContentType("image/jpeg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
            JPEGEncodeParam encodeParam = encoder.getDefaultJPEGEncodeParam(clippedImage);
            //encodeParam.setQuality(1.0f, false);
            encoder.encode(clippedImage, encodeParam);
        }
        catch(IOException e)
        {
            log(e.getMessage());
        }

    }
    
}


//--------------------------------------
// $Log: BufferedCellImageServer.java,v $
// Revision 1.3  2004/08/13 06:02:22  leo
// クリップした周りにborderを付けるようにした
//
// Revision 1.2  2004/08/12 17:48:26  leo
// update
//
// Revision 1.1  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
// Revision 1.3  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
// Revision 1.2  2004/07/26 19:57:40  leo
// sessionではなく引数で読み込むように変更
//
// Revision 1.1  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
//--------------------------------------
