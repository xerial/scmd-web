//--------------------------------------
// SCMDServer
// 
// ColLabelIndex.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.table.decollation.Decollator;

/**
 * @author leo
 *
 */
public class ColLabelIndex extends UpdateObserverBase 
{

    /**
     * @param table
     */
    public ColLabelIndex(Table table)
    {
        super();
    	changeTable(table);
    }
    
    public void invalidate()
    {
        if(_table != null)
            _table.removeObserver(this);
    }
    
    
    public void changeTable(Table table)
    {
        invalidate();
        _table = table;
        _indexToColLabelMap.clear();
        _colLabelToIndexMap.clear();
        _table.addObserver(this);
        
        if(_table.getColSize() < 1)
            return;
        // set index        
        for(int col=0; col<_table.getColSize(); col++)
        {
            addColIndex(_table.get(0, col).toString(), col);
        }
    }
    
    public TableIterator getVerticalIterator(String colLabel)
    {
        int colIndex = getColIndex(colLabel);
        if(colIndex != -1)
            return new VerticalIterator(_table, colIndex, 1, _table.getRowSize());
        else
            return new VerticalIterator(_table, 0, 0, 0);
    }
    
    public Collection getColLabelList()
    {
        return _indexToColLabelMap.values();
    }
    
    public void decollateCol(String colLabel, Decollator decollator) {
        int col = getColIndex(colLabel);
        for (int row = 1; row < _table.getRowSize(); row++)
        {
            _table.set(row, col, decollator.decollate(_table.get(row, col)));
        }
    }

    protected void addColIndex(String label, int index)
    {
        Integer indexObj = new Integer(index);
        _colLabelToIndexMap.put(label, indexObj);
        _indexToColLabelMap.put(indexObj, label);
    }
    protected void removeColIndex(int index)
    {
        Integer indexObj = new Integer(index);
        String value = (String) _indexToColLabelMap.get(indexObj);
        if(value != null)
        {
            _colLabelToIndexMap.remove(value);    
        }
        _indexToColLabelMap.remove(indexObj);
    }
    
    public TableElement get(int row, String colLabel)
    {
        return _table.get(row, getColIndex(colLabel));
    }
    
    public void set(int row, String colLabel, TableElement tableElement)
    {
        _table.set(row, getColIndex(colLabel), tableElement);
    }


    public int getColIndex(String colLabel) {
        Integer index = (Integer) _colLabelToIndexMap.get(colLabel);
        return index == null ? -1 : index.intValue();
    }

    
    HashMap _colLabelToIndexMap = new HashMap();
    TreeMap _indexToColLabelMap = new TreeMap();


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColInsertion(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterColInsertion(Table table, int col, Object[] colElement) {
        // colより大きいindexがついているものは、１ずつずらす
        SortedMap updateRegion = _indexToColLabelMap.tailMap(new Integer(col));
        TreeMap updatedMap = new TreeMap();
        for(Iterator it = updateRegion.keySet().iterator(); it.hasNext(); )
        {
            Integer key = (Integer) it.next();
            String value = (String) updateRegion.get(key);
            Integer newKey = new Integer(key.intValue() + 1);
            updatedMap.put(newKey, updateRegion.get(key));
            
            _colLabelToIndexMap.remove(value);
            _colLabelToIndexMap.put(value, newKey);
        }
        updateRegion.clear();
        _indexToColLabelMap.putAll(updatedMap);
        
        if(colElement.length > 0)
        {
            addColIndex(colElement[0].toString(), col);
        }
    }


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterRowAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterRowAddition(Table table, int row, Object[] rowElement) {
        // ラベル行の追加
        if(row == 0)
        {
            if(table.getColSize() < 1)
                return;
            // set index        
            for(int col=0; col<rowElement.length; col++)
            {
                addColIndex(rowElement[col].toString(), col);
            }
        }
    }


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterColAddition(Table table, int col, Object[] colElement) {
        if(colElement.length <= 0)
            return;
        TableElement labelElement = Table.translate(colElement[0]);
        addColIndex(labelElement.toString(), _table.getColSize() - 1);
    }


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterRowDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void afterRowDeletion(Table table, int row) {
        if(row == 0)
        {
            throw new UnsupportedOperationException("deletion of the row having col labels is not supported");
        }
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void afterColDeletion(Table table, int col) {
        removeColIndex(col);
        // colより大きいindexがついているものは、1つ前にずらす
        SortedMap updateRegion = _indexToColLabelMap.tailMap(new Integer(col));
        TreeMap updatedMap = new TreeMap();
        for(Iterator it = updateRegion.keySet().iterator(); it.hasNext(); )
        {
            Integer key = (Integer) it.next();
            String value = (String) updateRegion.get(key);
            Integer newKey = new Integer(key.intValue() - 1);
            updatedMap.put(newKey, updateRegion.get(key));
            
            _colLabelToIndexMap.remove(value);
            _colLabelToIndexMap.put(value, newKey);
        }
        updateRegion.clear();
        _indexToColLabelMap.putAll(updatedMap);
    }


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterUpdate(lab.cb.scmd.web.table.Table, int, int, lab.cb.scmd.web.table.TableElement)
     */
    public void afterUpdate(Table table, int row, int col, TableElement element) {
        if(row == 0)
        {
            // update the col label
            String previousLabel = (String) _indexToColLabelMap.get(new Integer(col));
            if(previousLabel != null)
            {
                // delete previous label
                _colLabelToIndexMap.remove(previousLabel);
            }
            // add a col index
            addColIndex(element.toString(), col);
        }

    }
    
    
    
    public void afterRowInsertion(Table table, int row, Object[] rowElement) {
        
        if(row == 0)
        {
            throw new UnsupportedOperationException("insertion before the indexed row is not supported");
        }
    }
    
    Table _table = null;
}


//--------------------------------------
// $Log: ColLabelIndex.java,v $
// Revision 1.7  2004/12/10 05:15:49  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.6  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
// Revision 1.5  2004/08/24 04:23:18  leo
// merge
//
// Revision 1.4.2.2  2004/08/24 04:04:42  leo
// Tableのindex周り、pasteが完成
//
// Revision 1.4.2.1  2004/08/24 02:36:12  leo
// ColIndexが完成
//
// Revision 1.4  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.3  2004/08/09 09:17:11  leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.2  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
// Revision 1.1  2004/08/06 12:17:22  leo
// Decolator Pattern で、rowIndex, colIndexを実現
// Visitor Patternで、Table内探索を行う
//
//--------------------------------------