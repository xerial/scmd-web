//--------------------------------------
// SCMDServer
// 
// Decollator.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;

/** TableElement��Decollation��킹��ɂ́Anew Decollation(TableElement)�Ƃ���K�v������B
 * �������A����A����Cell�����l������Decollation�𐶐�����̂́A�����������̂ŁA
 * ����Decollator�N���X�̃C���X�^���X���͂��߂ɐ������A
 * 
 * @author leo
 *
 */
public abstract class Decollator
{
    /** element�̏�ɁADecollation�����Ԃ����N���X�i�����TableElement�̔h���N���X�ɂȂ�j��Ԃ�
     * @param element
     * @return
     */
    public abstract TableElement decollate(TableElement element);
    
    /** Table�̃Z���ɗ^����ׂ������l��ǉ�����
     * @param properties
     * @return
     */
    protected Properties addCellProperties(Properties properties)
    {
        // �f�t�H���g�ł͉����Z�b�g�����ɕԂ�
        return properties;
    }

}


//--------------------------------------
// $Log: Decollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.4  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.3  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
//--------------------------------------