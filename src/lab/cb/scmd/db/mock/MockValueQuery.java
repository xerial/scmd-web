//--------------------------------------
// SCMDServer
// 
// MockValueQuery.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

import lab.cb.scmd.db.common.ValueQuery;

/**
 * @author leo
 *
 */
public class MockValueQuery extends MockQueryAPI implements ValueQuery
{

    /**
     * 
     */
    public MockValueQuery()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.ValueQuery#getMaxPhotoPage(java.lang.String)
     */
    public int getMaxPhotoPage(String orf) {
        return 30;
    }

}


//--------------------------------------
// $Log: MockValueQuery.java,v $
// Revision 1.2  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQueryÇÃinstanceÇ…
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBÇ∆ÇÃê⁄ë±äJén
//
//--------------------------------------