//-----------------------------------
// SCMDWeb Project
// 
// ViewORFParameter.java 
// Since: 2005/03/15
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.formbean.ViewORFParameterForm;
import lab.cb.scmd.web.sessiondata.MorphParameter;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 行：ORF、列：ORFパラメータ　のデータシートを準備するAction
 * @author leo
 *
 */
public class ViewORFParameter extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse responce) throws Exception
    {
        ViewORFParameterForm input = (ViewORFParameterForm) form;
        
        
        
        List<MorphParameter> selectedORFParameter = null;
        switch(input.getColumnType())
        {
        case input:
            selectedORFParameter = 
                (List<MorphParameter>)
                ConnectionServer.query(new BeanHandler(MorphParameter.class), 
                    "select * from $1 where scope='orf' and paramid in ($2)",
                    SCMDConfiguration.getProperty("DB_PARAMLIST", "parameterlist"),
                    SQLUtil.commaSeparatedList(input.getParamID(), SQLUtil.QuotationType.singleQuote));
            break;
        case custom:
            selectedORFParameter = SCMDSessionManager.getParamUserSelection(request).getOrfParamInfo();
            break;
        default:
            // TODO invalid columnType
            break;
        }

        if(selectedORFParameter.size() <= 0)
        {
            // TODO invalid column
        }
        
        
        String format = input.getFormat();
        if(format.equals("xml"))
        {
            // output xml data
            
            
            return super.execute(mapping, form, request, responce);
        }
        

        
        
        return mapping.findForward("success");
    }
    
    
}
