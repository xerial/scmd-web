//--------------------------------------
// SCMDServer
// 
// QueryAPI.java 
// Since: 2004/08/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import lab.cb.scmd.web.exception.DBConnectException;

/** DBへの問い合わせには、接続を保持する機能が必要 
 * 
 * @author leo
 *
 */
public interface QueryAPI
{
    DBConnect getConnection() throws DBConnectException;
    void setConnection(DBConnect connection);
    
}


//--------------------------------------
// $Log: QueryAPI.java,v $
// Revision 1.2  2004/08/14 11:23:25  sesejun
// add DBConnectionException handling
//
// Revision 1.1  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQueryのinstanceに
//
//--------------------------------------