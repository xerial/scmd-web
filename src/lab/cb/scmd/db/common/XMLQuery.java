//--------------------------------------
// SCMDServer
// 
// XMLQuery.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Collection;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.SelectedShape;


/**
 * @author leo
 *  
 */
public interface XMLQuery extends QueryAPI
{
	void getAvailableORF(OutputStream out, AbstractMap groupToPageMap);

	int	ORFINFO_GENENAME	= 0x0001;
	int	ORFINFO_ANNOTATION	= 0x0002;

	void getORFInfo(OutputStream out, String orf, int flag);
	void getORFInfo(OutputStream out, Collection orfList, int flag);
	
	void getSearchResult(OutputStream out, String keyword, int currentPage, int numElementInAPage);
	
	void getCellCoordinates(OutputStream out, String orf, int currentPage) throws SCMDException;
	void getCellCoordinates(OutputStream out, String orf, int currentPage, int cellID) throws SCMDException;
	
	int GROUP_BUDSIZE = 0;
	int GROUP_NUCLEUS = 1;
	int GROUP_ACTIN = 2;
	//void getGroupBySheet(OutputStream out, String orf, int groupType, int page);

	
}


//--------------------------------------
// $Log: XMLQuery.java,v $
// Revision 1.15  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.14  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.13  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.12  2004/08/14 10:28:21  leo
// 単体のCellの情報を取ってくる問い合わせを追加
//
// Revision 1.11  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQueryのinstanceに
//
// Revision 1.10  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.9  2004/08/11 14:02:32  leo
// Group by のシート作成
//
// Revision 1.8  2004/08/11 07:28:37  leo
// 設定ファイルで、XMLQueryのinstanceを変更できるようにした
//
// Revision 1.7  2004/08/11 05:47:13  leo
// XMLOutputterにクエリの結果を出力するのではなく、ただのOutputStreamにしました
//
// Revision 1.6  2004/08/09 03:39:23  sesejun
// ORFListをDBから生成
//
// Revision 1.5  2004/08/03 04:48:12  sesejun
// cellbox との連携をとる
//
// Revision 1.4  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.3  2004/07/25 11:26:09  leo
// 問い合わせを追加
//
// Revision 1.2  2004/07/22 14:42:39  leo
// testの仕方を若干変更
//
// Revision 1.1 2004/07/21 05:47:16 leo
// XML関係のモジュールを追加
//
//--------------------------------------
