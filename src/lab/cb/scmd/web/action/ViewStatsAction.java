//--------------------------------------
// SCMDServer
// 
// ViewStatsAction.java 
// Since: 2004/08/01
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.Height;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.Link;
import lab.cb.scmd.web.table.StringElement;
import lab.cb.scmd.web.table.Style;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableElementList;
import lab.cb.scmd.web.table.TableRange;
import lab.cb.scmd.web.table.Width;
import lab.cb.scmd.web.table.decollation.AttributeDecollation;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.Decollator;
import lab.cb.scmd.web.table.decollation.JavaScriptDecollation;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;

/**
 * @author leo
 *  
 */
public class ViewStatsAction extends Action
{

	/**
	 *  
	 */
	public ViewStatsAction()
	{
		super();
		// TODO Auto-generated constructor stub
	}


	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		CellViewerForm view = (CellViewerForm) form;
		TableQuery query = SCMDConfiguration.getTableQueryInstance();

		Map statParamMap = query.getShapeStatLine(view.getOrf());

		final String tableName[] = new String[] { "bud", "nucleus", "actin" };

		final String statParamName[][] = new String[][] { { "no", "small", "medium", "large" },
				{ "A", "A1", /* "A1|B", */"B", "C", "D", "E", "F" },
				{ "A", "B", "api", "iso", "E", "F" } };
		final String prefix[] = new String[] { "C", "D", "A" };
		String orf = view.getOrf();

		// calculate percentage
		for(int g = 0; g < prefix.length; g++)
		{
			Vector numList = new Vector();
			int cellNumTotal = 0;
			for(int i = 0; i < statParamName[g].length; i++)
			{
				String num = (String) statParamMap.get(prefix[g] + "-num-" + statParamName[g][i]);
				if(num != null)
				{
					int n = Integer.parseInt(num);
					cellNumTotal += n;
					numList.add(new Integer(n));
				}
				else
					numList.add(new Integer(0));
			}

			if(cellNumTotal <= 0)
				cellNumTotal = 1;

			for(int i = 0; i < statParamName[g].length; i++)
			{
				statParamMap.put(prefix[g] + "-percentage-" + statParamName[g][i], new Double(
						(double) ((Integer) numList.get(i)).intValue() / cellNumTotal * 100));
			}
		}

		for(int i = 0; i < 4; i++)
		{
			Table table = createStatTable(orf, statParamMap, i);
			request.setAttribute("statsTable" + i, table);
		}
		
		request.setAttribute("gene", DBUtil.getGeneInfo(view.getOrf()));	
		return mapping.findForward("success");
	}

	protected Table createStatTable(String orf, Map paramMap, int groupIndex) {
		final String statParamName[][] = new String[][] { { "no", "small", "medium", "large" },
				{ "A", "A1", /* "A1|B", */"B", "C" }, { "D", "E", "F" },
				{ "A", "B", "api", "iso", "E", "F" }, };

		final String[] param = new String[] { "longAxis", "roundness", "budNeckPosition",
				"budGrowthDirection", "areaRatio" };
		final String[] cellShapeParam = new String[] { "longAxis", "roundness", "neckPosition",
				"growthDirection", "areaRatio" };
		final String[] displayParamName = new String[] { "Long Axis", "Roundness", "Bud Neck Position",
				"Bud Growth Direction", "Daughtor / Mother" };
		final String[] groupPrefix = new String[] { "C", "D", "D", "A" };

		Table dataTable = new Table(6 + param.length, 1 + statParamName[groupIndex].length);

		// パラメータ名をセット
		for(int i = 0; i < param.length; i++)
		{
			dataTable.set(i + 3, 0, displayParamName[i]);
		}
		dataTable.set(dataTable.getRowSize() - 1, 0, "Search Similar Shape");

		// Group ラベルをセット
		final String[] groupName = new String[] { "Bud Size", "Nucleus Location", "Nucleus Location",
				"Actin Distribution" };
		dataTable.set(1, 0, groupName[groupIndex]);

		for(int i = 0; i < statParamName[groupIndex].length; i++)
		{
			String name = statParamName[groupIndex][i];
			TableElement link = null;
			switch (groupIndex)
			{
			case 0:
				dataTable.set(1, i + 1, name);
				break;
			case 1:
			case 2:
				link = new  StringElement(name);
				link = new AttributeDecollation(link, "id", groupPrefix[groupIndex] + i + statParamName[groupIndex][i]);
				link = new JavaScriptDecollation(link, "onMouseOver", "on(this.id);");
				link = new JavaScriptDecollation(link, "onMouseOut", "off(this.id);");
				link = new JavaScriptDecollation(link, "onClick", "javascript:help('help/nucleus_param.html');");
				dataTable.set(1, i + 1, link);
				break;
			case 3:
				link = new StringElement(name);
				link = new AttributeDecollation(link, "id", groupPrefix[groupIndex] + i + statParamName[groupIndex][i]);
				link = new JavaScriptDecollation(link, "onMouseOver", "on(this.id);");
				link = new JavaScriptDecollation(link, "onMouseOut", "off(this.id);");
				link = new JavaScriptDecollation(link, "onClick", "javascript:help('help/actin_param.html');");
				dataTable.set(1, i + 1, link);
				break;
			}
		}

		dataTable.set(2, 0, "Percentage (%)");

		Decollator sheetLabelDecollator = new StyleDecollator("sheetlabel");
		dataTable.decollateRow(1, sheetLabelDecollator);
		dataTable.decollateCol(0, new StyleDecollator("tablelabel"));
		dataTable.decollate(dataTable.getRowSize() - 1, 0, sheetLabelDecollator);
		dataTable.decollate(1, 0, new StyleDecollator("tablelabel_bright"));

		for(int i = 0; i < statParamName[groupIndex].length; i++)
		{
			String paramName = groupPrefix[groupIndex] + "-percentage-" + statParamName[groupIndex][i]; 
			Double percentage =  (Double) paramMap.get(paramName);
			if(percentage != null) 
				dataTable.set(2, i + 1, percentage);
			
			TreeMap map = new TreeMap();
			if(groupIndex == 0)
				map.put("budSize", statParamName[groupIndex][i]);
			
			String areaRatioParam = groupPrefix[groupIndex] + "-areaRatio_" + statParamName[groupIndex][i];
            String areaRatioStr = (String) paramMap.get(areaRatioParam);
            double areaRatio;
            if(areaRatioStr == null)
                areaRatio = 0.0;
            else
                areaRatio = Double.parseDouble((String) paramMap.get(areaRatioParam));
			for(int p = 0; p < param.length; p++)
			{
				String data = (String) paramMap.get(groupPrefix[groupIndex] + "-" + param[p] + "_"
						+ statParamName[groupIndex][i]);
				if(data == null || percentage.compareTo(new Double(0.0)) == 0 || 
				        (areaRatio == 0 && (param[p].equals("budNeckPosition") || param[p].equals("budGrowthDirection"))))
					data = "";
				map.put(cellShapeParam[p], data);
				dataTable.set(p + 3, i + 1, data);
			}
			dataTable.set(0, i + 1, new Width(128, new Height(128, new ImageElement(
					"cellshape.png", map))));

			TreeMap sheetMap = new TreeMap();
			sheetMap.put("orf", orf);
			sheetMap.put("stainType", new Integer(groupIndex > 1 ? groupIndex - 1 : groupIndex));
			sheetMap.put("group", statParamName[groupIndex][i]);
			TableElementList sheetLink = new TableElementList();
			sheetLink.add("[");
			sheetLink.add(new Link("ViewGroupDataSheet.do", sheetMap, "datasheet"));
			sheetLink.add("]");
			dataTable.set(dataTable.getRowSize() - 3, i + 1, new AttributeDecollation(sheetLink,
					"align", "center"));
			dataTable.decollate(dataTable.getRowSize() - 3, i + 1, new StyleDecollator("button"));

			TableElementList teardropLink = new TableElementList();
			teardropLink.add("[");
			teardropLink.add(new Link("ViewGroupByTearDrop.do", sheetMap, "teardrop view"));
			teardropLink.add("]");
			dataTable.set(dataTable.getRowSize() - 2, i + 1, new AttributeDecollation(teardropLink,
					"align", "center"));
			dataTable.decollate(dataTable.getRowSize() - 2, i + 1, new StyleDecollator("button"));

			TableElementList link = new TableElementList();
			link.add("[");
			TreeMap linkMap = (TreeMap) map.clone();
			linkMap.put("phase", "5");
			link.add(new Link("SelectShape.do", linkMap, "search"));
			link.add("]");
			dataTable.set(dataTable.getRowSize() - 1, i + 1, new AttributeDecollation(link,
					"align", "center"));
			dataTable.decollate(dataTable.getRowSize() - 1, i + 1, new StyleDecollator("button"));
		}

		for(int i = 0; i < 6; i++)
		{
			if(i % 2 == 0)
				dataTable.decollate(new TableRange(2 + i, 1, 2 + i, dataTable.getColSize() - 1),
						new StyleDecollator("datasheet"));
			else
				dataTable.decollate(new TableRange(2 + i, 1, 2 + i, dataTable.getColSize() - 1),
						new StyleDecollator("datasheet_gray"));
		}
		dataTable.decollate(new TableRange(2, 1, 2, dataTable.getColSize() - 1),
				new NumberFormatDecollator(1));
		dataTable.decollate(new TableRange(3, 1, dataTable.getRowSize() - 4,
				dataTable.getColSize() - 1), new NumberFormatDecollator(3));
		dataTable.decollate(new TableRange(2, 0, dataTable.getRowSize() - 1,
				dataTable.getColSize() - 1), new AttributeDecollator("align", "center"));

		TableElementList groupBySheetLink = new TableElementList();
		groupBySheetLink.add("[");
		TreeMap map = new TreeMap();
		map.put("orf", orf); 
		map.put("stainType", new Integer(groupIndex > 1 ? groupIndex - 1 : groupIndex));
		groupBySheetLink.add(new Link("ViewGroupBySheet.do",map, "group by sheet"));
		groupBySheetLink.add("]");
		dataTable.set(8, 0, new Style(groupBySheetLink, "button"));
		return dataTable;
	}
}


//--------------------------------------
// $Log: ViewStatsAction.java,v $
// Revision 1.17  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.16  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.15  2004/09/03 03:31:30  leo
// javascriptでCSSを変更できるようにした
//
// Revision 1.14  2004/09/02 08:49:48  leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.13  2004/09/01 06:39:59  leo
// TearDropViewを追加
//
// Revision 1.12  2004/08/29 13:15:11  leo
// morphology search。bud size -> bud area ratioに変更
//
// Revision 1.11  2004/08/14 11:09:08  leo
// Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
// Revision 1.10  2004/08/13 18:58:01  leo
// デザインの微調整
//
// Revision 1.9 2004/08/13 14:56:30 leo
// statviewとりあえず完成
//
// Revision 1.8 2004/08/13 13:57:12 leo
// Statページの更新
//
// Revision 1.7 2004/08/11 14:02:32 leo
// Group by のシート作成
//
// Revision 1.6 2004/08/09 12:33:03 leo
// StringAttributeDecollation -> AttributeDecollationに集約
//
// Revision 1.5 2004/08/09 12:26:42 leo
// Commentを追加
//
// Revision 1.4 2004/08/09 09:17:11 leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.3 2004/08/09 05:25:17 leo
// タグないに複数のタグを持てるように改良
//
// Revision 1.2 2004/08/01 14:58:22 leo
// 移動
//
// Revision 1.1 2004/08/01 08:20:12 leo
// BasicTableをHTMLに変換するツールを書き始めました
//
//--------------------------------------
