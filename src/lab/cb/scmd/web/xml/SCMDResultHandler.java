//--------------------------------------
// SCMDServer
// 
// SCMDResultHandler.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author leo
 *
 */
public class SCMDResultHandler extends DefaultHandler
{

    /**
     * 
     */
    public SCMDResultHandler()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

    public void characters(char[] arg0, int arg1, int arg2) throws SAXException
    {
        // TODO Auto-generated method stub
        super.characters(arg0, arg1, arg2);
    }
    public void endDocument() throws SAXException
    {
        // TODO Auto-generated method stub
        super.endDocument();
    }
    public void endElement(String arg0, String arg1, String arg2) throws SAXException
    {
        // TODO Auto-generated method stub
        super.endElement(arg0, arg1, arg2);
    }
    public void error(SAXParseException arg0) throws SAXException
    {
        // TODO Auto-generated method stub
        super.error(arg0);
    }
    public void fatalError(SAXParseException arg0) throws SAXException
    {
        // TODO Auto-generated method stub
        super.fatalError(arg0);
    }
    public void startDocument() throws SAXException
    {
        // TODO Auto-generated method stub
        super.startDocument();
    }
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
            throws SAXException
    {
        // TODO Auto-generated method stub
        super.startElement(arg0, arg1, arg2, arg3);
    }
    public void warning(SAXParseException arg0) throws SAXException
    {
        // TODO Auto-generated method stub
        super.warning(arg0);
    }
}


//--------------------------------------
// $Log: SCMDResultHandler.java,v $
// Revision 1.1  2004/07/21 05:47:16  leo
// XMLä÷åWÇÃÉÇÉWÉÖÅ[ÉãÇí«â¡
//
//--------------------------------------