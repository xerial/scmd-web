//--------------------------------------
// SCMDServer
// 
// TableWriter.java 
// Since: 2004/08/01
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.util;

import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

/**
 *　テーブルタグの生成
 * @author leo
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy$
 */
public class TableWriter
{
    /**
     *  
     */
    public TableWriter()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void writeAsHTML(Writer out, BasicTable table) {
        try
        {
            // print col label
            Vector colLabelList = table.getColLabelList();
            XMLOutputter xmlout = new XMLOutputter(out);
            xmlout.startTag("tr");
            xmlout.selfCloseTag("td");
            for (Iterator it = colLabelList.iterator(); it.hasNext();)
            {
                String label = (String) it.next();
                xmlout.startTag("td", new XMLAttribute("class", "sheetlabel"));
                xmlout.textContent(label);
                xmlout.closeTag();
            }
            xmlout.closeTag();

            XMLAttribute labelStyle = new XMLAttribute("class", "tablelabel");
            XMLAttribute dataStyle = new XMLAttribute("class", "datasheet");
            
            if(table.hasRowLabel())
            {
                Vector rowLabel = table.getRowLabelList();
                for (Iterator it = rowLabel.iterator(); it.hasNext();)
                {
                    xmlout.startTag("tr");
                    String label = (String) it.next();
                    xmlout.element("td", labelStyle, label);
                    for (TableIterator ti = table.getHorisontalIterator(label); ti.hasNext();)
                    {
                        Cell cell = ti.nextCell();
                        xmlout.element("td", dataStyle, cell.toString());
                    }
                    xmlout.closeTag();
                }
            }
            else
            {
                for (int i = 0; i < table.getRowSize(); i++)
                {
                    xmlout.startTag("tr");
                    for (TableIterator ti = table.getHorisontalIterator(i); ti.hasNext();)
                    {
                        Cell cell = ti.nextCell();
                        xmlout.element("td", dataStyle, cell.toString());
                    }
                    xmlout.closeTag();
                }
            }
        }
        catch (InvalidXMLException e)
        {
            e.what();
        }
    }

}

//--------------------------------------
// $Log: TableWriter.java,v $
// Revision 1.3  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.2  2004/08/01 08:55:51  leo
// Statsページを作成
//
// Revision 1.1  2004/08/01 08:20:12  leo
// BasicTableをHTMLに変換するツールを書き始めました
//
//--------------------------------------
