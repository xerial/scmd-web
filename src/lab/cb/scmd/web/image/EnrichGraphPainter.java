package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.SCMDConfiguration;


public class EnrichGraphPainter extends HttpServlet {

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		String parameter = request.getParameter("param");
		String goid = request.getParameter("goid");
		String fwd =  request.getParameter("fwd");
		String high =  request.getParameter("type");
		
		String suffix = "";
		String uri = "";
		if( fwd.equals("1") ) {
			suffix = "fg";
		} else if( high.equals("whole")) {
			suffix = "whole";
		} else if( high.equals("high")) {
			suffix = "high"; 
		} else {
			suffix = "low";
		}
		String _enrichURI = SCMDConfiguration.getProperty("ENRICHFIG_URI");
        URL imageURL = new URL(_enrichURI + "/" + parameter + "-" + goid.replace("GO:", "") + "_" + suffix + ".png");
		try
		{
			BufferedImage enrichgraph = ImageIO.read(imageURL);
			if( enrichgraph == null )
				throw new SCMDException("no image found");
			response.setContentType("image/png");
			ImageIO.write(enrichgraph, "png", response.getOutputStream());
		}
		catch (IOException e)
		{
			log(e.getMessage());
		} catch (SCMDException e) {
			e.printStackTrace();
		}	
    }
    
    double getParameter(HttpServletRequest request, String parameterName)
    {
        double val = -1;
        String input = request.getParameter(parameterName);
        if (input != null)
        {
            if (!input.equals("")) val = Double.parseDouble(input);
        }
        return val;
    }

}
