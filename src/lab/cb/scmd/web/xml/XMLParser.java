//--------------------------------------
// SCMDServer
// 
// XMLParser.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.InputStream;

/**
 * @author leo
 *
 */
abstract class XMLParser implements Runnable
{
    InputStream    _inputStream;
    /**
     * 
     */
    public void setInputStream(InputStream inputStream)
    {
        _inputStream = inputStream;
    }
    
    protected InputStream getInputStream()
    {
        return _inputStream;
    }

    public void run()
    {
        parse();
    }
    
    public abstract void parse();
    /*
    {
    	_parser.parse(_inputStream, ... );
    }
    */
}


//--------------------------------------
// $Log: XMLParser.java,v $
// Revision 1.1  2004/08/12 14:49:24  leo
// DBÇ∆ÇÃê⁄ë±äJén
//
//--------------------------------------