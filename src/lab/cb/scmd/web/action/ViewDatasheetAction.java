//--------------------------------------
// SCMDWeb Project
//
// ViewIndividualCellDatasheetAction.java
// Since: 2005/04/30
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.action.logic.ActionLogic;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellList;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.IndividualCell;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.common.SCMDThreadManager;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.image.ImageCache;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.sessiondata.ParamUserSelection;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.util.task.PhotoClippingProcess;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author leo
 *
 */
public class ViewDatasheetAction extends Action
{
    ActionLogic _logic = new ActionLogic();

    /**
     * 
     */
    public ViewDatasheetAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        CellViewerForm view = (CellViewerForm) form;
        ParamUserSelection userSelection = SCMDSessionManager.getParamUserSelection(request);

        _logic.handleAction(view, request);
        CellList cellList = _logic.loadCellList(view);
        view.loadImage();
        
        // 表示するパラメータの設定
        List<MorphParameter> columns = null;
        if( view.getSheetType() == DataSheetType.SHEET_CUSTOM ) 
        {
            columns = userSelection.getCellParamInfo();
            if(columns.size() == 0)
                return mapping.findForward("selection");
        } 
        else 
        {
            Integer[] paramIds = DataSheetType.getParameterIds(view.getSheetType());
            if(paramIds.length == 0)
                throw new SCMDException("invalid parameter list");
                
            columns = (List<MorphParameter>) ConnectionServer.query(new BeanListHandler(MorphParameter.class), 
                    "select * from $1 where id in ($2)",
                    SCMDConfiguration.getProperty("DB_PARAMETERLIST"),
                    SQLUtil.commaSeparatedList(paramIds, SQLUtil.QuotationType.none));
                   
            if(columns == null)
            {
                return mapping.findForward("failure");
            }
        }
        
        String[] columnName = new String[columns.size()];
        for(int i = 0; i < columns.size(); i++ ) 
            columnName[i] = columns.get(i).getName();
        
        // データの取得
        Table datasheet = (SCMDConfiguration.getTableQueryInstance()).getShapeDataSheet(view.getOrf(), view.getPhotoNum(), columnName);
        if(datasheet == null)
            return mapping.findForward("failure");
        
        ColLabelIndex colLabelIndex = new ColLabelIndex(datasheet);
        RowLabelIndex rowLabelIndex = new RowLabelIndex(datasheet);
        
        Table table = new Table();
        
        String [] imageLabel = new String[] {"Cell", "Nucleus", "Actin"};

        
        
        LinkedList<IndividualCell> cells = new LinkedList<IndividualCell>();        
        ImageCache imageCache = ImageCache.getImageCache(request);
        int rowNum = 1;
        LinkedList<Cell> cellsInTheDisplay = new LinkedList<Cell>();
        for(Cell cell : cellList.getCellList())
        {
            cellsInTheDisplay.add(cell);
            
            IndividualCell c = new IndividualCell();            
            // 染色ごとに、ImageをImageCacheに登録
            for(int stainType=0; stainType<StainType.STAIN_MAX; stainType++)
            {
                
                int w = cell.getBoundingRectangle().getX2() - cell.getBoundingRectangle().getX1() + 4;
                int h = cell.getBoundingRectangle().getY2() - cell.getBoundingRectangle().getY1() + 4;

                String imageID = cell.getImageID(view.getPhotoType(), stainType);
                imageCache.registerImage(imageID); 
                c.setImageID(stainType, imageID);
                c.setHeight(h);
                c.setWidth(w);
            }
                   
            for(MorphParameter param : columns)
            {
                TableElement data = colLabelIndex.get(rowNum, param.getName());
                if(data == null)
                    continue;
                try
                {
                    BigDecimal val = new BigDecimal(data.toString(), new MathContext(3));
                    c.setValue(param.getName(), val.toString());
                }
                catch(NumberFormatException e)
                {
                    c.setValue(param.getName(), data.toString());
                }                
            }
            cells.add(c);
            rowNum++;
        }
        

        SCMDThreadManager.addTask(new PhotoClippingProcess(imageCache, cellsInTheDisplay, view.getPhotoType(), StainType.getStainTypes()));
        SCMDThreadManager.addTask(new ImageCache.ImageRecallProcess(imageCache, 20));            

        request.setAttribute("cells", cells);
        request.setAttribute("tabName", DataSheetType.TAB_NAME);
        request.setAttribute("paramList", columns);
        request.setAttribute("gene", DBUtil.getGeneInfo(view.getOrf())); // TODO タグライブラリで用意すべき
        
        return mapping.findForward("success");
    }


}