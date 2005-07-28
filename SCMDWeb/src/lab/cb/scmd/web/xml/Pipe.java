//--------------------------------------
// SCMDServer
// 
// Pipe.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.OutputStream;
import java.io.InputStream;

/**
 * @author leo
 *
 */
public interface Pipe extends Runnable
{
    public void setOutputStream(OutputStream out);
    public void setInputStream(InputStream in);
}


//--------------------------------------
// $Log: Pipe.java,v $
// Revision 1.1  2004/08/02 07:27:12  leo
// XMLReaderThread‚ÉPipeˆ—‚ğ’Ç‰Á‚Å‚«‚é‚æ‚¤‚É•ÏX
//
//--------------------------------------