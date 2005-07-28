//--------------------------------------
// SCMDServer
// 
// XMLValidationHandler.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author leo
 *
 */
public class XMLValidationHandler extends XMLContentTestHandler
{
    ByteArrayOutputStream _buffer;
    PrintStream _err;
    boolean	_foundError = false;
    /**
     * 
     */
    public XMLValidationHandler()
    {
        super();
    }
    
    public boolean foundError()
    {
        return _foundError;
    }
    
    public String getErrorMessage()
    {
        return _buffer.toString();
    }
    
    public void startDocument() throws SAXException
    {
        super.startDocument();
        
        _buffer = new ByteArrayOutputStream();
        _err = new PrintStream(_buffer);
        _foundError = false;
    }
    
    
    public void error(SAXParseException arg0) throws SAXException
    {
        _err.println("[error]:\t" + arg0.getMessage());
        _foundError = true;
    }
    public void fatalError(SAXParseException arg0) throws SAXException
    {
        _err.println("[fatalError]:\t" + arg0.getMessage());
        _foundError = true;
    }


    public void warning(SAXParseException arg0) throws SAXException
    {
        _err.println("[warning]:\t" + arg0.getMessage());
    }
}


//--------------------------------------
// $Log: XMLValidationHandler.java,v $
// Revision 1.1  2004/07/22 07:11:46  leo
// xercesëŒâû
//
// Revision 1.2  2004/07/21 14:48:24  leo
// testópÇ…èCê≥
//
// Revision 1.1  2004/07/21 08:07:05  leo
// temp commit
//
//--------------------------------------