//--------------------------------------
// SCMDServer
// 
// CellViewerAction.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;


import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.action.logic.ActionLogic;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellList;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.common.StainType;

/**
 * @author leo
 *  
 */
public class CellViewerAction extends Action
{
    ActionLogic _logic = new ActionLogic();
    /**
     *  
     */
    public CellViewerAction()
    {
    }


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response)   throws Exception
    {
        CellViewerForm viewerForm = SCMDSessionManager.getCellViewerForm(request);
        
        _logic.handleAction(viewerForm, request);
        
        CellList cellList = null;
        try
        {
            cellList = _logic.loadCellList(viewerForm);
        }
        catch(SCMDException e)
        {
        	System.out.println(e);
            request.getSession(true).removeAttribute("view");
            return mapping.findForward("failure");
        }

        
        
        request.setAttribute("cellList", cellList.getCellList());
        request.setAttribute("gene", DBUtil.getGeneInfo(viewerForm.getOrf()));
        request.setAttribute("tabName", StainType.TAB_NAME);
        
        TreeMap map = new TreeMap();
        viewerForm.setupQueryMap(map);
        request.setAttribute("photoPropertyMap", map);

        return mapping.findForward("success");
    }

}

//--------------------------------------
// $Log: CellViewerAction.java,v $
// Revision 1.3  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.2  2004/08/12 17:48:26  leo
// update
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------
