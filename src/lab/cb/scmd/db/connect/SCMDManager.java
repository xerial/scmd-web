/**
 * 
 */
package lab.cb.scmd.db.connect;


/**
 * Singleton
 * @author mattun
 * @version $Revision$ $LastChangedDate$
 * $LastChangedBy$
 */
/**
 * @author mattun
 * 
 */
public class SCMDManager {
	protected static DBManager dbmanager = null;
	static boolean inited = false;

	public static void initialize() {
		inited = true;
		dbmanager = new DBManager();
		dbmanager.initialize();
	}
	
	public static void restart() {
		if(inited)
			destory();
		initialize();
	}
	
	public static void destory() {
		dbmanager = null;
	}
	
	public static DBManager getDBManager() {
		return dbmanager;
	}
}


//	-------------------------
//	$log: $
//	-------------------------