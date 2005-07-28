//--------------------------------------
// SCMDServer
// 
// VerticalIterator.java 
// Since: 2004/08/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;


import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.util.table.TableIterator;

/**
 * @author leo
 *
 */
class VerticalIterator implements TableIterator
{
	VerticalIterator(Table table, int colIndex)
	{
		_table = table;
		_colIndex = colIndex;
		_rowCursor = -1;
		_rowIndexBegin = 0;
		_rowIndexEnd = table.getRowSize()-1;
	}
	
	VerticalIterator(Table table, int colIndex, int rowIndexBegin, int rowIndexEnd)
	{
		_table = table;
		_colIndex = colIndex;
		_rowCursor = rowIndexBegin-1;
		_rowIndexBegin = rowIndexBegin;
		_rowIndexEnd = rowIndexEnd-1;
	}


	/* (non-Javadoc)
	 * @see lab.cb.scmd.util.table.TableIterator#hasNext()
	 */
	public boolean hasNext()
	{
		return _rowCursor < _rowIndexEnd;
	}
	

	/* (non-Javadoc)
	 * @see lab.cb.scmd.util.table.TableIterator#next()
	 */
	public Cell nextCell()
	{
		TableElement elem = _table.get(++_rowCursor, _colIndex);
		return new Cell(elem.toString());
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() 
	{
		VerticalIterator vi = new VerticalIterator(_table, _colIndex);
		vi._rowCursor = this._rowCursor;
		vi._rowIndexBegin = this._rowIndexBegin;
		vi._rowIndexEnd = this._rowIndexEnd;
		return vi;		
	}
	
	public int row()
	{
	    return _rowCursor;
	}
	public int col()
	{
	    return _colIndex;
	}
	
	

	Table _table;	
	int _rowCursor;

	int _colIndex;
	int _rowIndexBegin;
	int _rowIndexEnd;
    
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
        return nextCell();
    }

}


//--------------------------------------
// $Log: VerticalIterator.java,v $
// Revision 1.1  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
//--------------------------------------