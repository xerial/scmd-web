//--------------------------------------
// SCMDServer
// 
// GroupViewForm.java 
// Since: 2004/08/11
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.action.ActionForm;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.StainType;

/** 
 * @author leo
 *  
 */
public class GroupViewForm extends ActionForm
{
    String orf;
    int    stainType = StainType.STAIN_ConA;
    int    photoType = PhotoType.ANALYZED_PHOTO;
    PageStatus pageStatus = new PageStatus(1, 1);
    /**
     *  
     */
    public GroupViewForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Map getArgumentMap()
    {
        TreeMap map = new TreeMap();
        map.put("orf", orf);
        map.put("stainType", Integer.toString(stainType));
        return map;
    }
    
    public String getOrf() {
        return orf;
    }
    public void setOrf(String orf) {
        this.orf = orf;
    }
    public int getPage() {
        return pageStatus.getCurrentPage();
    }
    public void setPage(int page) {
        this.pageStatus.setCurrentPage(page);
    }

    public int getMaxPage() {
        return pageStatus.getMaxPage();
    }
    
    public void setMaxPage(int maxPage)
    {
        this.pageStatus.setMaxPage(maxPage);
    }
    public int getPhotoType() {
        return photoType;
    }
    public void setPhotoType(int photoType) {
        this.photoType = photoType;
    }
    public int getStainType() {
        return stainType;
    }
    public void setStainType(int stainType) {
        this.stainType = stainType;
    }

}

//--------------------------------------
// $Log: GroupViewForm.java,v $
// Revision 1.2  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.1  2004/08/31 08:44:01  leo
// Group By Sheet完成
//
// Revision 1.3  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.2  2004/08/15 07:14:42  sesejun
// GroupByのDB接続書き始め(未完成)
//
// Revision 1.1  2004/08/11 14:02:32  leo
// Group by のシート作成
//
//--------------------------------------
