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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.struts.upload.FormFile;
import org.xerial.util.XMLParserException;
import org.xerial.util.xml.InvalidXMLException;
import org.xerial.util.xml.bean.XMLBeanException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import lab.cb.scmd.web.design.PlotColor;
import lab.cb.scmd.web.xmlbean.Item;
import lab.cb.scmd.web.xmlbean.Selection;

/**
 * ユーザーからのORFの入力を記録するフォーム
 * 
 * TODO UserSelectionクラスそのものをXMLBeanの形にできるとよい
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
        _colorMap.put(orf.toUpperCase(), colorName);
    }
    public String getColor(String orf)
    {
        if(orf == null)
            return "white";
        
        PlotColor pc = getPlotColor(orf.toUpperCase());
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
    
    
    public void outputXML(OutputStream out)
    {
        Selection selectionBean = new Selection();
        Item[] item = new Item[_selection.size()];
        int i = 0;
        for(String orf : _selection)
        {
            String color = _colorMap.get(orf);
            item[i] = new Item(orf, color);
            i++;
        }
        selectionBean.setItem(item);
        
        try
        {
            XMLBeanUtil.outputAsXML(selectionBean, out);
        }
        catch (XMLBeanException e)
        {
            e.printStackTrace();
        }
        catch (InvalidXMLException e)
        {
            e.printStackTrace();
        }          
    }
    
    public void loadFromXML(FormFile file)
    {
        try
        {
            Selection selection = XMLBeanUtil.newInstance(Selection.class, file.getInputStream());
            _selection.clear();
            _colorMap.clear();
            for(Item item : selection.getItem())
            {
                addSelection(item.getOrf());
                setColor(item.getOrf(), item.getColor());
            }
        }
        catch (XMLParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (XMLBeanException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvalidXMLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
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