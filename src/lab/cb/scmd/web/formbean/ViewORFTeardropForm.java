//--------------------------------------
// SCMDWeb Project
//
// ViewORFTeardropForm.java
// Since: 2005/02/13
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.formbean;

import javax.servlet.http.HttpServletRequest;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.web.common.StainType;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * 各ORFに対する全パラメータのTeardrop表示に使用するフォーム
 * @author leo
 *
 */
public class ViewORFTeardropForm extends ActionForm
{
    static final String[] sheetTypeList = {"Cell", "Nucleus", "Actin", "Custom" };  
    String orf = "yor202w";
    int sheetType = -1;
    
    /**
     * 
     */
    public ViewORFTeardropForm()
    {
        super();
    }

    
    
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        if(sheetType < 0 || sheetType >= sheetTypeList.length)
            sheetType = 0; // default sheet type
        
        return super.validate(arg0, arg1);
    }
    
    public String[] getSheetTypeList()
    {
        return sheetTypeList; 
    }
    
    public String getOrf()
    {
        return orf;
    }
    public void setOrf(String orf)
    {
        this.orf = orf;
    }
    
    
    public int getSheetType()
    {
        return sheetType;
    }
    public void setSheetType(int sheetType)
    {
        this.sheetType = sheetType;
    }
}




