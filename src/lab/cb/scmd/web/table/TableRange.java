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

/** Tableの範囲指定に使う。 
 * 閉区間で
 *  rowBegin <= row <= rowEnd
 *  colBein <= col <= colEnd
 * の範囲を指定。
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
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.1  2004/08/09 09:17:11  leo
// Tableの範囲指定にTableRangeを使うようにした
//
//--------------------------------------