//--------------------------------------
// SCMDWeb Project
//
// ViewCustomizeForm.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.formbean;

import java.sql.SQLException;
import java.util.List;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.scripts.bean.Parameter;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.ActionForm;

/**
 * @author leo
 *
 */
public class ViewCustomizeForm extends ActionForm
{
    static private String[] _cellParams;
    static List<Parameter> _cellParameterList; 
    
    public static void loadParameters() throws SQLException
    {
        _cellParameterList = (List<Parameter>) ConnectionServer.query("select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id", new BeanListHandler(Parameter.class));
    }
    
    /**
     * 
     */
    public ViewCustomizeForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
}




