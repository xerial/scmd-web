//--------------------------------------
// SCMDServer
// 
// GroupByDatasheetForm.java 
// Since: 2004/08/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.GroupType;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.StainType;

/** 
 * @author leo
 *
 */
public class GroupByDatasheetForm extends ActionForm
{
    static String[][] _groupName = new String[][]
                                                {
            {"no", "small", "medium", "large"},
            {"A", "A1", "B", "C", "D", "E", "F"},
            {"A", "B", "api", "iso", "E", "F"}
                                                };
    
    String orf = "yor202w";
    String group = "no";
    int stainType = StainType.STAIN_ConA; 
    PageStatus pageStatus = new PageStatus(1, 1);
    int sheetType = DataSheetType.SHEET_CELL;
    int photoType = PhotoType.ANALYZED_PHOTO;

    
    
    /**
     * 
     */
    public GroupByDatasheetForm()
    {
        super();
    }
    
    

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors error = super.validate(mapping, request);
        
        // is valid stainType?
        if(!StainType.isValid(stainType))
        {
            stainType = StainType.STAIN_ConA;
        }
        
        // is valid group ?
        for(int i=0; i<_groupName[stainType].length; i++)
        {
            if(_groupName[stainType][i].equals(group))
            {
                // valid
                return error;
            }
        }
        group = _groupName[stainType][0];
        
        if(getPage() <= 0)
            setPage(1);
        
        return error;
    }
    
    
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    
    public String getGroupName()
    {
        return GroupType.STAIN_GROUP[stainType];
    }
    public String getOrf() {
        return orf;
    }
    public void setOrf(String orf) {
        this.orf = orf;
    }
    public int getStainType() {
        return stainType;
    }
    public void setStainType(int stainType) {
        this.stainType = stainType;
    }
    
    public void setPage(int page)
    {
        pageStatus.setCurrentPage(page);
    }
    public int getPage()
    {
        return pageStatus.getCurrentPage();
    }
    
    
    public int getSheetType() {
        return sheetType;
    }
    public void setSheetType(int sheetType) {
        this.sheetType = sheetType;
    }
    
    public int getMaxPage()
    {
        return pageStatus.getMaxPage();
    }
    
    public void setMaxPage(int maxPage)
    {
        pageStatus.setMaxPage(maxPage);
    }
    
    public Map getArgumentMap()
    {
        TreeMap map = new TreeMap();
        map.put("orf", getOrf());
        map.put("page", Integer.toString(getPage()));
        map.put("stainType", Integer.toString(getStainType()));
        map.put("group", getGroup());
        
        return map;
    }
    public int getPhotoType() {
        return photoType;
    }
    public void setPhotoType(int photoType) {
        this.photoType = photoType;
    }
}


//--------------------------------------
// $Log: GroupByDatasheetForm.java,v $
// Revision 1.3  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.2  2004/08/31 08:44:01  leo
// Group By SheetŠ®¬
//
// Revision 1.1  2004/08/30 10:43:13  leo
// GroupBySheet‚Ìì¬ page‚ÌˆÚ“®‚Í‚Ü‚¾
//
//--------------------------------------