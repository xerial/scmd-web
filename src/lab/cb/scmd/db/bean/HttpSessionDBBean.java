package lab.cb.scmd.db.bean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author mattun
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 */
public class HttpSessionDBBean {
	private String name;
	private String jsession;
	private byte[] obj;
	public String getJsession() {
		return jsession;
	}
	public void setJsession(String jsession) {
		this.jsession = jsession;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getObj() {
		return obj;
	}
	public void setObj(byte[] obj) {
		this.obj = obj;
	}

	/**
	 * 
	 * ObjectInputStreamでバイナリデータをオブジェクト化した状態を返す
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object getObject() {
		try{
			InputStream is = new ByteArrayInputStream(getObj());
			ObjectInputStream ois = new ObjectInputStream(is);
			Object object = ois.readObject();
			ois.close();
			is.close();
			return object;
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch(ClassNotFoundException cnfe) {
			return null;
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------