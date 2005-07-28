/*
 * Created on 2005/02/23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.go;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Def {
    String defstr = "";
    Dbxref[] dbxref = new Dbxref[0];
    
    public Dbxref[] getDbxref() {
        return dbxref;
    }
    public void setDbxref(Dbxref[] dbxref) {
        this.dbxref = dbxref;
    }
    public String getDefstr() {
        return defstr;
    }
    public void setDefstr(String defstr) {
        this.defstr = defstr;
    }
}
