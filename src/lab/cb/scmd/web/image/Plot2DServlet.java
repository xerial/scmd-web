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
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;


import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xerial.algorithm.Algorithm;
import org.xerial.util.MinMax;
import org.xerial.util.Pair;
import org.xerial.util.Tuple;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.util.stat.EliminateOnePercentOfBothSidesStrategy;
import lab.cb.scmd.util.stat.SampleFilteringStrategy;
import lab.cb.scmd.util.stat.Statistics;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ParamPlotForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
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

        CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);
        UserSelection selection = SCMDSessionManager.getUserSelection(request);
        TreeSet<String> selectedORFSet = new TreeSet<String>();
        selectedORFSet.addAll(selection.orfSet());
        if(selectedORFSet.isEmpty())
            selectedORFSet.add(view.getOrf().toUpperCase());
        
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        
        String param1 = plotForm.getParamName(plotForm.getParam1());
        String param2 = plotForm.getParamName(plotForm.getParam2());
        String sql = "SELECT t1.strainname, p1, p2 from "
            + "(select strainname, average as p1 from $1 where paramid=$2 and groupid=0) as t1 "
            + "left join (select strainname, average as p2 from $1 where paramid=$3 and groupid=0) as t2 "
            + "on t1.strainname = t2.strainname";
        String sql_wt = "select t1.average as p1, t2.average as p2 from $1 as t1 inner join $1 as t2 using (strainname) where t1.paramid=$2 and t2.paramid=$3 and t1.groupid=0 and t2.groupid=0";
        
        Table plotTable = null;  // 全mutantのデータ
        Table plotTable_wt = null; // 野生株のデータ
        try
        {
            plotTable = ConnectionServer.retrieveTable(sql, 
                            SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                            plotForm.getParam1(), plotForm.getParam2());
            plotTable_wt = ConnectionServer.retrieveTable(sql_wt, 
                    SCMDConfiguration.getProperty("DB_PARAMSTAT_WT", "paramstat_wt"),
                    plotForm.getParam1(), plotForm.getParam2());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }            
        if(plotTable == null || plotTable_wt == null)
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
        ColLabelIndex colIndex_wt = new ColLabelIndex(plotTable_wt);
        int param1_col_wt = colIndex_wt.getColIndex("p1");
        int param2_col_wt = colIndex_wt.getColIndex("p2");
        if(param1_col_wt == -1 || param2_col == -1)
        {
            printNAImage(request, response);
            return;
        }

        // calculate the image window size
        Statistics stat = new Statistics();
        Collection<Double> p1List = stat.filter(colIndex.getVerticalIterator("p1"));
        Collection<Double> p2List = stat.filter(colIndex.getVerticalIterator("p2"));
                
        double x_max = Statistics.getMaxValue(p1List);
        double x_min = Statistics.getMinValue(p1List);
        double x_ave = Statistics.calcMean(p1List);
        double y_max = Statistics.getMaxValue(p2List);
        double y_min = Statistics.getMinValue(p2List);
        double y_ave = Statistics.calcMean(p2List);
        
        double canvasWidth = Algorithm.<Double>minmax(x_ave-x_min, x_max-x_ave).max();
        double canvasHeight = Algorithm.<Double>minmax(y_ave-y_min, y_max-y_ave).max();
        
        Plot2DCanvas plotCanvas = new Plot2DCanvas(new Range(x_ave - canvasWidth, x_ave + canvasWidth), 
                                                   new Range(y_ave - canvasHeight, y_ave + canvasHeight), IMAGEWIDTH);        

        // 両端１％を除去
        //Pair<Range, Range> mutantRange = filterRange(plotTable);
        //Pair<Range, Range> wildtypeRange = filterRange(plotTable_wt);

        // convert the table data into a list format
        LinkedList<Point> mutantList = new LinkedList<Point>();
        LinkedList<Point> wildtypeList = new LinkedList<Point>();
        LinkedList<Pair<String, Point>> selectedORFList = new LinkedList<Pair<String, Point>>();
        for(int i=1; i<plotTable.getRowSize(); i++)
        {
            try
            {
                double x = Double.parseDouble(plotTable.get(i, param1_col).toString());
                double y = Double.parseDouble(plotTable.get(i, param2_col).toString());
                String orf = plotTable.get(i, strain_col).toString();
                Point p = new Point(x, y);                                
                if(selectedORFSet.contains(orf.toUpperCase()))               
                    selectedORFList.add(new Pair<String, Point>(orf.toUpperCase(), p));
                else
                {
                    mutantList.add(p);
                }
            }
            catch(NumberFormatException e)
            {
                // skip this point
            }
        }
        for(int i=1; i<plotTable_wt.getRowSize(); i++)
        {
            try
            {
                double x = Double.parseDouble(plotTable_wt.get(i, param1_col_wt).toString());
                double y = Double.parseDouble(plotTable_wt.get(i, param2_col_wt).toString());
                wildtypeList.add(new Point(x, y));
            }
            catch(NumberFormatException e)
            {
                // skip this point
            }
        }

        

        plotCanvas.plot(mutantList, 0x59E2EF);        // plot mutants        
        plotCanvas.plot(wildtypeList, 0x49BCC6);      // plot wildtype 
        // plot selected ORFs
        for(Pair<String, Point> p : selectedORFList)
        {
            String selectedORF = p.getFirst();
            Point point = p.getSecond();
            PlotColor plotColor = selection.getPlotColor(selectedORF);
            plotCanvas.plot(point, plotColor, 5);
        }
        // draw average lines
        plotCanvas.drawAverageLine(new Point(x_ave, y_ave), 0x90F0C0);
        plotCanvas.drawAverageLine(new Point(stat.calcAverage(plotTable_wt.getVerticalIterator(param1_col_wt)),
                                             stat.calcAverage(plotTable_wt.getVerticalIterator(param2_col_wt))), 0xEFC4C9);
        

        response.setContentType("image/png");
        ImageIO.write(plotCanvas.getImage(), "png", response.getOutputStream());
    }
    
    /** 上下１％に属するエントリを除去する場合の値域を返す
     * @param table
     * @return
     */
    private Pair<Range, Range> filterRange(Table table)
    {
        Table filteredTable = new Table();        
        ColLabelIndex colIndex = new ColLabelIndex(table);
        
        filteredTable.addRow(table.getRowList(0)); // ラベル行を(shallow)コピー
        
        StatisticsWithMissingValueSupport stat = new StatisticsWithMissingValueSupport(new String[] {".", "-1", "-1.0"}, new EliminateOnePercentOfBothSidesStrategy());
        List<Double> p1List = (List<Double>) stat.filter(colIndex.getVerticalIterator("p1"));
        List<Double> p2List = (List<Double>) stat.filter(colIndex.getVerticalIterator("p2"));
        Range p1Range = new Range(p1List.get(0), p1List.get(p1List.size()-1));
        Range p2Range = new Range(p2List.get(0), p2List.get(p2List.size()-1));
        return new Pair<Range, Range>(p1Range, p2Range);
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

class Range extends Pair<Double, Double>
{
    public Range(double min, double max)
    {
        super(min, max);
    }
    public double getMin() { return this.getFirst(); }
    public double getMax() { return this.getSecond(); }
    
    public boolean within(double value) 
    {
        return (this.getFirst() <= value) && ( value <= this.getSecond());
    }
}

class Point 
{
    private double x;
    private double y;
    public Point(double x, double y)
    {
        this.x = x; this.y = y;
    }
    public double getX() { return x; }
    public double getY() { return y; }
}

/**
 * 2D plotを描画するキャンバス
 * @author leo
 *
 */
class Plot2DCanvas 
{
    private int imageWidth;
    private Range xRange;
    private Range yRange;
    private BufferedImage canvas;
    private Graphics2D g;
    private int margin = 10;
    
    public Plot2DCanvas(Range xRange, Range yRange, int imageWidth)
    {
        this.xRange = xRange;
        this.yRange = yRange;
        this.imageWidth = imageWidth;
        
        canvas = new BufferedImage(imageWidth + margin * 2, imageWidth + margin * 2, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) canvas.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    public BufferedImage getImage()
    {
        return canvas;
    }
    
    public void plot(List<Point> pointList, int color)
    {
        g.setColor(new Color(color));        
        for(Point p : pointList)
            g.fillOval(calcX(p), calcY(p), 3, 3);            
    }
    
    public void plot(List<Point> pointList, int color, float alpha)
    {
        Composite prevComposite = g.getComposite();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);               
        g.setComposite(ac);        
        plot(pointList, color);        
        g.setComposite(prevComposite);        
    }
    
    public void plot(Point p, PlotColor color, int pointRadius)
    {
        g.setColor(color.getColor());        
        int offset = pointRadius / 2;
        g.fillOval(calcX(p) - offset, calcY(p) - offset, pointRadius, pointRadius);            
    }
    
    public void drawAverageLine(Point averagePoint, int rgb)
    {
        Composite prevComposite = g.getComposite();
        
        // average line
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);       
        g.setColor(new Color(rgb));
        g.setComposite(ac);
        int aveX = calcX(averagePoint);
        int aveY = calcY(averagePoint);
        g.drawLine(aveX, 0, aveX, canvas.getHeight());
        g.drawLine(0, aveY, canvas.getWidth(), aveY);
        
        g.setComposite(prevComposite);
    }
    
    
    private int calcX(Point p)
    {
        return (int) ((p.getX() - xRange.getMin()) * imageWidth / (xRange.getMax() - xRange.getMin())) + margin;
    }
    
    private int calcY(Point p)
    {
        return (int) (imageWidth - ((p.getY() - yRange.getMin()) * imageWidth / (yRange.getMax() - yRange.getMin()))) + margin;
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
