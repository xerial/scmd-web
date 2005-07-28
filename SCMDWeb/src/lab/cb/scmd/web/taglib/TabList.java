//--------------------------------------
// SCMDServer
// 
// TabList.java 
// Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author leo
 *
 */
public class TabList extends TagSupport
{
    String selected;

    public int doStartTag() throws JspException {
        
        JspWriter out = pageContext.getOut();
        try
        {
            out.println("<script language=\"JavaScript\"><!--function setStyle(idname, c){  document.getElementById(idname).className=c; }//--></script>");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        return (EVAL_BODY_INCLUDE);
    }
    
    public String getSelected() {
        return selected;
    }
    public void setSelected(String selected) {
        this.selected = selected;
    }
}


//--------------------------------------
// $Log: TabList.java,v $
// Revision 1.2  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.1  2004/07/30 07:51:18  leo
// TabSheet用のタグ<lab.cb.scmd-base:tablist> <lab.cb.scmd-base:tab>を追加
//
//--------------------------------------