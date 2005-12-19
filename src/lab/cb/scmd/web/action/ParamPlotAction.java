//--------------------------------------
// SCMDServer
//
// ParamPlotAction.java 
// Since: 2004/09/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.ParamPlotForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * 2D Plot���쐬����Action
 * 
 * @author leo
 * 
 */
public class ParamPlotAction extends Action {
	/**
	 * 
	 */
	public ParamPlotAction() {
		super();

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ParamPlotForm plotForm = (ParamPlotForm) form;
		// session����A�I������Ă���orf���擾
		UserSelection userSelection = SCMDSessionManager
				.getUserSelection(request);
		CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);

		request.setAttribute("addViewORF", false);

		// ORF���X�g��TreeSet�^�Ŏ��o��
		TreeSet<String> selectedORFSet = userSelection.orfSet();

		if (selectedORFSet.isEmpty() || plotForm.getOrfType().equals("current")) {
			selectedORFSet.clear();
			String orf = view.getOrf();
			selectedORFSet.add(orf.toUpperCase());
			if (selectedORFSet.size() == 1)
				request.setAttribute("addViewORF", true);
			request.setAttribute("gene", DBUtil.getGeneInfo(orf));
		}

		if (plotForm.getParam1() == -1 && plotForm.getParam2() == -1) {
			String[] orfs = new String[selectedORFSet.size()];
			int n = 0;
			for (Iterator it = selectedORFSet.iterator(); it.hasNext();) {
				orfs[n++] = (String) it.next();
			}
			selectMaximumInterclassVarianceCorrdinates(plotForm, orfs,
					SCMDConfiguration.getTableQueryInstance());
		}

		return mapping.findForward("success");
	}

	/**
	 * interclass variance���ő�ɂȂ�Q����I��
	 * @param plotForm
	 * @param tableQueryInstance
	 */
	private void selectMaximumInterclassVarianceCorrdinates(
			ParamPlotForm plotForm, String[] orf, TableQuery tableQuery) {
		// 501 rows and {paramname, average, sd} cols
		Table avgAndSD = tableQuery.getAnalysisAVGandSD();

		HashMap paramAverage = new HashMap();
		HashMap paramSD = new HashMap();
		for (int i = 1; i < avgAndSD.getRowSize(); i++) {
			paramAverage.put(avgAndSD.get(i, 0).toString(), Double
					.parseDouble(avgAndSD.get(i, 1).toString()));
			paramSD.put(avgAndSD.get(i, 0).toString(), Double
					.parseDouble(avgAndSD.get(i, 2).toString()));
		}

		// ** rows and 501 cols
		Table selectedValues = tableQuery.getSelectedAnalysisValue(orf);
		if (selectedValues == null || selectedValues.getRowSize() <= 1) {
			// ORF���I������Ă��Ȃ��Ƃ��ɂ͉������Ȃ�
			plotForm.setParam1(-1);
			plotForm.setParam2(-1);
			return;
		}
		int rowsize = selectedValues.getRowSize();
		int orfsize = rowsize - 1;
		List<String> optionList = plotForm.getOptions();
		String[] options = new String[optionList.size()];
		plotForm.getOptions().toArray(options);
		int optionSize = options.length + 1;
		double maxvar = 0.0;
		int[] maxcorrdinates = { 0, 0 };
		double[] avg = { 0.0, 0.0 };
		ColLabelIndex colLabelIndex = new ColLabelIndex(selectedValues);
		for (int i = 1; i < optionSize; i++) {
			// �P���ڂ̑I��
			int col0 = colLabelIndex.getColIndex(options[i - 1]);
			if (col0 == -1)
				continue;
			avg[0] = 0.0;
			for (int orfnum = 1; orfnum < rowsize; orfnum++) {
				avg[0] += zvalue(Double.parseDouble(selectedValues.get(orfnum,
						col0).toString()), ((Double) paramAverage
						.get(options[i - 1])).doubleValue(), ((Double) paramSD
						.get(options[i - 1])).doubleValue());
			}
			avg[0] /= (double) orfsize;
			for (int j = i + 1; j < optionSize; j++) {
				// 2���ڂ̑I��
				int col1 = colLabelIndex.getColIndex(options[j - 1]);
				if (col1 == -1)
					continue;
				double var = 0.0;
				avg[1] = 0.0;
				for (int orfnum = 1; orfnum < rowsize; orfnum++) {
					avg[1] += zvalue(Double.parseDouble(selectedValues.get(
							orfnum, col1).toString()), ((Double) paramAverage
							.get(options[j - 1])).doubleValue(),
							((Double) paramSD.get(options[j - 1]))
									.doubleValue());
				}
				avg[1] /= (double) orfsize;
				var = avg[0] * avg[0] + avg[1] * avg[1];
				// �œK�R���r�l�[�V�����̑g�ݍ��킹
				if (maxvar < var) {
					maxvar = var;
					maxcorrdinates[0] = i;
					maxcorrdinates[1] = j;
				}
			}
		}
		// �œK�ȑg�ݍ��킹���Z�b�g
		plotForm.setParam1(plotForm.getParamID(options[maxcorrdinates[0] - 1]));
		plotForm.setParam2(plotForm.getParamID(options[maxcorrdinates[1] - 1]));
	}

	private double zvalue(double val, double avg, double sd) {
		return (val - avg) / sd;
	}

}
