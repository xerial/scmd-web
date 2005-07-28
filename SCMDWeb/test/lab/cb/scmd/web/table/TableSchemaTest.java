//--------------------------------------
// SCMDServer
//
// TableSchemaTest.java
// Since: 2004/12/10
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.web.table;

import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class TableSchemaTest extends TestCase {

    Table _table;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        _table = new Table();
     
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        
    }
    
    
    public void testSchema()
    {
     /*
      * <lab.cb.scmd-base:table name="tableInstance" rowLabel="on">  <!-- schema付属のインスタンス？ -->
      * <lab.cb.scmd-base:table-style style="datasheet"/>  
      * <lab.cb.scmd-base:row-label-style style="label"/>
      * <lab.cb.scmd-base:row-style rowname="group" style="bold"/>
      * <lab.cb.scmd-base:row-style rowname="percentage" style="numeric">
      *   <lab.cb.scmd-base:format format="###.##"/>
      * </lab.cb.scmd-base:row-style>
      *  
      * </lab.cb.scmd-base:table>
      * 
      * 
      */   
        
    }
}



//--------------------------------------
// $Log: TableSchemaTest.java,v $
// Revision 1.1  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
//--------------------------------------

