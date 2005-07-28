//--------------------------------------
// SCMDServer
//
// ORFListViewForm.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.db.common.PageStatus;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ORFListViewForm extends ActionForm {

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        // TODO Auto-generated method stub
        super.reset(arg0, arg1);
    }
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        // TODO Auto-generated method stub
        return super.validate(arg0, arg1);
    }
    HashMap pageStatusMap = new HashMap(); 
    
    public ORFListViewForm()
    {
        super();
        pageStatusMap.put("wildtype", new PageStatus(1, 1, 10));
        pageStatusMap.put("mutant", new PageStatus(1, 2, 40));        
    }
    
    

    public PageStatus getPageStatus(String key) {
        return (PageStatus) pageStatusMap.get(key);
    }
    /**
     * @param pageStatusMap The pageStatusMap to set.
     */
    public void setPageStatus(String key, PageStatus value) {
        pageStatusMap.put(key, value);
    }
    /**
     * @return Returns the pageStatusMap.
     */
    public HashMap getPageStatusMap() {
        return pageStatusMap;
    }
    /**
     * @param pageStatusMap The pageStatusMap to set.
     */
    public void setPageStatusMap(HashMap pageStatusList) {
        this.pageStatusMap = pageStatusList;
    }
}


//--------------------------------------
// $Log: ORFListViewForm.java,v $
// Revision 1.3  2004/08/09 03:39:23  sesejun
// ORFListをDBから生成
//
// Revision 1.2  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page用のクラスを追加
//
//--------------------------------------