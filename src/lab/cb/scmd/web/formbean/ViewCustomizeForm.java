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
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.web.action.ViewCellInfoAction;
import lab.cb.scmd.web.common.ConfigObserver;
import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

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
        String sql = "select id, name, shortname, scope, datatype from parameterlist where scope='$1' and datatype='$2' order by id";        
        _cellParameterList = (List<Parameter>) ConnectionServer.query(new BeanListHandler(Parameter.class), sql, "cell", "num");
        _orfParameterList = (List<Parameter>) ConnectionServer.query(new BeanListHandler(Parameter.class), sql, "orf", "double");
        Table table = ConnectionServer.retrieveTable(sql, "orf");
         
        System.out.println("[scmd-server] ViewCustomizeForm is initialized");
    }

    
    private Integer[] selectedCellParameter = new Integer[] {};
    private Integer[] selectedORFParameter = new Integer[] {};
    
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        super.reset(arg0, arg1);
        
        //selectedCellParameter = new Integer[0];
        //selectedORFParameter = new Integer[0]; 
    }
    
    /**
     * 
     */
    public ViewCustomizeForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public List<Parameter> getCellParameterList()
    {
        return _cellParameterList;
    }
    public List<Parameter> getORFParameterList()
    {
        return _orfParameterList;
    }
    
    public Integer[] getSelectedCellParameter()
    {
        return selectedCellParameter;
    }
    public void setSelectedCellParameter(Integer[] selectedCellParameter)
    {
        this.selectedCellParameter = selectedCellParameter;
    }
    public Integer[] getSelectedORFParameter()
    {
        return selectedORFParameter;
    }
    public void setSelectedORFParameter(Integer[] selectedORFParameter)
    {
        this.selectedORFParameter = selectedORFParameter;
    }


}




