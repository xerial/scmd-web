/*
 * Created on 2005/03/02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Strainname {
    String strainname = "";
    String primaryname = "";
    
    public String getStrainname() {
        return strainname;
    }
    public void setStrainname(String strainname) {
        this.strainname = strainname;
    }
    
    public String getPrimaryname() {
        if( primaryname.length() == 0 )
            return strainname;
        return primaryname;
    }
    public void setPrimaryname(String primaryname) {
        this.primaryname = primaryname;
    }
}
