//--------------------------------------
// SCMDServer
// 
// StyleDecollator.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.web.table.Style;
import lab.cb.scmd.web.table.TableElement;

/**
 * @author leo
 *
 */
public class StyleDecollator extends AttributeDecollator
{
    public StyleDecollator(String style)
    {
        super("class", style);
    }
    
    public TableElement decollate(TableElement element)
    {
        return new Style(element, getValue());
    }

}


//--------------------------------------
// $Log: StyleDecollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web—p‚ÌTableƒNƒ‰ƒX
//
//--------------------------------------