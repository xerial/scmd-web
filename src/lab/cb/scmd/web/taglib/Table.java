//--------------------------------------
// SCMDServer
// 
// Table.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import lab.cb.scmd.web.design.TablePrinter;

/**
 * @author leo
 *
 */
public class Table extends TagSupport
{
    String name = "";

    public int doStartTag() throws JspException {
        
        Object tableInstance = pageContext.findAttribute(name);
        if(tableInstance == null)
            return SKIP_BODY;
        
        if(lab.cb.scmd.web.table.Table.class.isAssignableFrom(tableInstance.getClass()))
        {
            TablePrinter printer = new TablePrinter(pageContext.getOut());
            printer.printTable((lab.cb.scmd.web.table.Table) tableInstance);
            
        }
        return SKIP_BODY;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name; 
    }
}


//--------------------------------------
// $Log: Table.java,v $
// Revision 1.2  2004/08/24 07:36:15  leo
// AbstractTable‚ð”pŠü
//
// Revision 1.1  2004/08/09 09:17:11  leo
// Table‚Ì”ÍˆÍŽw’è‚ÉTableRange‚ðŽg‚¤‚æ‚¤‚É‚µ‚½
//
//--------------------------------------