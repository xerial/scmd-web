//--------------------------------------
//SCMDServer
//
//MorphologicalParameter.java 
//Since: 2005/02/08
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/bean/YeastGene.java $ 
//$LastChangedBy: sesejun $ 
//--------------------------------------
package lab.cb.scmd.web.sessiondata;

public class MorphParameter {
    int id = 0; 
    String name = "";
    String scope = "";
    String shortName = "";
    String datatype = "";
    String displayname = "";
    String stain = "";
    String groupid = ""; 

    public MorphParameter() {
        
    }
    
    public MorphParameter(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }
    public String getIdStr() {
        return "" + id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getDatatype() {
        return datatype;
    }
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
