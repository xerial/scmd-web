//--------------------------------------
// SCMDWeb Project
//
// ConnectionServer.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.util.table.AppendableTable;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.web.common.ConfigObserver;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.Table;

/**
 * JDBC�̃R�l�N�V������semarphore�̌`�ŕ����ێ����A��������N���X�BSingleton�B
 * �g�p�O�ɂ͕K��ConnectionServer.initialize()�����s���Ă�������
 * 
 * �g�����́A
 * <pre>
 *    ConnectionServer.query("select * from ... ", new BeanHandler(SomeBean.class)); 
 * </pre>
 * �ȂǁBResultSetHandler�̕����́AJakarta Commons ��DBUtils�Œ�`����Ă�����́B
 * @author leo
 *
 */
public class ConnectionServer implements ConfigObserver
{
    static private ConnectionServer _instance = null;
    private int _maxConnection = 10;
    private LinkedList<Connection> _connectionHolder = new LinkedList<Connection>();
    private QueryRunner _queryRunner = new QueryRunner();

    
   
    
    /** SQL�����s���Č��ʂ�Table�Ɋi�[���ĕԂ�
     * @param sql $1, $2, ...�Ƃ����ϐ����܂ނr�p�k��
     * @param args �����ɑ������l
     * @return lab.cb.web.table.Table �N���X�̃C���X�^���X
     * @throws SQLException
     */
    static public Table retrieveTable(String sql, Object... args) throws SQLException
    {
        return retrieveTable(new SQLExpression(sql).assign(args));
    }
    /** SQL�����s���Č��ʂ�Table�Ɋi�[���ĕԂ�
     * @param sql �r�p�k��
     * @return lab.cb.scmd.web.table.Table �N���X�̃C���X�^���X
     * @throws SQLException
     */
    static public Table retrieveTable(String sql) throws SQLException
    {
        return (Table) getInstance().execQuery(sql, new TableConverter()); 
    }
    
    /** SQL�����s���Č��ʂ�BasicTable�Ɋi�[���ĕԂ�
     * @param sql �r�p�k��
     * @return lab.cb.scmd.util.Table.BasicTable �N���X�̃C���X�^���X
     * @throws SQLException
     */
    static public BasicTable retrieveBasicTable(String sql, String keyColumnName) throws SQLException
    {
        return (BasicTable) getInstance().execQuery(sql, new BasicTableConverter(keyColumnName)); 
    }
    static private class BasicTableConverter implements ResultSetHandler
    {
        String keyColumnName = "";
        public BasicTableConverter()
        {
            
        }
        public BasicTableConverter(String keyColumnName)
        {
            this.keyColumnName = keyColumnName;
        }
        
        public Object handle(ResultSet resultSet) throws SQLException
        {
            AppendableTable at = null;            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns =  metaData.getColumnCount();
            String[] columnName = new String [numberOfColumns];
            int keyColumn = -1;
            for( int i = 0; i < numberOfColumns; i++ ) {
                columnName[i] = metaData.getColumnLabel(i + 1);
                if( keyColumnName.equals(metaData.getColumnLabel(i + 1))) {
                    keyColumn = i;
                }
            }
            int curcol = 0;
            int collength = 0;
            if( keyColumn < 0 ) {
                collength = numberOfColumns;
            } else {
                collength = numberOfColumns + 1;
                curcol = 1;
            }
            
            if( keyColumn < 0)
                at = new AppendableTable("SQL Result", columnName); // without rowlabel
            else
                at = new AppendableTable("SQL Result", columnName, true); // with rowlabel
            
            while( resultSet.next()){
                String[] row = new String[collength];
                if( keyColumn >= 0 )
                    row[0] = resultSet.getString(columnName[keyColumn]);
                for( int i = 0; i < numberOfColumns; i++ ) {
                    row[i + curcol] = resultSet.getString(columnName[i]);
                }
                at.append(row);
            }
            return at;
        }
    }
    
    static private class TableConverter implements ResultSetHandler
    {
        public Object handle(ResultSet resultSet) throws SQLException
        {
            Table table = new Table();
            
            ResultSetMetaData metadata = resultSet.getMetaData();
            Vector<String> columnLabel = new Vector<String>(metadata.getColumnCount());
            for(int i=1; i<=metadata.getColumnCount(); i++)
            {
                String columnName = metadata.getColumnName(i);
                columnLabel.add(columnName);
            }
            table.addRow(columnLabel);
            
            while(resultSet.next())
            {
                Vector<String> row = new Vector<String>(metadata.getColumnCount());
                for(int i=1; i<=metadata.getColumnCount(); i++)
                {
                    row.add(resultSet.getString(i));
                }
                table.addRow(row);
            }
            return table;
        }
        
    }
    
    
    /**
     * DB�ւ̖₢���킹���s��
     * @param sql SQL��
     * @param rsh ResultSetHandler
     * @return ResultSetHandler�ɂ���ĉ��H���ꂽObject
     * @throws SQLException
     */
    static public Object query(String sql, ResultSetHandler rsh) throws SQLException
    {
        return getInstance().execQuery(sql, rsh);
    }
    /**
     * DB�ւ̖₢���킹���s��
     * @param rsh ResultSetHandler
     * @param sqlWithVariable SQL���̈ꕔ���A$1, $2, ...�Ƃ����ϐ��ɒu��������ꂽ����
     * @param sqlArg $1, $2, ...�ɑ������l
     * @return ResultSetHandler�ɂ���ĉ��H���ꂽObject
     * @throws SQLException
     */
    static public Object query(ResultSetHandler rsh, String sqlWithVariable, Object... sqlArg) throws SQLException
    {
        return getInstance().execQuery(new SQLExpression(sqlWithVariable).assign(sqlArg), rsh);
    }
    
    protected Object execQuery(String sql, ResultSetHandler rsh) throws SQLException
    {
        try
        {
            Connection connection = null;
            // achieve a connection
            synchronized(this)
            {
                while(_connectionHolder.isEmpty())
                {
                    wait(60000);  // timeout �P��
                }
                connection = _connectionHolder.poll();
            }
            
            // confirm the connection
            if(connection == null || connection.isClosed())
                connection = createNewConnection();
                    
            Object result = _queryRunner.query(connection, sql, rsh);
            // release connection
            synchronized(this)
            {
                _connectionHolder.add(connection);
                notify();
            }
            return result;
        }
        catch(InterruptedException e)
        {
            // connection�̎擾�҂������f���ꂽ�ꍇ
            return null;
        }
    }
    
    static synchronized public ConnectionServer getInstance() throws SQLException
    {
        if(_instance == null)
        {
            initialize();
            return _instance;
        }
        else 
            return _instance;
    }
    
    
    /**
     * ConnectionServer������������B�v���O�����Ŏg�p�O�Ɉ�x�������s����
     */
    static public void initialize() throws SQLException
    {
        if(_instance != null)
            _instance.closeAll(); //  ��n��
        
        _instance = new ConnectionServer();
        SCMDConfiguration.addObserver(_instance);
    }
    
    
    /**
     * ��n�����s�����\�b�h�BTOMCAT�̍ċN���O�ɌĂяo�� 
     */
    static public void dispose() 
    {
        if(_instance != null)
            _instance.closeAll();
    }
    
    /**
     * 
     */
    private ConnectionServer() throws SQLException
    {
       prepareDataSource(); 
    }
    
    
    // @see java.lang.Object#finalize()
    protected void finalize() throws Throwable
    {
        closeAll();
        super.finalize();
    }
    
    protected void prepareDataSource() throws SQLException
    {
        closeAll();
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace(System.out);
            return;
        }
        
        for(int i=0; i<_maxConnection; i++)
        {
            _connectionHolder.add(createNewConnection());
        }
    }
    
    private String getConnectionURI()
    {
        String jdbcConnectionURI = "jdbc:postgresql://";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_IP", "localhost").trim() + ":";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_PORT", "5432").trim() + "/";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_DBNAME", "scmd").trim();
        
        return jdbcConnectionURI;
    }
    
    private Connection createNewConnection() throws SQLException
    {
        String user = SCMDConfiguration.getProperty("POSTGRESQL_USER", "postgres");
        String pass = SCMDConfiguration.getProperty("POSTGRESQL_PASSWORD", ""); 
        Connection con = DriverManager.getConnection(getConnectionURI(), user, pass);
        return con;
    }
    
    // @see lab.cb.scmd.web.common.ConfigObserver#reloaded()
    public void reloaded()
    {
        // SCMDConfiguration��reload�����m
        closeAll();
        try
        {
            prepareDataSource();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    protected void closeAll()
    {
        System.out.print("[scmd-server " + new Date(System.currentTimeMillis())  + "] closing connections ...");
        int count = 1;
        for(Connection con : _connectionHolder)
        {
            try
            {
                con.close(); 
                System.out.print(count++);
                System.out.print(" ");                
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        _connectionHolder.clear();
        System.out.println("finish.");
    }
    public void update(String sql,Object ...obj) throws SQLException{
        try
        {
            Connection connection = null;
            // achieve a connection
            synchronized(this)
            {
                while(_connectionHolder.isEmpty())
                {
                    wait(60000);  // timeout �P��
                }
                connection = _connectionHolder.poll();
            }
            
            // confirm the connection
            if(connection == null || connection.isClosed())
                connection = createNewConnection();
                    
            _queryRunner.update(connection,sql,obj);
            // release connection
            synchronized(this)
            {
                _connectionHolder.add(connection);
                notify();
            }
        }
        catch(InterruptedException e)
        {
            // connection�̎擾�҂������f���ꂽ�ꍇ
        }
    }
   
    public static void Update(String sql,Object ...obj) {
    	try{
    		getInstance().update(sql,obj);
    	} catch(SQLException e) {
    		
    	}
    }
}




