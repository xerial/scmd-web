//--------------------------------------
// SCMDServer
// 
// TableCursorImpl.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/**
 * @author leo
 *
 */
public class TableCursorImpl implements TableCursor
{
    Table _table = null;
    int _row = 0;
    int _col = 0;
    /**
     * 
     */
    public TableCursorImpl(Table table)
    {
        _table = table;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#get()
     */
    public TableElement get() {
        return _table.get(_row, _col);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#set(lab.cb.scmd.web.table.TableElement)
     */
    public void set(TableElement element) {
        _table.set(_row, _col, element);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#nextRowHead()
     */
    public void nextRowHead() {
        _row++;
        _col = 0;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#nextColHead()
     */
    public void nextColHead() {
        _col++;
        _row = 0;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#right()
     */
    public TableElement right() {
        _col++;
        return get();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#left()
     */
    public TableElement left() {
        _col--;
        return get();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#up()
     */
    public TableElement up() {
        _row--;
        return get();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#down()
     */
    public TableElement down() {
        _row++;
        return get();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#hasRight()
     */
    public boolean hasRight() {
        return (_col + 1) < _table.getColSize(); 
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#hasLeft()
     */
    public boolean hasLeft() {
        return (_col >= 1) && (_col - 1) < _table.getColSize(); 
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#hasUp()
     */
    public boolean hasUp() {
        return (_row >= 1) && (_row - 1) < _table.getRowSize();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableCursor#hasDown()
     */
    public boolean hasDown() {
        return (_row + 1) < _table.getRowSize();
    }

}


//--------------------------------------
// $Log: TableCursorImpl.java,v $
// Revision 1.2  2004/08/24 07:36:15  leo
// AbstractTable‚ð”pŠü
//
// Revision 1.1  2004/08/07 14:30:07  leo
// TableCursor‚ð’Ç‰Á
//
//--------------------------------------