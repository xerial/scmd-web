//--------------------------------------
// SCMDWeb Project
//
// ViewORFTeardropAction.java
// Since: 2005/02/13
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.common.SCMDThreadManager;
import lab.cb.scmd.web.formbean.ViewORFTeardropForm;
import lab.cb.scmd.web.image.ImageCache;
import lab.cb.scmd.web.image.TeardropPoint;
import lab.cb.scmd.web.image.teaddrop.Teardrop;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.sessiondata.ParamUserSelection;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.StringElement;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.Width;
import lab.cb.scmd.web.table.decollation.AttributeDecollation;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.xerial.util.Pair;

/**
 * ORFごとの全パラメータのTeardrop表示用のAction
 * @author leo
 *
 */
public class ViewORFTeardropAction extends Action
{

    /**
     * 
     */
    public ViewORFTeardropAction()
    {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        ViewORFTeardropForm teardropForm = (ViewORFTeardropForm) form;        
        UserSelection userSelection = SCMDSessionManager.getUserSelection(request);
        CellViewerForm view = SCMDSessionManager.getCellViewerForm(request);
        
        request.setAttribute("gene", DBUtil.getGeneInfo(teardropForm.getOrf()));
        
        int sheetType = teardropForm.getSheetType();
        if(sheetType < 0)
            sheetType = view.getSheetType();
        else
            view.setSheetType(sheetType);
        String sql = null;
        if(sheetType >= 0 && sheetType < 3)
        {
            int stainType = sheetType;
            // stainTypeに該当するparameter IDのリストを取得
            final String[] stainName = {"cell wall", "nucleus", "actin"};
            sql = SQLExpression.assignTo(
                    "select id, displayname, shortname from $1 where scope='orf' and datatype in ('double') and stain = '$2' order by id",
                    SCMDConfiguration.getProperty("DB_PARAMETERLIST", "parameterlist"),
                    stainName[stainType]
            );        
        }
        else
        {
            // customizeed parameter ID list
            ParamUserSelection paramSelection = SCMDSessionManager.getParamUserSelection(request);            
            Set<Integer> paramIDSet = paramSelection.getOrfParamSelection();
            if(paramIDSet.isEmpty())
                return mapping.findForward("success");
            sql = SQLExpression.assignTo(
                    "select id, displayname,    shortname from $1 where scope='orf' and dataytpe in ('double') and id in ($2)",
                    SCMDConfiguration.getProperty("DB_PARAMETERLIST", "parameterlist"),
                    SQLUtil.commaSeparatedList(paramIDSet, SQLUtil.QuotationType.none)
            );
        }
        List<MorphParameter> parameterList = (List<MorphParameter>) ConnectionServer.query(sql, new BeanListHandler(MorphParameter.class));

        Vector<Integer> paramIDList = new Vector<Integer>();
        for(MorphParameter param : parameterList)
        {
            paramIDList.add(param.getId());
        }
        
 
        Set<String> orfSet = new TreeSet<String>();
        for(String orf : userSelection.getSelection())
        {
            orfSet.add(orf.toUpperCase());    
        }        
        orfSet.add(teardropForm.getOrf().toUpperCase());


        ImageCache imageCache = ImageCache.getImageCache(request);
        Table teardropSheet = new Table();

        Vector<TableElement> labelRow = new Vector<TableElement>();
        Vector<String> minRow = new Vector<String>();
        Vector<String> maxRow = new Vector<String>();
        Vector<TableElement> teardropRow = new Vector<TableElement>();
        LinkedList<Pair<Teardrop, List<TeardropPoint>>> teardropList = new LinkedList<Pair<Teardrop, List<TeardropPoint>>>();
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        
        for(MorphParameter param : parameterList)
        {
            int paramID = param.getId();
            StringElement label = new StringElement(param.getDisplayname());
            labelRow.add(new AttributeDecollation(label, "title", param.getDisplayname()));

            String sql2 = SQLExpression.assignTo("select paramid, groupid, average, sd, min, max from $1 where groupid=0 and paramid=$2", 
                    SCMDConfiguration.getProperty("DB_PARAM_AVG_SD", "paramavgsd"), paramID);
            Teardrop teardrop = (Teardrop) ConnectionServer.query(sql2, new BeanHandler(Teardrop.class));
            teardrop.setGroupID(0);
            teardrop.setParamid(paramID);
            teardrop.setOrientation(Teardrop.Orientation.horizontal);
            
            minRow.add(format.format(teardrop.getMin()));
            maxRow.add(format.format(teardrop.getMax()));
            
            // Teardrop上の点の位置情報を取得
            String sql3 = SQLExpression.assignTo("select strainname, average from $1 where groupid='0' and strainname in ($2) and paramid=$3",
                    SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                        SQLUtil.commaSeparatedList(orfSet, SQLUtil.QuotationType.singleQuote),
                        paramID
                );
            List<TeardropPoint> plotList = (List<TeardropPoint>) ConnectionServer.query(sql3, new BeanListHandler(TeardropPoint.class));
            teardropList.add(new Pair<Teardrop, List<TeardropPoint>>(teardrop, plotList));
            
            String imageID = teardrop.getImageID();
            imageCache.registerImage(imageID);
            
            TreeMap<String, String> imgArg = new TreeMap<String, String>();
            imgArg.put("imageID", imageID);
            imgArg.put("encoding", "png"); 
            ImageElement img = new ImageElement("scmdimage.img", imgArg);
            img.setProperty("alt", "average = " + format.format(teardrop.getAverage()));
            img.setProperty("align", "center");
            teardropRow.add(img);
        }
        
        SCMDThreadManager.addTask(new DrowTeardropTask(teardropList, imageCache));
        
        teardropSheet.addCol(labelRow);
        teardropSheet.addCol(minRow);
        teardropSheet.addCol(teardropRow);
        teardropSheet.addCol(maxRow);
        
        //teardropSheet.decollateCol(1, new NumberFormatDecollator(3));
        teardropSheet.decollateCol(1, new AttributeDecollator("align", "right"));
        //teardropSheet.decollateCol(3, new NumberFormatDecollator(3));
        teardropSheet.decollateCol(3, new AttributeDecollator("align", "right"));
        teardropSheet.decollateCol(0, new StyleDecollator("tablelabel"));
        
        Table label = new Table();
        label.insertRow(0, new String[] {"parameter", "min", "Teardrop", "max"});
        label.decollate(0, 1, new AttributeDecollator("width", "70"));
        label.decollate(0, 3, new AttributeDecollator("width", "70"));
        label.setProperty("class", "datasheet");        
        label.decollateRow(0, new StyleDecollator("sheetlabel"));
        
        
        LinkedList<Table> tableList = new LinkedList<Table>();        
        int numPickedRow = 0;
        int availableRow = teardropSheet.getRowSize();
        int numSeparation = (int) Math.ceil(availableRow / 2 + 0.5);
        for(int i=0; i<availableRow; i+=numSeparation)
        {
            int rowSize = numSeparation;
            if(numPickedRow + rowSize >= availableRow)
                rowSize = availableRow - numPickedRow;
            
            Table newTable = new Table();
            newTable.appendToBottom(label);
            for(int row=numPickedRow; row<numPickedRow+rowSize; row++)
                newTable.appendToBottom(teardropSheet.getRow(row));

            numPickedRow += rowSize;           
            tableList.add(newTable);
        }
        
        Table tdTable = new Table();
        for(Table t : tableList)
        {
            tdTable.appendToRight(t);
            tdTable.addCol(new TableElement[] {new AttributeDecollation(new StringElement(""), "width", "20")});
        }
        
        request.setAttribute("teardropSheet", tdTable);


        return mapping.findForward("success");        
    }
    
    class DrowTeardropTask implements Runnable
    {
        LinkedList<Pair<Teardrop, List<TeardropPoint>>> teardropList;
        ImageCache imageCache;

        public DrowTeardropTask(LinkedList<Pair<Teardrop, List<TeardropPoint>>> teardropList, ImageCache cache)
        {
            this.teardropList = teardropList;
            this.imageCache = cache;
        }
        
        // @see java.lang.Runnable#run()
        public void run()
        {
            for(Pair<Teardrop, List<TeardropPoint>> teardrop : teardropList)
            {
                Teardrop td = teardrop.getFirst();
                List<TeardropPoint> tdlist = teardrop.getSecond();
                try
                {
                    imageCache.addImage(td.getImageID(), td.drawImage(tdlist));
                }
                catch(SCMDException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
}




