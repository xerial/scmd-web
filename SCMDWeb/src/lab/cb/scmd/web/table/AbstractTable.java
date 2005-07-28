//--------------------------------------
// SCMDServer
// 
// AbstractTable.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.List;

import lab.cb.scmd.web.table.decollation.Decollator;



/** @deprecated
 * @author leo
 *  
 */
public interface AbstractTable extends TableElement //, RowColIndexSupport
{
    public TableElement get(int row, int col) throws ArrayIndexOutOfBoundsException;

    /**
     * �e�[�u����TableComponent���Z�b�g����B �s��͈̔͂𒴂��đ}��
     * 
     * @param row
     * @param col
     * @param component
     */
    public void set(int row, int col, TableElement component) throws ArrayIndexOutOfBoundsException;
    public void set(int row, int col, Object obj) throws ArrayIndexOutOfBoundsException;


    public int getRowSize();

    public int getColSize();

    public void addRow(List elementList);

    public void addRow(Object[] element);

    public void addCol(List elementList);

    public void addCol(Object[] element);

    public void decollate(int row, int col, Decollator decollator);
    public void decollate(TableRange region, Decollator decollator);
    public void decollateRow(int row, Decollator decollator);
    public void decollateCol(int col, Decollator decollator);
    
    
    public TableCursor cursor();
    
    //public TableElement translate(Object obj);
    //    public Iterator getHorizontalIterator(int row);
    //    public Iterator getHorizontalIterator(String rowLabel);
    //    public Iterator getVerticalIterator(int col);
    //    public Iterator getVerticalIterator(String colLabel);
}

//--------------------------------------
// $Log: AbstractTable.java,v $
// Revision 1.10  2004/12/10 05:15:49  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.9  2004/08/24 07:36:15  leo
// AbstractTable��p��
//
// Revision 1.8  2004/08/24 06:58:09  leo
// �e�[�u����؂�o����view�𓾂郁�\�b�h��ǉ�
//
// Revision 1.7  2004/08/24 04:23:18  leo
// merge
//
// Revision 1.6.2.1  2004/08/23 07:08:26  leo
// branch������āAObserver�ɂ��Table���x���̍쐬�J�n
//
// Revision 1.6  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.5  2004/08/09 09:17:11  leo
// Table�͈͎̔w���TableRange���g���悤�ɂ���
//
// Revision 1.4  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
// Revision 1.3  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.2  2004/08/07 14:30:07  leo
// TableCursor��ǉ�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
// Revision 1.4 2004/08/06 12:17:22 leo
// Decolator Pattern �ŁArowIndex, colIndex������
// Visitor Pattern�ŁATable���T�����s��
//
//--------------------------------------
