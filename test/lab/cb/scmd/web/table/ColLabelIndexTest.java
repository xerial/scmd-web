//--------------------------------------
// SCMDServer
// 
// ColLabelIndexTest.java 
// Since: 2004/08/24
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import lab.cb.scmd.util.stat.Statistics;
import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class ColLabelIndexTest extends TestCase
{
    Table _table;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        _table = new Table();
        _table.addRow(new Object[] {"name", "position", "age", "sex"});
        _table.addRow(new Object[] {"John", "manager", "25", "male"});
        _table.addRow(new Object[] {"Philipe", "student", "18", "male"});
    }
    
    public void testColLabelIndex()
    {
        Table table = new Table();
        ColLabelIndex colLabelIndex = new ColLabelIndex(table);
        
        table.addRow(new Object[] {"name", "position", "age", "sex"});
        table.addRow(new Object[] {"John", "manager", "25", "male"});
        
        assertTrue(colLabelIndex.getColIndex("name") == 0);
        assertTrue(colLabelIndex.getColIndex("position") == 1);
        assertTrue(colLabelIndex.getColIndex("age") == 2);
        assertTrue(colLabelIndex.getColIndex("sex") == 3);
    }
    
    public void testColLableIndex2()
    {
        Table table = new Table();
        table.addRow(new Object[] {"name", "position", "age", "sex"});
        table.addRow(new Object[] {"John", "manager", "25", "male"});
        
        ColLabelIndex colLabelIndex = new ColLabelIndex(table);
        assertTrue(colLabelIndex.getColIndex("name") == 0);
        assertTrue(colLabelIndex.getColIndex("position") == 1);
        assertTrue(colLabelIndex.getColIndex("age") == 2);
        assertTrue(colLabelIndex.getColIndex("sex") == 3);
    }
    
    public void testAfterUpdate()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        _table.set(0, 1, "affiliation");
        
        assertTrue(colLabelIndex.getColIndex("affiliation") == 1);
        assertTrue(colLabelIndex.getColIndex("position") == -1);
    }
    
    public void testAfterColInsertion()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        _table.insertCol(2, new Object[] {"address"});
     
        assertTrue(colLabelIndex.getColIndex("name") == 0);
        assertTrue(colLabelIndex.getColIndex("position") == 1);
        assertTrue(colLabelIndex.getColIndex("address") == 2);
        assertTrue(colLabelIndex.getColIndex("age") == 3);
        assertTrue(colLabelIndex.getColIndex("sex") == 4);
    }
    
    public void testAfterColDeletion()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        _table.removeCol(2);
        
        assertTrue(colLabelIndex.getColIndex("name") == 0);
        assertTrue(colLabelIndex.getColIndex("position") == 1);
        assertTrue(colLabelIndex.getColIndex("sex") == 2);
    }
    
    public void testAfterRowDeletion()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        try
        {
            _table.removeRow(0);
            
            fail("we cannot agree with the deletion of rows having col lables");
        }
        catch(UnsupportedOperationException e)
        {
            
        }
    }
    
    public void testAfterRowDeletion2()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        try
        {
            colLabelIndex.invalidate();
            
            _table.removeRow(0);

        }
        catch(UnsupportedOperationException e)
        {
            fail();
        }
    }
    
    public void testAfterTableAddition()
    {
        Table anotherTable = new Table();
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);

        anotherTable.addRow(new Object[] {"e-mail", "TEL"});
        
        _table.appendToRight(anotherTable);
        assertTrue(colLabelIndex.getColIndex("name") == 0);
        assertTrue(colLabelIndex.getColIndex("position") == 1);
        assertTrue(colLabelIndex.getColIndex("age") == 2);
        assertTrue(colLabelIndex.getColIndex("sex") == 3);
        assertTrue(colLabelIndex.getColIndex("e-mail") == 4);
        assertTrue(colLabelIndex.getColIndex("TEL") == 5);
    }
    
    public void testTableIterator()
    {
        ColLabelIndex colLabelIndex = new ColLabelIndex(_table);
        
        Statistics stat = new Statistics();
        double ave = stat.calcAverage(colLabelIndex.getVerticalIterator("age"));
        assertEquals((double) (25.0 + 18.0) / 2.0, ave, 0.001);
    }
}


//--------------------------------------
// $Log: ColLabelIndexTest.java,v $
// Revision 1.2  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
// Revision 1.1  2004/08/24 04:05:03  leo
// test追加
//
//--------------------------------------