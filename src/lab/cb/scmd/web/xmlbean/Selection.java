//--------------------------------------
// SCMDWeb Project
//
// Selection.java
// Since: 2005/02/22
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.xmlbean;


/**
 * UserSelectionの内容を保存するためのクラス （XMLBean)
 * @author leo
 *
 */
public class Selection
{
    private Item[] item = null;
    
    public Selection()
    {
    }

    public void setItem(Item[] item)
    {
        this.item = item;
    }
    
    
    public Item[] getItem()
    {
        return item;
    }
}




