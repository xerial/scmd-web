//--------------------------------------
//SCMDWeb Project
//
//CustomizeViewAction.java
//Since: 2005/02/08
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/action/CustomizeViewAction.java $ 
//$Author: sesejun $
//--------------------------------------

package lab.cb.scmd.web.servlet.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.SCMDConfiguration;
import static lab.cb.scmd.web.image.teaddrop.Teardrop.Orientation.horizontal;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamFigPainter extends HttpServlet {

    public ParamFigPainter() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        try{
            String parameter = request.getParameter("param");
            if( parameter.matches("^[ACD]CV[0-9].*")) {
                parameter = parameter.replaceFirst("CV", "");
            }
            parameter = parameter.replaceFirst("_.*", "");
            String paramFigURI = SCMDConfiguration.getProperty("PARAMFIG_URI");
            
            URL imageURL;
            BufferedImage image;
            try
            {
                imageURL = new URL(paramFigURI + "/" + parameter + ".png");
                image = ImageIO.read(imageURL);
                if( image == null )
                    throw new SCMDException("no image found");
            }
            catch (MalformedURLException e)
            {
                throw new SCMDException(e);
            }
            catch (IOException e)
            {
                throw new SCMDException(e);
            }

            try
            {
                response.setContentType("image/png");
                ImageIO.write(image, "png", response.getOutputStream());
            }
            catch (IOException e)
            {
                log(e.getMessage());
            }
        
        }
        catch (NumberFormatException e)
        {
            log(e.getMessage());
            printWhiteBoard(request, response);
        }
        catch(SCMDException e)
        {
            log(e.getMessage());
            printWhiteBoard(request, response);            
        }
        
    }
    
    void printWhiteBoard(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        response.setContentType("image/png");
        request.getRequestDispatcher("/png/na_paramfig.png").forward(request, response);
        return;
    }

}
