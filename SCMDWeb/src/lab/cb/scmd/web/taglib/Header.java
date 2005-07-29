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

import javax.servlet.http.HttpServletRequest;
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
            out.println("<head><title>SCMD " + getTitle() + "</title>");
            out.println("<link rel=StyleSheet href=\"" + getCss() + "\" type=\"text/css\">");
            out.println("</head>");
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
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return request.getContextPath() + css;   
    }
    public void setCss(String css)
    {
        this.css = css;
    }
    String title = "";
    String css = "/css/scmd.css";
    
}


//--------------------------------------
// $Log: Header.java,v $
// Revision 1.3  2004/07/21 14:48:06  leo
// css�̃p�X�̎����}��������� �i��΃p�X�Ŏw�肵���ق����킩��₷�������j
//
// Revision 1.2  2004/07/17 08:03:46  leo
// session�Ǘ��ɂ��photoViewer���H
//
// Revision 1.1  2004/07/15 09:21:20  leo
// jsp �̎g�p�J�n
//
//--------------------------------------