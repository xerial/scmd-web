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
 *　Beansで返す部分はDBUtilを使用する
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

	//	DBUtilsな部分
	public QueryRunner runner;

	/**
	 * 生でインスタンス生成できないようにprotectedにする
	 */
	protected SqlQuery(HashMap<String,String> global) {
		this.global = global;
		params = new HashMap<String,String>();
	}
	
	protected void setSql(String sql) {
		this.sql = sql;
		//	最初にグローバル変数を適応しておく
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
	 * @param map XMLに記述された${*}の*にマッチするkeyがある場合はvalueで置換する
	 * @param cls Beanである必要があり、そしてその値をsetterに入れるためクラス クラス名.classを引数で入れる
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
	 * クラスからリフレクションを使用してgetを呼び出す
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
	 * SQL文章の${}部分を置換したSQL文章を渡す
	 * XMLに書いてあるparamに対応するパラメータのみ置換させる
	 * @return
	 */
	protected String sqlMarge(HashMap<String,String> map) {
		String select = this.sql;

		for(String param : params.keySet()) {
			String value = params.get(param);
			//	
			if(map.containsKey(param)) {
				select = select.replaceAll("\\$\\{"+param+"\\}",map.get(param));
			//	データがない場合はXMLからデータを取得する
			} else {
				select = select.replaceAll("\\$\\{"+param+"\\}",value);
			}
		}
		return select;
	}
	/**
	 * 
	 * sqlMargeのグローバル変数用
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
			//	データがない場合はXMLからデータを取得する
			} else {
				select = select.replaceAll("\\$\\{"+param+"\\}",value);
			}
		}
		return select;
	}
	/**
	 * 
	 * Beanにあるgetterを呼び出して値を取得後SQLを生成する
	 * getterは基本的にtoStringでキャストされる
	 * 
	 * @param bean paramからgetterを呼び出す
	 * @return
	 * @throws Exception
	 */
	public ResultSet query(Object bean) throws Exception {
		String sql = createSQLMarge(bean);
		return null;
	}
	
	/**
	 * 
	 * SQLを発行せずに
	 * SQL文を作りその文章を返すだけ
	 * 
	 * @param map
	 * @return
	 */
	public String createSQLMarge(HashMap<String,String> map) {
		return sqlMarge(map);
	}

	/**
	 * 
	 * SQLを発行せずに
	 * SQL文を作りその文章を返すだけ
	 * 
	 * @param bean getterがあるJavaBeans
	 * @return
	 */
	public String createSQLMarge(Object bean) {
		String select = this.sql;
		HashMap getterParams = new HashMap<String,String>();

		for(String param : params.keySet()) {
			String method = param.substring(0,1).toUpperCase()+param.substring(1);

			Object resp = null;
			try{
				//	ここはgetMethodsで全部取得して名前を見たほうがいいかも？
				resp = bean.getClass().getMethod("get"+method,null).invoke(bean);
				getterParams.put(param,resp);
			} catch(NoSuchMethodException nme) {
			//	new PeaceFrameWorkException("getterメソッドが見つからないです",nme);
			} catch(IllegalAccessException iae) {
			//	new PeaceFrameWorkException("getterメソッドが見つからないです",iae);
			} catch(InvocationTargetException ite) {
			//	new PeaceFrameWorkException("getterメソッドが見つからないです",ite);
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