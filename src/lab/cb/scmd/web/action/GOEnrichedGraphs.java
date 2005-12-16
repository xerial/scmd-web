package lab.cb.scmd.web.action;

import java.awt.List;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.web.bean.GeneOntology;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.container.Enrichments;
import lab.cb.scmd.web.formbean.EnrichedParamsForm;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GOEnrichedGraphs extends Action {
	
	public GOEnrichedGraphs() {
		super();
	}
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response)  {
        EnrichedParamsForm paramsForm = (EnrichedParamsForm) form;

        String[] goid = new String [1];
        goid[0] = paramsForm.getGoid();

        TableQuery goquery = SCMDConfiguration.getTableQueryInstance();
        Table gotable = goquery.getAssociatedGO(goid);
        if( gotable == null ) 
        	return mapping.findForward("failure");

        if( gotable.getRowSize() != 2 ) {
        	return mapping.findForward("failure");
        }
        
        ColLabelIndex colIndex = new ColLabelIndex(gotable);
        GeneOntology go = new GeneOntology();
        go.setGoid(colIndex.get(1, "goid").toString());
        go.setName(colIndex.get(1, "name").toString());
        go.setNamespace(colIndex.get(1, "namespace").toString());
        go.setDef(colIndex.get(1, "def").toString());
        
        LinkedList<Enrichments> enrichList = new LinkedList<Enrichments> ();
        Table fwdrevtable = goquery.getForwardReverseAssociations(go.getGoid());
        ColLabelIndex colIndex2 = new ColLabelIndex(fwdrevtable);
        for( int j = 1; j < fwdrevtable.getRowSize(); j++ ) {
        	Enrichments enrich = new Enrichments();
        	String paramID = colIndex2.get(j, "param").toString();
        	enrich.setParam(paramID);
        	enrich.setFwd(Integer.parseInt(colIndex2.get(j,"fwd" ).toString()));
        	enrich.setHigh(Integer.parseInt(colIndex2.get(j,"high" ).toString()));
        	enrich.setIngo(Integer.parseInt(colIndex2.get(j, "ingo").toString()));
        	enrich.setInabnorm(Integer.parseInt(colIndex2.get(j, "inabnorm").toString()));
        	enrich.setIntarget(Integer.parseInt(colIndex2.get(j, "intarget").toString()));
        	enrich.setPvalue(Double.parseDouble(colIndex2.get(j, "pvalue").toString()));
        	enrich.setRatio(Double.parseDouble(colIndex2.get(j, "ratio").toString()));
        	
        	MorphParameter param = new MorphParameter();
			try {
				param = (MorphParameter) ConnectionServer.query(new BeanHandler(MorphParameter.class), "select * from $1 list where id=$2 and scope='orf'", 
				        SCMDConfiguration.getProperty("DB_PARAMETERLIST", "visible_parameterlist"), paramID);
			} catch (SQLException e) {
				e.printStackTrace();
			}        
            if(param == null)
                return mapping.findForward("select_parameter");  // parameter‚Ì‘I‘ð‚µ‚È‚¨‚µ
            enrich.setMorphParameter(param);
        	go.addFwdRev(enrich);
        	enrichList.add(enrich);
        }

        request.setAttribute("go", go);
	    request.setAttribute("enrichList", enrichList);
 
	    return mapping.findForward("success");
    }
}
