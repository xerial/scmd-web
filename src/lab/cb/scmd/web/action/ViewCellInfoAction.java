//--------------------------------------
// SCMDServer
// 
// ViewCellInfoAction.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.web.bean.CellList;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.RecordThread;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
 * @author leo
 *
 */
public class ViewCellInfoAction extends Action
{

    /**
     * 
     */
    public ViewCellInfoAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CellViewerForm view = (CellViewerForm) form;
        
        String cellIDStr = (String) request.getParameter("cellID");
        if(cellIDStr == null)
            return mapping.findForward("failure");
        
        int cellID = Integer.parseInt(cellIDStr);
        
//        Cell targetCell = null;
//        for(Iterator it = cellList.iterator(); it.hasNext(); )
//        {
//            Cell cell = (Cell) it.next();
//            if(cell.getCellID() == cellID)
//            {
//                targetCell = cell;
//                continue;
//            }
//        }
//        if(targetCell == null)
//        {
//            return mapping.findForward("faliure");
//        }
//        
  
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        Table cellSheet = query.getShapeDataSheet(view.getOrf(), view.getPhotoNum(), cellID, view.getSheetType());
        
        if(cellSheet == null)
            return mapping.findForward("failure");
        
        XMLQuery xmlQuery = SCMDConfiguration.getXMLQueryInstance();
        
        DOMParser parser = new DOMParser();
        XMLReaderThread reader = new XMLReaderThread(parser);
        RecordThread recorder = new RecordThread();
        reader.addPipe(recorder);
        reader.start();
        xmlQuery.getCellCoordinates(reader.getOutputStream(), view.getOrf(), view.getPhotoPage(), cellID);
        reader.join();
        
        Document document = parser.getDocument();
        NodeList nodeList = document.getElementsByTagName("celllist");
        if(nodeList.getLength() <= 0)
            return mapping.findForward("failure");
        
        CellList cellList = new CellList((Element) nodeList.item(0));
        List list = cellList.getCellList();
        if(list.size() <= 0)
            return mapping.findForward("failure");
        Cell targetCell = (Cell) list.get(0);
        
        //�c�����Ђ�����Ԃ����e�[�u�����쐬
        Table table = new Table();
        for(int i=0; i<cellSheet.getRowSize(); i++)
            table.addCol(cellSheet.getRowArray(i));
        
        table.decollateCol(0, new StyleDecollator("tablelabel"));
        table.decollateCol(0, new AttributeDecollator("align", "left"));
        table.decollateCol(1, new AttributeDecollator("align", "right"));
        
        
        
        request.setAttribute("targetCell", targetCell);
        request.setAttribute("cellSheet", table);
        request.setAttribute("tabName", DataSheetType.TAB_NAME);
        
        
        return mapping.findForward("success");
    }
}


//--------------------------------------
// $Log: ViewCellInfoAction.java,v $
// Revision 1.13  2004/12/10 08:57:00  leo
// �t�@�C���̈ړ��B�ǉ�
//
// Revision 1.12  2004/08/26 08:45:52  leo
// Query�̒ǉ��B selection�̏C��
//
// Revision 1.11  2004/08/24 06:57:24  leo
// web.table�ɑΉ�
//
// Revision 1.10  2004/08/15 07:14:42  sesejun
// GroupBy��DB�ڑ������n��(������)
//
// Revision 1.9  2004/08/14 13:32:58  sesejun
// getCellShapeDataSheet�̌Ăяo���o�O�C��
//
// Revision 1.8  2004/08/14 12:34:50  leo
// cell�̕\�����C��
//
// Revision 1.7  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.6  2004/08/14 10:57:09  leo
// CellViewerForm�ւ̑Ή�
//
// Revision 1.5  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQuery��instance��
//
// Revision 1.4  2004/08/11 14:02:32  leo
// Group by �̃V�[�g�쐬
//
// Revision 1.3  2004/08/09 12:26:42  leo
// Comment��ǉ�
//
// Revision 1.2  2004/08/01 14:58:22  leo
// �ړ�
//
// Revision 1.1  2004/07/27 06:50:25  leo
// CellInfo�y�[�W��ǉ�
//
//--------------------------------------