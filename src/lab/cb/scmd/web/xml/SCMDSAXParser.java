//--------------------------------------
// SCMDServer
// 
// SCMDSAXParser.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lab.cb.scmd.exception.SCMDException;

/**
 * @author leo
 *  
 */
public class SCMDSAXParser extends XMLParser
{

    InputStream    _inputStream;
    DefaultHandler _xmlHandler;
    SAXParser      _parser;

    /**
     *  
     */

    public SCMDSAXParser(DefaultHandler xmlHandler) throws SCMDException
    {
        setup(xmlHandler, false);
    }
    
    public SCMDSAXParser(DefaultHandler xmlHandler, boolean enableValidation) throws SCMDException
    {
        setup(xmlHandler, enableValidation);
    }
    
    void setup(DefaultHandler xmlHandler, boolean enableValidation) throws SCMDException
    {
        _xmlHandler = xmlHandler;

        _parser = SCMDXMLParserFactory.getInstance().generateSAXParser(enableValidation);
    }
    
    public void setInputStream(InputStream inputStream)
    {
        _inputStream = inputStream;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void parse()
    {
        try
        {
            _parser.parse(_inputStream, _xmlHandler);
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
    

}

//--------------------------------------
// $Log: SCMDSAXParser.java,v $
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.5  2004/08/02 07:27:12  leo
// XMLReaderThreadにPipe処理を追加できるように変更
//
// Revision 1.4  2004/07/25 11:27:52  leo
// flagのエラーを修正
//
// Revision 1.3  2004/07/22 07:11:46  leo
// xerces対応
//
// Revision 1.2  2004/07/21 08:07:05  leo
// temp commit
//
// Revision 1.1  2004/07/21 05:47:16  leo
// XML関係のモジュールを追加
//
//--------------------------------------
