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
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.xerial.util.Pair;

import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.web.common.ConfigObserver;
import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 * JDBC�̃R�l�N�V������semarphore�̌`�ŕ����ێ����A��������N���X�BSingleton�B
 * �g�p�O�ɂ͕K��ConnectionServer.initialize()�����s���Ă�������
 * @author leo
 *
 */
public class ConnectionServer implements ConfigObserver
{
    static private ConnectionServer _instance = null;
    private int _maxConnection = 10;
    private Vector<Connection> _connectionHolder = new Vector<Connection>();
    private LinkedList<Integer> _availableSlotQueue = new LinkedList<Integer>();
    private QueryRunner _queryRunner = new QueryRunner();

    /**
     * DB�ւ̖₢���킹���s��
     * @param sql SQL��
     * @param rsh ResultSetHandler
     * @return ResultSetHandler�ɂ���ĉ��H���ꂽObject
     * @throws SQLException
     */
    static public Object query(String sql, ResultSetHandler rsh) throws SQLException
    {
        return _instance.execQuery(sql, rsh);
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
        return _instance.execQuery(new SQLExpression(sqlWithVariable).assign(sqlArg), rsh);
    }
    
    protected Object execQuery(String sql, ResultSetHandler rsh) throws SQLException
    {
        try
        {
            Pair<Connection, Integer> connection = getConnection();
            Object result = _queryRunner.query(connection.getFirst(), sql, rsh);
            Integer freedSlotID = connection.getSecond();
            synchronized(this)
            {
                _availableSlotQueue.add(freedSlotID);
                notify();
            }
            return result;
        }
        catch(InterruptedException e)
        {
            return null;
        }
    }
    
    
    synchronized protected Pair<Connection, Integer> getConnection() throws InterruptedException
    {
        while(_availableSlotQueue.isEmpty())
        {
            wait(60000);  // timeout �P��
        }
        Integer availableSlot = _availableSlotQueue.poll();
        return new Pair<Connection, Integer>(_connectionHolder.get(availableSlot), availableSlot);
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
     * 
     */
    private ConnectionServer() throws SQLException
    {
       prepareDataSource(); 
    }
    
    
    // @see java.lang.Object#finalize()
    protected void finalize() throws Throwable
    {
        super.finalize();
        closeAll();
    }
    
    protected void prepareDataSource() throws SQLException
    {
        closeAll();
        _connectionHolder.clear();
        _availableSlotQueue.clear();
        
        String jdbcConnectionURI = "jdbc:postgresql://";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_IP", "localhost").trim() + ":";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_PORT", "5432").trim() + "/";
        jdbcConnectionURI += SCMDConfiguration.getProperty("POSTGRESQL_DBNAME", "scmd").trim();
        
        String user = SCMDConfiguration.getProperty("POSTGRESQL_USER", "postgres");
        String pass = SCMDConfiguration.getProperty("POSTGRESQL_PASSWORD", ""); 
        for(int i=0; i<_maxConnection; i++)
        {
            Connection con = DriverManager.getConnection(jdbcConnectionURI, user, pass);
            _connectionHolder.add(con);
            _availableSlotQueue.add(i);
        }
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
        for(Connection con : _connectionHolder)
        {
            try
            {
                con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        _connectionHolder.clear();
    }
}




