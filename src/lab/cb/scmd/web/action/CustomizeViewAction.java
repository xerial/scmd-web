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
import lab.cb.scmd.web.sessiondata.MorphParameter;
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
        List<MorphParameter> cellParameter = selection.getCellParameterList();
        List<MorphParameter> orfParameter  = selection.getORFParameterList();
        
        // sessionを取得
        HttpSession session = request.getSession(true);
        ParamUserSelection paramSelection = (ParamUserSelection) session.getAttribute("paramSelection");
        if(paramSelection == null)
            paramSelection = new ParamUserSelection();

        if(selection.getButton().equals("remove all"))
        {
            paramSelection.resetCellParam();
            paramSelection.resetOrfParam();
        }
        else if (selection.getButton().equals("add") || selection.getButton().equals("change"))
        {
            Integer[] inputCellParam = selection.getSelectedCellParameter();
            Integer[] inputORFParam  = selection.getSelectedORFParameter();
            
            for(Integer id : inputCellParam )
                paramSelection.addCellParamSelection(id);
            for(Integer id : inputORFParam )
                paramSelection.addOrfParamSelection(id);
            selection.setSelectedCellParameter(new Integer [0]);
            selection.setSelectedORFParameter(new Integer [0]);
        } 
        else if (selection.getButton().equals("remove"))
        {
            Integer[] inputCellParam = selection.getRemoveCellParamList();
            Integer[] inputORFParam  = selection.getRemoveORFParamList();
            
            for(Integer id : inputCellParam )
                paramSelection.removeCellParamSelection(id);
            for(Integer id : inputORFParam )
                paramSelection.removeOrfParamSelection(id);
            selection.setRemoveCellParamList(new Integer [0]);
            selection.setRemoveORFParamList(new Integer [0]);
        }
        
        // パラメータの値を取得
        //LinkedList para
        List<MorphParameter> cellMorphParams = paramSelection.getCellParamInfo();
        List<MorphParameter> orfMorphParams = paramSelection.getOrfParamInfo();
        request.setAttribute("cellParameterList", cellMorphParams);
        request.setAttribute("orfParameterList", orfMorphParams);
        session.setAttribute("paramSelection", paramSelection);
        
        return mapping.findForward("success");
    }

    
}




