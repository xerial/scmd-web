//--------------------------------------
// SCMDServer
//
// PageMover.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.taglib;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import lab.cb.scmd.web.util.BeanUtil;
import lab.cb.scmd.web.util.CGIUtil;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PageMover extends TagSupport {

    int currentPage = 1; 
    int maxPage = 1;
    String page = ".";
    String target = "";
    
    String name = null;
    String property = null;
    /**
     * 
     */
    public PageMover() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException{
        JspWriter out = pageContext.getOut();
        
        Map map = BeanUtil.getMap(pageContext, name, property);
        
        
        if(maxPage==1)
            return(SKIP_BODY);
        int pageRange = 10;
        int rangeBegin = currentPage - pageRange;
        int rangeEnd = currentPage + pageRange;
        
        if(rangeBegin <= 0)
        {
            rangeEnd -= rangeBegin;
            rangeBegin = 1;
        }
        if(rangeEnd > maxPage)
            rangeEnd = maxPage;
        try
        {
            out.println("<table><tr class=\"small\">");
            out.println("<td> page: </td>");
            if(rangeBegin > 1)
            {
                out.println("<td class=\"button\"> [<a href=\"" + getEncodedURL(page, 1, map) +  "\"> top </a>]</td>");
            }
            if(currentPage > 1)
            {
                out.println("<td class=\"button\"> [<a href=\"" + getEncodedURL(page, currentPage-1,map) +  "\"> prev </a>]</td>");
                if(rangeBegin>1)
                    out.println("<td class=\"plain\"><a href=\""  + getEncodedURL(page, rangeBegin-1, map) + "\"> ... </a></td>");
            }
            for(int p = rangeBegin; p<=rangeEnd; p++)
            {
                if(p != currentPage)
                {
                    out.println("<td><a href=\"" + getEncodedURL(page, p, map) + "\">" + p + "</a></td>");                   
                }
                else
                {
                    out.println("<td class=\"notice\">" + p + "</td>");                                       
                }
            }
            if(rangeEnd < maxPage)
            {
                out.println("<td class=\"plain\"><a href=\""  + getEncodedURL(page, rangeEnd+1, map) + "\"> ... </a></td>");
            }
            if(currentPage < maxPage)
            {
                out.println("<td class=\"button\"> [<a href=\"" + getEncodedURL(page, currentPage+1, map) +  "\"> next </a>]</td>");
                out.println("<td class=\"button\"> [<a href=\"" + getEncodedURL(page, maxPage, map) +  "\"> last </a>]</td>");
            }
            out.println("</tr></table>");
        }
        catch(IOException e)
        {
            throw new JspException(e);
        }
        
        return (SKIP_BODY);
    }
    
    String getEncodedURL(String baseURL, int nextpage, Map argMap)
    {
        String actionURL = baseURL + "?page=" + nextpage + "&" + CGIUtil.getCGIArgument(argMap);
        if(!target.equals(""))
        {
            actionURL += "&target=" + target; 
        }
        actionURL = ((HttpServletResponse) pageContext.getResponse()).encodeURL(actionURL);
        return actionURL;
    }
    
    
    /**
     * @return Returns the currentPage.
     */
    public int getCurrentPage() {
        return currentPage;
    }
    /**
     * @param currentPage The currentPage to set.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    /**
     * @return Returns the maxPage.
     */
    public int getMaxPage() {
        return maxPage;
    }
    /**
     * @param maxPage The maxPage to set.
     */
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
    
    /**
     * @return Returns the page.
     */
    public String getPage() {
        return page;
    }
    /**
     * @param page The page to set.
     */
    public void setPage(String page) {
        this.page = page;
    }
    
    /**
     * @return Returns the target.
     */
    public String getTarget() {
        return target;
    }
    /**
     * @param target The target to set.
     */
    public void setTarget(String target) {
        this.target = target;
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
}


//--------------------------------------
// $Log: PageMover.java,v $
// Revision 1.7  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.6  2004/08/31 04:46:21  leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
// Revision 1.5  2004/08/24 07:35:56  leo
// prevボタンを追加
//
// Revision 1.4  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
// Revision 1.3  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.2  2004/07/26 14:20:25  leo
// *** empty log message ***
//
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page用のクラスを追加
//
//--------------------------------------
