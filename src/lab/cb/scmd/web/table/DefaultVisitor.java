//--------------------------------------
// SCMDServer
// 
// DefaultVisitor.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Iterator;

/**
 * �e�[�u���⃊�X�g��H��ȊO�͉������Ȃ�Visitor
 * 
 * @author leo
 *  
 */
public class DefaultVisitor implements TableVisitor
{ 
    public DefaultVisitor()
    {}

    public void visit(Table table) {
        // �f�t�H���g�ł̓e�[�u�������������ɖK��
        for (int row = 0; row < table.getRowSize(); row++)
        {
            for (int col = 0; col < table.getColSize(); col++)
            {
                TableElement elem = table.get(row, col);
                elem.accept(this);
            }
        }
    }
    
    public void visit(Tag tag)
    {
        tag.getBodyContent().accept(this);
    }
    
    
    public void visit(SelfClosedTag tag) {
        // do nothing
    }
    
    public void visit(LeafTableElement element)
    {
        // do nothing
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableVisitor#visit(lab.cb.scmd.web.table.TableElement)
     */
    public void visit(TableElementList list) 
    {
        for(Iterator it = list.iterator(); it.hasNext(); )
        {
            TableElement elem = (TableElement) it.next();
            elem.accept(this);
        }
    }


}

//--------------------------------------
// $Log: DefaultVisitor.java,v $
// Revision 1.10  2004/08/24 07:36:15  leo
// AbstractTable��p��
//
// Revision 1.9  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.8  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
// Revision 1.7  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.6  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.5  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
// Revision 1.4 2004/08/06 12:19:24 leo
// table�̒T���̎d�������������ɏC��
//
// Revision 1.3 2004/08/06 12:18:10 leo
// comment���C��
//
// Revision 1.2 2004/08/06 12:17:22 leo
// Decolator Pattern �ŁArowIndex, colIndex������
// Visitor Pattern�ŁATable���T�����s��
//
// Revision 1.1 2004/08/02 07:56:25 leo
// ������
//
//--------------------------------------
