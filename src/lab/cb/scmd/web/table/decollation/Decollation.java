//--------------------------------------
// SCMDServer
// 
// Decollation.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableVisitor;

/** TableElement��cellProperty��AtoString�����邽�߂̃N���X
 * ����ł���̂ŁA��{�I�ɁAvisitor�́A
 * visit(Decollation) �Ƃ������\�b�h�͌Ăяo���ꂸ�A
 * ����̒��g��TableElement�ɁAVisitor���n����� (�i���g��TableElement).accept(visitor) )
 * 
 * <b>NOTICE:</b> �]���āAtoString()��visitor���]�����邽�߂ɂ́A����ɁA
 * LeafTableElement�C���^�[�t�F�[�X��implement���Ȃ��ƁADefaultVisitor���~�܂��Ă���Ȃ�
 * 
 * 
 *  
 * @author leo
 *
 */
public class Decollation implements TableElement 
{
    private TableElement _tableElement = null;
    Decollator _decollator = null;

    /**
     * @param tableElement
     */
    protected Decollation(TableElement tableElement, Decollator decollator)
    {
        _tableElement = tableElement;
        _decollator = decollator;
    }

    /* 
     * cellProperty�ɂ���Decollation�N���X���^���鑮���l���X�ŃZ�b�g����
     * �Z�b�g����l�́ADecollator�����悤�ɁA��������
     */
    public final Properties getCellProperty() {
        return _decollator.addCellProperties(_tableElement.getCellProperty());
    }
    

    public String toString() {
        return _tableElement.toString();
    }

    public void accept(TableVisitor v) {
        // �����through���āA���g�ɐ�����ڂ�
        _tableElement.accept(v);
    }
    
    public final Properties getProperties() {
        // Decollation�́A��{�I�ɑ����l�������Ȃ�
        return new Properties();
    }
    
        
    public final void setProperty(String property, String value) {
        // Decollation�́A��{�I�ɑ����l�������Ȃ�
        throw new UnsupportedOperationException("cannot set any property to the subclass of AbstractAttribute");
    }

    
    public final void setProperties(Properties properties) {
        // Decollation�́A��{�I�ɑ����l�������Ȃ�
        throw new UnsupportedOperationException("cannot set any property to the subclass of AbstractAttribute");
    }
    
    
    /**
     * @return  ����̒��g��TableElement
     */
    protected final TableElement getTableElement()
    {
        return _tableElement;
    }
}


//--------------------------------------
// $Log: Decollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.3  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
//--------------------------------------