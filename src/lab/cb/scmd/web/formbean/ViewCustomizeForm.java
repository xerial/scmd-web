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
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.connect.SCMDTableQuery;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.sessiondata.MorphParameter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author leo
 *
 */
public class ViewCustomizeForm extends ActionForm 
{
    static List<MorphParameter> _cellParameterList; 
    static List<MorphParameter> _orfParameterList;
    String _button = "add selections";
    
        
    public static void loadParameters() throws SQLException
    {
        SCMDTableQuery query = new SCMDTableQuery();
        _cellParameterList = query.getParameterList("cell", "num");
        _orfParameterList = query.getParameterList("orf", "double");
        //Table table = query.getParameterTable();
        
        System.out.println("[scmd-server] ViewCustomizeForm is initialized");
    }

    private List<String> _cellCategoryList = new LinkedList<String> ();
    private List<String> _orfCategoryList = new LinkedList<String> ();
    private Integer[] selectedCellParameter = new Integer[] {};
    private Integer[] selectedORFParameter = new Integer[] {};
    private String cellCategory = "Cell Wall";
    private String orfCategory = "Cell Wall";
    private Integer[] removeCellParamList = new Integer[0];
    private Integer[] removeORFParamList = new Integer[0];
    
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

    public List<MorphParameter> getCellParameterList()
    {
        return _cellParameterList;
    }
    public List<MorphParameter> getORFParameterList()
    {
        return _orfParameterList;
    }
    
    public Integer[] getSelectedCellParameter()
    {
        return selectedCellParameter;
    }
    public void setSelectedCellParameter(Integer[] cellParameters) {
        this.selectedCellParameter = cellParameters;
    }
    public Integer[] getSelectedORFParameter()
    {
        return selectedORFParameter;
    }
    public void setSelectedORFParameter(Integer[] orfParameters)
    {
        this.selectedORFParameter = orfParameters;
    }
    public String getCellCategory() {
        return cellCategory;
    }
    public void setCellCategory(String cellCategory) {
        this.cellCategory = cellCategory;
    }
    
    public String getOrfCategory() {
        return orfCategory;
    }
    public void setOrfCategory(String orfCategory) {
        this.orfCategory = orfCategory;
    }
    public void setButton(String buttonName)
    {
        _button = buttonName;
    }
    public String getButton()
    {
        return _button;
    }
    public Integer[] getRemoveCellParamList() {
        return removeCellParamList;
    }
    public void setRemoveCellParamList(Integer[] removeCellParamList) {
        this.removeCellParamList = removeCellParamList;
    }
    public Integer[] getRemoveORFParamList() {
        return removeORFParamList;
    }
    public void setRemoveORFParamList(Integer[] removeORFParamList) {
        this.removeORFParamList = removeORFParamList;
    }
    public List<String> getCellCategoryList() {
        String[] cellCategory = {"Cell Wall", "Actin", "Nucleus"};
        List<String> cellCategoryList = new LinkedList<String> ();
        for(String c: cellCategory) {
            cellCategoryList.add(c);
        }
        return cellCategoryList;
    }

    public List<MorphParameter> getCellDetailCategoryList() {
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        Set<Integer> category = null;
        if( getCellCategory().equals("Cell Wall") ) {
            category = new TreeSet<Integer> ();
            for(int i = 4; i < 9; i++ ) {
                category.add(i);
            }
            for(int i = 24; i < 50; i++ ) { 
                category.add(i);
            }
        } else if( getCellCategory().equals("Actin") ) {
            category = new TreeSet<Integer> ();
            for(int i = 66; i < 79; i++ ) {
                category.add(i);
            }
        } else {
            category = new TreeSet<Integer> ();
            for(int i = 111; i < 211; i++ ) {
                category.add(i);
            }
        }
        
        List<MorphParameter> params = query.getParameterInfo(category);
        return params;
    }

    public List<String> getORFCategoryList() {
        String[] cellCategory = {"Cell Wall", "Actin", "Nucleus"};
        List<String> cellCategoryList = new LinkedList<String> ();
        for(String c: cellCategory) {
            cellCategoryList.add(c);
        }
        return cellCategoryList;
    }

    public List<MorphParameter> getORFDetailCategoryList() {
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        Set<Integer> category = null;
        if( getOrfCategory().equals("Cell Wall") ) {
            category = new TreeSet<Integer> ();
            for(int i = 221; i < 228; i++ ) {
                category.add(i);
            }
            // nucleus status A
            for(int i = 261; i < 269; i++ ) { 
                category.add(i);
            }
            // nucleus status A1B
            for(int i = 301; i < 328; i++ ) {
                category.add(i);
            }
            // nucleus status C
            for(int i = 380; i < 409; i++ ) {
                category.add(i);
            }
            
        } else if( getOrfCategory().equals("Actin") ) {
            category = new TreeSet<Integer> ();
            for(int i = 228; i < 243; i++ ) {
                category.add(i);
            }
            // nucleus A
            for(int i = 228; i < 243; i++ ) {
                category.add(i);
            }
            // nucleus A1B
            for(int i = 269; i < 298; i++ ) {
                category.add(i);
            }
            // nucleus C
            for(int i = 409; i < 428; i++ ) {
                category.add(i);
            }
        } else { // Dapi
            category = new TreeSet<Integer> ();
            for(int i = 243; i < 261; i++ ) {
                category.add(i);
            }
            // nucleus A
            for(int i = 279; i < 280; i++ ) {
                category.add(i);
            }
            // nucleus A1B
            for(int i = 347; i < 379; i++ ) {
                category.add(i);
            }
            // nucleus C
            for(int i = 428; i < 502; i++ ) {
                category.add(i);
            }
        }
        
        List<MorphParameter> params = query.getParameterInfo(category);
        return params;
    }
}




