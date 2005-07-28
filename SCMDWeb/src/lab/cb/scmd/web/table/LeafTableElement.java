//--------------------------------------
// SCMDServer
// 
// LeafTableElement.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** 中に、階層構造を持たないTableElementで、toString()で中身を評価する目印となるTableElement
 * @author leo
 *
 */
public interface LeafTableElement extends TableElement
{
    public String toString();
    
}

//--------------------------------------
// $Log: LeafTableElement.java,v $
// Revision 1.3  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.2  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
//--------------------------------------