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
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.formbean.DrawTeardropForm;
import lab.cb.scmd.web.image.TeardropPainter;
import lab.cb.scmd.web.image.TeardropPoint;
import lab.cb.scmd.web.image.teaddrop.Teardrop;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TeardropÇï`âÊÇ∑ÇÈAction
 * @author leo
 *
 */
public class DrawTeardropAction extends Action
{

    /**
     * 
     */
    public DrawTeardropAction()
    {
        super();
    }

    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DrawTeardropForm input = (DrawTeardropForm) form;
        int paramID = input.getParamID();
        
        // create teadrop
        String sql = SQLExpression.assignTo("select paramid as \"paramID\", groupid, average, sd, min, max from $1 where groupid=0 and paramid=$2", 
                SCMDConfiguration.getProperty("DB_PARAM_AVG_SD", "paramavgsd"), paramID);
        Teardrop teardrop = (Teardrop) ConnectionServer.query(sql, new BeanHandler(Teardrop.class));
        teardrop.setParamID(paramID);

        UserSelection selection = SCMDSessionManager.getUserSelection(request);
        
        // Teardropè„ÇÃì_ÇÃà íuèÓïÒÇéÊìæ
        List<TeardropPoint> plotList;
        if(selection.getSelection().isEmpty())
            plotList = new LinkedList<TeardropPoint>();
        else
        {
            String sql2 = SQLExpression.assignTo("select strainname, average from $1 where groupid='0' and strainname in ($2) and paramid=$3",
                    SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                    SQLUtil.commaSeparatedList(selection.getSelection(), SQLUtil.QuotationType.singleQuote),
                    paramID
            );
            plotList = (List<TeardropPoint>) ConnectionServer.query(sql2, new BeanListHandler(TeardropPoint.class));
        }
        // plot Ç…êFÇ√ÇØ
        for(TeardropPoint tp : plotList)
        {
            String orf = tp.getParamName();
            tp.setColor(selection.getPlotColor(orf).getColor());
        }

        teardrop.setOrientation(Teardrop.Orientation.horizontal);
        BufferedImage teardropImage  = null;
        try
        {
            teardropImage = teardrop.drawImage(plotList); 
            teardrop.drawRange(teardropImage, input.getRangeBegin(), input.getRangeEnd());
            response.setContentType("image/png");
            ImageIO.write(teardropImage, "png", response.getOutputStream());        
        }
        catch(SCMDException e)
        {
            TeardropPainter.printWhiteBoard(request, response);
        }
        
        return super.execute(mapping, form, request, response);
    }
    
    
    
}




