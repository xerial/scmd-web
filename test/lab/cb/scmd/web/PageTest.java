//--------------------------------------
// SCMDWeb Project
//
// PageTest.java
// Since: 2005/01/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web;


import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class PageTest extends TestCase
{
    String serverURL = "http://localhost:8080/scmd-server";
    
    
    protected void setUp() throws Exception
    {
        //HTMLParserFactory.setParserWarningsEnabled(true);
    }
    /** page�����݂��邩�e�X�g. JSP�y�[�W��precompile���\�b�h�Ƃ��Ă��g����
     * @throws Exception �y�[�W�𐳏�ɓǂݍ��߂Ȃ������ꍇ�B
     */
    public void testPagesExist() throws Exception 
    {
        String[] pagePath = {
                "", "about.jsp", "ViewPhoto.do", "ViewStats.do",
                "publication.jsp", "SelectShape.do", "ViewORFList.do",
                "ViewDataSheet.do", "ParameterHelp.do", "sitemap.jsp",
                "ViewSelection.do", "CustomizeView.do", "ORFTeardrop.do",
                "ViewORFParameter.do", "View2DPlot.do", "ViewGroupByTearDrop.do",
                "ViewGroupBySheet.do"
        };
        
        for(String path : pagePath)
        {
            String url = serverURL + "/" + path;
            WebResponse response = new WebConversation().getResponse(url);
            assertTrue(url, response.isHTML());
        }
    }
}




