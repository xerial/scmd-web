//--------------------------------------
// SCMDWeb Project
//
// Format.java
// Since: 2005/02/18
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.taglib;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * contentをDoubleとしてフォーマットして出力するタグ
 * @author leo
 *
 */
public class Format extends BodyTagSupport
{
    String format = "%f";
    Double value = null;
    /**
     * 
     */
    public Format()
    {
        super();
    }
    

    public int doEndTag() throws JspException
    {
        try
        {
            if(value == null)
                value = Double.parseDouble(getBodyContent().getString());
        }
        catch(NumberFormatException e) 
        {
            try
            {
                pageContext.getOut().print(getBodyContent().getString());
            }
            catch (IOException e1)
            {
                throw new JspException(e1);
            }
            return EVAL_PAGE;
        }        
        PrintWriter out = new PrintWriter(pageContext.getOut());
        out.printf(format, value);
        return EVAL_PAGE;
    }
    
    
    public String getFormat()
    {
        return format;
    }
    public void setFormat(String format)
    {
        this.format = format;
    }
    public Double getValue()
    {
        return value;
    }
    public void setValue(Double value)
    {
        this.value = value;
    }
}




