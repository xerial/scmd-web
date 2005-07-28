//--------------------------------------
// SCMDWeb Project
//
// PlotColor.java
// Since: 2005/02/10
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.design;

import java.awt.Color;
import java.util.TreeMap;
import java.util.Vector;

/**
 * plotÇÃColorÇï€éùÇ∑ÇÈÉNÉâÉX
 * @author leo
 *
 */
public class PlotColor
{
    static private Vector<PlotColor> plotColorList  = new Vector<PlotColor>();
    static private TreeMap<String, PlotColor> plotColorMap = new TreeMap<String, PlotColor>();
    static
    {
//        plotColorList.add(new PlotColor("pink",    0xFF7090));
        plotColorList.add(new PlotColor("skyblue", 0x1070FF));
        plotColorList.add(new PlotColor("red",     0xFF5050));
        plotColorList.add(new PlotColor("green",   0x10FF70));
        plotColorList.add(new PlotColor("yellow",  0xFFFF70));
        plotColorList.add(new PlotColor("purple",  0xE020FF));
        plotColorList.add(new PlotColor("navyblue", 0x113377));
        plotColorList.add(new PlotColor("gray",    0x606060));        
        plotColorList.add(new PlotColor("black",   0x333333));
        plotColorList.add(new PlotColor("default",  0xF090C0)); // do not remove this!
        
        for(PlotColor plotColor : plotColorList)
        {
            plotColorMap.put(plotColor.getColorName(), plotColor);
        }
    }
    
    private String colorName;
    private int colorCode;    
    
    static public Vector<PlotColor> getDefaultPlotColorList() 
    {
        return plotColorList;
    }    
    
    static public PlotColor getPlotColor(String name)
    {
        if(name == null)
            return plotColorMap.get("default");
        PlotColor plotColor = plotColorMap.get(name);
        return plotColor == null ? plotColorMap.get("default") : plotColor;
    }
    
    /**
     * 
     */
    public PlotColor(String colorName, int colorCode)
    {
        this.colorName = colorName;
        this.colorCode = colorCode;
    }    
    
    public String getColorCode()
    {
        return Integer.toString(colorCode, 16);
    }
    
    public Color getColor()
    {
        return new Color(colorCode);
    }
    
    public void setColorCode(int colorCode)
    {
        this.colorCode = colorCode;
    }
    public String getColorName()
    {
        return colorName;
    }
    public void setColorName(String colorName)
    {
        this.colorName = colorName;
    }
}




