/*
 * Created on 2004/08/13
 *
 */
package lab.cb.scmd.db.connect;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import lab.cb.scmd.algorithm.Algorithm;
import lab.cb.scmd.db.common.DBConnect;
import lab.cb.scmd.db.common.QueryRange;
import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.exception.DBConnectException;
import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

public class SCMDTableQuery extends ConnectionHolder implements TableQuery {

	public SCMDTableQuery () {
		
	}
	
	public DBConnect getConnection()
	{
	    try
	    {
	        if(_connection == null)
	            _connection = new SCMDDBConnect();
	        else if(_connection.isClosed())
	        {
	            // çƒê⁄ë±
	            _connection = new SCMDDBConnect();
	        }
	    }
	    catch(SCMDException e)
	    {
	        e.what();
            try {
				// çƒê⁄ë±
				_connection = new SCMDDBConnect();
			} catch (DBConnectException e1) {
				e.what();
			}
	    }
	    return _connection;
	}

	
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String, int, int)
	 */
	public Table getShapeDataSheet(String strain, int photoID, int cellID, int sheetType) {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = ((SCMDDBConnect)getConnection()).getDataSheet(strain, photoID, cellID,
					DataSheetType.getDetailParameters(sheetType));
		} catch (InvalidSQLException e) {
			return null;
		}
        return table;
	}
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String, int)
	 */
	public Table getShapeDataSheet(String strain, int photoID, int sheetType) {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = ((SCMDDBConnect)getConnection()).getDataSheet(strain, photoID, 
					DataSheetType.getParameters(sheetType));
		} catch (InvalidSQLException e) {
			return null;
		}
        return table;
	}
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeStatLine(java.lang.String)
	 */
	public Map getShapeStatLine(String strain) throws SCMDException {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = ((SCMDDBConnect)getConnection()).getAverageShapeStatSheet(strain);
		} catch (InvalidSQLException e) {
			TreeMap map = new TreeMap();
			return map;
		}
		TreeMap map = new TreeMap();
		
		RowLabelIndex rowIndex = new RowLabelIndex(table);
		int row = rowIndex.getRowIndex(strain);
		if(row == -1)
		    return map;
        
        for(int col=0; col<table.getColSize(); col++)
        {
            map.put(table.get(0, col).toString(), table.get(row, col).toString());
        }
        return map;
	}

	
    public Table getShapeStatOfParameter(String param) {
        Table table;
        try {
            table = ((SCMDDBConnect)getConnection()).getShapeStatOfParameter(param);
        } catch (InvalidSQLException e) {
            e.what();
            return null;
        }
        return table;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String, int, int, java.lang.String)
     */
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName) {
        
        return getGroupByDatasheet(orf, sheetType, stainType, groupName, -1, 10);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String, int, int, java.lang.String, int, int)
     */
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName, int page, int numElementInAPage) {
        Vector paramList = new Vector();
        paramList.add("cell_local_id");
        Algorithm.initializeCollection(paramList, DataSheetType.PHOTO_INFO_PARAM);
        Algorithm.initializeCollection(paramList, DataSheetType.getParameters(sheetType));
        
        String selectParam = "";
        for(Iterator it = paramList.iterator(); it.hasNext(); )
        {
            selectParam += "\"" + (String) it.next() + "\", ";
        }
        selectParam = selectParam.substring(0, selectParam.length() - 2);
        String sql = "SELECT "  + selectParam + " FROM individual_20040813 WHERE strainname='" + orf.toUpperCase() + "' AND \""
        + StainType.getStainTypeName(stainType) +  "group\"='" + groupName +  "' ORDER BY cell_local_id";
        if(page > 0 && numElementInAPage > 0)
            sql += " LIMIT " + numElementInAPage + " OFFSET " + (numElementInAPage * (page-1)); 
        
        return evalSQL(sql);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupCount(java.lang.String, int, java.lang.String)
     */
    public Table getGroupCount(String orf, int stainType) {
        String sql = "SELECT \"" + StainType.getStainTypeName(stainType) + "group\", COUNT(cell_local_id) FROM individual_20040813 WHERE strainname='" 
        + orf.toUpperCase() + "' GROUP BY  \"" + StainType.getStainTypeName(stainType) + "group\"";
        
        return evalSQL(sql);
    }
    
    protected Table evalSQL(String sql)
    {
        Table result = null;
        SCMDDBConnect connection = (SCMDDBConnect) getConnection();
        try
        {
            result = connection.getQueryResult(sql);
        }
        catch(InvalidSQLException e)
        {
            e.what();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, java.lang.String)
     */
    public Table getAveragePlot(String param1, String param2) {

        String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " + quoteAttribute(param2) 
        + " FROM strain_20040730 RIGHT JOIN summary_20040719 USING(strainid)";
        
        return evalSQL(sql);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, lab.cb.scmd.db.common.QueryRange, java.lang.String, lab.cb.scmd.db.common.QueryRange)
     */
    public Table getAveragePlot(String param1, QueryRange range1, String param2, QueryRange range2) {

        String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " + quoteAttribute(param2) 
        + " FROM strain_20040730 RIGHT JOIN summary_20040719 USING(strainid) WHERE ";
        
        sql += quoteAttribute(param1) + " BETWEEN " + range1.getBegin() + " AND " + range1.getEnd() + " AND ";
        sql += quoteAttribute(param2) + " BETWEEN " + range2.getBegin() + " AND " + range2.getEnd();

        return evalSQL(sql);
    }
    
    protected String quoteAttribute(String attribute)
    {
        return "\"" + attribute + "\""; 
    }

}
