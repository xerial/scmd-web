//--------------------------------------
// SCMDWeb Project
//
// ViewORFDataSheetAction.java
// Since: 2005/02/15
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.Range;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.container.ORFParamData;
import lab.cb.scmd.web.formbean.ViewORFDataSheetForm;
import lab.cb.scmd.web.image.teaddrop.Teardrop;
import lab.cb.scmd.web.sessiondata.MorphParameter;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * parameterごとにORFの一覧表示をする
 * @author leo
 *
 */
public class ViewORFDataSheetAction extends Action
{

    /**
     * 
     */
    public ViewORFDataSheetAction()
    {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse responce) throws Exception
    {
        ViewORFDataSheetForm input = (ViewORFDataSheetForm) form;
        CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);
        
        int paramID = input.getParamID();
        int currentPage = input.getPage() - 1;
        
        // validate the existence of the given parameter ID
        MorphParameter param = (MorphParameter) ConnectionServer.query(new BeanHandler(MorphParameter.class), "select * from $1 list where id=$2 and scope='orf'", 
                SCMDConfiguration.getProperty("DB_PARAMETERLIST", "parameterlist"), paramID);        
        if(param == null)
            return mapping.findForward("select_parameter");  // parameterの選択しなおし
        request.setAttribute("para", param);        
        
        // count available orfs 
        Number count = (Number) ConnectionServer.query(new ScalarHandler("count"),
                "select count(*) from $1 where paramid=$2",
                SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                paramID);       
        int numData = 0;
        if(count != null)
            numData = count.intValue();
        
        int numORFsInAPage = 200;
        int maxPage = (int) Math.ceil(((double) numData / numORFsInAPage));
        
        while(currentPage < 0)
            currentPage += numORFsInAPage;
        if(currentPage > maxPage)
            currentPage = maxPage % numORFsInAPage;
        
        PageStatus pageStatus = new PageStatus(currentPage+1, maxPage);    
        request.setAttribute("pageStatus", pageStatus);
        
        // dataの取得
        List<ORFParamData> orfData = 
            (List<ORFParamData>) ConnectionServer.query(new BeanListHandler(ORFParamData.class), 
                "select strainname as orf, average as data from $1 where paramid=$2 order by average $3 limit $4 offset $5",
                SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                paramID,
                input.getOrder().name(),
                numORFsInAPage,
                currentPage * numORFsInAPage);
                
        
        request.setAttribute("orfData", orfData);
        
        // 表示するデータのmin, maxを求める
        TreeSet<Double> dataList = new TreeSet<Double>();
        for(ORFParamData data : orfData)
            dataList.add(data.getData());
        
        double begin = 0, end=0;        
        if(!dataList.isEmpty())
        {
            begin = dataList.first();
            end = dataList.last();
        }
        Range range = new Range(begin, end);
        request.setAttribute("range", range);
        
        return mapping.findForward("success");        
    }
}




