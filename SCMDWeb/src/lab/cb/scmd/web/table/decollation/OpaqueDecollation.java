//--------------------------------------
// SCMDServer
// 
// OpaqueDecollation.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.web.table.LeafTableElement;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableVisitor;

/** Decollation�ƈႢ�AleafTableElement�Ƃ��āAvisitor�����̃m�[�h�Ɏ~�܂�A
 * toString���\�b�h�œ��e��]�����邱�Ƃ��ł���Decollation�̃x�[�X�N���X
 * @author leo
 *
 */
public abstract class OpaqueDecollation extends Decollation implements LeafTableElement
{
    /**
     * @param tableElement
     * @param decollator
     */
    protected OpaqueDecollation(TableElement tableElement, Decollator decollator)
    {
        super(tableElement, decollator);
    }
    
    public final void accept(TableVisitor v)
    {
        v.visit((LeafTableElement) this);
    }
    
    public abstract String toString();
}


//--------------------------------------
// $Log: OpaqueDecollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
//--------------------------------------