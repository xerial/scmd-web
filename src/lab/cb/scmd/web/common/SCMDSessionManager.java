//--------------------------------------
// SCMDWeb Project
//
// SCMDSessionManager.java
// Since: 2005/02/13
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.sessiondata.ParamUserSelection;

/**
 * tomcatのセッションを扱うコードを集めたクラス
 * @author leo
 *
 */
public class SCMDSessionManager
{
    /**
     * セッションに記録されたUserSelectionの情報を返す
     * @param request
     * @return requestに応じたUserSelectionのインスタンス
     */
    public static UserSelection getUserSelection(HttpServletRequest request)
    {
        HttpSession session = getSession(request);
        UserSelection userSelection = (UserSelection) session.getAttribute("userSelection");
        if(userSelection == null)
        {
            userSelection = new UserSelection();
            session.setAttribute("userSelection", userSelection);
        }
        return userSelection;
    }
    
    /**
     * セッションに記録されたParamUserSelectionの情報を返す
     * @param request 
     * @return requestに応じたParamUserSelectionのインスタンス
     */
    public static ParamUserSelection getParamUserSelection(HttpServletRequest request)
    {
        HttpSession session = getSession(request);
        ParamUserSelection paramUserSelection = (ParamUserSelection) session.getAttribute("paramSelection");
        if(paramUserSelection == null)
        {
            paramUserSelection = new ParamUserSelection();
            session.setAttribute("paramSelection", paramUserSelection);
        }
        return paramUserSelection;
    }

    
    public static HttpSession getSession(HttpServletRequest request)
    {
        return request.getSession(true);
    }
    
    /**
     * 
     */
    private SCMDSessionManager()
    {
    }

    /**
     * @param request
     * @return
     */
    public static CellViewerForm getCellViewerForm(HttpServletRequest request)
    {
        HttpSession session = getSession(request);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
        {
            view = new CellViewerForm();
            session.setAttribute("view", view);
        }
        return view;
    }

}




