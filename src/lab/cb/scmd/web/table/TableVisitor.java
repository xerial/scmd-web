//--------------------------------------
// SCMDServer
// 
// TableVisitor.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** Table����TableElement�����񂵂Ȃ���A�����𐶐����邽�߂�interface
 * @author leo
 *
 */
public interface TableVisitor
{
    public void visit(Table table);
    public void visit(TableElementList list);
    public void visit(Tag tag);
    public void visit(SelfClosedTag tag);
    public void visit(LeafTableElement element);
}


//--------------------------------------
// $Log: TableVisitor.java,v $
// Revision 1.8  2004/08/24 07:36:15  leo
// AbstractTable��p��
//
// Revision 1.7  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.6  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
// Revision 1.5  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.4  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.3  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
// Revision 1.2  2004/08/06 12:17:22  leo
// Decolator Pattern �ŁArowIndex, colIndex������
// Visitor Pattern�ŁATable���T�����s��
//
// Revision 1.1  2004/08/02 07:56:25  leo
// ������
//
//--------------------------------------