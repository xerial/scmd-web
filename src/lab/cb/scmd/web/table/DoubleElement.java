//--------------------------------------
// SCMDServer
// 
// DoubleElement.java 
// Since: 2004/08/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/**
 * @author leo
 *
 */
public class DoubleElement extends LeafTableElementBase
{

    double _value = 0;
    /**
     * 
     */
    public DoubleElement(double value)
    {
        _value = value;
    }

    public void accept(TableVisitor v) {
        v.visit(this);
    }
    
    public String toString()
    {
        return Double.toString(_value);
    }

}


//--------------------------------------
// $Log: DoubleElement.java,v $
// Revision 1.1  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.4  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.3  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.2  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
// Revision 1.1  2004/08/06 12:17:22  leo
// Decolator Pattern で、rowIndex, colIndexを実現
// Visitor Patternで、Table内探索を行う
//
//--------------------------------------