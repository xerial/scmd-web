//--------------------------------------
//SCMDServer
//
//TeardropPainter.java 
//Since: 2004/08/23
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.web.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.db.scripts.TeardropStatistics;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.image.teaddrop.Teardrop;

public class TeardropPainter extends HttpServlet{
	TeardropGenerator	_teardropGenerator;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String convertProgram = SCMDConfiguration
				.getProperty(SCMDConfiguration.IMAGEMAGICK_CONVERT);

		try
		{
        	//TODO use SCMDConfiguration
			_teardropGenerator = new TeardropGenerator(new File(convertProgram),
			        SCMDConfiguration.getProperty("TEARDROP_H_URI"));
		}
		catch (FileNotFoundException e)
		{
			throw new ServletException(e);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try
		{

			String parameter = request.getParameter("param");
		
			double avg = getParameter(request, "avg");
			double sd  = getParameter(request, "sd");
			double max  = getParameter(request, "max");
			double min  = getParameter(request, "min");
            
            int paramID = Integer.parseInt(request.getParameter("paramID"));
            int groupID = Integer.parseInt(request.getParameter("groupID"));
            
            List<TeardropPoint> points = getParameterArray(request, "value");                        
            Teardrop teardrop = new Teardrop(paramID, groupID, avg, sd, min, max);            
            teardrop.setOrientation(Teardrop.Orientation.horizontal);
            BufferedImage image = teardrop.drawImage(points);
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
		request.getRequestDispatcher("/png/na_teardrop.png").forward(request, response);
		return;
	}

	double getParameter(HttpServletRequest request, String parameterName) {
		double val = -1;
		String input = request.getParameter(parameterName);
		if(input != null)
		{
			val = Double.parseDouble(input);
		}
		return val;
	}
	
	
	// value = paramname:point[:color],
	// color はオプション
	List<TeardropPoint> getParameterArray(HttpServletRequest request, String parameterNamePrefix) {
        Vector<TeardropPoint> list = new Vector<TeardropPoint>();
		String value = request.getParameter("value");
		if(value == null)
            return list;
		    
		String[] tdvalues = value.split(",");
		
		Vector tearDropPointList = new Vector();
		for( int i = 0; i < tdvalues.length; i++ ) {
			String[] tdvalue = tdvalues[i].split(":");
			if(tdvalue.length<2)
			    continue;
			String name = tdvalue[0];
			String point = tdvalue[1];
			String color = "";
			if( tdvalue.length > 2 )
				color = tdvalue[2];
			list.add(new TeardropPoint(name, Double.parseDouble(point), color));
		}

		return list;
	}

}

//-------------------------------------------------------
// $Log: TeardropPainter.java,v $
// Revision 1.6  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.5  2004/09/03 04:48:59  leo
// 不正な値がきたときの処理を追加
//
//-------------------------------------------------------

