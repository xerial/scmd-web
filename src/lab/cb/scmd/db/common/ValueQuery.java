//--------------------------------------
// SCMDServer
// 
// ValueQuery.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

/**
 * @author leo
 *
 */
public interface ValueQuery 
{
    int getMaxPhotoPage(String orf);
    
    //int getMaxPageOfSearchResults(String query, int numElementInThePage);
}


//--------------------------------------
// $Log: ValueQuery.java,v $
// Revision 1.3  2004/08/27 08:57:43  leo
// �����@�\��ǉ� page�̈ړ��͂܂�
//
// Revision 1.2  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQuery��instance��
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
//--------------------------------------