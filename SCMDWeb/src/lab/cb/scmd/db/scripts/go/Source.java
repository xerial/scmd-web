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
public class Source {
    String source_type;
    String source_path;
    String source_md5;
    String source_mtime;
    
    
    public String getSource_md5() {
        return source_md5;
    }
    public void setSource_md5(String source_md5) {
        this.source_md5 = source_md5;
    }
    public String getSource_mtime() {
        return source_mtime;
    }
    public void setSource_mtime(String source_mtime) {
        this.source_mtime = source_mtime;
    }
    public String getSource_path() {
        return source_path;
    }
    public void setSource_path(String source_path) {
        this.source_path = source_path;
    }
    public String getSource_type() {
        return source_type;
    }
    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }
}
