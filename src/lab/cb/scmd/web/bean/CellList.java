//--------------------------------------
// SCMDServer
// 
// CellList.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.exception.XMLParseErrorException;
import lab.cb.scmd.web.viewer.Photo;

/** 一つの写真に含まれるCellのデータを格納するクラス
 * @author leo
 *
 */
public class CellList
{
    LinkedList _cellList = new LinkedList();
    Photo	_photo = null;
    YeastGene gene = null;
    
    /**
     * 
     */
    public CellList(Element cellListTagElement) throws XMLParseErrorException
    {
        super();
        loadXML(cellListTagElement);
    }
    public CellList()
    {
        
    }
    
    public Iterator iterator()
    {
    	return  _cellList.iterator();
    }
    
    
    public List getCellList()
    {
        return _cellList;
    }
    public YeastGene getYeastGene()
    {
        return gene;
    }
    
    public Photo getPhoto()
    {
        return _photo;
    }
    
    public void loadXML(Element cellListTagElement) throws XMLParseErrorException
    {
        // for each orf
        NodeList orfList = cellListTagElement.getElementsByTagName("orf");
        for(int o=0; o<orfList.getLength(); o++)
        {
            Element orf = (Element) orfList.item(o);
            String orfName = orf.getAttribute("orfname").toString();
            if(gene == null)
            {
                // load gene info
                gene = new YeastGene(orf);
            }
            
            // for each photo
            NodeList photoList = orf.getElementsByTagName("photo");
            for(int p=0; p<photoList.getLength(); p++)
            {
                Element photo = (Element) photoList.item(p);
                int photoID = Integer.parseInt(photo.getAttribute("id"));
                if(_photo == null)
                    _photo = new Photo(orfName, photoID);
                //int page = Integer.parseInt(photo.getAttribute("page"));

                // for each cell
                NodeList cellList = photo.getElementsByTagName("cell");
                for(int c=0; c<cellList.getLength(); c++)
                {
                    Element cell = (Element) cellList.item(c);
                    int cellID = Integer.parseInt(cell.getAttribute("id"));
                    int x1 = Integer.parseInt(cell.getAttribute("x1"));
                    int x2 = Integer.parseInt(cell.getAttribute("x2"));
                    int y1 = Integer.parseInt(cell.getAttribute("y1"));
                    int y2 = Integer.parseInt(cell.getAttribute("y2"));
                    
                    try
                    {
                        _cellList.add(new Cell(_photo, cellID, new BoundingRectangle(x1, x2, y1, y2)));
                    }
                    catch(lab.cb.scmd.exception.InvalidParameterException e)
                    {
                        System.err.println(e);
                    }
                }
            }
        }

    }

}


//--------------------------------------
// $Log: CellList.java,v $
// Revision 1.3  2004/08/14 10:56:39  leo
// CellViewerFormへの対応
//
// Revision 1.2  2004/08/12 17:48:26  leo
// update
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------