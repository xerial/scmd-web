//--------------------------------------
// SCMDServer
// 
// TableRange.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** Table�͈͎̔w��Ɏg���B 
 * ��Ԃ�
 *  rowBegin <= row <= rowEnd
 *  colBein <= col <= colEnd
 * �͈̔͂��w��B
 * 
 * @author leo
 *
 */
public class TableRange
{
    int rowBegin = 0;
    int colBegin = 0;
    int rowEnd = 0;
    int colEnd = 0;
    
    /**
     * 
     */
    public TableRange(int rowBegin, int colBegin, int rowEnd, int colEnd)
    {
        super();
        this.rowBegin = rowBegin;
        this.colBegin = colBegin;
        this.rowEnd = rowEnd;
        this.colEnd = colEnd;
    }

    
    public int getColBegin() {
        return colBegin;
    }
    public void setColBegin(int colBegin) {
        this.colBegin = colBegin;
    }
    public int getColEnd() {
        return colEnd;
    }
    public void setColEnd(int colEnd) {
        this.colEnd = colEnd;
    }
    public int getRowBegin() {
        return rowBegin;
    }
    public void setRowBegin(int rowBegin) {
        this.rowBegin = rowBegin;
    }
    public int getRowEnd() {
        return rowEnd;
    }
    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }
}


//--------------------------------------
// $Log: TableRange.java,v $
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.1  2004/08/09 09:17:11  leo
// Table�͈͎̔w���TableRange���g���悤�ɂ���
//
//--------------------------------------