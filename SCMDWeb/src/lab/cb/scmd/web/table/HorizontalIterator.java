//--------------------------------------
// SCMDServer
// 
// HorizontalIterator.java 
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
class HorizontalIterator implements TableIterator
{
	/**一行をフルにトレースするItratorを作成
	 * @param table
	 * @param rowIndex iteratorがたどる行
	 */
	protected HorizontalIterator(Table table, int rowIndex)
	{
		_table = table;
		_rowIndex = rowIndex;
		_colCursor = -1;
		_colIndexBegin = 0;
		_colIndexEnd = table.getColSize() - 1;
	}
	
	/** 一行の特定の範囲内を動くIterator
	 * @param table
	 * @param rowIndex iteratorがたどる行
	 * @param colIndexBegin 開始列番号
	 * @param colIndexEnd 終了列番号
	 */
	protected HorizontalIterator(Table table, int rowIndex, int colIndexBegin, int colIndexEnd)
	{
		_table = table;
		_rowIndex = rowIndex;
		_colCursor = colIndexBegin - 1;
		_colIndexBegin = colIndexBegin;
		_colIndexEnd = colIndexEnd - 1;		
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.util.table.TableIterator#hasNext()
	 */
	public boolean hasNext()
	{
		return _colCursor < _colIndexEnd;
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.util.table.TableIterator#next()
	 */
	public Cell nextCell()
	{
		TableElement elem = _table.get(_rowIndex, ++_colCursor);
		return new Cell(elem.toString());
	}

    
	public Object clone()
	{
		HorizontalIterator it = 
			new HorizontalIterator(_table, _rowIndex);
		it._colCursor = this._colCursor;
		it._colIndexBegin  = this._colIndexBegin;
		it._colIndexEnd = this._colIndexEnd;
		return it;
	}
	
	int _colCursor;
	Table _table;
	int _rowIndex;
	int _colIndexBegin;
	int _colIndexEnd;
    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
        return nextCell();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.util.table.TableIterator#row()
     */
    public int row() {
        return _rowIndex;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.util.table.TableIterator#col()
     */
    public int col() {
        return _colCursor;
    }	


}


//--------------------------------------
// $Log: HorizontalIterator.java,v $
// Revision 1.1  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
//--------------------------------------