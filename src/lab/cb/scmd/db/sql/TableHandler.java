//--------------------------------------
// SCMDWeb Project
//
// TableHandler.java
// Since: 2005/03/10
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.ResultSetHandler;

/**
 * SQL��ResultSet��Table�ɕϊ����ĕԂ�Handler
 * DBUtils�̋@�\
 * lab.cb.scmd.db.scripts���Ŏg�p��
 * @author leo
 *
 */
public class TableHandler implements ResultSetHandler
{

    /**
     * 
     */
    public TableHandler()
    {
        super();
    }

    // @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
    public Object handle(ResultSet rs) throws SQLException
    {
        Table paramGroupSheet = new Table();
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnName = new Vector<String>();
        int colSize = metaData.getColumnCount();
        for (int i = 1; i <= colSize; i++)
            columnName.add(metaData.getColumnName(i));
        
        paramGroupSheet.addRow(columnName);
        
        while (rs.next()) {
            Vector<Object> row = new Vector<Object>();
            for (int i = 1; i <= colSize; i++)
                row.add(rs.getObject(i));
            paramGroupSheet.addRow(row);
        }  
        return paramGroupSheet;
    }

}




