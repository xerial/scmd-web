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

import java.util.LinkedList;
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
import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.GeneOntology;
import lab.cb.scmd.web.bean.SearchResultViewForm;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.container.Enrichments;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
 * @author leo
 *  
 */
public class SearchKeywordAction extends Action
{

    /**
     *  
     */
    public SearchKeywordAction()
    {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        
        SearchResultViewForm searchForm = (SearchResultViewForm) form;
        String keyword = searchForm.getKeyword().trim();
        if(keyword.equals(""))
            return mapping.findForward("failure");
        
        String[] keywords = keyword.split("[ \\,/]+"); 
        Vector orfList = null;
        Vector<String> keywordList = new Vector<String>();
        for(String k: keywords){
            keywordList.add(k);
        }
        PageStatus pageStatus = new PageStatus(1, 1);
        
        // Gene Ontology の情報検索結果と情報をGO id & term で取得
        TableQuery goquery = SCMDConfiguration.getTableQueryInstance();
        Table gotable = goquery.getAssociatedGO(keywords);
        LinkedList<GeneOntology> goList = new LinkedList<GeneOntology>();
        if( gotable != null ) {
            ColLabelIndex colIndex = new ColLabelIndex(gotable);        
            for(int i=1; i<gotable.getRowSize(); i++)
            {
                GeneOntology go = new GeneOntology();
                go.setGoid(colIndex.get(i, "goid").toString());
                go.setName(colIndex.get(i, "name").toString());
                go.setNamespace(colIndex.get(i, "namespace").toString());
                go.setDef(colIndex.get(i, "def").toString());
                
                Table fwdrevtable = goquery.getForwardReverseAssociations(go.getGoid());
                ColLabelIndex colIndex2 = new ColLabelIndex(fwdrevtable);
                for( int j = 1; j < fwdrevtable.getRowSize(); j++ ) {
                	Enrichments fwdrev = new Enrichments();
                	fwdrev.setParam(colIndex2.get(j, "param").toString());
                	fwdrev.setFwd(Integer.parseInt(colIndex2.get(j,"fwd" ).toString()));
                	fwdrev.setHigh(Integer.parseInt(colIndex2.get(j,"high" ).toString()));
                	fwdrev.setIngo(Integer.parseInt(colIndex2.get(j, "ingo").toString()));
                	fwdrev.setInabnorm(Integer.parseInt(colIndex2.get(j, "inabnorm").toString()));
                	fwdrev.setIntarget(Integer.parseInt(colIndex2.get(j, "intarget").toString()));
                	fwdrev.setPvalue(Double.parseDouble(colIndex2.get(j, "pvalue").toString()));
                	fwdrev.setRatio(Double.parseDouble(colIndex2.get(j, "ratio").toString()));
                	go.addFwdRev(fwdrev);
                }
                goList.add(go);
            }
        }
        
        // 遺伝子名, GO等の検索結果を ORF名で取得
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
        	// 1つも該当遺伝子が無い場合も、exception
            e.what();
        }
        
	    request.setAttribute("orfList", orfList);
	    request.setAttribute("pageStatus", pageStatus);
        request.setAttribute("keywordList", keywordList);
        request.setAttribute("goList", goList);

        
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
