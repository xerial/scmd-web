//--------------------------------------
// SCMDServer
// 
// DOMParser.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import lab.cb.scmd.exception.SCMDException;

/**
 * @author leo
 *
 */
public class DOMParser extends XMLParser
{
    Document _document;
    DocumentBuilder _builder; 
    
    public DOMParser() throws SCMDException
    {
        _builder = SCMDXMLParserFactory.getInstance().generateDocumentBuilder();
    }
    
    public void parse() {
        
        try
        {
            _document = _builder.parse(getInputStream());
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public Document getDocument()
    {
        return _document;
    }

}


//--------------------------------------
// $Log: DOMParser.java,v $
// Revision 1.2  2004/08/14 11:09:08  leo
// Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------