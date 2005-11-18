package lab.cb.scmd.db.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import lab.cb.scmd.web.log.SCMDLogging;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 * 
 * JDK 1.5.0
 * 
 * GoF Design Pattern Type ***** 
 *�@Beans�ŕԂ�������DBUtil���g�p����
 * @author mattun
 * @version 
 * $Revision$ <br>
 * $LastChangedDate$<br>
 * $LastChangedBy$<br>
 */
public class SqlQuery {
	protected String sql = null;

	protected HashMap<String,String> params = null;
	protected HashMap<String,String> global = null;
	protected String name = null;
	protected Connection conn = null;

	//	DBUtils�ȕ���
	protected QueryRunner runner;

	/**
	 * ���ŃC���X�^���X�����ł��Ȃ��悤��protected�ɂ���
	 */
	protected SqlQuery(HashMap<String,String> global) {
		this.global = global;
		runner = new QueryRunner();
		params = new HashMap<String,String>();
	}
	
	protected void setSql(String sql) {
		this.sql = sql;
		//	�ŏ��ɃO���[�o���ϐ���K�����Ă���
		this.sql = sqlGlobalMarge(global);
	}
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	protected void putParams(String key,String value) {
		params.put(key,value);
	}

	/**
	 * 
	 *  cast�Ŏw�肳�ꂽClass�Ɋi�[����List�ŕԂ�
	 * @param <E>
	 * @param map XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��value�Œu������
	 * @param cls Bean�ł���K�v������A�����Ă��̒l��setter�ɓ���邽�߃N���X �N���X��.class�������œ����
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryResults(HashMap<String,String> map,Class<T> cast) throws SQLException {
		BeanListHandler blh = new BeanListHandler(cast);
		return (List<T>)runner.query(conn,createSQLMarge(map),blh);
	}
	public <T> List<T> queryResults(Object bean,Class<T> cast) throws SQLException {
		BeanListHandler blh = new BeanListHandler(cast);
		return (List<T>)runner.query(conn,createSQLMarge(bean),blh);
	}

	/**
	 * �P��̃J�����݂̂�Ԃ�
	 * @param map XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��value�Œu������
	 * @param column�@�Ԃ��J�������w��
	 * @return �w�肳�ꂽ�J�����̌^�ɂ��
	 * @throws SQLException
	 */
	public Object queryScalar(HashMap<String,String> map,String column) throws SQLException {
		ScalarHandler sh = new ScalarHandler(column);
		return runner.query(conn,createSQLMarge(map),sh);
	}
	/**
	 * �P��̃J�����݂̂�Ԃ�
	 * @param map XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��value�Œu������
	 * @param column�@�Ԃ��J�������w��
	 * @return �w�肳�ꂽ�J�����̌^�ɂ��
	 * @throws SQLException
	 */
	public Object queryScalar(HashMap<String,String> map,int columnNumber) throws SQLException {
		ScalarHandler sh = new ScalarHandler(columnNumber);
		return runner.query(conn,createSQLMarge(map),sh);
	}
	/**
	 * �P��̃J�����݂̂�Ԃ�
	 * @param bean XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��getter�Œl���擾���u������
	 * @param column�@�Ԃ��J�������w��
	 * @return�@�w�肳�ꂽ�J�����̌^�ɂ��
	 * @throws SQLException
	 */
	public Object queryScalar(Object bean,String column) throws SQLException {
		ScalarHandler sh = new ScalarHandler(column);
		return runner.query(conn,createSQLMarge(bean),sh);
	}
	/**
	 * �P��̃J�����݂̂�Ԃ�
	 * @param bean XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��getter�Œl���擾���u������
	 * @param column�@�Ԃ��J�������w��
	 * @return�@�w�肳�ꂽ�J�����̌^�ɂ��
	 * @throws SQLException
	 */
	public Object queryScalar(Object bean,int columnNumber) throws SQLException {
		ScalarHandler sh = new ScalarHandler(columnNumber);
		return runner.query(conn,createSQLMarge(bean),sh);
	}

	/**
	 * 
	 * �N���X���烊�t���N�V�������g�p����get���Ăяo��
	 * 
	 * @param obj
	 * @return
	 */
//	public ResultSet query(HashMap<String,String> map) {
//		String select = this.sql;
//		return null;
//	}
	
	/**
	 * 
	 * SQL���͂�${}������u������SQL���͂�n��
	 * XML�ɏ����Ă���param�ɑΉ�����p�����[�^�̂ݒu��������
	 * @return
	 */
	protected String sqlMarge(HashMap<String,String> map) {
		String select = this.sql;

		if(map != null) {
			for(String param : params.keySet()) {
				String value = params.get(param);
				//	
				if(map.containsKey(param)) {
					select = select.replaceAll("\\$\\{"+param+"\\}",map.get(param));
				//	�f�[�^���Ȃ��ꍇ��XML����f�[�^���擾����
				} else {
					select = select.replaceAll("\\$\\{"+param+"\\}",value);
				}
			}
		}
		SCMDLogging.file(name+"\n"+select,Level.SEVERE);
		return select;
	}
	/**
	 * 
	 * sqlMarge�̃O���[�o���ϐ��p
	 * 
	 * 
	 * @param map
	 * @return
	 */
	protected String sqlGlobalMarge(HashMap<String,String> map) {
		String select = this.sql;

		for(String param : map.keySet()) {
			String value = map.get(param);
			//	
			if((value == null || value.equals(""))) {
				select = select.replaceAll("\\$\\{"+param+"\\}",map.get(param));
			//	�f�[�^���Ȃ��ꍇ��XML����f�[�^���擾����
			} else {
				select = select.replaceAll("\\$\\{"+param+"\\}",value);
			}
		}
		return select;
	}
	/**
	 * 
	 * Bean�ɂ���getter���Ăяo���Ēl���擾��SQL�𐶐�����
	 * getter�͊�{�I��toString�ŃL���X�g�����
	 * 
	 * @param bean param����getter���Ăяo��
	 * @return
	 * @throws Exception
	 */
	public ResultSet query(Object bean) throws Exception {
		String sql = createSQLMarge(bean);
		return null;
	}
	
	/**
	 * 
	 * SQL�𔭍s������
	 * SQL������肻�̕��͂�Ԃ�����
	 * 
	 * @param map
	 * @return
	 */
	public String createSQLMarge(HashMap<String,String> map) {
		return sqlMarge(map);
	}

	/**
	 * 
	 * SQL�𔭍s������
	 * SQL������肻�̕��͂�Ԃ�����
	 * 
	 * @param bean getter������JavaBeans
	 * @return
	 */
	public String createSQLMarge(Object bean) {
		String select = this.sql;
		HashMap getterParams = new HashMap<String,String>();

		for(String param : params.keySet()) {
			String method = param.substring(0,1).toUpperCase()+param.substring(1);

			Object resp = null;
			try{
				//	������getMethods�őS���擾���Ė��O�������ق������������H
				resp = bean.getClass().getMethod("get"+method,null).invoke(bean);
				getterParams.put(param,resp);
			} catch(NoSuchMethodException nme) {
			} catch(IllegalAccessException iae) {
			} catch(InvocationTargetException ite) {
			}
		}
		select = sqlMarge(getterParams);
		return select;
	}

	/**
	 * INSERT,UPDATE,DELETE�Ȃǂ̂r�p�k���Ɏg��
	 * ������query�Ɠ���
	 * @param name
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public int update(HashMap map) throws SQLException{
		return runner.update(createSQLMarge(map));
	}
	/**
	 * INSERT,UPDATE,DELETE�Ȃǂ̂r�p�k���Ɏg��
	 * ������query�Ɠ���
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public int update(Object bean) throws SQLException{
		return runner.update(createSQLMarge(bean));
	}
}


//	-------------------------
//	$log: $
//	------------------------->>>>>>> .merge-right.r816
