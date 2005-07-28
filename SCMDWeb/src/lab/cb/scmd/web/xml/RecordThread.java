//--------------------------------------
// SCMDServer
// 
// RecordThread.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author leo
 *  
 */
public class RecordThread implements Pipe
{
    OutputStream          _out    = null;
    InputStream           _in     = null;
    ByteArrayOutputStream _buffer = new ByteArrayOutputStream();

    public RecordThread()
    {}

    public RecordThread(InputStream in, OutputStream out)
    {
        super();
        _out = out;
        _in = in;
    }

    public void run() {
        int readByte = -1;
        try
        {
            while ((readByte = _in.read()) != -1)
            {
                _buffer.write(readByte);
                _out.write(readByte);
            }
            _out.close();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }

    protected void finalize() {
        try
        {
            if(_out != null) _out.close();
            if(_in != null) _in.close();
            if(_buffer != null) _buffer.close();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }

    public String toString() {
        return _buffer.toString();
    }

    public void setInputStream(InputStream in) {
        _in = in;
    }

    public void setOutputStream(OutputStream out) {
        _out = out;
    }
}

//--------------------------------------
// $Log: RecordThread.java,v $
// Revision 1.1  2004/08/02 07:27:12  leo
// XMLReaderThreadÇ…PipeèàóùÇí«â¡Ç≈Ç´ÇÈÇÊÇ§Ç…ïœçX
//
//--------------------------------------
