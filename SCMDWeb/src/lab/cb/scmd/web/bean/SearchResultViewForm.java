//--------------------------------------
// SCMDServer
// 
// SearchResultViewForm.java 
// Since: 2004/08/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.action.ActionForm;

/**
 * @author leo
 *  
 */
public class SearchResultViewForm extends ActionForm
{
    String keyword = "";
    int    page    = 1;

    /**
     *  
     */
    public SearchResultViewForm()
    {
        super();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map getArgumentMap() {
        TreeMap map = new TreeMap();
        map.put("keyword", keyword);
        return map;
    }
}

//--------------------------------------
// $Log: SearchResultViewForm.java,v $
// Revision 1.2  2004/08/31 04:46:21  leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
// Revision 1.1 2004/08/27 08:57:43 leo
// 検索機能を追加 pageの移動はまだ
//
//--------------------------------------
