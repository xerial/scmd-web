/*
 * Created on 2004/08/13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.connect;

import lab.cb.scmd.db.common.DBConnect;
import lab.cb.scmd.db.common.ValueQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.web.exception.DBConnectException;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SCMDValueQuery implements ValueQuery {
	
    SCMDDBConnect _connection = null;
    
	public SCMDValueQuery () {
	    _connection = new SCMDDBConnect();
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.ValueQuery#getMaxPhotoPage(java.lang.String)
	 */
	public int getMaxPhotoPage(String orf) {
		BasicTable bt = null;
		try {
			bt = _connection.imagePageStatusQuery(orf.toUpperCase());
		} catch (SCMDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dbconnect.close();
		return bt.getRowSize();
	}



//	
//    /* (non-Javadoc)
//     * @see lab.cb.scmd.db.common.ValueQuery#getMaxPageOfSearchResults(java.lang.String, int)
//     */
//    public int getMaxPageOfSearchResults(String keyword, int numElementInThePage) {
//		SCMDDBConnect dbconnect = (SCMDDBConnect)getConnection();
//		
//		String exp = "'%" + keyword + "%'";
//		String whereClause = "WHERE systematicname" 
//		    + "ILIKE " + exp + " OR ILIKE " + exp + " OR ILIKE " + exp;
//		String sql = "SELECT COUNT(DISTINCT systematicname) FROM genename_20040714 " + whereClause; 
//			
//		int count = 0;
//		try {
//			Table countTable = dbconnect.getQueryResult(sql);
//			ColLabelIndex colLabelIndex = new ColLabelIndex(countTable);
//			count = Integer.parseInt(colLabelIndex.get(1, "count").toString());
//		} catch (SCMDException e) {
//			e.printStackTrace();
//		}
//		//dbconnect.close();
//		
//        return count;
//    }
}
