//--------------------------------------
// SCMDServer
// 
// CellCoordinatesSAXReader.java 
// Since: 2004/07/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.util.xml.BaseHandler;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 *
 */
public class CellCoordinatesSAXReader extends BaseHandler
{
    
    /**
     * 
     */
    public CellCoordinatesSAXReader()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        
        if(qName.equals("cell"))
        {
            try
            {
                BoundingRectangle br = new BoundingRectangle(Integer.parseInt(attributes.getValue("x1")),
                                                             Integer.parseInt(attributes.getValue("x2")),
                                                             Integer.parseInt(attributes.getValue("y1")),
                                                             Integer.parseInt(attributes.getValue("y2")));
                int cellID = Integer.parseInt(attributes.getValue("id"));
                
                if(_contextPhoto == null)
                    throw new SAXException("context photo is not specified");
                
                Cell cell = new Cell(_contextPhoto, cellID, br);
                _cellList.add(cell);
            }
            catch(InvalidParameterException e)
            {
                e.what();
            }
            return;
        }
        
        if(qName.equals("orf"))
        {
            _orf = attributes.getValue("orfname");
            return;
        }
        
        if(qName.equals("photo"))
        {
            _photoID = Integer.parseInt(attributes.getValue("id"));
            if(_orf == null)
                throw new SAXException("orf is not specified");
            _contextPhoto = new Photo(_orf, _photoID);
        }
    }
    
    public List getCellList()
    {
        return _cellList;
    }
    
    String _orf = null;
    int  _photoID;
    Photo _contextPhoto = null;
    LinkedList _cellList = new LinkedList();
}


//--------------------------------------
// $Log: CellCoordinatesSAXReader.java,v $
// Revision 1.1  2004/07/25 11:27:39  leo
// first ship
//
//--------------------------------------