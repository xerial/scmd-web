//--------------------------------------
// SCMDServer
// 
// DisplaySingleCellForm.java 
// Since: 2004/08/06
//
// $URL: http://phenome.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/bean/DisplaySingleCellForm.java $ 
// $LastChangedBy: leo $ 
//--------------------------------------

package lab.cb.scmd.web.formbean;


import org.apache.struts.action.ActionForm;

import lab.cb.scmd.util.image.BoundingRectangle;

/**
 * DisplaySingleCellAction で使うフォーム
 * @author leo
 *
 */
@SuppressWarnings("serial")
public class DisplaySingleCellForm extends ActionForm
{
    BoundingRectangle boundingRectangle = new BoundingRectangle();
    String orf;
    int photoNum;
    int photoType;
    int stainType;
    
    
    public DisplaySingleCellForm()
    {
        super();
    }

    
    public String getOrf()
    {
        return orf;
    }
    public void setOrf(String orf)
    {
        this.orf = orf;
    }
    public int getPhotoNum()
    {
        return photoNum;
    }
    public void setPhotoNum(int photoNum)
    {
        this.photoNum = photoNum;
    }
    public int getPhotoType()
    {
        return photoType;
    }
    public void setPhotoType(int photoType)
    {
        this.photoType = photoType;
    }

    public int getStainType()
    {
        return stainType;
    }
    public void setStainType(int stainType)
    {
        this.stainType = stainType;
    }


    protected boolean isValidRange(int begin, int end)
    {
        return (begin <= end) && (begin >= 0) && (end >= 0);
    }
    
    public boolean clippingRangeIsValid()
    {
        return isValidRange(getX1(), getX2()) && isValidRange(getY1(), getY2()) && (getX2() <= 696) && (getY2() <= 540);
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
// $Log: DisplaySingleCellForm.java,v $
// Revision 1.1  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
//--------------------------------------