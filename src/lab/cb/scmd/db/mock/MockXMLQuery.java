//--------------------------------------
// SCMDServer
// 
// MockXMLQuery.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

import java.io.File;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Collection;

import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 * @author leo
 *  
 */
public class MockXMLQuery extends MockQueryAPI implements XMLQuery
{
    boolean _testMode = false;

    /**
     *  
     */
    public MockXMLQuery()
    {}

    public MockXMLQuery(boolean testMode)
    {
        _testMode = testMode;
    }

    String getPath(String xmlSpecFile) {
        if(_testMode)
        {
            String userDir = System.getProperty("user.dir");
            int index = userDir.indexOf("SCMDServer");
            if(index == -1)
                return xmlSpecFile;
            File scmdServerPath = new File(userDir.substring(0, index));
            File sampleXMLFolder = new File(scmdServerPath, "SCMDServer/web/lab.cb.scmd-server/xml/sample");
            return sampleXMLFolder.getAbsolutePath() + File.separator + xmlSpecFile;
        }
        else
            return SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT) + "lab.cb.scmd-server" + File.separator + "xml"
                    + File.separator + "sample" + File.separator + xmlSpecFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.db.common.XMLQuery#getAvailableORF(lab.cb.scmd.util.xml.XMLOutputter)
     */
    public void getAvailableORF(OutputStream out, AbstractMap groupToPageMap) {
    	redirectXMLFile(out, "orflist.xml");
    }

	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.XMLQuery#getORFInfo(lab.cb.scmd.util.xml.XMLOutputter, int)
	 */
	public void getORFInfo(OutputStream out, String orf, int flag) {

		if((flag | ORFINFO_GENENAME) == ORFINFO_GENENAME) // ORFINFO_GENENAMEのみがセットされている
		{
			redirectXMLFile(out, "genename.xml");
	        return;
		}
	}
	
	
    public void getGroupBySheet(OutputStream out, String orf, int groupType, int page) {
        redirectXMLFile(out, "group.xml");
    }
    
	void redirectXMLFile(OutputStream out, String xmlFile)
	{
	    XMLOutputter xmlout = new XMLOutputter(out);
	    //xmlout.omitHeader();
        XMLRedirector redirector = new XMLRedirector(xmlout);
        try
        {
            redirector.redirect(getPath(xmlFile));
        }
        catch (SCMDException e)
        {
            e.what();
        }
        xmlout.closeStream();
	}

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getCellCoordinates(lab.cb.scmd.util.xml.XMLOutputter, java.lang.String, java.lang.String)
     * DBへ接続するには、CurrentView.java の loadCellList()内で
     * XMLQuery query = new MockXMLQuery();
     * を
     * XMLQuery query = new SCMDXMLQuery();
     * に変更すればよい。
     */
    public void getCellCoordinates(OutputStream out, String orf, int photoID) {
        redirectXMLFile(out, "cellbox.xml");
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getCellCoordinates(java.io.OutputStream, java.lang.String, int, int)
     */
    public void getCellCoordinates(OutputStream out, String orf, int currentPage, int cellID) throws SCMDException {
        redirectXMLFile(out, "cellbox_single.xml");
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getORFInfo(java.io.OutputStream, java.util.List, int)
     */
    public void getORFInfo(OutputStream out, Collection orfList, int flag) {
        // TODO Auto-generated method stub
        redirectXMLFile(out, "orflist.xml");
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getSearchResult(java.io.OutputStream, java.lang.String, int, int)
     */
    public void getSearchResult(OutputStream out, String keyword, int currentPage, int numElementInAPage) {
        // TODO Auto-generated method stub
        
    }

}

//--------------------------------------
// $Log: MockXMLQuery.java,v $
// Revision 1.13  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.12  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.11  2004/08/14 10:57:24  leo
// Mockの更新
//
// Revision 1.10  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQueryのinstanceに
//
// Revision 1.9  2004/08/11 14:02:32  leo
// Group by のシート作成
//
// Revision 1.8  2004/08/11 05:47:13  leo
// XMLOutputterにクエリの結果を出力するのではなく、ただのOutputStreamにしました
//
// Revision 1.7  2004/08/09 03:39:23  sesejun
// ORFListをDBから生成
//
// Revision 1.6  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.5  2004/07/25 11:26:23  leo
// 問い合わせを追加
//
// Revision 1.4  2004/07/24 14:35:31  leo
// *** empty log message ***
//
// Revision 1.3  2004/07/22 14:42:39  leo
// testの仕方を若干変更
//
// Revision 1.2  2004/07/22 07:11:15  leo
// testの書き方を変更
//
// Revision 1.1 2004/07/21 05:47:16 leo
// XML関係のモジュールを追加
//
//--------------------------------------
