//--------------------------------------
// SCMDWeb Project
//
// CustomizeViewAction.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author leo
 *
 */
public class CustomizeViewAction extends Action
{

    /**
     * 
     */
    public CustomizeViewAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    {
        
        
        return mapping.findForward("success");
    }

    
}




