//--------------------------------------
// SCMDServer
// 
// TablePrinter.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.design;

import java.io.Writer;
import java.util.Properties;

import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.table.DefaultVisitor;
import lab.cb.scmd.web.table.LeafTableElement;
import lab.cb.scmd.web.table.SelfClosedTag;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.Tag;

/**
 * @author leo
 *  
 */
public class TablePrinter extends DefaultVisitor
{
    XMLOutputter _xmlout = null;

    /**
     *  
     */
    public TablePrinter(Writer writer)
    {
        super();
        _xmlout = new XMLOutputter(writer);
    }
    public TablePrinter()
    {
        super();
        _xmlout = new XMLOutputter();
    }

    public void printTable(Table table)
    {
        _xmlout.omitHeader();
        //_xmlout.setContentFilter(new ThroughFilter());
        table.accept(this);
        try
        {
            _xmlout.endOutput();
        }
        catch(InvalidXMLException e)
        {
            e.what();
        }
    }
    
     
    public void visit(Table table) {
        try
        {
            _xmlout.startTag("table", new XMLAttribute(table.getProperties()));
            for (int row = 0; row < table.getRowSize(); row++)
            {
                _xmlout.startTag("tr");
                for (int col = 0; col < table.getColSize(); col++)
                {
                    TableElement elem = (TableElement) table.get(row, col);
                    Properties cellProperty = elem.getCellProperty();
                    _xmlout.startTag("td", new XMLAttribute(cellProperty));
                    elem.accept(this);
                    _xmlout.closeTag();
                    // TODO rowspanへの対応
                    String colSpanStr = cellProperty.getProperty("colspan");
                    if(colSpanStr != null)
                    {
                        int numSkipCol = Integer.parseInt(colSpanStr);
                        col += numSkipCol - 1;
                    }
                }
                _xmlout.closeTag();
            }
            _xmlout.closeTag();
      ;  }
        catch (InvalidXMLException e)
        {
            e.what();
        }
    }


    public void visit(SelfClosedTag tag) {
        _xmlout.selfCloseTag(tag.getTagName(), new XMLAttribute(tag.getProperties()));
    }
    

    public void visit(Tag tag) {
        try
        {
            _xmlout.startTag(tag.getTagName(), new XMLAttribute(tag.getProperties()));
            tag.getBodyContent().accept(this);
            _xmlout.closeTag();
        }
        catch(InvalidXMLException e) {
            e.what();
        }
    }
    
    public void visit(LeafTableElement element)
    {
      try
      {
          _xmlout.textContent(element.toString());
      }
      catch(InvalidXMLException e) {
          e.what();
      }

    }
}

//--------------------------------------
// $Log: TablePrinter.java,v $
// Revision 1.6  2004/08/25 09:05:43  leo
// colspan対応
//
// Revision 1.5  2004/08/24 07:36:15  leo
// AbstractTableを廃棄
//
// Revision 1.4  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.3  2004/08/09 03:39:04  leo
// TablePrinterクラスの簡単化
//
// Revision 1.2  2004/08/07 12:30:36  leo
// Visitorの動作確認
//
// Revision 1.1  2004/08/07 11:54:45  leo
// Layout用のTableを作りました
//
//--------------------------------------
