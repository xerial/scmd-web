//--------------------------------------
// SCMDServer
// 
// SingleCell.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.util.image.BoundingRectangle;

/**
 * @author leo
 *
 */
public class SingleCell extends TargetPhoto
{
    BoundingRectangle boundingRectangle = new BoundingRectangle();
    
    
    void init() 
    {
        setMagnification(100);
    }
    
    public SingleCell()
    {
        super();
        init();
    }
    
    public SingleCell(TargetPhoto photo, BoundingRectangle boundingRectangle)
    {
        super(photo);
        init();
        setX1(boundingRectangle.getX1());
        setX2(boundingRectangle.getX2());
        setY1(boundingRectangle.getY1());
        setY2(boundingRectangle.getY2());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        
        if(!(isValidRange(getX1(), getX2()) && isValidRange(getY1(), getY2()) && (getX2() <= 696) && (getY2() <= 540)))
            errors.add("cellbox", new ActionError("error.cell.box"));
        
        return errors;
    }
    
    protected boolean isValidRange(int begin, int end)
    {
        return (begin <= end) && (begin >= 0) && (end >= 0);
    }
    
    public Map getQueryMap()
    {
        Map map = super.getQueryMap();
        map.put("x1", new Integer(getX1()));
        map.put("y1", new Integer(getY1()));
        map.put("x2", new Integer(getX2()));
        map.put("y2", new Integer(getY2()));
        return map;
    }
    
    public BoundingRectangle getBoundingRectangle()
    {
        return boundingRectangle;
    }
    
    
    public int getX1() {
        return boundingRectangle.getX1();
    }
    public void setX1(int x1) {
        boundingRectangle.setX1(x1);
    }
    public int getX2() {
        return boundingRectangle.getX2();
    }
    public void setX2(int x2) {
        boundingRectangle.setX2(x2);
    }
    public int getY1() {
        return boundingRectangle.getY1();
    }
    public void setY1(int y1) {
        boundingRectangle.setY1(y1);
    }
    public int getY2() {
        return boundingRectangle.getY2();
    }
    public void setY2(int y2) {
        boundingRectangle.setY2(y2);
    }
}


//--------------------------------------
// $Log: SingleCell.java,v $
// Revision 1.1  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
//--------------------------------------