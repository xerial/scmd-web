//--------------------------------------
// SCMDServer
// 
// Footer.java 
// Since: 2004/07/17
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
public class Footer extends TagSupport
{

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        try
        {
            out.println("</html>");            
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
        return(SKIP_BODY);
    }

}


//--------------------------------------
// $Log: Footer.java,v $
// Revision 1.1  2004/07/17 08:03:46  leo
// sessionä«óùÇ…ÇÊÇÈphotoVieweríÖçH
//
//--------------------------------------