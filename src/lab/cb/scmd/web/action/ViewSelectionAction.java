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


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ORFSelectionForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
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
			HttpServletRequest request, HttpServletResponse response)
	throws Exception
    {
	    
	    ORFSelectionForm selection = (ORFSelectionForm) form;
	    String orf = selection.getInput();
	    
	    // sessionを取得
	    HttpSession session = request.getSession(true);
	    UserSelection userSelection = SCMDSessionManager.getUserSelection(request);
	    CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);
	    

        String[] inputList = selection.getInputList();
        
        if(selection.getButton().equals("save"))
        {
            response.setContentType("application/download");
            response.addHeader("Content-disposition", "filename=\"scmd_selection.xml\"");
            userSelection.outputXML(response.getOutputStream());
            return super.execute(mapping, form, request, response);
        }
        
        if(selection.getButton().equals("remove all"))
        {
            userSelection.clear();
        }
        else
        {
            if(selection.getButton().equals("remove"))
            {
                // ユーザーの入力を消去
                for(String s : inputList)
                    userSelection.removeSelection(s);
            }
            else
            {
                if(selection.getButton().equals("load"))
                {
                    // ユーザーの入力を追加
                    FormFile inputXMLFile = selection.getFile();
                    if(inputXMLFile != null)
                    {
                        // XMLファイルから読み込み
                        userSelection.loadFromXML(inputXMLFile);
                    }
                }
                else 
                {
                    if(orf != null)
                    {
                        userSelection.addSelection(orf);
                        userSelection.setColor(orf, "skyblue");
                    }
                    for(String s : inputList)
                    {
                        userSelection.addSelection(s);
                        userSelection.setColor(s, "skyblue");
                    }
                }
            }
        }


        // 色名(ORF_color)を<ORF,color> のHashに入れる。
        for(String colorMap : selection.getColorList())
        {
            String[] orfColorSet = colorMap.split("_");
            if(orfColorSet.length == 2 )
                userSelection.setColor(orfColorSet[0], orfColorSet[1]);
        }
        
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
                YeastGene yeastGene = new YeastGene((Element) nodeList.item(i));
//                //各ORF情報に色指定を入れる
//                if( colorMap.containsKey(yeastGene.getOrf()) )
//                    yeastGene.setColor(colorMap.get(yeastGene.getOrf()));
	            orfList.add(yeastGene);
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
	    
	    request.setAttribute("gene", new YeastGene(view.getOrf()));
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