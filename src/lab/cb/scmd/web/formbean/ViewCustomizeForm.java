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
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.web.action.ViewCellInfoAction;
import lab.cb.scmd.web.common.ConfigObserver;
import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.ActionForm;

/**
 * @author leo
 *
 */
public class ViewCustomizeForm extends ActionForm 
{
    static List<Parameter> _cellParameterList; 
    static List<Parameter> _orfParameterList;
    
    public static void loadParameters() throws SQLException
    {
        String sql = "select id, name, scope, datatype from parameterlist where scope='$1' and datatype='num' order by id";        
        _cellParameterList = (List<Parameter>) ConnectionServer.query(new BeanListHandler(Parameter.class), sql, "cell");
        _orfParameterList = (List<Parameter>) ConnectionServer.query(new BeanListHandler(Parameter.class), sql, "orf");
        Table table = ConnectionServer.retrieveTable(sql, "orf");
         
        System.out.println("[scmd-server] ViewCustomizeForm is initialized");
    }

    
    private String[] selectedCellParameter = new String[] {};
    private String[] selectedORFParameter = new String[] {};
    
    /**
     * 
     */
    public ViewCustomizeForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }


    
    public String[] getSelectedCellParameter()
    {
        return selectedCellParameter;
    }
    public void setSelectedCellParameter(String[] selectedCellParameter)
    {
        this.selectedCellParameter = selectedCellParameter;
    }
    public String[] getSelectedORFParameter()
    {
        return selectedORFParameter;
    }
    public void setSelectedORFParameter(String[] selectedORFParameter)
    {
        this.selectedORFParameter = selectedORFParameter;
    }


}




