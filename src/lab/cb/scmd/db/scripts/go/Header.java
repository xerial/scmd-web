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
public class Header {
    String format_version;
    String date;
    String saved_by;
    String auto_generated_by;
    String default_namespace;
    String remark;
    Subsetdef[] subsetdef = new Subsetdef[0];
    
    public String getAuto_generated_by() {
        return auto_generated_by;
    }
    public void setAuto_generated_by(String auto_generated_by) {
        this.auto_generated_by = auto_generated_by;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDefault_namespace() {
        return default_namespace;
    }
    public void setDefault_namespace(String default_namespace) {
        this.default_namespace = default_namespace;
    }
    public String getFormat_version() {
        return format_version;
    }
    public void setFormat_version(String format_version) {
        this.format_version = format_version;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSaved_by() {
        return saved_by;
    }
    public void setSaved_by(String saved_by) {
        this.saved_by = saved_by;
    }
    public Subsetdef[] getSubsetdef() {
        return subsetdef;
    }
    public void setSubsetdef(Subsetdef[] subsetdef) {
        this.subsetdef = subsetdef;
    }
}
