//--------------------------------------
// SCMDServer
// 
// ViewSelectionAction.java 
// Since: 2004/08/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;


import java.util.LinkedList;

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

import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.ORFSelectionForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
 * @author leo
 *
 */
public class ViewSelectionAction extends Action
{

    /**
     * 
     */
    public ViewSelectionAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	    
	    ORFSelectionForm selection = (ORFSelectionForm) form;
	    String orf = selection.getInput();

	    // sessionを取得
	    HttpSession session = request.getSession(true);
	    UserSelection userSelection = (UserSelection) session.getAttribute("userSelection");
	    if(userSelection == null)
	        userSelection = new UserSelection();
	    
	    // ユーザーの入力を追加
	    if(orf != null)
	        userSelection.addSelection(orf);
	    String[] inputList = selection.getInputList();
	    if(inputList != null)
	    {
            if(selection.getButton().equals("remove"))
                for(String s : inputList)
                    userSelection.removeSelection(s);
            else
                for(String s : inputList)
                    userSelection.addSelection(s);
	    }
	    session.setAttribute("userSelection", userSelection);
	    
	    
	    LinkedList orfList = new LinkedList();
	    
	    try
	    {
	        // 各ORFのデータを取得
	        DOMParser parser = new DOMParser();
	        XMLReaderThread reader = new XMLReaderThread(parser);
	        XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
	        reader.start();
	        query.getORFInfo(reader.getOutputStream(), userSelection.getSelection(), 0);
	        reader.join();
	        
	        Document document = parser.getDocument();
	        NodeList nodeList = document.getElementsByTagName("orf");
	        
	        for(int i=0; i<nodeList.getLength(); i++)
	        {
	            orfList.add(new YeastGene((Element) nodeList.item(i)));
	        }
	        
	    }
	    catch(SCMDException e)
	    {
	        System.out.println(e);
	    }
	    catch(InterruptedException e)
	    {
	        System.out.println(e);
	    }
	    
	    request.setAttribute("orfList", orfList);
	    
	    
	    
	    return mapping.findForward("success");
	}
}


//--------------------------------------
// $Log: ViewSelectionAction.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionの追加
//
//--------------------------------------