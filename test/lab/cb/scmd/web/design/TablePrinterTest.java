//--------------------------------------
// SCMDServer
// 
// TablePrinterTest.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.design;

import java.util.TreeMap;


import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLUtil;
import lab.cb.scmd.web.table.Height;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.Link;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.StringElement;
import lab.cb.scmd.web.table.Style;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableElementList;
import lab.cb.scmd.web.table.Width;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.JavaScriptDecollator;
import lab.cb.scmd.web.table.decollation.NumberFormatDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;
import lab.cb.scmd.web.table.decollation.TagDecollator;
import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class TablePrinterTest extends TestCase
{
    public void testVisitTable() throws InvalidXMLException
    {
        Table table = new Table();
        table.setProperty("border", "1");
        //table.setProperty("height", "800");
        ImageElement image = new ImageElement("/photo.jpg");
        image.setWidth(50);
        image.setHeight(100);
        
        
        Table dataTable = new Table();
        RowLabelIndex rowLabelIndex = new RowLabelIndex(dataTable);
        dataTable.addRow(new Object[] {new Style(new StringElement("bud size"), "label"), new StringElement("small")});
        dataTable.addRow(new Object[] {"roundness", new Double(1.3)});
        dataTable.addRow(new Object[] {"bud growth direction", "30.3434"});
        dataTable.addRow(new Object[] {"bud neck position", "234.234234"});
        TableElementList list = new TableElementList();
        list.add("<<");
        list.add(new Link("newpage.jsp", "page"));
        list.add(">>");
        String longMessage = "hello world, asdfasdfasdfkjklj  lasf  lare ger lkasdf adfadfafjklasdf kjfdas fdasfaj  adfasdfad dkjfa ";
        dataTable.addRow(new Object[] { list, longMessage });
        
        //int r = dataTable.getRowIndex("roundness");
        
        dataTable.decollateCol(1, new StyleDecollator("data"));
        dataTable.decollateCol(0, new AttributeDecollator("width", "50"));
        rowLabelIndex.decollateRow("roundness", new StyleDecollator("sorted"));
        dataTable.decollateCol(1, new NumberFormatDecollator(3));
        
        
        TagDecollator fontDecollator = new TagDecollator("font");
        fontDecollator.setAttribute("name", "arial");
        //dataTable.decollateCol(0, fontDecollator);

        TagDecollator spanDecollator = new TagDecollator("span");
        spanDecollator.setAttribute("class", "bold");
        //dataTable.decollateCol(0, spanDecollator);

        table.addRow(new TableElement[] {new Height(30, new Width(56, new Style(image, "image"))), new Style(dataTable, "subtable")});
        
        Table frameTable = new Table(2, 1);
        frameTable.set(0, 0, table);
        
        TreeMap map = new TreeMap();
        map.put("orf", "yor202w");
        map.put("imagenumber", "3");
        ImageElement image2 = new ImageElement("button.jpg", map);
        image2.setProperty("align", "center");
        TagDecollator tagDecollator = new TagDecollator("span");
        tagDecollator.setAttribute("class", "sampleimage");
        
        frameTable.set(1, 0, new Style(new Link("newpage.jsp", new Width(134, image2)), "link"));
        frameTable.decollate(1, 0, tagDecollator);
        JavaScriptDecollator javasciptDecollator = new JavaScriptDecollator("onClick", "'item0'");
        frameTable.decollate(1, 0, javasciptDecollator);
        
        image2.setProperty("onClick", XMLUtil.createCDATA("'item13'"));

        TablePrinter printer = new TablePrinter();
        printer.printTable(frameTable);
        System.out.flush();
    }
}


//--------------------------------------
// $Log: TablePrinterTest.java,v $
// Revision 1.9  2004/12/10 08:57:00  leo
// �t�@�C���̈ړ��B�ǉ�
//
// Revision 1.8  2004/08/26 04:29:58  leo
// cdata�̕\����XMLUtil�ɔC����
//
// Revision 1.7  2004/08/24 04:44:37  leo
// �V����table�ɑΉ�
//
// Revision 1.6  2004/08/09 12:26:42  leo
// Comment��ǉ�
//
// Revision 1.5  2004/08/09 09:32:00  leo
// EmptyCell��toString() ��ݒ�
//
// Revision 1.4  2004/08/09 09:17:11  leo
// Table�͈͎̔w���TableRange���g���悤�ɂ���
//
// Revision 1.3  2004/08/09 03:39:04  leo
// TablePrinter�N���X�̊ȒP��
//
// Revision 1.2  2004/08/07 12:30:36  leo
// Visitor�̓���m�F
//
// Revision 1.1  2004/08/07 11:54:45  leo
// Layout�p��Table�����܂���
//
//--------------------------------------