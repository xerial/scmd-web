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

import javax.servlet.http.HttpServletRequest;

import lab.cb.scmd.db.connect.SCMDTableQuery;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.web.table.Table;

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
    String _button = "add selections";
        
    public static void loadParameters() throws SQLException
    {
        SCMDTableQuery query = new SCMDTableQuery();
        _cellParameterList = query.getParameterList("cell", "num");
        _orfParameterList = query.getParameterList("orf", "double");
        //Table table = query.getParameterTable();
         
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

    
    public void setButton(String buttonName)
    {
        _button = buttonName;
    }
    public String getButton()
    {
        return _button;
    }

}




