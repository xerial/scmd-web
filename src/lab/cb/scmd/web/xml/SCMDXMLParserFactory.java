//--------------------------------------
// SCMDServer
// 
// SCMDXMLParserFactory.java 
// Since: 2004/07/24
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import lab.cb.scmd.exception.SCMDException;

/** DOMやSAXのparserを生成します
 * @author leo
 *
 */
public class SCMDXMLParserFactory
{

    /**
     * 
     */
    protected SCMDXMLParserFactory()
    {
        _saxParserFactory = SAXParserFactory.newInstance();
        _domParserFactory = DocumentBuilderFactory.newInstance();
    }
    
    static public SCMDXMLParserFactory getInstance()
    {
        if(_instance == null)
        {
            _instance = new SCMDXMLParserFactory();
        }
        return _instance;
    }
    
    public SAXParser generateSAXParser() throws SCMDException
    {
        return this.generateSAXParser(false);
    }
    
    public DocumentBuilder generateDocumentBuilder() throws SCMDException
    {
        return generateDocumentBuilder(false);
    }
    
    public SAXParser generateSAXParser(boolean enableValidation) throws SCMDException
    {
        SAXParser parser = null;
        try
        {
            //parserFactory.setFeature("http://xml.org/sax/features/resolve-dtd-uris", false);
            _saxParserFactory.setValidating(enableValidation);
            parser = _saxParserFactory.newSAXParser();
            //parser.setProperty("validation", new Boolean(enableValidation));
        }
        catch (ParserConfigurationException e)
        {
            throw new SCMDException(e);
        }
        catch (SAXException e)
        {
            throw new SCMDException(e);
        }
        return parser;
    }
    
    public DocumentBuilder generateDocumentBuilder(boolean enableValidation)  throws SCMDException
    {
        DocumentBuilder builder = null;
        _domParserFactory.setValidating(false);
        try
        {
            builder = _domParserFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw new SCMDException(e);
        }
        return builder;
    }
    
    SAXParserFactory _saxParserFactory;
    DocumentBuilderFactory _domParserFactory;
    static SCMDXMLParserFactory _instance = null;

}


//--------------------------------------
// $Log: SCMDXMLParserFactory.java,v $
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.2  2004/07/25 11:27:52  leo
// flagのエラーを修正
//
// Revision 1.1  2004/07/24 14:35:31  leo
// *** empty log message ***
//
//--------------------------------------