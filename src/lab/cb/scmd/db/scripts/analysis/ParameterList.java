package lab.cb.scmd.db.scripts.analysis;

/**
 * @author sesejun
 * 
 * parameterlist ‚ğ•Û‚·‚éƒNƒ‰ƒX
 */
public class ParameterList {
    int pid;
    String name;
    String definition;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDefinition() {
        return definition;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
}
