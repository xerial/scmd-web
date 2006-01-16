/**
 * 
 */
package lab.cb.scmd.web.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

/**
 * GoF Design Pattern Type ***** 
 *
 * @author mattun
 * @version 
 * $Revision: $<br>
 * $LastChangedDate: $<br>
 * $LastChangedBy: $<br>
 */
public class SCMDLogging {
	protected static SCMDLogging logging = new SCMDLogging();

	protected Logger filelogger = null;

	public static void Initialize(String filepath,Level level,boolean xmllog) throws IOException{
		logging.Initialize(filepath,level,xmllog,0);
	}
	
	/**
	 * 
	 * 
	 * @param filepath　ログを吐き出すパス
	 * @param level　LOGレベル java.util.logging.Level
	 * @param xmllog　XML形式で吐き出すか？
	 * @param t　オーバーロード用
	 * @throws IOException
	 */
	public void Initialize(String filepath,Level level,boolean xmllog,int t) throws IOException{
		filelogger = Logger.getLogger("systemlog");
		FileHandler fh = new FileHandler(filepath+"smcd.log");
		if(xmllog) {
			fh.setFormatter(new XMLFormatter());
		} else {
			fh.setFormatter(new SimpleFormatter());
		}
		filelogger.addHandler(fh);
		filelogger.setLevel(level);

	}
	
	public void console() {
		
	}
	
	public static void file(String log,Level level) {
		logging.filelogger.log(level,log);
	}
	
	public void socket() {
		
	}
	
	public void memoery() {
		
	}
}


//	-------------------------
//	$log: $
//	-------------------------