//--------------------------------------
// SCMDServer
//
// ViewDataSheetAction.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



import lab.cb.scmd.web.action.logic.ActionLogic;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellList;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableRange;
import lab.cb.scmd.web.table.decollation.AttributeDecollation;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;

/**
 * @author leo
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ViewDataSheetAction extends Action {

	ActionLogic _logic = new ActionLogic();
	
    /**
     *  
     */
    public ViewDataSheetAction() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	CellViewerForm view = (CellViewerForm) form;

    	_logic.handleAction(view, request);
    	CellList cellList = _logic.loadCellList(view);
    	view.loadImage();
    	
    	
    	// table 作成
        Table datasheet = (SCMDConfiguration.getTableQueryInstance()).getShapeDataSheet(view.getOrf(), view.getPhotoNum(), view.getSheetType());
        if(datasheet == null)
        {
            return mapping.findForward("failure");
        }
        ColLabelIndex colLabelIndex = new ColLabelIndex(datasheet);
        RowLabelIndex rowLabelIndex = new RowLabelIndex(datasheet);
        
        Table table = new Table();
        
        LinkedList colLabelList = new LinkedList();
        String [] imageLabel = new String[] {"Cell", "Nucleus", "Actin"};
        for (int i = 0; i < imageLabel.length; i++)
        {
            colLabelList.add(imageLabel[i]);
        }
        table.addRow(colLabelList);
        table.paste(0, table.getColSize(), datasheet.getRow(0));
      	
//        int width[] = new int[StainType.STAIN_MAX];
//        for(int i=0; i<width.length; i++)
//            width[i] = 0;
//        int height[] = new int[cellList.getCellList().size()];
//        for(int i=0; i<height.length; i++)
//            height[i] = 0;
        
        int rowNum = 0;
        for(Iterator it = cellList.iterator(); it.hasNext(); rowNum++)
        {
            LinkedList row = new LinkedList();
            Cell cell = (Cell) it.next();
            for(int stainType=0; stainType<StainType.STAIN_MAX; stainType++)
            {
                Map queryMap = cell.getBoundingRectangle().getQueryMap();

                int w = cell.getBoundingRectangle().getX2() - cell.getBoundingRectangle().getX1() + 4;
//                if(w > width[stainType])
//                    width[stainType] = w;
                int h = cell.getBoundingRectangle().getY2() - cell.getBoundingRectangle().getY1() + 4;
//                if(h > height[rowNum])
//                    height[rowNum] = h;
                
                queryMap.put("stainType", Integer.toString(stainType));
                
                queryMap.put("photoType", Integer.toString(view.getPhotoType()));
                queryMap.put("orf", view.getOrf());
                queryMap.put("photoNum", Integer.toString(view.getPhotoNum()));
                
                ImageElement imageCell = new ImageElement("DisplayCell.do", queryMap);
                imageCell.setProperty("border", "0");
                imageCell.setProperty("alt", "ID=" + cell.getCellID());
                imageCell.setProperty("width", Integer.toString(w));
                imageCell.setProperty("height", Integer.toString(h));
                //imageCell.setProperty("align", "center");
                row.add(new AttributeDecollation(imageCell, "bgcolor", "black"));
            }
            table.addRow(row);
            
            // cellIDに対応するデータ行を追加
            int correspondingRowNum = rowLabelIndex.getRowIndex(Integer.toString(cell.getCellID()));
            if(correspondingRowNum != -1)
                table.paste(table.getRowSize()-1, StainType.STAIN_MAX, datasheet.getRow(correspondingRowNum));
        }
        table.appendToBottom(table.getRow(0));
        table.removeCol(StainType.STAIN_MAX); // cell_local_idに対応する行を削除

        // decollation
        table.setProperty("class", "datasheet");
        table.decollateRow(0, new StyleDecollator("sheetlabel"));
        table.decollateRow(table.getRowSize() - 1, new StyleDecollator("sheetlabel"));
        table.decollate(new TableRange(1, 0, table.getRowSize()-2, 2), new AttributeDecollator("align", "center"));
        
        // ３列毎に色を変える
        for(int col=3; col<table.getColSize(); col++)
        {
            table.decollateCol(col, new AttributeDecollator("align", "right"));
            if(col % 3 == 1)
            {
                table.decollateCol(col, new AttributeDecollator("bgcolor", "#E0F0F0"));
            }
            else if(col % 3 == 2)
                table.decollateCol(col, new AttributeDecollator("bgcolor", "#F0F0F0"));
            table.decollateCol(col, new NumberFormatDecollator(3));
        }
        
        //table.setProperty("cellpadding", "0");
        //table.setProperty("cellspacing", "1");
        
        request.setAttribute("datasheet", table);
        request.setAttribute("tabName", DataSheetType.TAB_NAME);
        request.setAttribute("gene", DBUtil.getGeneInfo(view.getOrf()));
        
        return mapping.findForward("success");
    }

}

//--------------------------------------
// $Log: ViewDataSheetAction.java,v $
// Revision 1.23  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.22  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.21  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.20  2004/09/02 08:49:48  leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.19  2004/09/01 06:39:59  leo
// TearDropViewを追加
//
// Revision 1.18  2004/08/24 06:57:24  leo
// web.tableに対応
//
// Revision 1.17  2004/08/24 04:28:11  leo
// 新しいtableに対応
//
// Revision 1.16  2004/08/14 14:18:00  leo
// cell_local_idを表示させないようにした
//
// Revision 1.15  2004/08/14 13:30:44  leo
// sheetTypeをTableQueryに渡すようにしました
//
// Revision 1.14  2004/08/14 13:26:43  leo
// MockTableの修正
//
// Revision 1.13  2004/08/14 12:42:51  leo
// リンク修正
//
// Revision 1.12  2004/08/14 11:13:06  sesejun
// DataSheet対応開始。
// getCellCordinates()にcellidを追加
//
// Revision 1.11  2004/08/12 19:20:48  leo
// 結果のサイズが０の時の対処を追加
//
// Revision 1.10  2004/08/12 17:48:26  leo
// update
//
// Revision 1.9  2004/08/11 14:02:32  leo
// Group by のシート作成
//
// Revision 1.8  2004/08/10 14:29:01  leo
// *** empty log message ***
//
// Revision 1.7  2004/08/09 12:33:03  leo
// StringAttributeDecollation -> AttributeDecollationに集約
//
// Revision 1.6  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.5  2004/08/09 09:17:11  leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.4  2004/08/01 14:58:22  leo
// 移動
//
// Revision 1.3  2004/07/27 05:17:50  leo
// Datasheetのサンプル表示
//
// Revision 1.2  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
// Revision 1.1 2004/07/26 19:33:31 leo
// Actionの修正。DataSheetページ着工
//
//--------------------------------------
