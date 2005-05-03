//--------------------------------------
// SCMDServer
// 
// ViewGroupByTearDropAction.java 
// Since: 2004/09/01
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.lang.reflect.InvocationTargetException;
import java.net.ResponseCache;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellShape;
import lab.cb.scmd.web.bean.GroupByDatasheetForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.GroupType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.Link;
import lab.cb.scmd.web.table.StringElement;
import lab.cb.scmd.web.table.Style;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableElementList;
import lab.cb.scmd.web.table.TableRange;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;

/**
 * @author leo
 *  
 */
public class ViewGroupByTearDropAction extends Action
{

    /**
     *  
     */
    public ViewGroupByTearDropAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        GroupByDatasheetForm sheetForm = (GroupByDatasheetForm) form;

        HttpSession session = request.getSession(true);
        UserSelection selection = (UserSelection) session.getAttribute("userSelection");
        if(selection == null) selection = new UserSelection();
        Set orfSet = selection.getSelection();

        TableQuery query = SCMDConfiguration.getTableQueryInstance();

        LinkedList<Table> tableList = new LinkedList<Table>();
        int stainType = sheetForm.getStainType();
        for (int i = 0; i < GroupType.GROUP_NAME[stainType].length; i++)
        {
            tableList.add(getTearDropSheet(selection, query, sheetForm, orfSet, GroupType.GROUP_NAME[stainType][i], GroupType.GROUP_PARAM_ID[stainType][i], response));
        }


        request.setAttribute("gene", DBUtil.getGeneInfo(sheetForm.getOrf()));
        request.setAttribute("tableList", tableList);
        request.setAttribute("groupNameList", StainType.TAB_NAME);
        return mapping.findForward("success");
    }

    public Table getTearDropSheet(UserSelection selection, TableQuery query, GroupByDatasheetForm sheetForm, Set orfSet, String group, int groupParamID, HttpServletResponse response)
    {
        Table datasheet = new Table();
        final String[] label = { "Long Axis", "Roundness", "Bud Neck Position", "Bud Growth Direction",
                "Daughtor / Mother"};
        final int[] paramID = { 31, 43, 33, 34, 46};
        final String[] cellShapeParam = new String[] { "longAxis", "roundness", "budNeckPosition",
                "budGrowthDirection", "areaRatio"};

        Map dataMap = new TreeMap();
        try
        {
            dataMap = query.getShapeStatLine(sheetForm.getOrf());
        }
        catch (SCMDException e)
        {
            // no data is available
            e.what();
        }

        Vector paramMapList = new Vector(orfSet.size());
        for (Iterator it = orfSet.iterator(); it.hasNext();)
        {
            try
            {
                Map statMap = query.getShapeStatLine((String) it.next());
                paramMapList.add(statMap);
            }
            catch (SCMDException e1)
            {
                paramMapList.add(new TreeMap());
            }
        }

        
        // 画像の例を表示
        CellShape cellShape = new CellShape();

        for (int i = 0; i < cellShapeParam.length; i++)
        {
            LinkedList row = new LinkedList();
            String paramName = StainType.getStainTypeName(sheetForm.getStainType()) + "-" + cellShapeParam[i] + "_"
                    + group;
            String numParamName = StainType.getStainTypeName(sheetForm.getStainType()) + "-num-" + group;
            String areaRatioParamName = StainType.getStainTypeName(sheetForm.getStainType()) + "-areaRatio_" + group;
            Table tearDropData = query.getShapeStatOfParameter(paramName);
            if(tearDropData.getRowSize() < 2) continue;
            ColLabelIndex colLabelIndex = new ColLabelIndex(tearDropData);

            String pointArg = "";

            // TODO refactoring
            boolean displayValueFlag = true;
            if(dataMap.get(paramName) == null || dataMap.get(numParamName) == null)
                continue;
            
            double paramValue = Double.parseDouble(dataMap.get(paramName).toString());
            int numCells = Integer.parseInt(dataMap.get(numParamName).toString());

            TreeMap argMap = new TreeMap();
            //argMap.put("param", paramName);
            argMap.put("value", pointArg);
            argMap.put("paramID", paramID[i]);
            argMap.put("groupID", groupParamID);
            argMap.put("plotTargetORF", "true");
            argMap.put("orientation", "horizontal");

            try
            {
                if(numCells > 0 && displayValueFlag)
                        BeanUtils.setProperty(cellShape, cellShapeParam[i], new Double(paramValue));
            }
            catch (IllegalAccessException e)
            {
                System.out.println(e.getMessage());
            }
            catch (InvocationTargetException e)
            {
                System.out.println(e.getMessage());
            }

            // add point
            ImageElement img = new ImageElement(response.encodeURL("DrawTeardrop.do"), argMap);
            img.setProperty("width", "134");
            img.setProperty("height", "30");

            row.add(label[i]);
            if(numCells > 0 && displayValueFlag)
                row.add(Double.toString(paramValue));
            else
                row.add("");
            row.add(colLabelIndex.get(1, "minvalue"));            
            row.add(img);
            row.add(colLabelIndex.get(1, "maxvalue"));
            
            row.add(colLabelIndex.get(1, "average"));
            row.add(colLabelIndex.get(1, "sd"));



            datasheet.addRow(row);
        }

        datasheet.setProperty("class", "datasheet");
        datasheet.setProperty("cellpadding", "0");
        datasheet.decollateCol(0, new StyleDecollator("tablelabel"));
        TableRange dataRange = new TableRange(0, 1, datasheet.getRowSize() - 1, 2);
        TableRange dataRange2 = new TableRange(0, 4, datasheet.getRowSize() - 1, 6);
        datasheet.decollate(dataRange, new NumberFormatDecollator(2));
        datasheet.decollate(dataRange, new AttributeDecollator("width", "50"));
        datasheet.decollate(dataRange, new AttributeDecollator("align", "right"));
        datasheet.decollate(dataRange2, new NumberFormatDecollator(2));
        datasheet.decollate(dataRange2, new AttributeDecollator("width", "50"));
        datasheet.decollate(dataRange2, new AttributeDecollator("align", "right"));
        for (int i = 0; i < 5; i += 2)
        {
            datasheet.decollateCol(2 + i, new StyleDecollator("datasheet_gray"));
        }

        datasheet.insertRow(0, new String[] { "Parameter", "value", "min", "Teardrop", "max", "avg.", "SD"});
        datasheet.decollateRow(0, new StyleDecollator("sheetlabel"));

        Table frame = new Table();
        Table imageTable = new Table();

        ImageElement cellImg = new ImageElement(response.encodeURL("cellshape.png"), cellShape.getArgumentMap());
        cellImg.setProperty("width", "128");
        cellImg.setProperty("height", "128");
        TableElementList datasheetLink = new TableElementList();
        datasheetLink.add("[");
        TreeMap linkMap = new TreeMap();
        linkMap.put("orf", sheetForm.getOrf());
        linkMap.put("stainType", Integer.toString(sheetForm.getStainType()));
        linkMap.put("group", group);
        datasheetLink.add(new Style(new Link(response.encodeURL("ViewGroupDataSheet.do"), linkMap, "datasheet"), "button"));
        datasheetLink.add("]");
        imageTable.addCol(new TableElement[] { cellImg, new Style(new StringElement(group), "title"), datasheetLink});
        imageTable.decollateCol(0, new AttributeDecollator("align", "center"));

        frame.addRow(new TableElement[] { imageTable, datasheet});

        return frame;
    }

}

//--------------------------------------
// $Log: ViewGroupByTearDropAction.java,v $
// Revision 1.7  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.6  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.5  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.4 2004/09/03 03:31:30 leo
// javascriptでCSSを変更できるようにした
//
// Revision 1.3 2004/09/02 08:49:48 leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.2 2004/09/01 07:59:02 leo
// tearDropView
//
// Revision 1.1 2004/09/01 06:39:59 leo
// TearDropViewを追加
//
//--------------------------------------
