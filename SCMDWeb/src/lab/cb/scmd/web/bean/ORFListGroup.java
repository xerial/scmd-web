//--------------------------------------
// SCMDServer
//
// ORFListGroup.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

//import java.util.AbstractMap;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//import lab.cb.scmd.db.common.XMLQuery;
//import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.web.common.SCMDConfiguration;
//import lab.cb.scmd.web.xml.ORFListReaderSAXHandler;
////import lab.cb.scmd.web.xml.RecordThread;
//import lab.cb.scmd.web.xml.SCMDSAXParser;
//import lab.cb.scmd.web.xml.XMLReaderThread;

/** @deprecated DOM���g���悤�ɂ��ĕK�v�Ȃ��Ȃ�܂���
 * @author leo 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
//public class ORFListGroup {
//
//    List groupList = new LinkedList();
//    
//    /**
//     * 
//     */
//    public ORFListGroup() {
//     
//    }
//    
//    public List loadORFList(AbstractMap groupToPageMap)
//    {
//
//        ORFListReaderSAXHandler orfListReader = new ORFListReaderSAXHandler();
//
//        try {
//            SCMDSAXParser parser = new SCMDSAXParser(orfListReader);  
//            XMLReaderThread queryEngine = new XMLReaderThread(parser);
//            XMLQuery query = (XMLQuery)SCMDConfiguration.getXMLQueryInstance();
//            //RecordThread recorder = new RecordThread();
//            //queryEngine.addPipe(recorder);
//            queryEngine.start();
//            query.getAvailableORF(queryEngine.getOutputStream(), groupToPageMap);
//            queryEngine.join();
//        } catch (SCMDException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Collection c = orfListReader.getGroupNameList();
//        for(Iterator it = c.iterator(); it.hasNext(); )
//        {
//            String group = (String) it.next();
//            groupList.add(new ORFList(group, orfListReader.getYeastGeneList(group), orfListReader.getPageStatus(group)));
//        }
//        return getGroupList();
//    }
//    
//    public List getGroupList()
//    {
//        return groupList;
//    }
//
//    
//}
//

//--------------------------------------
// $Log: ORFListGroup.java,v $
// Revision 1.13  2004/08/26 08:45:52  leo
// Query�̒ǉ��B selection�̏C��
//
// Revision 1.12  2004/08/15 02:51:41  sesejun
// Mock����Ăяo�����ł��Ȃ��Ȃ��Ă����L���X�g�o�O���C��
//
// Revision 1.11  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.10  2004/08/13 10:00:20  sesejun
// QueryAPI�Ή�
// SCMDServer.config.sample �Ɂ@POSTGRESQL_* ��ǉ����A�Ή��B
//
// Revision 1.9  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.8  2004/08/11 07:28:37  leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.7  2004/08/11 05:47:13  leo
// XMLOutputter�ɃN�G���̌��ʂ��o�͂���̂ł͂Ȃ��A������OutputStream�ɂ��܂���
//
// Revision 1.6  2004/08/09 13:47:03  leo
// �ʐ^���Ȃ��Ƃ��A��։摜��\������悤�ɂ���
//
// Revision 1.5  2004/08/09 12:26:42  leo
// Comment��ǉ�
//
// Revision 1.4  2004/08/09 03:39:23  sesejun
// ORFList��DB���琶��
//
// Revision 1.3  2004/07/26 19:33:31  leo
// Action�̏C���BDataSheet�y�[�W���H
//
// Revision 1.2  2004/07/26 14:20:24  leo
// *** empty log message ***
//
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page�p�̃N���X��ǉ�
//
//--------------------------------------
