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
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamPlotAction extends Action
{
    /**
     * 
     */
    public ParamPlotAction() {
        super();

    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) 
    {
        ParamPlotForm plotForm = (ParamPlotForm) form;
        // sessionÇ©ÇÁÅAëIëÇ≥ÇÍÇƒÇ¢ÇÈorfÇéÊìæ
        HttpSession session = request.getSession(true);
        UserSelection userSelection = (UserSelection) session.getAttribute("userSelection");
        if(userSelection == null) 
            userSelection = new UserSelection();
        
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
            view = new CellViewerForm();

        //ç°ëIÇŒÇÍÇΩorfÇéÊìæ
        String orf = view.getOrf();
        
        if(plotForm.getParam1().equals("-1") && plotForm.getParam2().equals("-1")) {
            TreeSet selectedORFSet = (TreeSet)userSelection.orfSet();
            selectedORFSet.add(orf);
            String[] orfs = new String[selectedORFSet.size()];
            int n = 0;
            for(Iterator it = selectedORFSet.iterator(); it.hasNext(); ) {
                orfs[n++] = (String)it.next(); 
            }
            selectMaximumInterclassVarianceCorrdinates(plotForm,  orfs, SCMDConfiguration.getTableQueryInstance());
        }
        
        request.setAttribute("gene", DBUtil.getGeneInfo(orf));
        
        return mapping.findForward("success");
    }

    /**
     * @param plotForm
     * @param tableQueryInstance
     */
    private void selectMaximumInterclassVarianceCorrdinates(ParamPlotForm plotForm, String[] orf, TableQuery tableQuery) {
        // 501 rows and {paramname, average, sd} cols
        Table avgAndSD = tableQuery.getAnalysisAVGandSD();
        
        HashMap paramAverage = new HashMap();
        HashMap paramSD = new HashMap();
        for(int i = 1; i < avgAndSD.getRowSize(); i++ ) {
            paramAverage.put(avgAndSD.get(i, 0).toString(), Double.parseDouble(avgAndSD.get(i,1).toString()));
            paramSD.put(avgAndSD.get(i, 0).toString(), Double.parseDouble(avgAndSD.get(i,2).toString()));
        }
        
        // ** rows  and 501 cols
        Table selectedValues = tableQuery.getSelectedAnalysisValue(orf);
        int rowsize = selectedValues.getRowSize();
        if(rowsize <= 1 )
            return;
        int orfsize = rowsize - 1;
        String[] options = plotForm.getOptions();
        int optionSize = options.length + 1;
        double maxvar = 0.0;
        int[] maxcorrdinates = {0,0};
        double[] avg = {0.0, 0.0};
        ColLabelIndex colLabelIndex = new ColLabelIndex(selectedValues);
        for(int i = 1; i < optionSize; i++ ) {
            int col0 = colLabelIndex.getColIndex(options[i-1]);
            avg[0] = 0.0;
            for(int orfnum = 1; orfnum < rowsize; orfnum++ ) {
                avg[0] += zvalue(Double.parseDouble(selectedValues.get(orfnum, col0).toString()),
                            ((Double)paramAverage.get(options[i-1])).doubleValue(),
                            ((Double)paramSD.get(options[i-1])).doubleValue());
            }
            avg[0] /= (double)orfsize;
            for( int j = i + 1; j < optionSize; j++ ) {
                int col1 = colLabelIndex.getColIndex(options[j-1]);
                double var = 0.0;
                avg[1] = 0.0;
                for(int orfnum = 1; orfnum < rowsize; orfnum++ ) {
                    avg[1] += zvalue( Double.parseDouble(selectedValues.get(orfnum, col1).toString()),
                                ((Double)paramAverage.get(options[j-1])).doubleValue(),
                                ((Double)paramSD.get(options[j-1])).doubleValue());
                }
                avg[1] /= (double)orfsize;
                var = avg[0] * avg[0] + avg[1] * avg[1];
                if(maxvar < var) {
                    maxvar = var;
                    maxcorrdinates[0] = i;
                    maxcorrdinates[1] = j;
                }
            }
        }
        plotForm.setParam1(options[maxcorrdinates[0]-1]);
        plotForm.setParam2(options[maxcorrdinates[1]-1]);
    }
    
    private double zvalue(double val, double avg, double sd) {
        return (val - avg) / sd;
    }
    
}


//--------------------------------------
// $Log:  $
//
//--------------------------------------
