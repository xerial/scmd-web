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

import java.awt.Color;
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
import lab.cb.scmd.web.table.Link;
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
    static public final Color shadeMinus = new Color(0x0086FF);
    static public final Color shadeMedium = new Color(0xFFFFFF);
    static public final Color shadePlus = new Color(0xFF0070);
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
        
        String inputtedOrf = teardropForm.getOrf();
        view.setOrf(inputtedOrf);
        
        
        int sheetType = teardropForm.getSheetType();
        if(sheetType < 0)
            sheetType = view.getOrfSheetType();
        else
            view.setSheetType(sheetType);
        String sql = null;
        if(sheetType >= 0 && sheetType < 3)
        {
            int stainType = sheetType;
            // stainTypeに該当するparameter IDのリストを取得
            final String[] stainName = {"cell wall", "nucleus", "actin"};
            sql = SQLExpression.assignTo(
                    "select id, name, displayname, shortname from $1 where scope='orf' and datatype in ('double', 'cv') and stain = '$2' order by id",
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
                    "select id, name, displayname, shortname from $1 where scope='orf' and datatype in ('double', 'cv') and id in ($2)",
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
        Vector<TableElement> valRow = new Vector<TableElement>();
        Vector<String> maxRow = new Vector<String>();
        Vector<TableElement> teardropRow = new Vector<TableElement>();
        LinkedList<Pair<Teardrop, List<TeardropPoint>>> teardropList = new LinkedList<Pair<Teardrop, List<TeardropPoint>>>();
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setMaximumFractionDigits(3);
        format.setMinimumFractionDigits(3);
        NumberFormat roughFormat = NumberFormat.getIntegerInstance();
        roughFormat.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);        
        
        for(MorphParameter param : parameterList)
        {
            int paramID = param.getId();
            //StringElement label = new StringElement(param.getShortName());
            Link label = new Link("ParamSheet.do?param=" + param.getName(), param.getShortName());
            //labelRow.add(new AttributeDecollation(label, "title", param.getDisplayname()));
            labelRow.add(new AttributeDecollation(label, "title", param.getDisplayname()));

            String sql2 = SQLExpression.assignTo("select paramid, groupid, average, sd, min, max from $1 where groupid=0 and paramid=$2", 
                    SCMDConfiguration.getProperty("DB_PARAM_AVG_SD", "paramavgsd"), paramID);
            Teardrop teardrop = (Teardrop) ConnectionServer.query(sql2, new BeanHandler(Teardrop.class));
            teardrop.setGroupID(0);
            teardrop.setParamID(paramID);
            teardrop.setOrientation(Teardrop.Orientation.horizontal);
            
            minRow.add(roughFormat.format(teardrop.getMin()));
            maxRow.add(roughFormat.format(teardrop.getMax()));
            
            // Teardrop上の点の位置情報を取得
            String sql3 = SQLExpression.assignTo("select strainname, average from $1 where groupid='0' and strainname in ($2) and paramid=$3",
                    SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat"),
                        SQLUtil.commaSeparatedList(orfSet, SQLUtil.QuotationType.singleQuote),
                        paramID
                );
            List<TeardropPoint> plotList = (List<TeardropPoint>) ConnectionServer.query(sql3, new BeanListHandler(TeardropPoint.class));
            double value = -1;
            for(TeardropPoint tp : plotList)
            {
                String orf = tp.getParamName();
                tp.setColor(userSelection.getPlotColor(orf).getColor());
                if(orf.toUpperCase().equals(teardropForm.getOrf().toUpperCase()))
                {
                    value = tp.getValue();
                }
            }
            if(value != -1)
            {
                double ave = teardrop.getAverage();
                double sd = teardrop.getSD();
                if(sd > 0)
                {
                    double diff  = (value - ave) / sd;
                    double absDiff = Math.abs(diff);
                    double strength = absDiff / 4.0;
                    if(strength > 1.0) 
                        strength = 1.0;
                    if(strength < -1.0)
                        strength = -1.0;
                    double score = (diff < 0) ? -strength : strength;
                    valRow.add(new AttributeDecollation(new StringElement(format.format(value)), "bgcolor", 
                            getShadingColorCode(shadeMinus, shadeMedium, shadePlus, score)));
                }
                else
                    valRow.add(new StringElement(format.format(value)));
            }
            else
                valRow.add(new StringElement(""));
            
            teardropList.add(new Pair<Teardrop, List<TeardropPoint>>(teardrop, plotList));
            
            String imageID = teardrop.getImageID();
            imageCache.registerImage(imageID);
            
            TreeMap<String, String> imgArg = new TreeMap<String, String>();
            imgArg.put("imageID", imageID);
            imgArg.put("encoding", "png"); 
            ImageElement img = new ImageElement("scmdimage.png", imgArg);
            img.setProperty("alt", "average = " + format.format(teardrop.getAverage()));
            teardropRow.add(img);
        }
        
        SCMDThreadManager.addTask(new DrowTeardropTask(teardropList, imageCache));
        
        teardropSheet.addCol(labelRow);
        teardropSheet.addCol(valRow);
        teardropSheet.addCol(minRow);
        teardropSheet.addCol(teardropRow);
        teardropSheet.addCol(maxRow);
        
        //teardropSheet.decollateCol(1, new NumberFormatDecollator(3));
        teardropSheet.decollateCol(1, new AttributeDecollator("align", "right"));
        teardropSheet.decollateCol(2, new AttributeDecollator("align", "right"));
        //teardropSheet.decollateCol(3, new NumberFormatDecollator(3));
        teardropSheet.decollateCol(4, new AttributeDecollator("align", "right"));
        teardropSheet.decollateCol(0, new StyleDecollator("tablelabel"));
        
        Table label = new Table();
        label.insertRow(0, new String[] {"parameter", "value", "min", "Teardrop", "max"});
        label.decollate(0, 1, new AttributeDecollator("width", "50"));
        label.decollate(0, 2, new AttributeDecollator("width", "50"));
        label.decollate(0, 4, new AttributeDecollator("width", "50"));
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
        tdTable.setProperty("class", "datasheet");  
        tdTable.setProperty("cellpadding", "0");
        for(Table t : tableList)
        {
            tdTable.appendToRight(t);
            tdTable.addCol(new TableElement[] {new AttributeDecollation(new StringElement(""), "width", "20")});
        }
        tdTable.removeCol(tdTable.getColSize() -1);
        
        request.setAttribute("teardropSheet", tdTable);


        double shadingUnit = 0.05;
        int numShadingCol = (int) (2 / shadingUnit) +1;        
        Table shadingTable = new Table(1, numShadingCol);
        shadingTable.setProperty("cellspacing", "0");
        shadingTable.setProperty("cellpadding", "0");
        for(int i=0; i<numShadingCol; i++)
        {
            shadingTable.decollate(0, i, new AttributeDecollator("bgcolor", getShadingColorCode(shadeMinus, shadeMedium, shadePlus, -1 + shadingUnit * i)));
            shadingTable.decollate(0, i, new AttributeDecollator("width", "10"));
            shadingTable.decollate(0, i, new AttributeDecollator("height", "15"));
        }
        request.setAttribute("shadingTable", shadingTable);
        
         
        return mapping.findForward("success");        
    }
    
    
    
    /**
     * @param low 低いscoreの方の色
     * @param middle 中間色
     * @param high 高いscoreの方の色
     * @param score -1.0 から 1.0の値
     * @return カラーコード
     */
    static public String getShadingColorCode(Color low, Color middle, Color high, double score)
    {
        if(score >= 0)
            return getShade(middle, high, score);
        else
            return getShade(middle, low, -score);
    }
    
    static private String getShade(Color base, Color target, double score)
    {
        int r = (int) ((target.getRed() - base.getRed()) * score + base.getRed());
        int g = (int) ((target.getGreen() - base.getGreen()) * score + base.getGreen());
        int b = (int) ((target.getBlue() - base.getBlue()) * score + base.getBlue());
        
        return "#" + formatColorCode(r) + formatColorCode(g) + formatColorCode(b);
    }
    
    static private String formatColorCode(int oneOfRGB)
    {
        String colorCode = Integer.toString(oneOfRGB, 16);
        if(colorCode.length() == 1)
            colorCode = "0" + colorCode;
        return colorCode;
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




