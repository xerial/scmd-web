//--------------------------------------
// SCMDServer
//
// ParamPlotAction.java 
// Since: 2004/09/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ParamPlotForm;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamPlotAction extends Action
{
    /**
     * 
     */
    public ParamPlotAction() {
        super();

    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) 
    {
        ParamPlotForm plotForm = (ParamPlotForm) form;
        
        HttpSession session = request.getSession(true);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
            view = new CellViewerForm();

        String orf = view.getOrf();
        
        request.setAttribute("gene", DBUtil.getGeneInfo(orf));
        
        return mapping.findForward("success");
    }
    
}


//--------------------------------------
// $Log: ParamPlotAction.java,v $
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plot‚ð’Ç‰Á
//
//--------------------------------------
