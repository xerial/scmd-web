/**
 * 
 */

package lab.cb.scmd.db.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * GoF Design Pattern Type ***** 
 *�@Beans�ŕԂ�������DBUtil���g�p����
 * @author mattun
 * @version 
 * $Revision$ <br>
 * $LastChangedDate$<br>
 * $LastChangedBy$<br>
 */
public class SqlQuery {
	
	public String sql = null;

	public HashMap<String,String> params = null;
	public HashMap<String,String> global = null;
	public String name = null;
	public Connection conn = null;

	//	DBUtils�ȕ���
	public QueryRunner runner;

	/**
	 * ���ŃC���X�^���X�����ł��Ȃ��悤��protected�ɂ���
	 */
	protected SqlQuery(HashMap<String,String> global) {
		this.global = global;
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
	 *  
	 * @param <E>
	 * @param map XML�ɋL�q���ꂽ${*}��*�Ƀ}�b�`����key������ꍇ��value�Œu������
	 * @param cls Bean�ł���K�v������A�����Ă��̒l��setter�ɓ���邽�߃N���X �N���X��.class�������œ����
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryBean(HashMap map,Class<T> cls) throws Exception {
		String select = this.sql;
		BeanListHandler blh = new BeanListHandler(cls);

		return (List<T>)runner.query(conn,sqlMarge(map),blh);
	}

	/**
	 * 
	 * �N���X���烊�t���N�V�������g�p����get���Ăяo��
	 * 
	 * @param obj
	 * @return
	 */
	public ResultSet query(HashMap<String,String> map) {
		String select = this.sql;

//		System.out.println(sqlMarge(map));
		
		return null;
	}
	
	/**
	 * 
	 * SQL���͂�${}������u������SQL���͂�n��
	 * XML�ɏ����Ă���param�ɑΉ�����p�����[�^�̂ݒu��������
	 * @return
	 */
	protected String sqlMarge(HashMap<String,String> map) {
		String select = this.sql;

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
			//	new PeaceFrameWorkException("getter���\�b�h��������Ȃ��ł�",nme);
			} catch(IllegalAccessException iae) {
			//	new PeaceFrameWorkException("getter���\�b�h��������Ȃ��ł�",iae);
			} catch(InvocationTargetException ite) {
			//	new PeaceFrameWorkException("getter���\�b�h��������Ȃ��ł�",ite);
			}
		}
		select = sqlMarge(getterParams);
		return select;
	}

	public int update(HashMap map) {
		return 0;
	}
	public int update(Object map) {
		return 0;
	}
}


//	-------------------------
//	$log: $
//	-------------------------