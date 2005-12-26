//--------------------------------------
// SCMDServer
// 
// ActionLogic.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action.logic;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.CellList;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.exception.InvalidPageException;
import lab.cb.scmd.web.exception.XMLParseErrorException;
import lab.cb.scmd.web.viewer.Photo;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.RecordThread;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
 * @author leo
 *  
 */
public class ActionLogic
{
    static HashMap   _actionToIDMap = new HashMap();
    static String[]  _actionString  = new String[] { "next", "next10", "prev", "prev10"};

    final static int UNKNOWN        = -1;
    final static int NEXT           = 0;
    final static int NEXT10         = 1;
    final static int PREV           = 2;
    final static int PREV10         = 3;

    public ActionLogic()
    {
        super();
        for (int i = 0; i < _actionString.length; i++)
            _actionToIDMap.put(_actionString[i], new Integer(i));
    }

    protected int getActionType(String action) {
        Integer type = (Integer) _actionToIDMap.get(action);
        if(type == null)
            return UNKNOWN;
        else
            return type.intValue();
    }

    /**
     * actionを見て次に進む前に戻るなどページ移動処理
     * @param form
     * @param request
     */
    public void handleAction(CellViewerForm form, HttpServletRequest request) {
        String action = (String) request.getParameter("action");

        int page = form.getPhotoPage();
        
        int maxPage;
        if(form.isReadyPhotoPageMax())
            maxPage = form.getPhotoPageMax();
        else
            maxPage = SCMDConfiguration.getValueQueryInstance().getMaxPhotoPage(form.getOrf());

        if(maxPage <=0)
        {
            form.setPhotoPageMax(0);
            form.setPhotoPage(0);
            form.setPhotoNum(0);
            return;
        }
        int nextPage = page;
        switch (getActionType(action))
        {
        case NEXT:
            page = page + 1;
            break;
        case NEXT10:
            page = page + 10;
            break;
        case PREV:
            page = page - 1; 
            break;
        case PREV10:
            page = page - 10;
            break;
        case UNKNOWN:
        default:
            break;
        }
        page = adjustPage(page, maxPage);
        
        form.setPhotoPage(page);
        form.setPhotoPageMax(maxPage);

    }
    
    protected int adjustPage(int page, int maxPage)
    {
        while(page <= 0)
        {
            page += maxPage;
        }
        if(page > maxPage)
        {
            page = (page - 1) % maxPage + 1;
        }
        return page;
    }

    
    public CellList loadCellList(CellViewerForm form) throws SCMDException
    {        
        CellList cellList = null;
        try
        {
            cellList = load(form);
        }
        catch(InvalidPageException e)
        {
            int invalidPage = e.getPage();
            int maxPage = e.getMaxPage();
            if(invalidPage > maxPage)
                form.setPhotoPage(maxPage);
            form.setPhotoPageMax(maxPage);

            // reload
            return loadCellList(form);
        }

        Photo photo = cellList.getPhoto();
        if(photo!=null)
            form.setPhotoNum(photo.getPhotoNum());
        else
            form.setPhotoNum(0);


        return cellList;
    }
    
    protected static CellList load(CellViewerForm viewerForm) throws SCMDException {
        
        String orf = viewerForm.getOrf();
        int page = viewerForm.getPhotoPage();

        DOMParser parser = new DOMParser();
        XMLReaderThread reader = new XMLReaderThread(parser);
        RecordThread recorder = new RecordThread();
        reader.addPipe(recorder);
        try
        {
            reader.start();
            SCMDConfiguration.getXMLQueryInstance().getCellCoordinates(reader.getOutputStream(), orf, page);
            reader.join();
        }
        catch (InterruptedException e)
        {
            throw new SCMDException(e);
        }
        

        Document document = parser.getDocument();

        NodeList celllistElem = (NodeList) document.getElementsByTagName("celllist");
        if(celllistElem.getLength() <= 0) 
            throw new XMLParseErrorException("no celllist tag");

//        if(viewerForm.getPhotoPageMax() <= 0)
//            return new CellList();

        return new CellList((Element) celllistElem.item(0));
    }

}

//--------------------------------------
// $Log: ActionLogic.java,v $
// Revision 1.4  2004/08/14 10:56:01  leo
// maxPage <=0 のときの処理
//
// Revision 1.3  2004/08/13 09:31:59  leo
// InvalidPageExceptionからmaxpageの情報を受け取れるようにした
//
// Revision 1.2  2004/08/12 17:48:26  leo
// update
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------
