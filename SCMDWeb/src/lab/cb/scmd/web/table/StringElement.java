//--------------------------------------
// SCMDServer
// 
// StringElement.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;


/**
 * @author leo
 *
 */
public class StringElement extends LeafTableElementBase
{
    /**
     * 
     */
    public StringElement(String content)
    {
        super();
        _content = content;
    }

    public String toString()
    {
        return _content;
    }
    
    public void accept(TableVisitor v) {
        v.visit(this);
    }
    
    String _content;
}


//--------------------------------------
// $Log: StringElement.java,v $
// Revision 1.1  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
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