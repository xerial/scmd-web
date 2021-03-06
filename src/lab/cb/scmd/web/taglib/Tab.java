//--------------------------------------
// SCMDServer
// 
// Tab.java 
// Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author leo
 *
 */
public class Tab extends BodyTagSupport{
    String name;
    int width = -1;
    
    public int doStartTag() throws JspException {

        TabList tabList = (TabList) findAncestorWithClass(this, TabList.class);
        if(tabList == null)
        {
            throw new JspException("tab must be used inside tablist");
        }
        
        try
        {
            JspWriter out = pageContext.getOut();
            if(name.equals(tabList.getSelected()))
            {
                out.print("<td class=\"selected\"");
                //out.print(" onMouseOver=\"switchStyle(this, 'selected_hover');\"");
                //out.print(" onMouseOut=\"switchStyle(this, 'selected');\"");
            }
            else
            {
                out.print("<td class=\"imagetab\"");                
                //out.print(" onMouseOver=\"switchStyle(this, 'imagetab_hover');\"");
                //out.print(" onMouseOut=\"switchStyle(this, 'imagetab');\"");
            }
            if(width > 0)
            {
                out.print(" width=\"" + width + "\"");
            }
            out.println(">");
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
        
        return EVAL_BODY_INCLUDE;
    }
    
    
    
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        
        try
        {
            out.println("</td>");
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    
    
}


//--------------------------------------
// $Log: Tab.java,v $
// Revision 1.2  2004/08/01 09:16:11  leo
// bugを修正
//
// Revision 1.1  2004/07/30 07:51:18  leo
// TabSheet用のタグ<lab.cb.scmd-base:tablist> <lab.cb.scmd-base:tab>を追加
//
//--------------------------------------