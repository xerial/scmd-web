//--------------------------------------
// SCMDServer
// 
// PageButton.java 
// Since: 2004/08/31
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.taglib;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.util.BeanUtil;
import lab.cb.scmd.web.util.CGIUtil;

/**
 * @author leo <lab.cb.scmd-base:pagebutton name="instance" property="map"
 *         link="http://..." page="1" maxPage="10"/>
 */
public class PageButton extends TagSupport
{
    String name;
    String property;
    String link;

    int    page;
    int    maxPage;

    int[]  pageList = new int[4];

    /**
     *  
     */
    public PageButton()
    {
        super();
    }

    public int doStartTag() throws JspException {
        Map map = BeanUtil.getMap(pageContext, name, property);

        int current = page - 1;

        if(maxPage < page) { return SKIP_BODY; }
        pageList[0] = current - 10;
        pageList[1] = current - 1;
        pageList[2] = current + 1;
        pageList[3] = current + 10;

        for (int i = 0; i < pageList.length; i++)
        {
            pageList[i] = adjustPage(pageList[i], maxPage);
        }

        XMLOutputter out = new XMLOutputter(pageContext.getOut());
        out.omitHeader();
        try
        {
            out.startTag("span", new XMLAttribute("class", "button"));
            printButton(out, 0, map);
            out.textContent(" " + page + " / " + maxPage + " ");          
            printButton(out, 2, map);
            out.closeTag();
            out.endOutput();
        }
        catch (InvalidXMLException e)
        {
            e.what();
        }
        
        return SKIP_BODY;
    }

    public void printButton(XMLOutputter out, int index, Map map) throws InvalidXMLException {
        final String[] label = new String[] { "prev10", "prev", "next", "next10"};

        for (int i = 0; i < 2; i++)
        {
            out.textContent("[ ");
            map.put("page", Integer.toString(pageList[index + i]));
            String href = link;
            if(href.indexOf("?") >=0)
                href += CGIUtil.getCGIArgument(map); 
            else
                href += "?" + CGIUtil.getCGIArgument(map);
            href = ((HttpServletResponse) pageContext.getResponse()).encodeURL(href);
            out.startTag("a", new XMLAttribute("href", href));
            out.textContent(label[index + i]);
            out.closeTag();
            out.textContent(" ]");
        }
    }

    public int adjustPage(int inputPage, int maxPage) {
        if(maxPage <= 0)
            maxPage = 1;
        while (inputPage < 0)
        {
            inputPage += maxPage;
        }
        inputPage %= maxPage;
        inputPage++;
        return inputPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setMaxPage(String max) {
        this.maxPage = Integer.parseInt(max);
    }

    public void setPage(String page) {
        this.page = Integer.parseInt(page);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

//--------------------------------------
// $Log: PageButton.java,v $
// Revision 1.4  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.3  2004/09/03 09:34:46  leo
// *** empty log message ***
//
// Revision 1.2  2004/08/31 08:44:01  leo
// Group By Sheet完成
//
// Revision 1.1  2004/08/31 04:46:21  leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
//--------------------------------------
