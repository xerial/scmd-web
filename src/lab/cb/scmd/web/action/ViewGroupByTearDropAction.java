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
            HttpServletResponse responce) {
        GroupByDatasheetForm sheetForm = (GroupByDatasheetForm) form;

        HttpSession session = request.getSession(true);
        UserSelection selection = (UserSelection) session.getAttribute("userSelection");
        if(selection == null) selection = new UserSelection();
        Set orfSet = selection.getSelection();

        TableQuery query = SCMDConfiguration.getTableQueryInstance();

        LinkedList tableList = new LinkedList();
        int stainType = sheetForm.getStainType();
        for (int i = 0; i < GroupType.GROUP_NAME[stainType].length; i++)
        {
            tableList.add(getTearDropSheet(query, sheetForm, orfSet, GroupType.GROUP_NAME[stainType][i]));
        }


        request.setAttribute("gene", DBUtil.getGeneInfo(sheetForm.getOrf()));
        request.setAttribute("tableList", tableList);
        request.setAttribute("groupNameList", StainType.TAB_NAME);
        return mapping.findForward("success");
    }

    public Table getTearDropSheet(TableQuery query, GroupByDatasheetForm sheetForm, Set orfSet, String group) {
        Table datasheet = new Table();
        final String[] label = { "Long Axis", "Roundness", "Bud Neck Position", "Bud Growth Direction",
                "Daughtor / Mother"};

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
            // user selectionにあるORFについてポイントを表示する
            int o = 0;
            for (Iterator it = orfSet.iterator(); it.hasNext(); o++)
            {
                String orf = (String) it.next();
                Map map = (Map) paramMapList.get(o);
                Object v = map.get(paramName);
                if(v == null) continue;

                // 0個だったら平均値は０となって変な場所に表示されてしまうので、表示しない
                Object num = map.get(numParamName);
                if(num == null)
                    continue;
                else
                {
                    int n = Integer.parseInt((String) num);
                    if(n == 0) continue;
                }

                if(cellShapeParam[i].equals("budNeckPosition") || cellShapeParam[i].equals("budGrowthDirection"))
                {
                    String areaRatioStr = (String) map.get(areaRatioParamName);
                    if(areaRatioStr != null)
                    {
                        double areaRatio = Double.parseDouble(areaRatioStr);
                        if(areaRatio == 0.0)
                        {
                            continue;
                        }
                    }
                    else
                        continue;
                }

                pointArg += orf + ":" + v.toString();
                pointArg += ":" + "%2330F0C0,";
            }

            // 現在見ているORFの点を追加
            // TODO refactoring
            boolean displayValueFlag = true;
            double paramValue = Double.parseDouble(dataMap.get(paramName).toString());
            int numCells = Integer.parseInt(dataMap.get(numParamName).toString());
            if(numCells > 0)
            {
                if(cellShapeParam[i].equals("budNeckPosition") || cellShapeParam[i].equals("budGrowthDirection"))
                {
                    String areaRatioStr = (String) dataMap.get(areaRatioParamName);
                    if(areaRatioStr != null)
                    {
                        double areaRatio = Double.parseDouble(areaRatioStr);
                        if(areaRatio != 0.0)
                            pointArg += sheetForm.getOrf() + ":" + paramValue + ":" + "%23FFB0C0";
                        else
                            displayValueFlag = false;
                    }
                    else
                        displayValueFlag = false;
                }
                else
                    pointArg += sheetForm.getOrf() + ":" + paramValue + ":" + "%23FFB0C0";
            }

            TreeMap argMap = new TreeMap();
            argMap.put("param", paramName);
            argMap.put("value", pointArg);

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

            // add min, max, sd, avg
            final String param[] = { "max", "min", "avg", "sd"};
            final String realParam[] = { "maxvalue", "minvalue", "average", "sd"};
            for (int p = 0; p < param.length; p++)
            {
                argMap.put(param[p], colLabelIndex.get(1, realParam[p]).toString());
            }
            // add point
            ImageElement img = new ImageElement("teardrop.png", argMap);
            img.setProperty("width", "128");
            img.setProperty("height", "30");

            row.add(label[i]);
            row.add(img);

            if(numCells > 0 && displayValueFlag)
                row.add(Double.toString(paramValue));
            else
                row.add("");
            row.add(colLabelIndex.get(1, "average"));
            row.add(colLabelIndex.get(1, "sd"));
            row.add(colLabelIndex.get(1, "minvalue"));
            row.add(colLabelIndex.get(1, "maxvalue"));

            datasheet.addRow(row);
        }

        datasheet.setProperty("class", "datasheet");
        datasheet.setProperty("cellpadding", "0");
        datasheet.decollateCol(0, new StyleDecollator("tablelabel"));
        TableRange dataRange = new TableRange(0, 2, datasheet.getRowSize() - 1, datasheet.getColSize() - 1);
        datasheet.decollate(dataRange, new NumberFormatDecollator(2));
        datasheet.decollate(dataRange, new AttributeDecollator("width", "50"));
        datasheet.decollate(dataRange, new AttributeDecollator("align", "right"));
        for (int i = 0; i < 5; i += 2)
        {
            datasheet.decollateCol(2 + i, new StyleDecollator("datasheet_gray"));
        }

        datasheet.insertRow(0, new String[] { "Parameter", "Teardrop", "value", "avg.", "SD", "min", "max"});
        datasheet.decollateRow(0, new StyleDecollator("sheetlabel"));

        Table frame = new Table();
        Table imageTable = new Table();

        ImageElement cellImg = new ImageElement("cellshape.png", cellShape.getArgumentMap());
        cellImg.setProperty("width", "128");
        cellImg.setProperty("height", "128");
        TableElementList datasheetLink = new TableElementList();
        datasheetLink.add("[");
        TreeMap linkMap = new TreeMap();
        linkMap.put("orf", sheetForm.getOrf());
        linkMap.put("stainType", Integer.toString(sheetForm.getStainType()));
        linkMap.put("group", group);
        datasheetLink.add(new Style(new Link("ViewGroupDataSheet.do", linkMap, "datasheet"), "button"));
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
