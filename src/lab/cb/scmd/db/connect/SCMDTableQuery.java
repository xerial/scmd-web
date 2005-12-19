/*
 * Created on 2004/08/13
 *
 */
package lab.cb.scmd.db.connect;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import lab.cb.scmd.algorithm.Algorithm;
import lab.cb.scmd.db.common.QueryRange;
import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.datagen.ParamPair;
import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * 
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $ $LastChangedBy$
 */
public class SCMDTableQuery implements TableQuery {

	private SCMDDBConnect _connection = null;

	public SCMDTableQuery() {
		_connection = new SCMDDBConnect();
	}

	// public DBConnect getConnection()
	// {
	// ConnectionServer.
	//        
	// // try
	// // {
	// // if(_connection == null)
	// // _connection = new SCMDDBConnect();
	// // else if(_connection.isClosed())
	// // {
	// // // 再接続
	// // _connection = new SCMDDBConnect();
	// // }
	// // }
	// // catch(SCMDException e)
	// // {
	// // e.what();
	// // try {
	// // // 再接続
	// // _connection = new SCMDDBConnect();
	// // } catch (DBConnectException e1) {
	// // e.what();
	// // }
	// // }
	// // return _connection;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String,
	 *      int, int)
	 */
	public Table getShapeDataSheet(String strain, int photoID, int cellID,
			int sheetType) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String,
	 *      int)
	 */
	public Table getShapeDataSheet(String strain, int photoID, int sheetType) {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = _connection.getDataSheet(strain, photoID, DataSheetType
					.getParameters(sheetType));
		} catch (InvalidSQLException e) {
			return null;
		}
		return table;
	}

	public Table getShapeDataSheet(String strain, int photoID, String[] colName) {
		strain = strain.toUpperCase();
		Table table;
		try {
			table = _connection.getDataSheet(strain, photoID, colName);
		} catch (InvalidSQLException e) {
			return null;
		}
		return table;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (row == -1)
			return map;

		for (int col = 0; col < table.getColSize(); col++) {
			map.put(table.get(0, col).toString(), table.get(row, col)
					.toString());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String,
	 *      int, int, java.lang.String)
	 */
	public Table getGroupByDatasheet(String orf, int sheetType, int stainType,
			String groupName) {

		return getGroupByDatasheet(orf, sheetType, stainType, groupName, -1, 10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String,
	 *      int, int, java.lang.String, int, int)
	 */
	public Table getGroupByDatasheet(String orf, int sheetType, int stainType,
			String groupName, int page, int numElementInAPage) {
		Vector paramList = new Vector();
		paramList.add("cell_local_id");
		Algorithm.initializeCollection(paramList,
				DataSheetType.PHOTO_INFO_PARAM);
		Algorithm.initializeCollection(paramList, DataSheetType
				.getParameters(sheetType));

		String selectParam = "";
		for (Iterator it = paramList.iterator(); it.hasNext();) {
			selectParam += "\"" + (String) it.next() + "\", ";
		}
		selectParam = selectParam.substring(0, selectParam.length() - 2);
		String sql = "SELECT " + selectParam + " FROM "
				+ SCMDConfiguration.getProperty("DB_INDIVIDUAL")
				+ " WHERE strainname='" + orf.toUpperCase() + "' AND \""
				+ StainType.getStainTypeName(stainType) + "group\"='"
				+ groupName + "' ORDER BY cell_local_id";

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("selectParam", selectParam);
		map.put("orf", orf.toUpperCase());
		map.put("groupname", groupName);
		map.put("straintype",StainType.getStainTypeName(stainType)+"group");
		if (page > 0 && numElementInAPage > 0) {
			// sql += " LIMIT " + numElementInAPage + " OFFSET " +
			// (numElementInAPage * (page-1));
			map.put("limit", String.valueOf(numElementInAPage));
			map.put("offset", String.valueOf(numElementInAPage * (page - 1)));
			return evalSQL("TableQuery:getGroupByDatasheetUseLimit", map);
		} else {
			return evalSQL("TableQuery:getGroupByDatasheet", map);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getGroupCount(java.lang.String,
	 *      int, java.lang.String)
	 */
	public Table getGroupCount(String orf, int stainType) {
		// String sql = "SELECT \"" + StainType.getStainTypeName(stainType) +
		// "group\", COUNT(cell_local_id) FROM " +
		// SCMDConfiguration.getProperty("DB_INDIVIDUAL") + " WHERE
		// strainname='"
		// + orf.toUpperCase() + "' GROUP BY \"" +
		// StainType.getStainTypeName(stainType) + "group\"";
		// return evalSQL(sql);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orf", orf.toUpperCase());
		map.put("strainname", StainType.getStainTypeName(stainType) + "group");
		return evalSQL("TableQuery:getGroupCount", map);
	}

	protected Table evalSQL(String name, HashMap<String, String> map) {
		try {
			return SCMDManager.getDBManager().queryTable(name, map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * protected Table evalSQL(String sql) { Table result = null; try { //
	 * result = _connection.getQueryResult(sql); result =
	 * ConnectionServer.retrieveTable(sql); } catch (SQLException e) {
	 * e.printStackTrace(); } return result; }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String,
	 *      java.lang.String)
	 * @deprecated summary tableは使用しない
	 */
	public Table getAveragePlot(String param1, String param2) {
		// String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " +
		// quoteAttribute(param2)
		// + " FROM " + SCMDConfiguration.getProperty("DB_STRAIN") + " RIGHT
		// JOIN summary_20040719 USING(strainid)";
		// return evalSQL(sql);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("param1", quoteAttribute(param1));
		map.put("param2", quoteAttribute(param2));
		return evalSQL("TableQuery:getAveragePlot1", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String,
	 *      lab.cb.scmd.db.common.QueryRange, java.lang.String,
	 *      lab.cb.scmd.db.common.QueryRange) @depracated
	 */
	public Table getAveragePlot(String param1, QueryRange range1,
			String param2, QueryRange range2) {
		/*
		 * String sql = "SELECT strainname, " + quoteAttribute(param1) + ", " +
		 * quoteAttribute(param2) + " FROM " +
		 * SCMDConfiguration.getProperty("DB_STRAINTABLE") + " RIGHT JOIN
		 * summary_20040719 USING(strainid) WHERE ";
		 * 
		 * sql += quoteAttribute(param1) + " BETWEEN " + range1.getBegin() + "
		 * AND " + range1.getEnd() + " AND "; sql += quoteAttribute(param2) + "
		 * BETWEEN " + range2.getBegin() + " AND " + range2.getEnd();
		 */
		// return evalSQL(sql);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("param1", quoteAttribute(param1));
		map.put("param2", quoteAttribute(param2));
		map.put("rangebegin1", String.valueOf(range1.getBegin()));
		map.put("rangebegin2", String.valueOf(range2.getBegin()));
		map.put("rangeend1", String.valueOf(range1.getEnd()));
		map.put("rangeend2", String.valueOf(range2.getEnd()));
		return evalSQL("TableQuery:getAveragePlot2", map);
	}

	protected String quoteAttribute(String attribute) {
		return "\"" + attribute + "\"";
	}

	/*
	 * 現在、返るテーブルのcolumn(変数名key)にはパラメータ名を入れているが、group横断で検索を行いたい場合には、変更の必要あり
	 */
	public Table getShapeZScoreTable(ParamPair[] paramSets) {
		String sql = "SELECT strainname";
		String sql_from = " FROM ";
		String sql_using = "";

		// String sql_param = "SELECT id, displayname FROM " +
		// SCMDConfiguration.getProperty("DB_PARAMETERLIST") + " WHERE ";
		String sql_param = "";
		for (int i = 0; i < paramSets.length; i++) {
			ParamPair pair = paramSets[i];
			if (i != 0)
				sql_param += " OR ";
			sql_param += "id=" + pair.getParamid();
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("params", sql_param);
		Table paramTable = evalSQL(
				"lab.cb.scmd.db.connect.SCMDTableQuery:getShapeZScoreTable1",
				map);

		RowLabelIndex rowLabelIndex = new RowLabelIndex(paramTable);
		for (int i = 0; i < paramSets.length; i++) {
			ParamPair pair = paramSets[i];
			/* get parameter and group name */
			// String paramname = getParameterName(pair.getParamid());
			// String groupname = getGroupName(pair.getGroupid());
			String paramname = paramTable.get(
					rowLabelIndex.getRowIndex(pair.getParamid() + ""), 1)
					.toString();
			String key = paramname;
			/* */
			sql += "," + quote(paramname);
			String subsql = "SELECT strainname, paramid, groupid, zscore AS "
					+ quote(key) + " FROM "
					+ SCMDConfiguration.getProperty("DB_ZSCORE") + " WHERE ";
			if (pair.getParamid() == -1 && pair.getGroupid() == -1)
				continue;
			if (paramSets[i].getParamid() != -1) {
				subsql += "paramid=" + paramSets[i].getParamid();
				if (paramSets[i].getGroupid() != -1) {
					subsql += " AND groupid=" + paramSets[i].getGroupid();
				}
			} else {
				subsql += "groupid=" + paramSets[i].getGroupid();
			}
			if (i == 0) {
				sql_from += "(" + subsql + ") AS s" + pair.getParamid();
			} else {
				sql_from += " LEFT JOIN (" + subsql + ") AS s"
						+ pair.getParamid();
				sql_using += " USING (strainname)";
			}
		}
		map.clear();
		map.put("sql", sql + sql_from + sql_using);
		return evalSQL(
				"lab.cb.scmd.db.connect.SCMDTableQuery:getShapeZScoreTable2",
				map);
	}

	public Table getGroupAvgSDTable(ParamPair[] paramSets) {
		HashMap<String, String> map = new HashMap<String, String>();
		String parampair = "";
		for (int i = 0; i < paramSets.length; i++) {
			ParamPair pair = paramSets[i];
			if (i != 0)
				parampair += " OR ";
			parampair += "( paramid=" + pair.getParamid() + "AND groupid="
					+ pair.getGroupid() + ")";
		}
		map.put("parampair", parampair);
		return evalSQL(
				"lab.cb.scmd.db.connect.SCMDTableQuery:getGroupAvgSDTable", map);
		/*
		 * String sql_param = "SELECT paramid, groupid, average, sd FROM " +
		 * SCMDConfiguration.getProperty("DB_PARAM_AVG_SD") + " WHERE "; for(int
		 * i = 0; i < paramSets.length; i++ ) { ParamPair pair = paramSets[i];
		 * if( i != 0 ) sql_param += " OR "; sql_param += "( paramid=" +
		 * pair.getParamid() + "AND groupid=" + pair.getGroupid() + ")"; }
		 * return evalSQL(sql_param);
		 */}

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
		// String sql = "SELECT paramname, average, sd FROM " +
		// SCMDConfiguration.getProperty("DB_TMP_ANALYSISZSCORE");
		return evalSQL(
				"lab.cb.scmd.db.connect.SCMDTableQuery:getAnalysisAVGandSD",
				null);
	}

	public Table getSelectedAnalysisValue(String[] orf) {
		if (orf.length < 1) {
			return null;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		// String sql = "SELECT * FROM " +
		// SCMDConfiguration.getProperty("DB_SUMMARY") + " WHERE ";
		String sql = "";
		for (int i = 0; i < orf.length; i++) {
			if (i != 0) {
				sql += " OR ";
			}
			sql += "strainname='" + orf[i].toUpperCase() + "'";
		}
		map.put("orfs", sql);
		return evalSQL(
				"lab.cb.scmd.db.connect.SCMDTableQuery:getSelectedAnalysisValue",
				map);
	}

	public List<MorphParameter> getParameterList(String scope, String datatype)
			throws SQLException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("scope", scope);
		map.put("datatype", datatype);
		List<MorphParameter> result = SCMDManager.getDBManager().queryResults(
				"TableQuery:getParameterList", map, MorphParameter.class);
		// String sql = "select id, name, shortname, scope, datatype from " +
		// SCMDConfiguration.getProperty("DB_PARAMETERLIST") + " where
		// scope='$1' and datatype='$2' order by id";
		// List<MorphParameter> result = (List<MorphParameter>)
		// ConnectionServer.query(new BeanListHandler(MorphParameter.class),
		// sql, scope, datatype);
		return result;
	}

	public List<MorphParameter> getParameterInfo(Set<Integer> parameter) {
		// String sql = "SELECT id, name, shortname, displayname, scope,
		// datatype,description, systematicname FROM "
		// + SCMDConfiguration.getProperty("DB_PARAMETERLIST");
		String sql_where = "";
		for (Object param : parameter) {
			if (sql_where.length() == 0)
				sql_where = " WHERE";
			else
				sql_where += " OR";
			sql_where += " id='" + param + "'";
		}
		// sql += sql_where;
		List<MorphParameter> result = null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("sqlwhere", sql_where);
		try {
			result = SCMDManager.getDBManager().queryResults(
					"SCMDTableQuery.getParameterInfo", map, MorphParameter.class);
			// result = (List<MorphParameter>) ConnectionServer.query(new
			// BeanListHandler(MorphParameter.class), sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// TODO このメソッドはパラメータが足りてないし使われてないので必要ない？
	// public Table getParameterTable() {
	// // パラメータが足りてない？
	// String sql = "select id, name, shortname, scope, datatype from " +
	// SCMDConfiguration.getProperty("DB_PARAMETERLIST") + " where scope='$1'
	// and datatype='$2' order by id";
	// Table table = null;
	// try {
	// HashMap<String,String> map = new HashMap<String,String>();
	// map.put("scope","orf");
	// map.put("datatype","");
	// table =
	// SCMDManager.getDBManager().queryTable("TableQuery:getParameterTable",map);
	// //table = ConnectionServer.retrieveTable(sql, "orf");
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return table;
	// }

	public MorphParameter getOneParameterInfo(String param, String scope) {
		String sql = "SELECT id, name, shortname, displayname, scope, datatype,description FROM "
				+ SCMDConfiguration.getProperty("DB_PARAMETERLIST");
		sql += " WHERE name='" + param + "' and scope='" + scope + "'";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("param", param);
		map.put("scope", scope);
		List<MorphParameter> result = null;
		try {
			result = SCMDManager.getDBManager()
					.queryResults("TableQuery:getOneParameterInfo", map,
							MorphParameter.class);
			// result = (List<MorphParameter>) ConnectionServer.query(new
			// BeanListHandler(MorphParameter.class), sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result.size() == 0)
			return null;
		return result.get(0);
	}
    
    public Table getAssociatedGO(String[] keywords) {
//    	String sql = "SELECT goid, name, namespace, def FROM term WHERE ";
    	String where = "";
    	Table table = null;
    	for(String keyword: keywords) {
    		if( where.length() != 0 ) {
    			where += " AND ";
    		}
    		where += "( goid ILIKE '%" + keyword + "%'";
    		where += " OR ";
    		where += "name ILIKE '%" + keyword + "%' )";
    	}
//    	sql = sql + where;
        try {
    		//	これはBasicTableの方？$1とかないので・・・　BasicTableの場合はqueryBasicTableに
//            table = ConnectionServer.retrieveTable(sql, "go");
        	HashMap map = new HashMap();
        	map.put("where",where);
        	table = SCMDManager.getDBManager().queryTable("SCMDTableQuery.getAssociatedGO",map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return table;
    }
    
    public Table getForwardReverseAssociations(String goid) {
//    	String sql = "SELECT * FROM goenrich WHERE goid = '" + goid + "'";
    	Table table = null;
    	try {
    		//	これはBasicTableの方？$1とかないので・・・　BasicTableの場合はqueryBasicTableに
//			table = ConnectionServer.retrieveTable(sql, "fwdrev");
        	HashMap map = new HashMap();
        	map.put("goid",goid);
        	table = SCMDManager.getDBManager().queryTable("SCMDTableQuery.getForwardReverseAssociations",map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return table;
    }
}
