//--------------------------------------
// SCMDServer
// 
// LeafTableElement.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** ���ɁA�K�w�\���������Ȃ�TableElement�ŁAtoString()�Œ��g��]������ڈ�ƂȂ�TableElement
 * @author leo
 *
 */
public interface LeafTableElement extends TableElement
{
    public String toString();
    
}

//--------------------------------------
// $Log: LeafTableElement.java,v $
// Revision 1.3  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.2  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
//--------------------------------------