//--------------------------------------
// SCMDServer
// 
// RowLabelIndex.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.table.decollation.Decollator;

/**
 * 行のラベルに対するインデックス
 * 
 * label1 0 1 2 label2 1 3 4
 * 
 * というテーブルが与えられれば、 label1 -> 0行目 label2 -> 1行目 というのを記憶する
 * 
 * @author leo
 *  
 */
public class RowLabelIndex extends UpdateObserverBase 
{

    /**
     * @param table
     */
    public RowLabelIndex(Table table)
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
        _indexToRowLabelMap.clear();
        _rowLabelToIndexMap.clear();
        _table.addObserver(this);

        if(_table.getRowSize() < 1) return;
        // set index
        for (int row = 0; row < _table.getRowSize(); row++)
        {
            addRowIndex(_table.get(row, 0).toString(), row);
        }
    }
   
    public TableIterator getHorizontalIterator(String rowLabel)
    {
        int rowIndex = getRowIndex(rowLabel);
        if(rowIndex != -1)
            return new HorizontalIterator(_table, rowIndex, 1, _table.getColSize());
        else
            return new HorizontalIterator(_table, 0, 0, 0);
    }

    public void set(String rowLabel, int col, TableElement component) {
        _table.set(getRowIndex(rowLabel), col, component);
    }

    public TableElement get(String rowLabel, int col) {
        return _table.get(getRowIndex(rowLabel), col);
    }

    protected void addRowIndex(String label, int index) {
        Integer indexObj = new Integer(index);
        _rowLabelToIndexMap.put(label, indexObj);
        _indexToRowLabelMap.put(indexObj, label);
    }

    protected void removeRowIndex(int index) {
        Integer indexObj = new Integer(index);
        String value = (String) _indexToRowLabelMap.get(indexObj);
        if(value != null)
        {
            _rowLabelToIndexMap.remove(value);
        }
        _indexToRowLabelMap.remove(indexObj);

    }

    public int getRowIndex(String rowLabel) {
        Integer index = (Integer) _rowLabelToIndexMap.get(rowLabel);
        return index == null ? -1 : index.intValue();
    }

    public void decollateRow(String rowLabel, Decollator decollator) {
        int row = getRowIndex(rowLabel);
        for (int col = 1; col < _table.getColSize(); col++)
        {
            _table.set(row, col, decollator.decollate(_table.get(row, col)));
        }
    }

    public void afterColAddition(Table table, int col, Object[] colElement) {
        if(col == 0)
        {
            for (int i = 0; i < colElement.length; i++)
            {
                addRowIndex(colElement[i].toString(), i);
            }
        }
    }

    public void afterColDeletion(Table table, int col) {
        if(col == 0) { throw new UnsupportedOperationException(
                "deletion of the indexed col is not supported. remove index first"); }
    }

    public void afterColInsertion(Table table, int col, Object[] colElement) {
        if(col == 0) { throw new UnsupportedOperationException("insertion to the indexed col is not supported."); }
    }

    public void afterRowAddition(Table table, int row, Object[] rowElement) {
        if(rowElement.length <= 0) return;

        addRowIndex(rowElement[0].toString(), row);
    }

    public void afterRowDeletion(Table table, int row) {
        removeRowIndex(row);
        // rowより大きいindexがついているものは、1つ前にずらす
        SortedMap updateRegion = _indexToRowLabelMap.tailMap(new Integer(row));
        TreeMap updatedMap = new TreeMap();
        for (Iterator it = updateRegion.keySet().iterator(); it.hasNext();)
        {
            Integer key = (Integer) it.next();
            String value = (String) updateRegion.get(key);
            Integer newKey = new Integer(key.intValue() - 1);
            updatedMap.put(newKey, updateRegion.get(key));

            _rowLabelToIndexMap.remove(value);
            _rowLabelToIndexMap.put(value, newKey);
        }
        updateRegion.clear();
        _indexToRowLabelMap.putAll(updatedMap);
    }

    public void afterRowInsertion(Table table, int row, Object[] rowElement) {
        // rowより大きいindexがついているものは、1つずらす
        SortedMap updateRegion = _indexToRowLabelMap.tailMap(new Integer(row));
        TreeMap updatedMap = new TreeMap();
        for (Iterator it = updateRegion.keySet().iterator(); it.hasNext();)
        {
            Integer key = (Integer) it.next();
            String value = (String) updateRegion.get(key);
            Integer newKey = new Integer(key.intValue() + 1);
            updatedMap.put(newKey, updateRegion.get(key));

            _rowLabelToIndexMap.remove(value);
            _rowLabelToIndexMap.put(value, newKey);
        }
        updateRegion.clear();
        _indexToRowLabelMap.putAll(updatedMap);

        if(rowElement.length > 0) addRowIndex(rowElement[0].toString(), row);
    }

    public void afterUpdate(Table table, int row, int col, TableElement element) {
        if(col == 0)
        {
            // update the row label
            String previousLabel = (String) _indexToRowLabelMap.get(new Integer(row));
            if(previousLabel != null)
            {
                // delete previous label
                _rowLabelToIndexMap.remove(previousLabel);
            }
            // insert a new label
            addRowIndex(element.toString(), row);
        }
    }

    protected HashMap _rowLabelToIndexMap = new HashMap();
    protected TreeMap _indexToRowLabelMap = new TreeMap();

    Table             _table = null;
}

//--------------------------------------
// $Log: RowLabelIndex.java,v $
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
// Revision 1.4.2.1  2004/08/24 04:04:42  leo
// Tableのindex周り、pasteが完成
//
// Revision 1.4 2004/08/09 12:26:15 leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.3 2004/08/09 09:17:11 leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.2 2004/08/07 11:48:43 leo
// Web用のTableクラス
//
// Revision 1.1 2004/08/06 12:17:22 leo
// Decolator Pattern で、rowIndex, colIndexを実現
// Visitor Patternで、Table内探索を行う
//
//--------------------------------------
