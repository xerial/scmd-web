//--------------------------------------
// SCMDServer
// 
// TableTest.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.io.IOException;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.TabFormatter;
import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class TableTest extends TestCase
{
    void createDataFile(String fileName, String[][] matrix)
    {
        try
        {
        	TabFormatter formatter = new TabFormatter(fileName);
        	formatter.outputMatrix(matrix);
        	formatter.close();
        }
        catch (IOException e)
        {
            fail(e.getMessage());
        }
    }

    public void testLoadFromFile() throws SCMDException
    {
        String[][] array = { 
        		{ "a", "a1", "a2", "a3"}, 
        		{ "0", "1", "2", "3"},
                { "4.5", "NaN", "-1", "3"}, 
                { "5", "5", "3", "4"}};
        createDataFile("__testtable.tab", array);

        Table table = new Table("__testtable.tab");
        
        TableCursor cursor = table.cursor();
        assertEquals("a", cursor.get().toString());
        assertEquals("a1", cursor.right().toString());
        assertEquals("a2", cursor.right().toString());
        assertEquals("a3", cursor.right().toString());
        cursor.nextRowHead();        
        assertEquals("0", cursor.get().toString());
        assertEquals("1", cursor.right().toString());
        assertEquals("2", cursor.right().toString());
        assertEquals("3", cursor.right().toString());
        cursor.nextRowHead();        
        assertEquals("4.5", cursor.get().toString());
        assertEquals("NaN", cursor.right().toString());
        assertEquals("-1", cursor.right().toString());
        assertEquals("3", cursor.right().toString());
        cursor.nextRowHead();        
        assertEquals("5", cursor.get().toString());
        assertEquals("5", cursor.right().toString());
        assertEquals("3", cursor.right().toString());
        assertEquals("4", cursor.right().toString());
        
    }
}


//--------------------------------------
// $Log: TableTest.java,v $
// Revision 1.4  2004/08/24 04:05:03  leo
// test追加
//
// Revision 1.3  2004/08/07 11:54:45  leo
// Layout用のTableを作りました
//
// Revision 1.2  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
// Revision 1.1  2004/08/02 07:57:16  leo
// 未完成
//
//--------------------------------------