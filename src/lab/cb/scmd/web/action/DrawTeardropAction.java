//--------------------------------------
// SCMDWeb Project
//
// DrawTeardropAction.java
// Since: 2005/02/18
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.SCMDManager;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.formbean.DrawTeardropForm;
import lab.cb.scmd.web.image.TeardropPoint;
import lab.cb.scmd.web.image.teaddrop.Teardrop;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Teardropを描画するAction
 * 
 * @author leo
 * 
 */
public class DrawTeardropAction extends Action {

	/**
	 * 
	 */
	public DrawTeardropAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DrawTeardropForm input = (DrawTeardropForm) form;
		CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);
		int paramID = input.getParamID();
		int groupID = input.getGroupID();

		// create teadrop
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("paramID", String.valueOf(paramID));
		map.put("groupID", String.valueOf(groupID));

		//	SQLを発行
		
		List<Teardrop> list = SCMDManager.getDBManager().queryResults("DrawTeardrop:teardrop",map,Teardrop.class);
//		List<Teardrop> list = ConnectionServer.queryResult("DrawTeardrop:teardrop",map,Teardrop.class);
		Teardrop teardrop = null;
		if(list.size()==1) {
			teardrop = list.get(0);
		}
/*`
		String sql = SQLExpression
				.assignTo(
						"select t1.paramid as \"paramID\", t1.groupid, t1.average, t1.sd, t1.min, t1.max, t2.average as wt_average, t2.sd as wt_sd from $1 as t1 inner join $2 as t2 using(paramid) where t1.groupid=$4 and t2.groupid=$4 and paramid=$3",
						SCMDConfiguration.getProperty("DB_PARAM_AVG_SD",
								"paramavgsd"), SCMDConfiguration.getProperty(
								"DB_PARAM_AVG_SD_WT", "paramavgsd_wt"),
						paramID, groupID);
		Teardrop teardrop = (Teardrop) ConnectionServer.query(sql,
				new BeanHandler(Teardrop.class));*/

		if (teardrop == null)
			return printNAImage(mapping, form, request, response);
		teardrop.setParamID(paramID);
		teardrop.setGroupID(groupID);

		UserSelection selection = SCMDSessionManager.getUserSelection(request);

		// Teardrop上の点の位置情報を取得
		Set<String> orfSet = new TreeSet<String>();
		if (input.isPlotTargetORF())
			orfSet.add(view.getOrf().toUpperCase());
		if (input.isPlotUserORF())
			orfSet.addAll(selection.getSelection());
		List<TeardropPoint> plotList;
		if (orfSet.isEmpty())
			plotList = new LinkedList<TeardropPoint>();
		else {
			/*String sql2 = SQLExpression
					.assignTo(
							"select strainname, average from $1 where groupid='$4' and strainname in ($2) and paramid=$3",
							SCMDConfiguration.getProperty("DB_PARAMSTAT",
									"paramstat"), SQLUtil.commaSeparatedList(
									orfSet, SQLUtil.QuotationType.singleQuote),
							paramID, groupID);
			plotList = (List<TeardropPoint>) ConnectionServer.query(sql2,
					new BeanListHandler(TeardropPoint.class));*/
			//HashMap<String, String> map = new HashMap<String, String>();
			map.put("paramID", String.valueOf(paramID));
			map.put("groupID", String.valueOf(groupID));
			map.put("separatedlist", SQLUtil.commaSeparatedList(orfSet,SQLUtil.QuotationType.singleQuote));

			//	
//			plotList = ConnectionServer.queryResult("DrawTeardrop:plotlist",map,TeardropPoint.class);
			plotList = SCMDManager.getDBManager().queryResults("DrawTeardrop:plotlist",map,TeardropPoint.class);
		}
		// plot に色づけ
		for (TeardropPoint tp : plotList) {
			String orf = tp.getParamName();
			tp.setColor(selection.getPlotColor(orf).getColor());
		}

		if (input.getOrientation().equals("vertical"))
			teardrop.setOrientation(Teardrop.Orientation.vertical);
		else
			teardrop.setOrientation(Teardrop.Orientation.horizontal);
		BufferedImage teardropImage = null;
		try {
			teardropImage = teardrop.drawImage(plotList);
			if (input.getRangeBegin() >= 0)
				teardrop.drawRange(teardropImage, input.getRangeBegin(), input
						.getRangeEnd());
			response.setContentType("image/png");
			ImageIO.write(teardropImage, "png", response.getOutputStream());
		} catch (SCMDException e) {
			e.printStackTrace();
			return printNAImage(mapping, form, request, response);
		} catch (IOException e) {
			e.printStackTrace();
			return printNAImage(mapping, form, request, response);
		}

		return super.execute(mapping, form, request, response);
	}

	private ActionForward printNAImage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("image/png");
		request.getRequestDispatcher("/png/na_teardrop.png").forward(request,
				response);
		return super.execute(mapping, form, request, response);
	}

}
