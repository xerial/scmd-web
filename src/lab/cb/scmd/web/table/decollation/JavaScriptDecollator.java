//--------------------------------------
// SCMDServer
// 
// JavaScriptDecollator.java 
// Since: 2004/08/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.util.xml.XMLUtil;

/**
 * @author leo
 *
 */
public class JavaScriptDecollator extends AttributeDecollator
{

    /**
     * @param attributeName
     * @param value
     */
    public JavaScriptDecollator(String attributeName, String value)
    {
        super(attributeName, XMLUtil.createCDATA(value));
    }

}


//--------------------------------------
// $Log: JavaScriptDecollator.java,v $
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