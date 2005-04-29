//--------------------------------------
//SCMDServer
//
//MockXMLQuery.java 
//Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.connect;

//import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
//import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
//import java.util.Iterator;
//import java.util.Set;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.common.SCMDConfiguration;
//import lab.cb.scmd.web.exception.InvalidSQLException;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;


public class SCMDXMLQuery implements XMLQuery {
	private SCMDDBConnect _connection = null;
    //private ConnectionHolder _connectionHolder = new ConnectionHolder();
    
	private String		_wildtype 	= "wildtype";
	private String		_mutant		= "mutant";
	private	String[]	_wildtypeORFs = {"YOR202W"};

	public SCMDXMLQuery() {
	    _connection = new SCMDDBConnect();
	}

	
	
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.XMLQuery#getAvailableORF(lab.cb.scmd.util.xml.XMLOutputter, java.util.AbstractMap, int)
	 */
	public void getAvailableORF(OutputStream out, AbstractMap groupToPageMap) {
	    XMLOutputter xmlout = new XMLOutputter(out);
        ORFList orfList = new ORFList();

//		註: groupToPagemap.keySet()が、
//		"wildtype", "mutant"の順番を保持できないかもしれない
//		(逆の順番を返すかもしれない)ため、開発中のコードを放置し
//      決めうちに移行
//      by sesejun on Sun Aug  8 15:35:20
//        
//		Set groupNames = groupToPageMap.keySet();
//		Iterator it = groupNames.iterator();
//		while( it.hasNext() ) {
//			String groupName = (String)it.next();
//			if( groupName.equals(_wildtype) ) {
//				
//			} else if( groupName.equals(_mutant) ) {
//				
//			}
//				
//		}
        String	 group1Name = _wildtype;
        String[] group1orf = _wildtypeORFs;
        PageStatus group1PageStatus = (PageStatus)groupToPageMap.get(group1Name);

        String	 group2Name	= _mutant;
        String[] group2orf	= {};
        PageStatus group2PageStatus = (PageStatus)groupToPageMap.get(group2Name);
//        String[] group2orf = {"YAL001C","YAL005C","YAL007C","YAL008W","YAL010W"};
        
        //TODO gene nameや、aliasを与えられた際に
        //ORF名に変換して検索をする
        orfList.addGroup(group1Name, group1orf);	// 第一引数は、グループ名
        orfList.addGroup(group2Name, group2orf);	// 第二引数は、gene nameを配列で入力
        orfList.setMaxElementsInAPage(group1Name, group1PageStatus.getMaxElementsInAPage());
        orfList.setMaxElementsInAPage(group2Name, group2PageStatus.getMaxElementsInAPage());
        orfList.setCurrentPage(group1Name, group1PageStatus.getCurrentPage());
        orfList.setCurrentPage(group2Name, group2PageStatus.getCurrentPage()); 

        //パラメータの値の表示は必要なし？
        //orfList.addParameter("roundness");
        //orfList.addParameter("C11-1_A");
        
        try {
            orfList.setData(_connection);
        } catch (SCMDException e) {
        	System.err.println("Data Reading Error in ORFList");
            e.printStackTrace();
        }
        
        try {
            orfList.outputXML(xmlout);
            xmlout.endOutput();
            xmlout.closeStream();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.XMLQuery#getORFInfo(lab.cb.scmd.util.xml.XMLOutputter, java.lang.String, int)
	 */
	public void getORFInfo(OutputStream out, String orf, int flag) {
		// TODO Auto-generated method stub
	    Vector orfList = new Vector();
	    orfList.add(orf);
	    getORFInfo(out, orfList, flag);
	}

	
//    /* (non-Javadoc) 
//     * @see lab.cb.scmd.db.common.XMLQuery#getGroupBySheet(java.io.OutputStream, java.lang.String, int, int)
//     */
//    public void getGroupBySheet(OutputStream out, String strain, int groupType, int page) {
//        // TODO Auto-generated method stub
//    	strain = strain.toUpperCase();
//        XMLOutputter xmlout = new XMLOutputter(out);
//        GroupBySheetQuery groupBySheet = new GroupBySheetQuery(strain, groupType);
//        groupBySheet.setCurrentPage(page);
//        try
//        {
//            groupBySheet.setData((SCMDDBConnect) getConnection());
//            groupBySheet.outputXML(xmlout);
//            xmlout.endOutput();
//            xmlout.closeStream();
//        }
//        catch (InvalidXMLException e)
//        {
//            e.printStackTrace();
//        } catch (InvalidSQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DBConnectException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//
	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.XMLQuery#getCellCoordinates(lab.cb.scmd.util.xml.XMLOutputter, java.lang.String, int)
	 */
	public void getCellCoordinates(OutputStream out, String orf_in, int currentPage) throws SCMDException {
	    XMLOutputter xmlout = new XMLOutputter(out); 
		String orf = orf_in.toUpperCase();
        CellBox cellBox = new CellBox(orf);
        cellBox.setCurrentPage(currentPage);
        
        cellBox.setData(_connection);
        
        try
        {
            cellBox.outputXML(xmlout);
            xmlout.endOutput();
            xmlout.closeStream();
        }
        catch (InvalidXMLException e)
        {
            e.printStackTrace();
        }
	}

	/* (non-Javadoc)
	 * @see lab.cb.scmd.db.common.XMLQuery#getCellCoordinates(java.io.OutputStream, java.lang.String, int, int)
	 */
	public void getCellCoordinates(OutputStream out, String orf_in, int currentPage, int cellID) throws SCMDException {
	    XMLOutputter xmlout = new XMLOutputter(out); 
		String orf = orf_in.toUpperCase();
        CellBox cellBox = new CellBox(orf);
        cellBox.setCurrentPage(currentPage);
        cellBox.setCellID(cellID);
        
        cellBox.setData(_connection);
        
        try
        {
            cellBox.outputXML(xmlout);
            xmlout.endOutput();
            xmlout.closeStream();
        }
        catch (InvalidXMLException e)
        {
            e.printStackTrace();
        }
	}

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getORFInfo(java.io.OutputStream, java.util.List, int)
     */
    public void getORFInfo(OutputStream out, Collection inputOrfList, int flag) {
	    XMLOutputter xmlout = new XMLOutputter(out);
        ORFList orfList = new ORFList();

//		註: groupToPagemap.keySet()が、
//		"wildtype", "mutant"の順番を保持できないかもしれない
//		(逆の順番を返すかもしれない)ため、開発中のコードを放置し
//      決めうちに移行
//      by sesejun on Sun Aug  8 15:35:20
//        
//		Set groupNames = groupToPageMap.keySet();
//		Iterator it = groupNames.iterator();
//		while( it.hasNext() ) {
//			String groupName = (String)it.next();
//			if( groupName.equals(_wildtype) ) {
//				
//			} else if( groupName.equals(_mutant) ) {
//				
//			}
//				
//		}
        /*
        String	 group1Name = _wildtype;
        String[] group1orf = _wildtypeORFs;
        PageStatus group1PageStatus = new PageStatus(1, 1, 1);
        */

        String	 group2Name	= _mutant;
        
        String[] group2orf	= new String[inputOrfList.size()];
        int i=0;
        for (Iterator it = inputOrfList.iterator(); it.hasNext();)
        {
            String orf = (String) it.next();
            group2orf[i++] = orf.toUpperCase();
        }
        PageStatus group2PageStatus = new PageStatus(1, 1, inputOrfList.size());
//        String[] group2orf = {"YAL001C","YAL005C","YAL007C","YAL008W","YAL010W"};
        
        //TODO gene nameや、aliasを与えられた際に
        //ORF名に変換して検索をする
        //orfList.addGroup(group1Name, group1orf);	// 第一引数は、グループ名
        orfList.addGroup(group2Name, group2orf);	// 第二引数は、gene nameを配列で入力
        //orfList.setMaxElementsInAPage(group1Name, group1PageStatus.getMaxElementsInAPage());
        orfList.setMaxElementsInAPage(group2Name, group2PageStatus.getMaxElementsInAPage());
        //orfList.setCurrentPage(group1Name, group1PageStatus.getCurrentPage());
        orfList.setCurrentPage(group2Name, group2PageStatus.getCurrentPage()); 

        //パラメータの値の表示は必要なし？
        //orfList.addParameter("roundness");
        //orfList.addParameter("C11-1_A");
        
        try {
            orfList.setData(_connection);
        } catch (SCMDException e) {
        	System.err.println("Data Reading Error in ORFList");
            e.printStackTrace();
        }
        
        try {
            orfList.outputXML(xmlout);
            xmlout.endOutput();
            xmlout.closeStream();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.XMLQuery#getSearchResult(java.io.OutputStream, java.lang.String, int, int)
     */
    public void getSearchResult(OutputStream out, String keywordstr, int currentPage, int numElementInAPage) {

	    XMLOutputter xmlout = new XMLOutputter(out);
	    ORFList orfList = new ORFList();
        String summaryTable = SCMDConfiguration.getProperty("DB_SUMMARY");
        String genenameTable = SCMDConfiguration.getProperty("DB_GENENAME");
    
        String[] keyword = keywordstr.split("[ \\,/]+");
        String whereClause = "WHERE ";
        for(int i = 0; i < keyword.length; i++ ) {
            if( i != 0 )
                whereClause += " AND ";
            String curkey = keyword[i];
            if( curkey.matches("^GO:[0-9]+") ) {
                while(curkey.length() < 10) {
                    curkey = "GO:0" + curkey.substring(3);
                }
                    
                whereClause += "systematicname in " +
                        "( select distinct strainname as systematicname " +
                        "from goassociation where goid in " +
                        "( select cid as goid from term_graph " +
                        "where pid = '" + curkey + "'))";
            } else {
                String exp = "'" + keyword[i] + "%'";
                String exp_annot = "'%" + keyword[i] + "%'";
                whereClause += "(systematicname ILIKE " + exp 
                    + " OR primaryname ILIKE " + exp 
                    + " OR aliasname ILIKE " + exp
                    + " OR annotation ILIKE " + exp_annot + ")";
            }
        }
		String sql = "SELECT systematicname, primaryname, aliasname, annotation FROM "
            + "(SELECT strainname FROM " + summaryTable + ") AS GT INNER JOIN " 
            + "(SELECT systematicname, primaryname, aliasname, annotation FROM " + genenameTable 
            + " " + whereClause + ") AS QT ON systematicname = strainname";
//		    + " genename_20040719 " + whereClause;

        try {
            Table orfTable = _connection.getQueryResult(sql);
            ColLabelIndex colLabelIndex = new ColLabelIndex(orfTable);
            
            int count = orfTable.getRowSize() - 1;
            int maxPage = count / numElementInAPage;
            if((count % numElementInAPage) != 0)
                maxPage++;
            
            int startRow = numElementInAPage * (currentPage - 1) + 1;
            int endRow = startRow + numElementInAPage;
            endRow = endRow > orfTable.getRowSize() ? orfTable.getRowSize() : endRow;
            xmlout.startTag("lab.cb.scmdresult");
            xmlout.startTag("orflist");
            xmlout.startTag("page", 
                            new XMLAttribute("current", Integer.toString(currentPage)).
                            add("max", Integer.toString(maxPage)).
                            add("elements", Integer.toString(numElementInAPage)));
            for(int row=startRow; row<endRow; row++)
            {
                String orfName = colLabelIndex.get(row, "systematicname").toString();
                xmlout.startTag("orf", new XMLAttribute("orfname", orfName));
                xmlout.selfCloseTag("standardname", new XMLAttribute("name", colLabelIndex.get(row, "primaryname").toString()));
                String[] aliasList = colLabelIndex.get(row, "aliasname").toString().split("\\|"); 
                for(int i=0; i<aliasList.length; i++)
                    xmlout.selfCloseTag("alias", new XMLAttribute("name", aliasList[i]));
                xmlout.startTag("annotation");
                xmlout.textContent(colLabelIndex.get(row, "annotation").toString());
                xmlout.closeTag();
                xmlout.closeTag();
            }
            
            xmlout.closeTag();
            xmlout.closeTag();
            xmlout.endOutput();
            xmlout.closeStream();
        } 
        catch(InvalidXMLException e)
        {
            e.what();
        }
        catch (SCMDException e) {
        	System.err.println(e.getMessage());
            e.what();
        }


    }
    

}

//--------------------------------------
//$Log: SCMDXMLQuery.java,v $
//Revision 1.17  2004/09/21 06:13:05  leo
//warning fix
//
//Revision 1.16  2004/09/03 07:31:53  leo
//デザインの調整
//standardnameを表示
//
//Revision 1.15  2004/08/30 10:43:13  leo
//GroupBySheetの作成 pageの移動はまだ
//
//Revision 1.14  2004/08/27 08:57:43  leo
//検索機能を追加 pageの移動はまだ
//
//Revision 1.13  2004/08/26 08:45:52  leo
//Queryの追加。 selectionの修正
//
//Revision 1.12  2004/08/15 07:14:42  sesejun
//GroupByのDB接続書き始め(未完成)
//
//Revision 1.11  2004/08/14 11:40:09  sesejun
//cellIDの取得のバグを修正
//
//Revision 1.10  2004/08/14 11:13:06  sesejun
//DataSheet対応開始。
//getCellCordinates()にcellidを追加
//
//Revision 1.9  2004/08/13 15:20:02  sesejun
//*** empty log message ***
//
//Revision 1.8  2004/08/13 10:00:20  sesejun
//QueryAPI対応
//SCMDServer.config.sample に　POSTGRESQL_* を追加し、対応。
//
//Revision 1.7  2004/08/13 09:33:10  leo
//Connectionが切れていた場合に再接続するようにした
//
//Revision 1.6  2004/08/13 05:50:32  leo
//XMLQuery, TableQuery, ValueQueryのinstanceに
//
//Revision 1.5  2004/08/12 14:49:24  leo
//DBとの接続開始
//
//Revision 1.4  2004/08/11 07:28:37  leo
//設定ファイルで、XMLQueryのinstanceを変更できるようにした
//
//Revision 1.3  2004/08/11 05:47:13  leo
//XMLOutputterにクエリの結果を出力するのではなく、ただのOutputStreamにしました
//
//Revision 1.2  2004/08/09 03:39:23  sesejun
//ORFListをDBから生成
//
//Revision 1.1  2004/08/03 04:48:12  sesejun
//cellbox との連携をとる
//

