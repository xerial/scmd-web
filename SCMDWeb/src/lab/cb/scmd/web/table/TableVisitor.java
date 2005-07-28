//--------------------------------------
// SCMDServer
// 
// TableVisitor.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** Table内のTableElementを巡回しながら、何かを生成するためのinterface
 * @author leo
 *
 */
public interface TableVisitor
{
    public void visit(Table table);
    public void visit(TableElementList list);
    public void visit(Tag tag);
    public void visit(SelfClosedTag tag);
    public void visit(LeafTableElement element);
}


//--------------------------------------
// $Log: TableVisitor.java,v $
// Revision 1.8  2004/08/24 07:36:15  leo
// AbstractTableを廃棄
//
// Revision 1.7  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.6  2004/08/09 05:25:17  leo
// タグないに複数のタグを持てるように改良
//
// Revision 1.5  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.4  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.3  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
// Revision 1.2  2004/08/06 12:17:22  leo
// Decolator Pattern で、rowIndex, colIndexを実現
// Visitor Patternで、Table内探索を行う
//
// Revision 1.1  2004/08/02 07:56:25  leo
// 未完成
//
//--------------------------------------