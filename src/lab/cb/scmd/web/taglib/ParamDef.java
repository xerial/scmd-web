//--------------------------------------
// SCMDWeb Project
//
// ParamDef.java
// Since: 2005/05/03
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.taglib;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import lab.cb.scmd.web.sessiondata.MorphParameter;

/**
 * パラメータの説明分から、パラメータ名の部分にリンクを張る
 * @author leo
 *
 */
public class ParamDef extends TagSupport
{
    String def = "";
    /**
     * 
     */
    public ParamDef()
    {
        super();
    }

    @Override
    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        
        final Pattern p = Pattern.compile("[ACD](CV)?[0-9]+(-[0-9])?(_(A|A1B|C))?");
        if(def == null)
            return SKIP_BODY;
        
        Matcher m = p.matcher(def);
        StringBuffer buffer = new StringBuffer();
        while(m.find())
        {
            String paramName = def.substring(m.start(), m.end());
            int paramID = MorphParameter.parameterID(paramName);
            if(paramID != -1)
            {
                
                m.appendReplacement(buffer, "<a href=\"" + response.encodeURL("ViewORFParameter.do?columnType=input&paramID=" + paramID + "&sortspec=" + paramID) + "\">" + paramName + "</a>");
            }
            else
                m.appendReplacement(buffer, paramName);
        }
        m.appendTail(buffer);
        
        try
        {
            out.print(buffer.toString());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        
        return SKIP_BODY;
    }

    public String getDef()
    {
        return def;
    }

    public void setDef(String def)
    {
        this.def = def;
    }

}




