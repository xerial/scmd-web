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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;


import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.xerial.util.Pair;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ParamPlotForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.design.PlotColor;
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
        UserSelection selection = (UserSelection) session.getAttribute("userSelection");
        if(selection == null) 
        {
            selection = new UserSelection();
            session.setAttribute("userSelection", selection);
        }
        TreeSet selectedORFSet = new TreeSet();
        selectedORFSet.add(view.getOrf().toUpperCase());
        for(Object orf : selection.orfSet())
        {
            selectedORFSet.add(orf);
        }
        
        if(view == null)
        {
            view = new CellViewerForm();
            session.setAttribute("view", view);
        }
        
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        
        String param1 = plotForm.getParamName(plotForm.getParam1());
        String param2 = plotForm.getParamName(plotForm.getParam2());
        String sql = "SELECT t1.strainname, p1, p2 from "
            + "(select strainname, average as p1 from $1 where paramid=$2 and groupid=0) as t1 "
            + "left join (select strainname, average as p2 from $1 where paramid=$3 and groupid=0) as t2 "
            + "on t1.strainname = t2.strainname";
        
        Table plotTable = null;
        try
        {
            plotTable = ConnectionServer.retrieveTable(sql, 
                            SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                            plotForm.getParam1(), plotForm.getParam2());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }            
        if(plotTable == null)
        {
            printNAImage(request, response);
            return;
        }
 
        ColLabelIndex colIndex = new ColLabelIndex(plotTable);
        int param1_col = colIndex.getColIndex("p1");
        int param2_col = colIndex.getColIndex("p2");
        int strain_col = colIndex.getColIndex("strainname");
        if(param1_col == -1 || param2_col == -1 || strain_col == -1)
        {
            printNAImage(request, response);
            return;
        }
        
        // create graphics 

        BufferedImage image = create2DPlot(param1, param2, plotTable, IMAGEWIDTH);
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
        
        double x_max = stat.getMaxValue(colIndex.getVerticalIterator("p1"));
        double x_min = stat.getMinValue(colIndex.getVerticalIterator("p1"));
        double x_ave = stat.calcAverage(colIndex.getVerticalIterator("p1"));
        double y_max = stat.getMaxValue(colIndex.getVerticalIterator("p2"));
        double y_min = stat.getMinValue(colIndex.getVerticalIterator("p2"));
        double y_ave = stat.calcAverage(colIndex.getVerticalIterator("p2"));
        
//        xmlout.selfCloseTag("rect", new XMLAttribute("x", "0")
//                            .add("y", "0")
//                            .add("width", Integer.toString(IMAGEWIDTH))
//                            .add("height", Integer.toString(IMAGEWIDTH))
//                            .add("style", "fill:white; stroke:none;"));
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, IMAGEWIDTH, IMAGEWIDTH);
        
        g.setColor(new Color(0x90C0E0));
        
        LinkedList<Pair<String, Point>> selectedORFPointList = new LinkedList<Pair<String, Point>>();
        
        for(int i=1; i<plotTable.getRowSize(); i++)
        {
            try
            {
                double x = Double.parseDouble(plotTable.get(i, param1_col).toString());
                double y = Double.parseDouble(plotTable.get(i, param2_col).toString());
                String orf = plotTable.get(i, strain_col).toString();
                
                int xplot = (int) (x * IMAGEWIDTH / x_max);
                int yplot = (int) (IMAGEWIDTH - (y * IMAGEWIDTH / y_max));
        
                if(selectedORFSet.contains(orf.toUpperCase()))
                {
                    selectedORFPointList.add(new Pair<String, Point>(orf, new Point(xplot,yplot)));
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
        

        for(Pair<String, Point> p : selectedORFPointList)
        {
            String selectedORF = p.getFirst();
            Point point = p.getSecond();
            PlotColor plotColor = selection.getPlotColor(selectedORF);
            g.setColor(plotColor.getColor());
            g.fillOval(point.x - 2, point.y - 2, 5, 5);
        }

        // average line
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);       
        g.setColor(new Color(0x90F0C0));
        g.setComposite(ac);
        int x_ave_axis = (int) (x_ave * IMAGEWIDTH / x_max);
        int y_ave_axis = (int) (IMAGEWIDTH - (y_ave * IMAGEWIDTH / y_max));        
        g.drawLine(x_ave_axis, 0, x_ave_axis, IMAGEWIDTH);
        g.drawLine(0, y_ave_axis, IMAGEWIDTH, y_ave_axis);

//            xmlout.selfCloseTag("rect", new XMLAttribute("x", Integer.toString(t_x-2))
//                                .add("y", Integer.toString(t_y-2))
//                                .add("width", Integer.toString(5))
//                                .add("height", Integer.toString(5))
//                                .add("style", "fill:rgb(255,128,128); stroke:none;"));


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
    
    /**
     * 2DPlotの下地を作成する
     * @param xParamName 
     * @param yParamName
     * @param plotTable (strainname, xParamName, yParamName) というスキーマのテーブルの入力を想定
     * @param imageWidth
     * @return 2DPlotの下地
     */
    static public BufferedImage create2DPlot(String xParamName, String yParamName, Table plotTable, int imageWidth)
    {
        if(plotTable == null)
        {
            return createNAImage(imageWidth);
        }
        ColLabelIndex colIndex = new ColLabelIndex(plotTable);
        int param1_col = colIndex.getColIndex(xParamName);
        int param2_col = colIndex.getColIndex(yParamName);
        int strain_col = colIndex.getColIndex("strainname");
        if(param1_col == -1 || param2_col == -1 || strain_col == -1)
            return createNAImage(imageWidth);
        
        // create graphics 

        BufferedImage image = new BufferedImage(imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        StatisticsWithMissingValueSupport stat = new StatisticsWithMissingValueSupport(new String[] {".", "-1", "-1.0"});
        
        double x_max = stat.getMaxValue(colIndex.getVerticalIterator(xParamName));
        double x_min = stat.getMinValue(colIndex.getVerticalIterator(xParamName));
        double y_max = stat.getMaxValue(colIndex.getVerticalIterator(yParamName));
        double y_min = stat.getMinValue(colIndex.getVerticalIterator(yParamName));
        
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, imageWidth, imageWidth);
        g.setColor(new Color(0x90C0E0));

        LinkedList<Point> selectedORFPointList = new LinkedList<Point>();
        
        for(int i=1; i<plotTable.getRowSize(); i++)
        {
            try
            {
                double x = Double.parseDouble(plotTable.get(i, param1_col).toString());
                double y = Double.parseDouble(plotTable.get(i, param2_col).toString());
                String orf = plotTable.get(i, strain_col).toString();
                
                int xplot = (int) (x * imageWidth / x_max);
                int yplot = (int) (imageWidth - (y * imageWidth / y_max));
        
                g.fillOval(xplot-1, yplot-1, 3, 3);
            }
            catch(NumberFormatException e)
            {
                // skip this point
            }
        }
        
        return image;
    }
    
    public void printNAImage(HttpServletRequest request, HttpServletResponse response)
    	throws IOException
    {
        BufferedImage image = createNAImage(IMAGEWIDTH);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());        
    }
    
    /** 
     * plotを作成できないときに、白紙のイメージを返す
     * @param imageWidth
     * @return 白紙のイメージ
     */
    static public BufferedImage createNAImage(int imageWidth)
    {
        BufferedImage image = new BufferedImage(imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();    
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, imageWidth, imageWidth);
        return image;
    }
}


//--------------------------------------
// $Log: Plot2DServlet.java,v $
// Revision 1.4  2004/09/09 01:34:50  leo
// antialiasingをして、画像を描くようにした
//
// Revision 1.3  2004/09/08 04:21:23  leo
// range query
//
// Revision 1.2  2004/09/08 02:30:32  leo
// batikを追加
//
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plotを追加
//
//--------------------------------------
