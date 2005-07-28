//--------------------------------------
// SCMDServer
// 
// MockQueryAPI.java 
// Since: 2004/08/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

import lab.cb.scmd.db.common.DBConnect;
import lab.cb.scmd.db.common.QueryAPI;

/**
 * @author leo
 *
 */
public class MockQueryAPI implements QueryAPI
{

    /**
     * 
     */
    public MockQueryAPI()
    {
        super();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.QueryAPI#getConnection()
     */
    public DBConnect getConnection() {
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.QueryAPI#setConnectino(lab.cb.scmd.db.common.DBConnect)
     */
    public void setConnection(DBConnect connection) {
    }

}


//--------------------------------------
// $Log: MockQueryAPI.java,v $
// Revision 1.1  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQuery‚Ìinstance‚É
//
//--------------------------------------