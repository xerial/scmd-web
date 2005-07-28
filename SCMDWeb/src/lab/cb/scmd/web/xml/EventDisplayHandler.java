//--------------------------------------
// SCMDServer
// 
// EventDisplayHandler.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author leo
 *
 */
public class EventDisplayHandler extends DefaultHandler
{

    PrintStream _out = System.out; 
    /**
     * 
     */
    public EventDisplayHandler()
    {

    }

    
    
    public void characters(char[] arg0, int arg1, int arg2) throws SAXException
    {
        _out.print("[character]:\t");
        _out.println(new String(arg0, arg1, arg2));
    }
    public void endDocument() throws SAXException
    {
        _out.println("[endDocument]");
    }
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        _out.println("[endElement]:\t" + qName);
    }
    
    public void error(SAXParseException arg0) throws SAXException
    {
        _out.println("[error]:\t" + arg0.getMessage());
    }
    public void fatalError(SAXParseException arg0) throws SAXException
    {
        _out.println("[fatalError]:\t" + arg0.getMessage());
    }
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException
    {
        _out.println("[ignorableWhiteSpaces]:\t" + new String(arg0, arg1, arg2));
    }

    public void processingInstruction(String arg0, String arg1) throws SAXException
    {
        _out.println("[PI]:\ttarget=" + arg0 + " data=" + arg1);
    }
    
    
    public void startDocument() throws SAXException
    {
        _out.println("[startDocument]");
    }
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
            throws SAXException
    {
        _out.println("[startElement]:\t" + arg2);
        for(int i=0; i<arg3.getLength(); i++)
        {
            _out.println("[attribute]:\t" + arg3.getQName(i) + "=\"" + arg3.getValue(i) + "\"");
        }
    }

    public void warning(SAXParseException arg0) throws SAXException
    {
        _out.println("[warning]:\t" + arg0.getMessage());
    }
}


//--------------------------------------
// $Log: EventDisplayHandler.java,v $
// Revision 1.1  2004/07/21 05:47:16  leo
// XMLŠÖŒW‚Ìƒ‚ƒWƒ…[ƒ‹‚ð’Ç‰Á
//
//--------------------------------------