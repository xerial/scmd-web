//--------------------------------------
// SCMDServer
// 
// SCMDResultHandlerTest.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.io.IOException;
//import java.io.PipedInputStream;
//import java.io.PipedOutputStream;
//import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.SAXException;

//import lab.cb.scmd.db.mock.MockXMLQuery;
import lab.cb.scmd.exception.SCMDException;

import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class SCMDResultHandlerTest extends TestCase
{

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    
    public void testReader()
    {
        try
        {
            //XMLReader xmlReader = XMLReaderFactory.createXMLReader("org.");
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            SCMDResultHandler handler = new SCMDResultHandler();
            parser.parse("../web/spec/xml/orflist.xml", handler);
            //xmlReader.setContentHandler(handler);
            //xmlReader.parse("orflist.xml");
        }
        catch(SAXException e)
        {
            fail(e.getMessage());
        }
        catch(IOException e)
        {
            fail(e.getMessage());
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            fail(e.getMessage());
        }
        
    }
    
    /**@deprecated �d�l�ύX�̂��߁A�R���p�C�����ʂ�Ȃ��Ȃ��Ă��܂�
     * @throws SCMDException
     * @throws IOException
     */
    public void testPipedOutput() throws SCMDException, IOException
    {
        
        /*
         * 
        PipedInputStream input = new PipedInputStream();
        PipedOutputStream output = new PipedOutputStream(input);
        //XMLOutputter xmlout = new XMLOutputter(output);
        
        MockXMLQuery mockQueryEngine = new MockXMLQuery(true);
        
        SCMDSAXParser reader = new SCMDSAXParser(input, new EventDisplayHandler()); 
        Thread thread = new Thread(reader);
        thread.start();
        mockQueryEngine.getAvailableORF(output, new HashMap());
        //xmlout.closeStream();
        
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            fail(e.getMessage());
        }
        */
    }
    

}


//--------------------------------------
// $Log: SCMDResultHandlerTest.java,v $
// Revision 1.7  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.6  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.5  2004/08/11 07:28:37  leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.4  2004/08/09 03:39:23  sesejun
// ORFList��DB���琶��
//
// Revision 1.3  2004/07/26 19:33:31  leo
// Action�̏C���BDataSheet�y�[�W���H
//
// Revision 1.2  2004/07/21 14:49:44  leo
// remove warnings
//
// Revision 1.1  2004/07/21 05:47:16  leo
// XML�֌W�̃��W���[����ǉ�
//
//--------------------------------------