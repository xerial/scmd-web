//--------------------------------------
// SCMDServer
//
// Plot2DServlet.java 
// Since: 2004/09/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ParamPlotForm;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * @author leo
 *
 */
public class Plot2DServlet extends HttpServlet
{
    public Plot2DServlet() {
        super();
    }

    final int IMAGEWIDTH = 300;        
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        ParamPlotForm plotForm = (ParamPlotForm) request.getAttribute("plotForm");
        HttpSession session = request.getSession(true);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
        {
            view = new CellViewerForm();
            session.setAttribute("view", view);
        }
        
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        
        String param1 = plotForm.getParam1();
        String param2 = plotForm.getParam2();
        
        Table plotTable = query.getAveragePlot(param1, param2);
        if(plotTable == null)
        {
            printNAImage(request, response);
            return;
        }
        ColLabelIndex colIndex = new ColLabelIndex(plotTable);
        int param1_col = colIndex.getColIndex(param1);
        int param2_col = colIndex.getColIndex(param2);
        int strain_col = colIndex.getColIndex("strainname");
        if(param1_col == -1 || param2_col == -1 || strain_col == -1)
        {
            printNAImage(request, response);
            return;
        }
        
        // create graphics 

        BufferedImage image = new BufferedImage(IMAGEWIDTH, IMAGEWIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        //Document svgDoc = domImpl.createDocument(null, "svg", null);
        //SVGGraphics2D g = new SVGGraphics2D(svgDoc);
        //g.setSVGCanvasSize(new Dimension(IMAGEWIDTH, IMAGEWIDTH));
        
        //response.setContentType("image/svg+xml");
        //ServletOutputStream out = response.getOutputStream();
        //XMLOutputter xmlout = new XMLOutputter(out);
        //xmlout.omitHeader();
        //xmlout.startTag("svg", new XMLAttribute("width", Integer.toString(IMAGEWIDTH)).add("height", Integer.toString(IMAGEWIDTH)));
        //xmlout.startTag("g");
        
        StatisticsWithMissingValueSupport stat = new StatisticsWithMissingValueSupport(new String[] {".", "-1", "-1.0"});
        
        double x_max = stat.getMaxValue(colIndex.getVerticalIterator(param1));
        double x_min = stat.getMinValue(colIndex.getVerticalIterator(param1));
        double y_max = stat.getMaxValue(colIndex.getVerticalIterator(param2));
        double y_min = stat.getMinValue(colIndex.getVerticalIterator(param2));
        
//        xmlout.selfCloseTag("rect", new XMLAttribute("x", "0")
//                            .add("y", "0")
//                            .add("width", Integer.toString(IMAGEWIDTH))
//                            .add("height", Integer.toString(IMAGEWIDTH))
//                            .add("style", "fill:white; stroke:none;"));
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, IMAGEWIDTH, IMAGEWIDTH);
        g.setColor(new Color(0x90C0E0));
        String targetOrf = view.getOrf().toUpperCase();
        int t_x = -10;
        int t_y = -10;
        boolean foundTarget = false;
        for(int i=1; i<plotTable.getRowSize(); i++)
        {
            try
            {
                double x = Double.parseDouble(plotTable.get(i, param1_col).toString());
                double y = Double.parseDouble(plotTable.get(i, param2_col).toString());
                String orf = plotTable.get(i, strain_col).toString();
                
                int xplot = (int) (x * IMAGEWIDTH / x_max);
                int yplot = (int) (IMAGEWIDTH - (y * IMAGEWIDTH / y_max));
        
                if(orf.equals(targetOrf))
                {
                    t_x = xplot;
                    t_y = yplot;
                    foundTarget = true;
                    continue;
                }
                g.fillOval(xplot-1, yplot-1, 3, 3);
//                xmlout.selfCloseTag("rect", new XMLAttribute("x", Integer.toString(xplot-1))
//                                    .add("y", Integer.toString(yplot-1))
//                                    .add("width", Integer.toString(3))
//                                    .add("height", Integer.toString(3))
//                                    .add("style", "fill:rgb(144,192,224); stroke:none;"));
            }
            catch(NumberFormatException e)
            {
                // skip this point
            }
        }
        
        if(foundTarget)
        {
            g.setColor(new Color(0xFF8080));
            g.fillOval(t_x-2, t_y-2, 5, 5);
//            xmlout.selfCloseTag("rect", new XMLAttribute("x", Integer.toString(t_x-2))
//                                .add("y", Integer.toString(t_y-2))
//                                .add("width", Integer.toString(5))
//                                .add("height", Integer.toString(5))
//                                .add("style", "fill:rgb(255,128,128); stroke:none;"));
        }

//        try
//        {
//            xmlout.endOutput();
//        }
//        catch(InvalidXMLException e)
//        {
//            e.what();
//        }
//        
//        g.stream(new OutputStreamWriter(out, "UTF-8"), true);

        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }
    
    
    
    public void printNAImage(HttpServletRequest request, HttpServletResponse response)
    	throws IOException
    {
        BufferedImage image = new BufferedImage(IMAGEWIDTH, IMAGEWIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();	
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, IMAGEWIDTH, IMAGEWIDTH);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());        
    }
}


//--------------------------------------
// $Log: Plot2DServlet.java,v $
// Revision 1.4  2004/09/09 01:34:50  leo
// antialiasing‚ð‚µ‚ÄA‰æ‘œ‚ð•`‚­‚æ‚¤‚É‚µ‚½
//
// Revision 1.3  2004/09/08 04:21:23  leo
// range query
//
// Revision 1.2  2004/09/08 02:30:32  leo
// batik‚ð’Ç‰Á
//
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plot‚ð’Ç‰Á
//
//--------------------------------------
