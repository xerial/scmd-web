//--------------------------------------
// SCMD Project
// 
// Cell.java 
// Since:  2004/07/15
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 *
 */
public class Cell 
{
    /**
     * 
     */
    
    public Cell()
    {
    }
    
    public Cell(Photo photo_, int cellID_, BoundingRectangle boundingRectangle_)
    {
        super();
        
        photo = photo_;
        cellID = cellID_;
        
        boundingRectangle = boundingRectangle_;
    }

    public BoundingRectangle getBoundingRectangle()
    {
        return boundingRectangle;
    }
    
    public Photo getPhoto() throws InvalidParameterException 
    {
        return photo;
    }
    
    public int getCellID() {
        return cellID;
    }
    
    Photo photo;
    int cellID;
    BoundingRectangle boundingRectangle;
}


//--------------------------------------
// $Log: Cell.java,v $
// Revision 1.5  2004/07/27 06:50:25  leo
// CellInfo�y�[�W��ǉ�
//
// Revision 1.4  2004/07/25 11:28:52  leo
// cellID getter��ǉ�
//
// Revision 1.3  2004/07/20 15:45:59  leo
// xerces2.6.2��jar��ǉ�
//
// Revision 1.2  2004/07/15 09:21:20  leo
// jsp �̎g�p�J�n
//
// Revision 1.1  2004/07/15 02:31:39  leo
// interface�ł͂Ȃ��Ahas-a�̊֌W�Ƃ��Ďg�p����悤�ɕύX
//
//--------------------------------------