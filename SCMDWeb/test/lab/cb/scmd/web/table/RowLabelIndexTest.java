//--------------------------------------
// SCMDServer
// 
// RowLabelIndexTest.java 
// Since: 2004/08/24
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import lab.cb.scmd.util.table.TableIterator;
import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class RowLabelIndexTest extends TestCase
{
    Table _table;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        _table = new Table();
        _table.addRow(new Object[] {"name", "position", "age", "sex"});
        _table.addRow(new Object[] {"John", "manager", "25", "male"});
        _table.addRow(new Object[] {"Philipe", "student", "18", "male"});
    }
    
    public void testRowLabelIndex()
    {
        Table table = new Table();
        RowLabelIndex rowLabelIndex = new RowLabelIndex(table);
        
        table.addRow(new Object[] {"name", "position", "age", "sex"});
        table.addRow(new Object[] {"John", "manager", "25", "male"});
        table.addRow(new Object[] {"Fiona", "adviser", "20", "female"});
        
        assertEquals(1, rowLabelIndex.getRowIndex("John"));
        assertEquals(2, rowLabelIndex.getRowIndex("Fiona"));
    }

    public void testRowLabelIndex2()
    {
        Table table = new Table();
        
        table.addRow(new Object[] {"name", "position", "age", "sex"});
        table.addRow(new Object[] {"John", "manager", "25", "male"});
        table.addRow(new Object[] {"Fiona", "adviser", "20", "female"});
        
        RowLabelIndex rowLabelIndex = new RowLabelIndex(table);
        assertEquals(1, rowLabelIndex.getRowIndex("John"));
        assertEquals(2, rowLabelIndex.getRowIndex("Fiona"));
    }
    
    public void testAfterUpdate()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        _table.set(1, 0, "Sam");
        
        assertTrue(rowLabelIndex.getRowIndex("Sam") == 1);
        assertTrue(rowLabelIndex.getRowIndex("John") == -1);
    }
    
    public void testAfterRowInsertion()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        _table.insertRow(1, new Object[] {"Charlie", "student"});
     
        assertTrue(rowLabelIndex.getRowIndex("Charlie") == 1);
        assertTrue(rowLabelIndex.getRowIndex("John") == 2);
        assertTrue(rowLabelIndex.getRowIndex("Philipe") == 3);
    }
    
    public void testAfterRowDeletion()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        _table.removeRow(1);
        
        assertTrue(rowLabelIndex.getRowIndex("John") == -1);
        assertTrue(rowLabelIndex.getRowIndex("Philipe") == 1);
    }
    
    public void testAfterColDeletion()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);

        try
        {
            _table.removeCol(0);
            
            fail("we cannot agree with the deletion of cols having row lables");
        }
        catch(UnsupportedOperationException e)
        {
            
        }
        
    }
    public void testAfterColDeletion2()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        try
        {
            rowLabelIndex.invalidate();
            _table.removeCol(0);
            
        }
        catch(UnsupportedOperationException e)
        {
            fail();
        }
   
    }
    
    public void testAfterTableAddition()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        
        Table anotherTable = new Table();
        anotherTable.addRow(new Object[] {"Lucy"});
        anotherTable.addRow(new Object[] {"King"});
        
        _table.appendToBottom(anotherTable);
        
        assertTrue(rowLabelIndex.getRowIndex("John") == 1);
        assertTrue(rowLabelIndex.getRowIndex("Philipe") == 2);
        assertTrue(rowLabelIndex.getRowIndex("Lucy") == 3);
        assertTrue(rowLabelIndex.getRowIndex("King") == 4);
    }

    public void testTableIterator()
    {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(_table);
        TableIterator ti = rowLabelIndex.getHorizontalIterator("Philipe");
        
        assertEquals("student", ti.nextCell().toString());
        assertEquals("18", ti.nextCell().toString());
        assertEquals("male", ti.nextCell().toString());
        assertFalse(ti.hasNext());
        
    }

}


//--------------------------------------
// $Log: RowLabelIndexTest.java,v $
// Revision 1.2  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
// Revision 1.1  2004/08/24 04:05:03  leo
// test追加
//
//--------------------------------------