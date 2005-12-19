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

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.SCMDManager;
import lab.cb.scmd.db.sql.SQLExpression;
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
		List<MorphParameter> cellParamList = SCMDManager.getDBManager().queryResults("ParameterHelp:cellParamList",null,MorphParameter.class);
		List<MorphParameter> orfParamList = SCMDManager.getDBManager().queryResults("ParameterHelp:orfParamList",null,MorphParameter.class);

/*        
        String sql = SQLExpression.assignTo(
                "select t1.id, t1.name, displayname, scope, t1.stain, t2.specifier as \"groupName\", t1.datatype as datatype from $1 as t1 left join $2 as t2 on groupid = t2.id where datatype in ('num', 'coordinate') and scope ='$3' order by t1.id ",
                SCMDConfiguration.getProperty("DB_PARAMETERLIST", "visible_parameterlist"),
                SCMDConfiguration.getProperty("DB_GROUPLIST", "groups")); 
                

        List<MorphParameter> cellParamList = (List<MorphParameter>) ConnectionServer.query(
                new BeanListHandler(MorphParameter.class),
                sql,
                "", "", // $1, $2 (dummy)
                "cell");
        List<MorphParameter> orfParamList = (List<MorphParameter>) ConnectionServer.query(
                new BeanListHandler(MorphParameter.class),
                sql,
                "", "", // $1, $2 (dummy)
                "orf");
*/        
        request.setAttribute("cellParamList", cellParamList);                           
        request.setAttribute("orfParamList", orfParamList);                           
        
        return mapping.findForward("success");
    }
}




