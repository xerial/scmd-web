//--------------------------------------
// SCMDServer
// 
// AttributeDecollation.java 
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
public class AttributeDecollation extends Decollation
{
    /**
     * @param tableElement
     */
    public AttributeDecollation(TableElement tableElement, String attributeName, String value)
    {
        super(tableElement, new AttributeDecollator(attributeName, value));
    }
    public AttributeDecollation(TableElement tableElement, Decollator decollator)
    {
        super(tableElement, decollator);
    }
}


//--------------------------------------
// $Log: AttributeDecollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/09 12:32:47  leo
// StringAttributeDecollation -> AttributeDecollationに集約
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
//--------------------------------------