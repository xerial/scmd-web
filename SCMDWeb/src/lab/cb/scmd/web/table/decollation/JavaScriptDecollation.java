//--------------------------------------
// SCMDServer
// 
// JavaScriptDecollation.java 
// Since: 2004/08/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.util.xml.XMLUtil;
import lab.cb.scmd.web.table.TableElement;

/**
 * @author leo
 *
 */
public class JavaScriptDecollation extends AttributeDecollation
{

    /**
     * @param tableElement
     * @param attributeName
     * @param value
     */
    public JavaScriptDecollation(TableElement tableElement, String attributeName, String value)
    {
        super(tableElement, attributeName, XMLUtil.createCDATA(value));
    }

    /**
     * @param tableElement
     * @param decollator
     */
    public JavaScriptDecollation(TableElement tableElement, Decollator decollator)
    {
        super(tableElement, decollator);
    }

}


//--------------------------------------
// $Log: JavaScriptDecollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.2  2004/08/26 04:29:35  leo
// cdata‚Ì•\Ž¦‚ðXMLUtil‚É”C‚¹‚é
//
// Revision 1.1  2004/08/26 04:28:37  leo
// add
//
//--------------------------------------