//--------------------------------------
// SCMDServer
// 
// UserSelection.java 
// Since: 2004/08/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import lab.cb.scmd.web.design.PlotColor;

/**
 * ユーザーからのORFの入力を記録するフォーム
 * @author leo
 *
 */
public class UserSelection
{

    private TreeSet<String> _selection = new TreeSet<String>();
    private TreeMap<String, String> _colorMap = new TreeMap<String, String>();
    /**
     * 
     */
    public UserSelection()
    {
        super();
    }
    
    public void addSelection(String orf)
    {
        _selection.add(orf.toUpperCase());
    }
    
    public Set<String> getSelection()
    {
        return _selection;
    }

    
    public void removeSelection(String orf)
    {
        _selection.remove(orf.toUpperCase());
    }
    
    public Set<String> orfSet()
    {
        return (Set<String>) _selection.clone();
    }
       
    public Vector<String> getColorList()
    {
        Vector<String> colorMap = new Vector<String>();
        for(String orf : _colorMap.keySet())
        {
            colorMap.add(orf + "_" + _colorMap.get(orf));
        }
        return colorMap;
    }
    
    public void setColor(String orf, String colorName)
    {
        _colorMap.put(orf.toLowerCase(), colorName);
    }
    public String getColor(String orf)
    {
        if(orf == null)
            return "white";
        
        PlotColor pc = getPlotColor(orf);
        if(pc != null)
            return "#" + pc.getColorCode();
        else
            return "white";
    }
    
    public PlotColor getPlotColor(String orf)
    {        
        return PlotColor.getPlotColor(_colorMap.get(orf.toUpperCase()));
    }
    
    /**
     *  
     */
    public void clear()
    {
       _selection.clear();
       _colorMap.clear();
    }
}


//--------------------------------------
// $Log: UserSelection.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionの追加
//
//--------------------------------------