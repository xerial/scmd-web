//--------------------------------------
// SCMD Project
// 
// Validator.java 
// Since:  2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.xml.XMLValidationHandler;
import lab.cb.scmd.web.xml.SCMDSAXParser;

/** @deprecated XMLReaderThread�̎d�l�ύX�̂��߁A�g���܂���
 * @author leo
 *
 */
abstract class Validator implements Runnable
{
    XMLOutputter					xmlout;
	XMLQuery						query;
	PipedInputStream				input;
	PipedOutputStream				output;
	XMLValidationHandler	errorReporter	= new XMLValidationHandler();
	SCMDSAXParser					streamParser;

	public Validator(XMLQuery query_) throws IOException, SCMDException
	{
		query = query_;
		input = new PipedInputStream();
		output = new PipedOutputStream(input);
		//streamParser = new SCMDSAXParser(input, errorReporter, true);
		xmlout = new XMLOutputter(output);
	}

    public XMLQuery getXMLQuery() {
        return query;
    }
    public OutputStream getOutputStream() {
        return output;
    }
    public XMLOutputter getXMLOutputter()
    {
        return xmlout;
    }
	public boolean validate() throws InterruptedException
	{
		Thread thread = new Thread(streamParser);
		thread.start();
		this.run();
		thread.join();
		return !(errorReporter.foundError());
	}

	public String getErrorMessage()
	{
		return errorReporter.getErrorMessage();
	}
	
	public List getXMLElementList(String tagName)
	{
	    return errorReporter.getXMLElementList(tagName);
	}

	public abstract void run();

}


//--------------------------------------
// $Log: Validator.java,v $
// Revision 1.6  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.5  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.4  2004/08/11 07:28:37  leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.3  2004/07/22 14:42:39  leo
// test�̎d�����኱�ύX
//
// Revision 1.2  2004/07/22 07:11:47  leo
// xerces�Ή�
//
// Revision 1.1  2004/07/21 14:49:12  leo
// Validation�̕����𕪗�
//
//--------------------------------------