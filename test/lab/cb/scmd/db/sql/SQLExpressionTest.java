//-----------------------------------
// SCMDWeb Project
// 
// SQLExpressionTest.java 
// Since: 2005/02/01
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.sql;

import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class SQLExpressionTest extends TestCase
{
    
    public void testAssign()
    {
        SQLExpression sql = new SQLExpression("select * from $1");
        String assignedSQL = sql.assign("sampletable");
        assertEquals("select * from sampletable", assignedSQL);        
    }
    
    public void testAssign2()
    {
        SQLExpression sql = new SQLExpression("select * from $1 where id=$2 and name=$2");
        String assignedSQL = sql.assign("sampletable", "'leo'");
        assertEquals("select * from sampletable where id='leo' and name='leo'", assignedSQL);        
    }
    
    public void testAssignShortCut()
    {
        assertEquals("select * from sampletable", 
                SQLExpression.assignTo("select * from $1", "sampletable"));        
    }
    
    public void testAssign3()
    {
        assertEquals("select $11 from t1", 
                SQLExpression.assignTo("select $11 from t1", "distinct p")
        );
    }
    public void testAssign4()
    {
        assertEquals("select distinct p from t1", 
                SQLExpression.assignTo("select $11 from t1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "distinct p")
        );
    }
    
}
