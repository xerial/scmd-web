//--------------------------------------
//SCMDServer
//
//ViewParamSheetAction.java 
//Since: 2004/07/26
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/action/ViewORFListAction.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------
package lab.cb.scmd.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ORFListViewForm;
import lab.cb.scmd.web.formbean.ViewParamSheetForm;
import lab.cb.scmd.web.sessiondata.MorphParameter;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author sesejun
 *
 */
public class ViewParamSheetAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String parameter = (String) request.getParameter("param");

//        HttpSession session = request.getSession(true);
//        ViewParamSheetForm view = (ViewParamSheetForm) session.getAttribute("view");
//        if(view == null)
//            view = new ViewParamSheetForm();

        request.setAttribute("param", DBUtil.getParamInfo(parameter, "orf"));
        return mapping.findForward("success");
    }
    
}
