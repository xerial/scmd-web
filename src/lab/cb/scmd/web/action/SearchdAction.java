//--------------------------------------
// SCMDServer
// 
// SearchKeywordAction.java 
// Since: 2004/08/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.SearchResultViewForm;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
 *
 * @author leo
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy$
 */
public class SearchdAction extends Action
{

    /**
     *  test
     */
    public SearchdAction()
    {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        SearchResultViewForm searchForm = (SearchResultViewForm) form;
        String keyword = searchForm.getKeyword().trim();
        if(keyword.equals(""))
            return mapping.findForward("failure");
        
        Vector orfList = null;
        Vector<String> keywordList = new Vector<String>();
        for(String k: keyword.split("[ \\,/]+") ){
            keywordList.add(k);
        }
        PageStatus pageStatus = new PageStatus(1, 1);
        try
        {
            XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
            DOMParser parser = new DOMParser();
            XMLReaderThread reader = new XMLReaderThread(parser);
            reader.start();
            query.getSearchResult(reader.getOutputStream(), keyword, searchForm.getPage(), 20);
            reader.join();
            
            Document document = parser.getDocument();
            NodeList nodeList = document.getElementsByTagName("orf");
            orfList = new Vector(nodeList.getLength());
	        for(int i=0; i<nodeList.getLength(); i++)
	        {
	            orfList.add(new YeastGene((Element) nodeList.item(i)));
	        }
	        
	        NodeList pageList = document.getElementsByTagName("page");
	        if(pageList.getLength() > 0)
	        {
	            pageStatus = new PageStatus((Element) pageList.item(0));
	        }
	        if(orfList.size() == 0)
	        {
	            request.setAttribute("keyword", keyword);
	            return mapping.findForward("notfound");
	        }
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SCMDException e)
        {
            e.what();
        }
        
	    request.setAttribute("orfList", orfList);
	    request.setAttribute("pageStatus", pageStatus);
        request.setAttribute("keywordList", keywordList);

        
        return mapping.findForward("success");
    }

}

//--------------------------------------
// $Log: SearchKeywordAction.java,v $
// Revision 1.3  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.2  2004/08/27 09:01:51  leo
// no entryのときエラーページに飛ぶ
//
// Revision 1.1  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
//--------------------------------------
