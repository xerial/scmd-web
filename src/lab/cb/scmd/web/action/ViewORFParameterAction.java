//-----------------------------------
// SCMDWeb Project
// 
// ViewORFParameterAction.java 
// Since: 2005/03/15
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.action;

import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.spi.XmlWriter;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.bean.Range;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.formbean.ViewORFParameterForm;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.xerial.util.xml.XMLAttribute;
import org.xerial.util.xml.XMLGenerator;



/**
 * 行：ORF、列：ORFパラメータ　のデータシートを準備するAction
 * @author leo
 *
 */
public class ViewORFParameterAction extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ViewORFParameterForm input = (ViewORFParameterForm) form;
               
        List<MorphParameter> selectedORFParameter = null;
        switch(input.columnType())
        {
        case input:
            if(input.getParamID().length == 0)
                break;
            selectedORFParameter = 
                (List<MorphParameter>)
                ConnectionServer.query(new BeanListHandler(MorphParameter.class), 
                        "select * from $1 where scope='orf' and id in ($2)",
                        SCMDConfiguration.getProperty("DB_PARAMLIST", "parameterlist"),
                        SQLUtil.commaSeparatedList(input.getParamID(), SQLUtil.QuotationType.singleQuote));
            break;
        case custom:
            selectedORFParameter = SCMDSessionManager.getParamUserSelection(request).getOrfParamInfo();
            break;
        }

        if(selectedORFParameter == null || selectedORFParameter.size() <= 0)
        {
            return mapping.findForward("selection");
        }
        
        String dbTable = SCMDConfiguration.getProperty("DB_PARAMSTAT", "paramstat");
        
        Vector<Integer> paramIDs = new Vector<Integer>(selectedORFParameter.size());
        Vector<String> paramShortNames = new Vector<String>(selectedORFParameter.size());
        Vector<String> sqlParamList = new Vector<String>(selectedORFParameter.size());
        Vector<String> sqlCondition = new Vector<String>(selectedORFParameter.size());
        StringBuilder sqlTable = new StringBuilder();
        int count = 0;
        String sortParamName = "strainname";
        MorphParameter targetParam = null;
        for(MorphParameter p : selectedORFParameter)
        {
            paramIDs.add(p.getId());
            paramShortNames.add(p.getName());
            String tableAlias = "t" + count;
            sqlParamList.add(SQLUtil.doubleQuote(p.getName()));
            if(input.getSortspec() == p.getId())
            {
                sortParamName = p.getName();
                targetParam = p;
            }
//            sqlCondition.add(tableAlias + ".paramid=" + p.getId());
//            if(count == 0)
//                sqlTable.append(dbTable + " as " + tableAlias);
//            else
//                sqlTable.append(" inner join " + dbTable + " as " + tableAlias + " using(strainname)");
//            
//            count++;
        }
        
        // calculate the max page        
        Integer numItem = (Integer) ConnectionServer.query(new ScalarHandler("count"), 
                "select cast(count(*) as int4) from $1",
                SCMDConfiguration.getProperty("DB_ANALYSISDATA", "analysisdata_20050131"));

         
        List<YeastGene> geneList = null;
                
        
        String format = input.getFormat();
        if(format.equals("xml"))
        {
            geneList = retrieveYeastGeneList(selectedORFParameter, -1, 0, sqlParamList, sortParamName);                       
            
            // output xml data
            response.setContentType("application/octet-stream");
            String xmlFile = "orfparam.xml"; 
            response.addHeader("Content-disposition", "attachment; filename=" + SQLUtil.doubleQuote(xmlFile));
            XMLGenerator xout = new XMLGenerator(response.getOutputStream());
            xout.startTag("orfparameter");
            for(YeastGene gene : geneList)
            {
                XMLAttribute orfAttrib = new XMLAttribute("orf", gene.getOrf());
                if(!gene.getStandardName().equals(""))
                    orfAttrib.add("standardname", gene.getStandardName());
                xout.startTag("orfdata", orfAttrib);
                for(MorphParameter p : selectedORFParameter)
                {
                    xout.startTag("param", new XMLAttribute().add("name", p.getShortName()));
                    xout.text(gene.getParameter(p.getName()).toString());
                    xout.endTag();
                }
                xout.endTag();
            }
            xout.endTag();
            xout.flush();
            return super.execute(mapping, form, request, response);
        }
        
        if(format.equals("tab"))
        {
            geneList = retrieveYeastGeneList(selectedORFParameter, -1, 0, sqlParamList, sortParamName);                       

            // output tab-separated data
            response.setContentType("application/octet-stream");
            String tabFile = "orfparam.tab"; 
            response.addHeader("Content-disposition", "attachment; filename=" + SQLUtil.doubleQuote(tabFile));
            ServletOutputStream out = response.getOutputStream();
            out.println("ORF\tstandard_name\t" + SQLUtil.separatedList(paramShortNames, "\t", SQLUtil.QuotationType.none));            
            for(YeastGene gene : geneList)
            {
                Vector<String> line = new Vector<String>();
                line.add(gene.getOrf());
                line.add(gene.getStandardName());
                for(MorphParameter p : selectedORFParameter)
                    line.add(gene.getParameter(p.getName()).toString());
                out.println(SQLUtil.separatedList(line, "\t", SQLUtil.QuotationType.none));
        
            }
            out.flush();
            return super.execute(mapping, form, request, response);            
        }
        
        if(numItem == null)
            numItem = 1;
        int numRows = 50;
        int maxPage = numItem / numRows + 1; 
        if(input.getPage() > maxPage)
            input.setPage(0);
        int offset = numRows * input.getPage();                 
        geneList = retrieveYeastGeneList(selectedORFParameter, numRows, offset, sqlParamList, sortParamName);
        
        if(selectedORFParameter.size() == 1 || input.getSortspec() != -1)
        {
            if(targetParam == null)
                targetParam = selectedORFParameter.get(0);
            request.setAttribute("targetParam", targetParam);
            double begin = 0, end=0;        
            if(!geneList.isEmpty())
            {
                begin = Double.parseDouble(geneList.get(0).getParameter(targetParam.getName()).toString());
                end = Double.parseDouble(geneList.get(geneList.size()-1).getParameter(targetParam.getName()).toString());
            }
            Range range = new Range(begin, end);
            request.setAttribute("range", range);
        }
        
        request.setAttribute("geneList", geneList);
        request.setAttribute("paramList", selectedORFParameter);
        request.setAttribute("pageStatus", new PageStatus(input.getPage()+1, maxPage));
        request.setAttribute("paramIDs", paramIDs);
        request.setAttribute("sortspec", input.getSortspec());
        
        return mapping.findForward("success");
    }
    
    private LinkedList<YeastGene> retrieveYeastGeneList(List<MorphParameter> selectedORFParameter, int numRows, int offset, Vector<String> sqlParamList, String sortParamName) throws SQLException
    {
        // retrieve datasheet
        Table datasheet = ConnectionServer.retrieveTable(
                numRows!=-1 ? // -1 なら全行取得する
                        "select strainname as \"ORF\",primaryname, aliasname, annotation, $1 from $2 left join $5 on strainname = systematicname order by $6 limit $3 offset $4" :
                        "select strainname as \"ORF\",primaryname, aliasname, annotation, $1 from $2 left join $5 on strainname = systematicname order by $6",                    
                SQLUtil.commaSeparatedList(sqlParamList, SQLUtil.QuotationType.none),               
                SCMDConfiguration.getProperty("DB_ANALYSISDATA", "analysisdata_20050131"),
                numRows,
                offset,
                SCMDConfiguration.getProperty("DB_GENENAME"),
                SQLUtil.doubleQuote(sortParamName)
        );
        
        ColLabelIndex colIndex = new ColLabelIndex(datasheet);        
        LinkedList<YeastGene> geneList = new LinkedList<YeastGene>();
        for(int i=1; i<datasheet.getRowSize(); i++)
        {
            YeastGene gene = new YeastGene();
            gene.setOrf(colIndex.get(i, "ORF").toString());
            gene.setStandardName(colIndex.get(i, "primaryname").toString());
            gene.setAnnotation(colIndex.get(i, "annotation").toString());
            gene.setAlias(colIndex.get(i, "aliasname").toString());
            for(MorphParameter p : selectedORFParameter)
            {
                String param = p.getName();
                BigDecimal value = new BigDecimal(colIndex.get(i, param).toString(), new MathContext(3));
                gene.setParameter(param, value.toEngineeringString());
            }
            geneList.add(gene);
        }
        return geneList;
    }
    
    
}
