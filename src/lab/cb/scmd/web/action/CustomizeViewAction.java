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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.formbean.ViewCustomizeForm;
import lab.cb.scmd.web.sessiondata.ParamUserSelection;

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
        ViewCustomizeForm selection = (ViewCustomizeForm) form;
        List cellParameter = selection.getCellParameterList();
        List orfParameter  = selection.getORFParameterList();
        
        // sessionを取得
        HttpSession session = request.getSession(true);
        ParamUserSelection paramSelection = (ParamUserSelection) session.getAttribute("paramSelection");
        if(paramSelection == null)
            paramSelection = new ParamUserSelection();
        
        if(selection.getButton().equals("reset all"))
        {
            paramSelection.resetCellParam();
            paramSelection.resetOrfParam();
        }
        
        // パラメータの値を取得
        //TableQuery query = SCMDConfiguration.getTableQueryInstance();
        //List parameterInfo = query.getParameterInfo();
        //request.setAttribute("paramList");
        
        
        return mapping.findForward("success");
    }

    
}




