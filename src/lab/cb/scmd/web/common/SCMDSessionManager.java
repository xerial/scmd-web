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

import lab.cb.scmd.db.common.HttpSessionDB;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.sessiondata.ParamUserSelection;

/**
 * tomcat�̃Z�b�V�����������R�[�h���W�߂��N���X
 * @author leo
 *
 */
public class SCMDSessionManager
{
	
	/**
	 * 
	 * SCMDSessionManager����f�[�^�x�[�X���g�p���邩�ݒ�����o��
	 * ���x�̃p�[�X�̓I�[�o�[�w�b�h����
	 * @return
	 */
	protected static boolean useHttpSessionDB() {
    	boolean useDB = false;
    	try{
    		useDB = Boolean.parseBoolean(SCMDConfiguration.getProperty("HTTPSESSIONDB_USE"));
    	} catch(Exception e) {
    		
    	}
    	return useDB;
	}
	
	/**
     * �Z�b�V�����ɋL�^���ꂽUserSelection�̏���Ԃ�
     * @param request
     * @return request�ɉ�����UserSelection�̃C���X�^���X
     */
    public static UserSelection getUserSelection(HttpServletRequest request)
    {
    	//HttpSession session = getSession(request);
    	HttpSession session = new HttpSessionDB(request,useHttpSessionDB());
        UserSelection userSelection = (UserSelection) session.getAttribute("userSelection");
        if(userSelection == null)
        {
            userSelection = new UserSelection();
            session.setAttribute("userSelection", userSelection);
        }
        return userSelection;
    }
    
    /**
     * �Z�b�V�����ɋL�^���ꂽParamUserSelection�̏���Ԃ�
     * @param request 
     * @return request�ɉ�����ParamUserSelection�̃C���X�^���X
     */
    public static ParamUserSelection getParamUserSelection(HttpServletRequest request)
    {
//        HttpSession session = getSession(request);
    	HttpSession session = new HttpSessionDB(request,useHttpSessionDB());
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
//        HttpSession session = getSession(request);
    	HttpSession session = new HttpSessionDB(request,useHttpSessionDB());
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
        {
            view = new CellViewerForm();
            session.setAttribute("view", view);
        }
        return view;
    }

}




