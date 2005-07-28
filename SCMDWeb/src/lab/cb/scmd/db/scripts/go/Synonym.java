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
public class Synonym {
    String scope = "";
    String synonym_text = "";
    
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getSynonym_text() {
        return synonym_text;
    }
    public void setSynonym_text(String synonym_text) {
        this.synonym_text = synonym_text;
    }
    
    public String[] attributes() { return new String[] { "scope" }; }   
}
