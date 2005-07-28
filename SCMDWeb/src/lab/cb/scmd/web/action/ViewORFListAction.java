//--------------------------------------
// SCMDServer
//
// ViewORFListAction.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;



import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import lab.cb.scmd.web.bean.ORFGroup;
import lab.cb.scmd.web.bean.ORFListViewForm;
import lab.cb.scmd.web.bean.ORFSelectionForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;


/**
 * @author leo
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ViewORFListAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ORFListViewForm listForm = (ORFListViewForm) form;
        

        String pageStr = (String) request.getParameter("page");
        String target = (String) request.getParameter("target");
        int page = 1;
        if(pageStr != null && target != null)
        {
            page  = Integer.parseInt(pageStr);
            PageStatus pageStatus = listForm.getPageStatus(target);
            pageStatus.setCurrentPage(page);
        }
        
        List groupList = loadORFList(request, listForm.getPageStatusMap());
        request.setAttribute("groupList", groupList);
        
        // 現在のpage内にあるorfのリストを作る
        TreeSet orfListInThePage = new TreeSet();
        for(Iterator it = groupList.iterator(); it.hasNext(); )
        {
            ORFGroup group = (ORFGroup) it.next();
            for(Iterator gt = group.getOrfList().iterator(); gt.hasNext(); )
            {
                YeastGene gene = (YeastGene) gt.next();
                orfListInThePage.add(gene.getOrf().toLowerCase());
            }
        }
        
        

        return mapping.findForward("success");
    }
    
    List loadORFList(HttpServletRequest request, Map pageMap) 
    {
        try
        {
            DOMParser parser = new DOMParser();
            XMLReaderThread reader = new XMLReaderThread(parser);
            XMLQuery query = (XMLQuery)SCMDConfiguration.getXMLQueryInstance();
            reader.start();
            query.getAvailableORF(reader.getOutputStream(), (AbstractMap) pageMap);
            reader.join();
            
            Document document = parser.getDocument();
            
            NodeList nodeList = document.getElementsByTagName("orfgroup");
            
            Vector orfGroupList = new Vector(nodeList.getLength());
            for(int i=0; i<nodeList.getLength(); i++)
            {
                Element groupElement = (Element) nodeList.item(i);
                orfGroupList.add(new ORFGroup(groupElement));
            }
            return orfGroupList;
        }
        catch(SCMDException e)
        {
            System.out.println(e);
        }
        catch(InterruptedException e)
        {
            System.out.println(e);
        }
        return new Vector();
    }

}

//--------------------------------------
// $Log: ViewORFListAction.java,v $
// Revision 1.5  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.4  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.3  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
// Revision 1.2  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page用のクラスを追加
//
//--------------------------------------
