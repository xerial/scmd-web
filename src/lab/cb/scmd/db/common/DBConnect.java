//--------------------------------------
//SCMDServer
//
//DBConnect.java 
//Since:  2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.sql.SQLException;
import java.util.LinkedList;

import lab.cb.scmd.db.bean.Image;
import lab.cb.scmd.db.bean.Strain;
import lab.cb.scmd.db.connect.SCMDManager;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.web.exception.InvalidSQLException;

public class DBConnect {
    protected boolean _useSQL = true;
//    protected static String _dbaddress = "";
//    protected static String _portno	  = "";
//    protected static String _dbname	  = "";
    
    protected String _genenameTable	= "genename_20040719";
    protected String _strainTable 	= "strain_20040730";
    protected String _orfaliasTable = "orfaliasname_20040719"; // gene name を orf に変換
    protected String _summaryTable  = "summary_20040719";
    protected String _individualTable = "individual_20040813";
    
//    private Connection _connection = null;
//    private Statement  _statement  = null;
//    private ResultSet  _resultset  = null;
  
    public DBConnect()
    {}
    
//    public DBConnect (String dbaddress, String portno, String dbname)  {
//    	if( dbaddress == null || portno == null || dbname == null )
//    		throw new DBConnectException();
//        if( dbaddress.equals("flatfile") ) {
//            _useSQL = false;
//            return;
//        }
//
//        _useSQL = true;
//        _dbaddress = dbaddress;
//        _portno = portno;
//        _dbname = dbname;
//        connect();
//    }

//    public void connect () {
//    	if( _connection != null )
//    		return;
//        
//        try{
//            Class.forName("org.postgresql.Driver");
//        }catch(ClassNotFoundException e){
//            e.printStackTrace();
////            System.exit(1);
//        }
//        try{
//            String connectCommand = "jdbc:postgresql:";
//            if( !_dbaddress.equals("localhost") ) {
//                connectCommand  += "//" + _dbaddress;
//                if( !_portno.equals("5432") )
//                    connectCommand += ":" + _portno;
//                connectCommand += "/";
//            }
//            connectCommand += _dbname;
//            _connection = DriverManager.getConnection(connectCommand,"postgres","");
//        }catch(SQLException e){
//        }
//    }

    // query に対し、BasicTableを返す。
    // ただし, 各行に対するunique keyを必要とする場合は、keyColumn に
    // unique keyの列数を指定する。
    protected BasicTable query (String sql) throws InvalidSQLException {
        return query(sql, "", false);
    }
    /**
     * @param sql
     * @param keyColumnName
     * @return
     * @throws InvalidSQLException
     */
    protected BasicTable query (String sql, String keyColumnName) throws InvalidSQLException {
    	return query(sql, keyColumnName, true);
    }

    /**
     * @param sql
     * @param keyColumnName
     * @param isShowColumn
     * @return
     * @throws InvalidSQLException
     */
    protected BasicTable query (String sql, String keyColumnName, boolean isShowColumn) throws InvalidSQLException {
        try
        {
        	return SCMDManager.getDBManager().queryBasicTable(sql, keyColumnName);
//            return ConnectionServer.retrieveBasicTable(sql, keyColumnName);
        } 
        catch (SQLException e) 
        {
            throw new InvalidSQLException(e);
        }
    }
    
//    public Table getQueryResult(String sql) throws InvalidSQLException {
//        try
//        {
//            return ConnectionServer.retrieveTable(sql);
//        } 
//        catch (SQLException e) {
//            throw new InvalidSQLException(e);
//        }
//    }
//    public synchronized void close() 
//    {
//        if(_useSQL)
//        {
//            try
//            {
//            	if(_statement  != null) {
//            		_statement.close();
//            	}
//            	if(_resultset != null) {
//            		_resultset.close();
//            	}
//                if(_connection != null)
//                {
//                    if(!_connection.isClosed())
//                        _connection.close();
//                }
//            }
//            catch(SQLException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    
//    public boolean isClosed() throws SCMDException
//    {
//        try
//        {
//            if(_connection != null)
//                return _connection.isClosed();
//            else
//            {
//                return true;
//            }
//        }
//        catch(SQLException e)
//        {
//            throw new SCMDException(e);
//        }
//    }
    
    
//    public void finalize() throws Throwable {
//        close();
//        super.finalize();
//    }

    public BasicTable geneGroupQuery(LinkedList genes, PageStatus page, String[] columns, String params[]) throws SCMDException {
        BasicTable bt = null;

        if( _useSQL ) {
            //SELECT systematicname, primaryname, aliasname, annotation, "C11-1_A" from 
            // (SELECT strainid, systematicname,primaryname, aliasname, annotation from strain_20040719 left join genename_20040719 on strain_20040719.geneid = genename_20040719.geneid where visible = 't') 
            // as genetable left join summary_20040719 on genetable.strainid = summary_20040719.strainid;
            String genetablesql = "SELECT ";
            String sql_columns = columns[0];
            for( int i = 1; i < columns.length; i++ ) {
                sql_columns += "," + columns[i];
           	}
            genetablesql += sql_columns;
            genetablesql += " FROM ";
            genetablesql += _genenameTable;
            if( genes.size() > 0) {
                genetablesql += " WHERE ";
                genetablesql += "systematicname = \'" + ((GeneInformation)genes.get(0)).getSystematicName() + "\'";
                for( int i = 1; i < genes.size(); i++ ) {
                	genetablesql += " OR systematicname = \'" + ((GeneInformation)genes.get(i)).getSystematicName() + "\'";
                }
                genetablesql += " ";
            }
            
            String sql = "SELECT " + sql_columns;
            for( int i = 0; i < params.length; i++ ) {
                sql += ",\"" + params[i] + "\"";
            }
            sql += " FROM (" + genetablesql + ") AS genetable ";
            sql += "INNER JOIN " + _summaryTable + " ON ";
            sql += "genetable.systematicname = " + _summaryTable + ".strainname";
            sql += " ORDER BY systematicname";
//            sql += page.toSQL(); // limit
            
//            System.out.println("SQL --- " + sql);
            
            bt = query(sql, "systematicname");
//            HashMap<String,String> map = new HashMap<String,String>();
//            map.put("columns",sql_columns);
//            map.put("systematicname",sql_columns);
        } else {
            throw new SCMDException("This class only support to read data from DB");
        }
        
        return bt;
    }


    public String[] toSystematicNames(String[] geneNames) throws SCMDException {
        BasicTable bt = null;
        String[] columns = {"alias", "orfname"};
        if( _useSQL ) {
            String sql = "SELECT alias,orfname FROM ";
            sql += _orfaliasTable;
            if( geneNames.length > 0 ) {
                sql += " WHERE alias = '" + geneNames[0] + "'";
            }
            for( int i = 1; i < geneNames.length; i++ ) {
                sql += "OR alias = '" + geneNames[i] + "'";
            }
            bt = query(sql, "alias");
        } else {
            throw new SCMDException("This class only support to read data from DB");
        }
        String[] systematicName = new String [bt.getRowSize()];
        for( int i = 0; i < bt.getRowSize(); i++ ) {
            Cell orf = bt.getCell(geneNames[i], 1);
            systematicName[i] = orf.toString();
        }
        return systematicName;
    }

    public BasicTable individualBoxQuery(Strain strain, Image photo, String[] columns, String params[]) throws SCMDException {
        BasicTable bt = null;

        if( _useSQL ) {
            //SELECT systematicname, primaryname, aliasname, annotation, "C11-1_A" from 
            // (SELECT strainid, systematicname,primaryname, aliasname, annotation from strain_20040719 left join genename_20040719 on strain_20040719.geneid = genename_20040719.geneid where visible = 't') 
            // as genetable left join summary_20040719 on genetable.strainid = summary_20040719.strainid;

            String individualTableSQL = "SELECT cell_id,";
            String sql_columns = columns[0];
            for( int i = 1; i < columns.length; i++ ) {
                sql_columns += "," + columns[i];
           	}
            individualTableSQL += sql_columns;
            individualTableSQL += " FROM ";
            individualTableSQL += _strainTable + " JOIN ";
            individualTableSQL += _individualTable;
            individualTableSQL += " ON " + _strainTable + ".strainid = ";
            individualTableSQL += _individualTable + ".strainid ";
            individualTableSQL += " WHERE visible = 't' ";
            if( strain != null )
            	individualTableSQL += "AND strainname = '" + strain.getName() + "' ";
            if( photo != null)
            	individualTableSQL += "AND image_number = " + photo.getID();
        } else {
            throw new SCMDException("This class only support to read data from DB");
        }
        
        return bt;
    }

	public BasicTable imagePageStatusQuery(String strainname) throws SCMDException {
		BasicTable bt = new BasicTable();
        if( _useSQL ) {
            // select distinct image_number from individual_sample left join strain_20040730 
        	// on individual_sample.strainid = strain_20040730.strainid 
        	// where individual_sample.strainid = 7345;
// individualTableの形式変更に伴って、コメントアウト
//            String individualTableSQL = "SELECT DISTINCT image_number ";
//            individualTableSQL += " FROM ";
//            individualTableSQL += _strainTable + " JOIN ";
//            individualTableSQL += _individualTable;
//            individualTableSQL += " ON " + _strainTable + ".strainid = ";
//            individualTableSQL += _individualTable + ".strainid ";
//            individualTableSQL += " WHERE visible = 't' ";
//           	individualTableSQL += "AND strainname = '" + strainname + "' ";
        	String individualTableSQL = "SELECT DISTINCT image_number ";
        	individualTableSQL += " FROM " + _individualTable;
        	individualTableSQL += " WHERE strainname = '" + strainname + "' ORDER BY image_number";
            bt = query(individualTableSQL, "image_number");
        } else {
            throw new SCMDException("This class only support to read data from DB");
        }
        
        return bt;
	}

//    public static void main(String[] args) {
//        DBConnect dbconnect = null;
//		try {
//			dbconnect = new DBConnect("157.82.250.67", "5432", "lab.cb.scmd_pre_20040714");
//		} catch (DBConnectException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		String sql = "select systematicname, annotation from genename_20040714 limit 200";
//        String[] cols = {"systematicname","annotation"};
//        BasicTable result = null;
//        try {
//            result = dbconnect.query(sql);
//        } catch (SCMDException e) {
//            e.printStackTrace();
//        }
//        for(int i = 0; i < result.getRowSize(); i++ ) {
//            for( int j = 0; j < result.getColSize(); j++ ) {
//                System.out.print("\t" + result.getCell(i, j).toString());
//            }
//            System.out.println();
//        }
//    }
}
