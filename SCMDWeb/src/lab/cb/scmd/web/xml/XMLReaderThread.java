//--------------------------------------
// SCMDServer
//
// XMLReaderThread.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * --(InputStream)--> XMLParser --> SAXParser.parse()
 * 
 * @author leo
 *
 */
public class XMLReaderThread 
{

	PipedInputStream				_input = new PipedInputStream();
	PipedOutputStream				_output = new PipedOutputStream();

	XMLParser						_xmlParser;
	Thread 	_thread;
	
	LinkedList _pipeList = new LinkedList();

    /**
     * @throws IOException
     * 
     */
//    public XMLReaderThread(DefaultHandler handler) throws SCMDException {
//        _handler = handler;
//        _streamParser = new SCMDSAXParser(_input, _handler, true);
//    }
//    

	public XMLReaderThread(XMLParser parser) 
	{
	    _xmlParser = parser;
	    _xmlParser.setInputStream(_input);
	}
	
    /** XMLを出力するOutputStreamを返す
     * @return
     */
    public OutputStream getOutputStream() {
        return _output;
    }
    
    /*
    public void setPipedInputStream(PipedInputStream input)
    {
        _input = input;
        _xmlParser.setInputStream(_input);
    }
    public void setPipedOutputStream(PipedOutputStream output)
    {
        _output = output;
    }
    */
	
    protected void connectPipes()
    {
        PipedOutputStream pipeOut = _output;
        try
	    {
            for(Iterator it = _pipeList.iterator(); it.hasNext(); )
            {
                Pipe pipe = (Pipe) it.next();
                PipedInputStream pipeIn = new PipedInputStream(pipeOut);
                pipe.setInputStream(pipeIn);
                
                pipeOut = new PipedOutputStream();
                pipe.setOutputStream(pipeOut);
                
                new Thread(pipe).start();
            }
            _input.connect(pipeOut);
	    }
	    catch(IOException e)
	    {
	        System.err.println(e);
	    }
    }
    
    public void addPipe(Pipe pipe)
    {
        _pipeList.add(pipe);
    }
    
	public void start()
	{
	    connectPipes();

	    _thread = new Thread(_xmlParser);
        _thread.start();
	}
	
	public void join() throws InterruptedException 
	{
	    try
	    {
	        _thread.join();
	    }
	    catch(InterruptedException e)
	    {
	        System.err.println(e.getMessage());
	    }
	}
	
}


//--------------------------------------
// $Log: XMLReaderThread.java,v $
// Revision 1.4  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.3  2004/08/11 05:47:13  leo
// XMLOutputterにクエリの結果を出力するのではなく、ただのOutputStreamにしました
//
// Revision 1.2  2004/08/02 07:27:12  leo
// XMLReaderThreadにPipe処理を追加できるように変更
//
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page用のクラスを追加
//
//--------------------------------------
