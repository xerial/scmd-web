//--------------------------------------
// SCMDServer
// 
// IntegerAttributeDecollation.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.web.table.TableElement;


/**
 * @author leo
 *
 */
public class IntegerAttributeDecollation extends Decollation
{
    /**
     * @param tableElement
     */
    public IntegerAttributeDecollation(String attributeName, int value, TableElement tableElement)
    {
        super(tableElement, new IntegerAttributeDecollator(attributeName, value));
    }
    
    public IntegerAttributeDecollation(IntegerAttributeDecollator decollator, TableElement tableElement)
    {
        super(tableElement, decollator);
    }
    
}


//--------------------------------------
// $Log: IntegerAttributeDecollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, DecollatorÇêÆóù
//
// Revision 1.1  2004/08/07 11:48:43  leo
// WebópÇÃTableÉNÉâÉX
//
//--------------------------------------