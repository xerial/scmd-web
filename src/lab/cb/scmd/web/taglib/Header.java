//--------------------------------------
// SCMDServer
// 
// Header.java 
// Since: 2004/07/15
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
public class Header extends TagSupport
{

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        try
        {
            out.println("<html>");            
            out.println("<link rel=stylesheet type=\"text/css\" href=\"" + getCss() + "\"/>");
            out.println("<head><title>SCMD " + getTitle() + "</title></head>");
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
        return(SKIP_BODY);
    }
    
    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getCss()
    {
        return css;
    }
    public void setCss(String css)
    {
        this.css = css;
    }
    String title = "";
    String css = "scmd.css";
    
}


//--------------------------------------
// $Log: Header.java,v $
// Revision 1.3  2004/07/21 14:48:06  leo
// cssのパスの自動挿入を取りやめ （絶対パスで指定したほうがわかりやすかった）
//
// Revision 1.2  2004/07/17 08:03:46  leo
// session管理によるphotoViewer着工
//
// Revision 1.1  2004/07/15 09:21:20  leo
// jsp の使用開始
//
//--------------------------------------