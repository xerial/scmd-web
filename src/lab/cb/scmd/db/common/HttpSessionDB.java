/**
 * 
 */
package lab.cb.scmd.db.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import lab.cb.scmd.db.bean.HttpSessionDBBean;
import lab.cb.scmd.db.connect.SCMDManager;
import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 *
 * setするオブジェクトはSerializableをimplementsしたクラスでなければならない
 * 通常のデータを扱うクラスString HashMap等はSerializableです
 * Beansも規約によりSerializableをつけることになっているので問題ない
 * 
 * データベースの設計
 * テーブル名 httpsession
 * jsession テキスト型 プライマリーキー
 * name テキスト型
 * object バイナリ型
 * 
 * @author mattun
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 */
public class HttpSessionDB implements HttpSession{
	protected String jsessionid = null;
	protected HttpSession session = null;	//	インスタンス生成時に入れる
	protected boolean useDB = false;		//	データベースを使用するか？デフォルトでは使用しない

	/**
	 * 
	 * sessionからjsession情報を取り出してデータベースを参照する
	 * 
	 * @param session
	 */
	public HttpSessionDB(HttpServletRequest request) {
		session = request.getSession();
		jsessionid = session.getId();
	}

	/**
	 * sessionからjsession情報を取り出してデータベースを参照する
	 * 
	 * @param request
	 * @param useDB TRUE=DBを使用する,FALSE=DBを使用しない
	 */
	public HttpSessionDB(HttpServletRequest request,boolean useDB) {
		this.useDB = useDB;
		session = request.getSession();
		jsessionid = session.getId();
	}
	
	/**
	 * 
	 * jsessionとnameから検索するSQLを生成
	 * 
	 * @param jsession
	 * @param name
	 * @return
	 */
//	protected String searchSql(String jsession) {
//		return String.format("SELECT jsession,name,object FROM "+SCMDConfiguration.getProperty("DB_HTTPSESSION")+" WHERE jsession = '%s';",jsession);
//	}
//	protected String searchSql(String jsession,String name) {
//		return String.format("SELECT jsession,name,obj FROM "+SCMDConfiguration.getProperty("DB_HTTPSESSION")+" WHERE jsession = '%s' AND name = '%s';",jsession,name);
//	}
	
	public Object getAttribute(String name) {
		if(useDB) {
			try{
				//	jsessionとnameをキーとしてデータベースに問い合わせてオブジェクトを取り出す
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("jsession",jsessionid);
				map.put("name",name);
				List<HttpSessionDBBean> sessionbeans = SCMDManager.getDBManager().queryResults("lab.cb.scmd.db.common.HttpSessionDB:searchParam",map,HttpSessionDBBean.class);
				//	データがある場合はバイナリデータをオブジェクトに変換して返す
				if(sessionbeans.size() == 1) {
					return sessionbeans.get(0).getObject();
				} else return null;
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			return session.getAttribute(name);
		}
		return null;
	}

	//	TODO 未実装
	public Enumeration getAttributeNames() {
		if(useDB) {
			//	キーリストを取得してEnumerationで返す
//			try{
//				Vector<Object> vec = new Vector<Object>();
//				BasicTable bt = query(searchSql(jsessionid));
//				//Enumeration emun = new Enumeration();
//				for(int i=0;i<bt.getColSize();i++) {
//				}
//				if(bt.getColSize() != 0) {
//					
//				}
//			} catch (InvalidSQLException ise) {
//				
//			}
		} else {
			return session.getAttributeNames();
		}
		return null;
	}

	public long getCreationTime() {
		return session.getCreationTime();
	}

	public String getId() {
		return session.getId();
	}

	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	public Object getValue(String name) {
		return getAttribute(name);
	}

	public String[] getValueNames() {
		if(useDB) {
		} else {
			return session.getValueNames();			
		}
		//	keylistを返す
		return null;
	}

	public void invalidate() {
		if(useDB) {
			try {
				SCMDManager.getDBManager().executeUpdate("DELETE FROM httpsession WHERE jsession = ?",jsessionid);
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			session.invalidate();			
		}
	}

	public boolean isNew() {
		return session.isNew();
	}

	public void putValue(String name, Object obj) {
		setAttribute(name,obj);
	}

	public void removeAttribute(String name) {
		if(useDB) {
			try{
				SCMDManager.getDBManager().executeUpdate("DELETE FROM httpsession WHERE jsession = ? AND name = ?",jsessionid,name);
			} catch(SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			session.removeAttribute(name);
		}
	}

	public void removeValue(String name) {
		removeAttribute(name);
	}

	public void setAttribute(String name, Object obj) {
		if(useDB) {
			ObjectOutputStream out = null;
			try{
				ByteArrayOutputStream byteout = new ByteArrayOutputStream();
				out = new ObjectOutputStream(byteout);
				out.writeObject(obj);
				try{
					if(getAttribute(name) == null) {
						SCMDManager.getDBManager().executeUpdate("INSERT INTO "+SCMDConfiguration.getProperty("DB_HTTPSESSION")+"(jsession,name,obj) VALUES(?,?,?)",jsessionid,name,byteout.toByteArray());
					} else {
						SCMDManager.getDBManager().executeUpdate("UPDATE "+SCMDConfiguration.getProperty("DB_HTTPSESSION")+" SET obj = ? WHERE jsession = ? AND name = ?",byteout.toByteArray(),jsessionid,name);
					}
				} catch(SQLException sqle) {
					sqle.printStackTrace();
				}
				byteout.close();
				out.close();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			session.setAttribute(name,obj);
		}
	}

	public void setMaxInactiveInterval(int time) {
		//	セッションがなくなればjsessionidも変わるのでセッションの保持時間はセッションに任せる
		session.setMaxInactiveInterval(time);
	}
}

//	-------------------------
//	$log: $
//	-------------------------