//--------------------------------------
// SCMDWeb Project
//
// ParameterHelpAction.java
// Since: 2005/03/25
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.sessiondata.MorphParameter;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ParameterHelpÇÃÇΩÇﬂÇÃèÓïÒÇèWÇﬂÇÈÉNÉâÉX
 * @author leo
 *
 */
public class ParameterHelpAction extends Action
{

    /**
     * 
     */
    public ParameterHelpAction()
    {
        super();
    }

    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        String sql = "select t1.id, t1.name, displayname, scope, t1.stain, t2.specifier as \"groupName\" from $1 as t1 left join $2 as t2 on groupid = t2.id where datatype='num' and scope in ('cell', 'orf') order by t1.id ";
        List<MorphParameter> paramList = (List<MorphParameter>) ConnectionServer.query(
                new BeanListHandler(MorphParameter.class), 
                sql,
                SCMDConfiguration.getProperty("DB_PARAMETERLIST", "visible_parameterlist"),
                SCMDConfiguration.getProperty("DB_GROUPLIST", "groups"));
        
        request.setAttribute("paramList", paramList);                           
        
        return mapping.findForward("success");
    }
}




