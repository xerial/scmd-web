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
 *　Beansで返す部分はDBUtilを使用する
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

	//	DBUtilsな部分
	protected QueryRunner runner;

	/**
	 * 生でインスタンス生成できないようにprotectedにする
	 */
	protected SqlQuery(HashMap<String,String> global) {
		this.global = global;
		runner = new QueryRunner();
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
	 *  castで指定されたClassに格納してListで返す
	 * @param <E>
	 * @param map XMLに記述された${*}の*にマッチするkeyがある場合はvalueで置換する
	 * @param cls Beanである必要があり、そしてその値をsetterに入れるためクラス クラス名.classを引数で入れる
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
	 * 単一のカラムのみを返す
	 * @param map XMLに記述された${*}の*にマッチするkeyがある場合はvalueで置換する
	 * @param column　返すカラムを指定
	 * @return 指定されたカラムの型による
	 * @throws SQLException
	 */
	public Object queryScalar(HashMap<String,String> map,String column) throws SQLException {
		ScalarHandler sh = new ScalarHandler(column);
		return runner.query(conn,createSQLMarge(map),sh);
	}
	/**
	 * 単一のカラムのみを返す
	 * @param map XMLに記述された${*}の*にマッチするkeyがある場合はvalueで置換する
	 * @param column　返すカラムを指定
	 * @return 指定されたカラムの型による
	 * @throws SQLException
	 */
	public Object queryScalar(HashMap<String,String> map,int columnNumber) throws SQLException {
		ScalarHandler sh = new ScalarHandler(columnNumber);
		return runner.query(conn,createSQLMarge(map),sh);
	}
	/**
	 * 単一のカラムのみを返す
	 * @param bean XMLに記述された${*}の*にマッチするkeyがある場合はgetterで値を取得し置換する
	 * @param column　返すカラムを指定
	 * @return　指定されたカラムの型による
	 * @throws SQLException
	 */
	public Object queryScalar(Object bean,String column) throws SQLException {
		ScalarHandler sh = new ScalarHandler(column);
		return runner.query(conn,createSQLMarge(bean),sh);
	}
	/**
	 * 単一のカラムのみを返す
	 * @param bean XMLに記述された${*}の*にマッチするkeyがある場合はgetterで値を取得し置換する
	 * @param column　返すカラムを指定
	 * @return　指定されたカラムの型による
	 * @throws SQLException
	 */
	public Object queryScalar(Object bean,int columnNumber) throws SQLException {
		ScalarHandler sh = new ScalarHandler(columnNumber);
		return runner.query(conn,createSQLMarge(bean),sh);
	}

	/**
	 * 
	 * クラスからリフレクションを使用してgetを呼び出す
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
	 * SQL文章の${}部分を置換したSQL文章を渡す
	 * XMLに書いてあるparamに対応するパラメータのみ置換させる
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
				//	データがない場合はXMLからデータを取得する
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
			} catch(IllegalAccessException iae) {
			} catch(InvocationTargetException ite) {
			}
		}
		select = sqlMarge(getterParams);
		return select;
	}

	/**
	 * INSERT,UPDATE,DELETEなどのＳＱＬ文に使う
	 * 引数はqueryと同じ
	 * @param name
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public int update(HashMap map) throws SQLException{
		return runner.update(createSQLMarge(map));
	}
	/**
	 * INSERT,UPDATE,DELETEなどのＳＱＬ文に使う
	 * 引数はqueryと同じ
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
