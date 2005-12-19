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
 * set����I�u�W�F�N�g��Serializable��implements�����N���X�łȂ���΂Ȃ�Ȃ�
 * �ʏ�̃f�[�^�������N���XString HashMap����Serializable�ł�
 * Beans���K��ɂ��Serializable�����邱�ƂɂȂ��Ă���̂Ŗ��Ȃ�
 * 
 * �f�[�^�x�[�X�̐݌v
 * �e�[�u���� httpsession
 * jsession �e�L�X�g�^ �v���C�}���[�L�[
 * name �e�L�X�g�^
 * object �o�C�i���^
 * 
 * @author mattun
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 */
public class HttpSessionDB implements HttpSession{
	protected String jsessionid = null;
	protected HttpSession session = null;	//	�C���X�^���X�������ɓ����
	protected boolean useDB = false;		//	�f�[�^�x�[�X���g�p���邩�H�f�t�H���g�ł͎g�p���Ȃ�

	/**
	 * 
	 * session����jsession�������o���ăf�[�^�x�[�X���Q�Ƃ���
	 * 
	 * @param session
	 */
	public HttpSessionDB(HttpServletRequest request) {
		session = request.getSession();
		jsessionid = session.getId();
	}

	/**
	 * session����jsession�������o���ăf�[�^�x�[�X���Q�Ƃ���
	 * 
	 * @param request
	 * @param useDB TRUE=DB���g�p����,FALSE=DB���g�p���Ȃ�
	 */
	public HttpSessionDB(HttpServletRequest request,boolean useDB) {
		this.useDB = useDB;
		session = request.getSession();
		jsessionid = session.getId();
	}
	
	/**
	 * 
	 * jsession��name���猟������SQL�𐶐�
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
				//	jsession��name���L�[�Ƃ��ăf�[�^�x�[�X�ɖ₢���킹�ăI�u�W�F�N�g�����o��
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("jsession",jsessionid);
				map.put("name",name);
				List<HttpSessionDBBean> sessionbeans = SCMDManager.getDBManager().queryResults("lab.cb.scmd.db.common.HttpSessionDB:searchParam",map,HttpSessionDBBean.class);
				//	�f�[�^������ꍇ�̓o�C�i���f�[�^���I�u�W�F�N�g�ɕϊ����ĕԂ�
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

	//	TODO ������
	public Enumeration getAttributeNames() {
		if(useDB) {
			//	�L�[���X�g���擾����Enumeration�ŕԂ�
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
		//	keylist��Ԃ�
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
		//	�Z�b�V�������Ȃ��Ȃ��jsessionid���ς��̂ŃZ�b�V�����̕ێ����Ԃ̓Z�b�V�����ɔC����
		session.setMaxInactiveInterval(time);
	}
}

//	-------------------------
//	$log: $
//	-------------------------