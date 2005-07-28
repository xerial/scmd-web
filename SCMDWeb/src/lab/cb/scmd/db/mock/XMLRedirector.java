//--------------------------------------
// SCMDServer
// 
// XMLRedirector.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

/** xmlファイルを読み取り、その内容を、XMLOutputterに出力するクラス
 * @author leo
 *
 */
public class XMLRedirector extends DefaultHandler
{

    /**
     * 
     */
    public XMLRedirector(XMLOutputter xmlout)
    {
        super();
        _xmlout = xmlout;
    }
    
    public void redirect(String file) throws SCMDException
    {
        try
        {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(new File(file), this);
        }
        catch(SAXException e)
        {
            throw new SCMDException(e);
        }
        catch(IOException e)
        {
            throw new SCMDException(e);
        }
        catch (ParserConfigurationException e)
        {
            throw new SCMDException(e);
        }
    }
    
    
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        String peek = (String) _elementContentStack.pop();
        peek += new String(ch, start, length);
        _elementContentStack.push(peek);
    }
    public void endDocument() throws SAXException
    {
        try
        {
            _xmlout.endOutput();
        }
        catch(InvalidXMLException e)
        {
            throw new SAXException(e);
        }
    }
    public void endElement(String arg0, String arg1, String arg2) throws SAXException
    {
        String content = ((String) _elementContentStack.pop()).trim();
        
        try
        {
            if(content.length() != 0)
                _xmlout.textContent(content);

            _xmlout.closeTag();
        }
        catch(InvalidXMLException e)
        {
            new SAXException(e);
        }
        
    }
    public void startDocument() throws SAXException
    {
        _elementContentStack.push(new String(""));
    }
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        
        if(attributes.getLength() == 0)
            _xmlout.startTag(qName);
        else
        {
            XMLAttribute outputAttribute = new XMLAttribute();
            for(int i=0; i<attributes.getLength(); i++)
            {
                outputAttribute.add(attributes.getQName(i), attributes.getValue(i));
            }            
            _xmlout.startTag(qName, outputAttribute);
        }
        _elementContentStack.push(new String(""));
    }
    
    XMLOutputter _xmlout;
    Stack _elementContentStack = new Stack();
}


//--------------------------------------
// $Log: XMLRedirector.java,v $
// Revision 1.1  2004/07/21 05:47:16  leo
// XML関係のモジュールを追加
//
//--------------------------------------