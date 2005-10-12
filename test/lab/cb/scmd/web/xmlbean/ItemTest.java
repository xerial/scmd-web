//--------------------------------------
// SCMDWeb Project
//
// ItemTest.java
// Since: 2005/10/12
//
// $URL$ 
// $Author$
//--------------------------------------

package lab.cb.scmd.web.xmlbean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.xerial.util.XMLParserException;
import org.xerial.util.xml.bean.XMLBeanException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import junit.framework.TestCase;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class ItemTest extends TestCase
{
    String[] orfName = { "YDL025C", "YKL183C-A", "YJL156W-A", "YKL032C", "YKL032C", "YKL032C", "YNL325C", "YNL325C",
            "YJR150C", "YJR150C", "YJR150C", "YPR097W", "YER048C", "YER048C", "YNL098C", "YNL098C", "YNL098C",
            "YNL098C", "YNL098C", "YNL098C", "YJL166W", "YJL166W", "YJL166W", "YDR006C", "YDR006C", "YCR014C",
            "YCR014C", "YCR014C", "YOR291W", "YGL215W", "YGL215W", "YMR027W", "YBR111C", "YBR111C", "YBR111C",
            "YMR046C", "YNL284C-A", "YJL200C", "YJR030C", "YLR159W", "YFL066C", "YGR270C-A", "YHR072W-A", "YHR072W-A",
            "YBR023C", "YBR023C", "YBR023C", "YBR023C", "YBR023C", "YBR023C" };
    
    String[] color = {"skyblue", "red", "green", "yellow", "purple", "navyblue", "gray", "black"};
    
    public void testRead() 
        throws InvalidXMLException, XMLParserException, XMLBeanException, IOException, org.xerial.util.xml.InvalidXMLException
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        XMLOutputter xout = new XMLOutputter(buf);
        int count = 0;
        
        // ランダムに選ばれた50個のORFからなるXML文書を作成
        xout.startTag("selection"); // <selection> タグを生成
        for(String orf : orfName)
        {
            // <item orf="YAL002W" color="skyblue"/> という形式のXML文書を生成
            xout.selfCloseTag("item", new XMLAttribute().add("orf", orf).add("color", color[count++ % color.length]));
        }
        xout.closeTag();
        xout.endOutput();
        
        ByteArrayInputStream bufIn = new ByteArrayInputStream(buf.toByteArray());   // 生成されたXML文書の入力
        Selection readData = XMLBeanUtil.newInstance(Selection.class, bufIn);   // XML文書のデータをSelectionクラスにバインドする
        
        // orfが正しく読み込まれていることを確認
        int index = 0;
        assertEquals(orfName.length, readData.getItem().length);    // 50個全てが読み込まれているか？
        for(String orf : orfName)
        {
            Item item = readData.getItem()[index];
            assertNotNull(item);
            assertEquals(orf, item.getOrf());    // ORF名が一致しているか？
            assertEquals(color[index % color.length], item.getColor());
            index++;
        }
    }
}




