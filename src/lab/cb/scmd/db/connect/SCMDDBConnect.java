//--------------------------------------
//SCMDServer
//
//SCMDDBConnect.java 
//Since:  2004/07/16
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.connect;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import lab.cb.scmd.db.bean.Image;
import lab.cb.scmd.db.bean.Strain;
import lab.cb.scmd.db.common.DBConnect;
import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.FlatTable;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.table.Table;

public class SCMDDBConnect extends DBConnect {
    private String _groupQueryTableFile	= ""; 
    private String _cellBoxQueryTableFile = "";

    protected String _averageShapeTable		= "average_shape_20040813"; 
    protected String _tdAverageShapeTable 	= "td_average_shape_20040820";

    public SCMDDBConnect ()  {
//    	super(SCMDConfiguration.getProperty("POSTGRESQL_IP"),
//    			SCMDConfiguration.getProperty("POSTGRESQL_PORT"),
//				SCMDConfiguration.getProperty("POSTGRESQL_DBNAME"));
    	
        if(SCMDConfiguration.getProperty("DB_GENENAME") != null ) {
            _genenameTable = SCMDConfiguration.getProperty("DB_GENENAME");
        }
        if(SCMDConfiguration.getProperty("DB_STRAIN") != null ) {
            _strainTable = SCMDConfiguration.getProperty("DB_STRAIN");
        }
        if(SCMDConfiguration.getProperty("DB_ORFALIAS") != null ) {
            _orfaliasTable = SCMDConfiguration.getProperty("DB_ORFALIAS");
        }
        if(SCMDConfiguration.getProperty("DB_SUMMARY") != null ) {
            _summaryTable = SCMDConfiguration.getProperty("DB_SUMMARY");
        }
        if(SCMDConfiguration.getProperty("DB_INDIVIDUAL") != null ) {
            _individualTable = SCMDConfiguration.getProperty("DB_INDIVIDUAL");
        }
        if(SCMDConfiguration.getProperty("DB_AVERAGESHAPE") != null ) {
            _averageShapeTable = SCMDConfiguration.getProperty("DB_AVERAGESHAPE");
        }
        if(SCMDConfiguration.getProperty("DB_TD_AVERAGESHAPE") != null ) {
            _tdAverageShapeTable = SCMDConfiguration.getProperty("DB_TD_AVERAGESHAPE");
        }
    }
    
//    public SCMDDBConnect (String dbaddress, String portno, String dbname)  {
//        //super(dbaddress, portno, dbname);
//        _groupQueryTableFile = dbname;
//    }

    
//    public void setDB(String filename, String dbname) {
//    	if( filename.equals("GroupQuery") )
//    		_groupQueryTableFile = filename;
//    	else if ( filename.equals("CellBox") )
//    		_cellBoxQueryTableFile = filename;
//    }

    public BasicTable geneGroupQuery(LinkedList _genes, PageStatus _page, String columns[], String params[]) throws SCMDException {
        BasicTable bt = null;
        if( _useSQL ) {
            bt = super.geneGroupQuery(_genes, _page, columns, params);
        } else {
            bt = new FlatTable(_groupQueryTableFile, true, true);
        }
        return bt;
    }
    
    public BasicTable cellBoxQuery(Strain strain, Image photo, String[] columns, String params[]) throws SCMDException {
    	return cellBoxQuery(strain, photo, -1, columns, params);
    }

    public BasicTable cellBoxQuery(Strain strain, Image photo, int cellID, String[] columns, String params[]) throws SCMDException {
    	BasicTable bt = null;
    	if( _useSQL ) {
//            String individualTableSQL = "SELECT cell_id,";
//            String sql_columns = columns[0];
//            for( int i = 1; i < columns.length; i++ ) {
//                sql_columns += "," + columns[i];
//           	}
//            individualTableSQL += sql_columns;
//            individualTableSQL += " FROM ";
//            individualTableSQL += _strainTable + " JOIN ";
//            individualTableSQL += _individualTable;
//            individualTableSQL += " ON " + _strainTable + ".strainid = ";
//            individualTableSQL += _individualTable + ".strainid ";
//            individualTableSQL += " WHERE visible = 't' ";
//            if( strain != null )
//            	individualTableSQL += "AND strainname = '" + strain.getName() + "' ";
//            if( photo != null)
//            	individualTableSQL += "AND image_number = " + photo.getID();
//            if( cellID >= 0 )
//            	individualTableSQL += "AND cell_local_id = " + cellID;
//            
//            bt = query(individualTableSQL, "cell_id");
    		//String cellBoxSQL = "SELECT cell_id, ";
//            String cellBoxSQL = "SELECT ";
    		String sql_columns = "";
    		if( columns.length > 0)
    			sql_columns = columns[0];
    		for( int i = 1; i < columns.length; i++ ) {
    			sql_columns += "," + columns[i];
            }
//    		cellBoxSQL += sql_columns;
//    		cellBoxSQL += " FROM " + _individualTable;
//    		// TODO how to handle visible/invisible
//    		cellBoxSQL += " WHERE strainname = '" + strain.getName() + "' ";
//   			if( photo != null)
//   				cellBoxSQL += "AND image_number = " + photo.getID();
//   			if( cellID >= 0 )
//   				cellBoxSQL += "AND cell_local_id = " + cellID;
//   			cellBoxSQL += " ORDER BY cell_local_id";
//   			//bt = query(cellBoxSQL, "cell_id");
    		
    		//	SQL‚Ì—pˆÓ
   			HashMap<String,String> map = new HashMap<String,String>();
   			map.put("columns",sql_columns);
   			map.put("photoid",String.valueOf(photo.getID()));
   			map.put("strain",strain.getName());
   			map.put("cellid",String.valueOf(cellID));

   			try{
   	   			if( cellID >= 0 )
   	   				bt = SCMDManager.getDBManager().queryBasicTable("SCMDDBConnect.cellBoxQuery",map,"cell_id");
   	   			else
   	   				bt = SCMDManager.getDBManager().queryBasicTable("SCMDDBConnect.cellBoxQuery_nocellid",map,"cell_id");
   			} catch(SQLException e) {
   				throw new InvalidSQLException(e);
   			}
    	} else {
    		bt = new FlatTable(_cellBoxQueryTableFile);
    	}
    	return bt;
    }

	public Table getAverageShapeStatSheet(String strain) throws InvalidSQLException  {
//		String sql = "SELECT * FROM " + _averageShapeTable + " WHERE strainname = '" + strain + "'";
//		return getQueryResult(sql);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("strain",strain);
		try{
			return SCMDManager.getDBManager().queryTable("SCMDDBConnect.getAverageShapeStatSheet",map);
		} catch(SQLException sqle) {
			throw new InvalidSQLException(sqle);
		}
	}

	public Table getDataSheet(String strain, int photoID, String[] params) throws InvalidSQLException {
		String columns = "";
		columns = "cell_local_id ";
//		if( params.length > 0 )
//			columns = "\"" + params[0] + "\"";
		for( int i = 0; i < params.length; i++ ) {
			columns += ", \"" + params[i] + "\"";
		}
//		String sql = "SELECT " + columns + " FROM " + _individualTable;
//		sql += " WHERE strainname = '" + strain + "'";
//		sql += " AND image_number = " + photoID;
//		return getQueryResult(sql);
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("columns",columns);
		map.put("photoid",String.valueOf(photoID));
		map.put("strain",strain);
		try{
			return SCMDManager.getDBManager().queryTable("SCMDDBConnect.getDataSheet1",map);
		} catch(SQLException e) {
			throw new InvalidSQLException(e);
		}
	}

	/**
	 * @param strain
	 * @param photoID
	 * @param cellID
	 * @param params
	 * @return
	 * @throws InvalidSQLException
	 */
	public Table getDataSheet(String strain, int photoID, int cellID, String[] params) throws InvalidSQLException {
		String columns = "";
		if( params.length > 0 )
			columns = "\"" + params[0] + "\"";
		for( int i = 1; i < params.length; i++ ) {
			columns += ", \"" + params[i] + "\"";
		}
//		String sql = "SELECT " + columns + " FROM " + _individualTable;
//		sql += " WHERE strainname = '" + strain + "'";
//		sql += " AND image_number = " + photoID;
//		sql += " AND cell_local_id = " + cellID;
//		Table table = getQueryResult(sql);
//		//bt.addRowLabel(new Integer(cellID).toString());
//		return table;

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("columns",columns);
		map.put("photoid",String.valueOf(photoID));
		map.put("strain",strain);
		map.put("cellid",String.valueOf(cellID));
		try{
			return SCMDManager.getDBManager().queryTable("SCMDDBConnect.getDataSheet2",map);
		} catch(SQLException e) {
			throw new InvalidSQLException(e);
		}
	}

	/**
	 * @param string
	 * @return
	 * @throws InvalidSQLException
	 */
	public BasicTable getNumberOfCellsGroupedBy(String strain, String category) throws InvalidSQLException {
//		String sql = "SELECT \"" + category + "\", count(strainname) ";
//		sql += " FROM " + _individualTable + " WHERE strainname = '" + strain + "'";
//		sql += " GROUP BY \"" + category + "\"";
//		BasicTable bt = query(sql, "\"" + category + "\"");
//		BasicTable bt = query(sql, category);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("strain",strain);
		map.put("category",category);
		try{
			return SCMDManager.getDBManager().queryBasicTable("SCMDDBConnection.getNumberOfCellsGroupedBy",map,category);
		} catch(SQLException e) {
			throw new InvalidSQLException(e);
		}
//		return bt;
	}

    /**
     * @param param
     * @return
     * @throws InvalidSQLException
     */
    public Table getShapeStatOfParameter(String param) throws InvalidSQLException {
//        String sql = "SELECT paramname, average, sd, maxvalue, minvalue FROM " + _tdAverageShapeTable;
//        sql += " WHERE paramname = '" + param + "'";
//        return getQueryResult(sql);
        
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("param",param);
        try{
        	return SCMDManager.getDBManager().queryTable("SCMDDBConnection.getShapeStatOfParameter",map);
        } catch(SQLException sqle) {
        	throw new InvalidSQLException(sqle);
        }

    }

}
