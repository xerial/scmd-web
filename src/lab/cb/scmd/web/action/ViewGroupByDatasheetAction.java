//--------------------------------------
// SCMDServer
// 
// ViewGroupByDatasheetAction.java 
// Since: 2004/08/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.GroupByDatasheetForm;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.image.ImageCache;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 *  
 */
public class ViewGroupByDatasheetAction extends Action
{

    /**
     *  
     */
    public ViewGroupByDatasheetAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse responce) throws Exception {

        GroupByDatasheetForm datasheetForm = (GroupByDatasheetForm) form;

        HttpSession session = request.getSession(true);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null) view = new CellViewerForm();

        if(request.getParameter("sheetType") != null)
                view.setSheetType(Integer.parseInt(request.getParameter("sheetType")));
        if(request.getParameter("photoType") != null)
                view.setPhotoType(Integer.parseInt(request.getParameter("photoType")));

        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        Table table = query.getGroupByDatasheet(datasheetForm.getOrf(), view.getSheetType(), datasheetForm
                .getStainType(), datasheetForm.getGroup());
        ColLabelIndex colIndex = new ColLabelIndex(table);

        Table datasheet = new Table();

        int numElementInAPage = 10;

        int numRow = table.getRowSize() - 1;
        int maxPage = numRow / numElementInAPage + (numRow % numElementInAPage == 0 ? 0 : 1);

        datasheetForm.setMaxPage(maxPage);
        if(datasheetForm.getPage() > maxPage) datasheetForm.setPage(maxPage);

        LinkedList labelList = new LinkedList(colIndex.getColLabelList());
        labelList.remove("cell_local_id");
        for (int i = 0; i < DataSheetType.PHOTO_INFO_PARAM.length; i++)
            labelList.remove(DataSheetType.PHOTO_INFO_PARAM[i]);

        if(numRow > 0)
        {
            int rowBegin = 1 + (datasheetForm.getPage() - 1) * numElementInAPage;
            if(rowBegin <= 0) rowBegin = 1;
            int rowEnd = rowBegin + numElementInAPage;
            if(rowEnd > numRow) rowEnd = numRow;

            ImageCache imageCache = ImageCache.getImageCache(request);
            LinkedList<Cell> cellsInTheDisplay = new LinkedList<Cell>();
            for (int row = rowBegin; row <= rowEnd; row++)
            {
                LinkedList elementList = new LinkedList();
                // image
                TreeMap argMap = new TreeMap();
                int x1 = Integer.parseInt(colIndex.get(row, "x1").toString());
                int x2 = Integer.parseInt(colIndex.get(row, "x2").toString());
                int y1 = Integer.parseInt(colIndex.get(row, "y1").toString());
                int y2 = Integer.parseInt(colIndex.get(row, "y2").toString());
                int w = x2 - x1 + 4;
                int h = y2 - y1 + 4;

                Photo photo = new Photo(datasheetForm.getOrf(), Integer.parseInt(colIndex.get(row, "image_number").toString()));
                Cell cell = new Cell(photo, Integer.parseInt(colIndex.get(row, "cell_local_id").toString()), new BoundingRectangle(x1, x2, y1, y2));
                cellsInTheDisplay.add(cell);
                for (int stain = 0; stain < StainType.STAIN_MAX; stain++)
                {
                    String imageID = cell.getImageID(view.getPhotoType(), stain);
                    imageCache.registerImage(imageID);
                    argMap.put("imageID", imageID);
                    argMap.put("encoding", "jpeg");
                    ImageElement img = new ImageElement("scmdimage.img", (Map) argMap.clone());
                    img.setProperty("alt", "cell ID=" + colIndex.get(row, "cell_local_id"));
                    img.setProperty("width", Integer.toString(w));
                    img.setProperty("height", Integer.toString(h));
                    elementList.add(img);

                }
                for (Iterator it = labelList.iterator(); it.hasNext();)
                {
                    elementList.add(colIndex.get(row, (String) it.next()));
                }
                datasheet.addRow(elementList);
            }

            PhotoClippingProcess photoClippingProcess = new PhotoClippingProcess(imageCache, cellsInTheDisplay, view.getPhotoType(), StainType.getStainTypes());
            photoClippingProcess.process();
            
            for (int i = 0; i < StainType.STAIN_MAX; i++)
            {
                datasheet.decollateCol(i, new AttributeDecollator("bgcolor", "black"));
                datasheet.decollateCol(i, new AttributeDecollator("align", "center"));
            }
            datasheet.setProperty("class", "datasheet");
            // 1列毎に色を変える
            for (int col = 3; col < datasheet.getColSize(); col++)
            {
                datasheet.decollateCol(col, new NumberFormatDecollator(3));
                if(col % 3 == 1)
                {
                    datasheet.decollateCol(col, new AttributeDecollator("bgcolor", "#E8F8F8"));
                }
                else if(col % 3 == 2) datasheet.decollateCol(col, new AttributeDecollator("bgcolor", "#F0F0F0"));
            }
            //datasheet.setProperty("cellspacing", "2");
            //datasheet.setProperty("cellpadding", "0");

        }
        labelList.add(0, "Cell");
        labelList.add(1, "Nucleus");
        labelList.add(2, "Actin");
        Table label = new Table();
        label.addRow(labelList);
        label.decollateRow(0, new StyleDecollator("sheetlabel"));
        datasheet.appendToTop(label);
        datasheet.appendToBottom(label);

        request.setAttribute("gene", DBUtil.getGeneInfo(datasheetForm.getOrf()));
        request.setAttribute("datasheet", datasheet);
        request.setAttribute("tabName", DataSheetType.TAB_NAME);
        request.setAttribute("groupNameList", StainType.TAB_NAME);

        return mapping.findForward("success");
    }
}

//--------------------------------------
// $Log: ViewGroupByDatasheetAction.java,v $
// Revision 1.10  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.9  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.8  2004/09/03 09:34:46  leo
// *** empty log message ***
//
// Revision 1.7 2004/09/03 07:31:53 leo
// デザインの調整
// standardnameを表示
//
// Revision 1.6 2004/09/02 08:49:48 leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.5 2004/09/01 06:39:59 leo
// TearDropViewを追加
//
// Revision 1.4 2004/08/31 08:44:01 leo
// Group By Sheet完成
//
// Revision 1.3 2004/08/31 04:46:21 leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
// Revision 1.2 2004/08/30 16:53:26 leo
// *** empty log message ***
//
// Revision 1.1 2004/08/30 10:43:13 leo
// GroupBySheetの作成 pageの移動はまだ
//
//--------------------------------------
