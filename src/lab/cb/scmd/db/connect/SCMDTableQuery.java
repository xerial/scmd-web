/*
 * Created on 2004/08/13
 *
 */
package lab.cb.scmd.db.connect;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;


import org.apache.commons.dbutils.handlers.BeanListHandler;


import lab.cb.scmd.algorithm.Algorithm;
import lab.cb.scmd.db.common.QueryRange;
import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.datagen.ParamPair;
import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

public class SCMDTableQuery implements TableQuery {

    private SCMDDBConnect _connection = null;
	public SCMDTableQuery () 
    {
	    _connection = new SCMDDBConnect();
    }
	
//	public DBConnect getConnection()
//	{
//	    ConnectionServer.
//        
////	    try
////	    {
////	        if(_connection == null)
////	            _connection = new SCMDDBConnect();
////	        else if(_connection.isClosed())
////	        {
////	            // 再接続
////	            _connection = new SCMDDBConnect();
////	        }
////	    }
////	    catch(SCMDException e)
////	    {
////	        e.what();
////            try {
////				// 再接続
////				_connection = new SCMDDBConnect();
////			} catch (DBConnectException e1) {
////				e.what();
////			}
////	    }
////	    return _connection;
//	}

	
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String, int, int)
	 */
	public Table getShapeDataSheet(String strain, int photoID, int cellID, int sheetType) {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = _connection.getDataSheet(strain, photoID, cellID,
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
			table = _connection.getDataSheet(strain, photoID, DataSheetType.getParameters(sheetType));
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
			table = _connection.getAverageShapeStatSheet(strain);
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
            table = _connection.getShapeStatOfParameter(param);
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
        String sql = "SELECT "  + selectParam + " FROM " + SCMDConfiguration.getProperty("DB_INDIVIDUAL") + " WHERE strainname='" + orf.toUpperCase() + "' AND \""
        + StainType.getStainTypeName(stainType) +  "group\"='" + groupName +  "' ORDER BY cell_local_id";
        if(page > 0 && numElementInAPage > 0)
            sql += " LIMIT " + numElementInAPage + " OFFSET " + (numElementInAPage * (page-1)); 
        
        return evalSQL(sql);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupCount(java.lang.String, int, java.lang.String)
     */
    public Table getGroupCount(String orf, int stainType) {
        String sql = "SELECT \"" + StainType.getStainTypeName(stainType) + "group\", COUNT(cell_local_id) FROM " + SCMDConfiguration.getProperty("DB_INDIVIDUAL") + " WHERE strainname='" 
        + orf.toUpperCase() + "' GROUP BY  \"" + StainType.getStainTypeName(stainType) + "group\"";
        
        return evalSQL(sql);
    }
    
    protected Table evalSQL(String sql)
    {
        Table result = null;
        try {
//                  result = _connection.getQueryResult(sql);
            result = ConnectionServer.retrieveTable(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, java.lang.String)
     */
    public Table getAveragePlot(String param1, String param2) {
        String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " + quoteAttribute(param2) 
        + " FROM " + SCMDConfiguration.getProperty("DB_STRAIN") + " RIGHT JOIN summary_20040719 USING(strainid)";
        
        return evalSQL(sql);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, lab.cb.scmd.db.common.QueryRange, java.lang.String, lab.cb.scmd.db.common.QueryRange)
     */
    public Table getAveragePlot(String param1, QueryRange range1, String param2, QueryRange range2) {
        String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " + quoteAttribute(param2) 
        + " FROM " + SCMDConfiguration.getProperty("DB_STRAINTABLE") + " RIGHT JOIN summary_20040719 USING(strainid) WHERE ";
        
        sql += quoteAttribute(param1) + " BETWEEN " + range1.getBegin() + " AND " + range1.getEnd() + " AND ";
        sql += quoteAttribute(param2) + " BETWEEN " + range2.getBegin() + " AND " + range2.getEnd();

        return evalSQL(sql);
    }
    
    protected String quoteAttribute(String attribute)
    {
        return "\"" + attribute + "\""; 
    }

    /*
     * 現在、返るテーブルのcolumn(変数名key)にはパラメータ名を入れているが、group横断で検索を行いたい場合には、変更の必要あり
     */
    public Table getShapeZScoreTable(ParamPair[] paramSets) {
        String sql = "SELECT strainname";
        String sql_from = " FROM ";
        String sql_using = "";
        
        String sql_param = "SELECT id, displayname FROM " + SCMDConfiguration.getProperty("DB_PARAMETERLIST") + " WHERE ";
        for(int i = 0; i < paramSets.length; i++ ) {
            ParamPair pair = paramSets[i];
            if( i != 0 )
                sql_param += " OR ";
            sql_param += "id=" + pair.getParamid();
        }
        Table paramTable = evalSQL(sql_param);
        RowLabelIndex rowLabelIndex = new RowLabelIndex(paramTable);
        for(int i = 0; i < paramSets.length; i++ ) {
            ParamPair pair = paramSets[i];
            /* get parameter and group name */
            //String paramname = getParameterName(pair.getParamid());
            //String groupname = getGroupName(pair.getGroupid());
            String paramname = paramTable.get(rowLabelIndex.getRowIndex(pair.getParamid() + ""), 1).toString();
            String key = paramname;
            /* */
            sql += "," + quote(paramname);
            String subsql = "SELECT strainname, paramid, groupid, zscore AS " + quote(key) + " FROM " + SCMDConfiguration.getProperty("DB_ZSCORE") + " WHERE ";
            if( pair.getParamid() == -1 && pair.getGroupid() == -1 )
                continue;
            if( paramSets[i].getParamid() != -1 ) {
                subsql += "paramid=" + paramSets[i].getParamid();
                if( paramSets[i].getGroupid() != -1 ) {
                    subsql += " AND groupid=" + paramSets[i].getGroupid();
                }
            } else {
                subsql += "groupid=" + paramSets[i].getGroupid();
            }
            if( i == 0 ) {
                sql_from += "(" + subsql + ") AS s" + pair.getParamid();
            } else {
                sql_from += " LEFT JOIN (" + subsql + ") AS s" + pair.getParamid();
                sql_using += " USING (strainname)";
            }
        }
        return evalSQL(sql + sql_from + sql_using);
    }

    public Table getGroupAvgSDTable(ParamPair[] paramSets) {
        String sql_param = "SELECT paramid, groupid, average, sd FROM " + SCMDConfiguration.getProperty("DB_PARAM_AVG_SD") + " WHERE ";
        for(int i = 0; i < paramSets.length; i++ ) {
            ParamPair pair = paramSets[i];
            if( i != 0 )
                sql_param += " OR ";
            sql_param += "( paramid=" + pair.getParamid() + "AND groupid=" + pair.getGroupid() + ")";
        }
        return evalSQL(sql_param);
    }

    public String getParameterName(int paramid) {
        return "" + paramid;
    }
    
    public String getGroupName(int groupid) {
        return "" + groupid;
    }
    
    public String quote(String str) {
        return "\"" + str + "\"";
    }
    
    public Table getAnalysisAVGandSD() {
        String sql = "SELECT paramname, average, sd FROM " + SCMDConfiguration.getProperty("DB_TMP_ANALYSISZSCORE");
        return evalSQL(sql);
    }
    public Table getSelectedAnalysisValue(String[] orf) {
        String sql = "SELECT * FROM " + SCMDConfiguration.getProperty("DB_SUMMARY") + " WHERE ";
        for(int i = 0; i < orf.length; i++ ) {
            if( i != 0 ) {
                sql += " OR ";
            }
            sql += "strainname='" + orf[i].toUpperCase() + "'";
        }
        return evalSQL(sql);
    }

    /**
     * @param string
     * @param string2
     * @return
     */
    public List<Parameter> getParameterList(String string, String string2) throws SQLException {
        String sql = "select id, name, shortname, scope, datatype from parameterlist where scope='$1' and datatype='$2' order by id";
        List<Parameter> result = (List<Parameter>) ConnectionServer.query(new BeanListHandler(Parameter.class), sql, "cell", "num");
        return result;
    }


}
