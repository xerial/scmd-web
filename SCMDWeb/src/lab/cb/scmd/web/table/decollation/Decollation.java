//--------------------------------------
// SCMDServer
// 
// Decollation.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableVisitor;

/** TableElementのcellPropertyや、toStringを飾るためのクラス
 * 飾りであるので、基本的に、visitorの、
 * visit(Decollation) というメソッドは呼び出されず、
 * 飾りの中身のTableElementに、Visitorが渡される (（中身のTableElement).accept(visitor) )
 * 
 * <b>NOTICE:</b> 従って、toString()をvisitorが評価するためには、さらに、
 * LeafTableElementインターフェースをimplementしないと、DefaultVisitorが止まってくれない
 * 
 * 
 *  
 * @author leo
 *
 */
public class Decollation implements TableElement 
{
    private TableElement _tableElement = null;
    Decollator _decollator = null;

    /**
     * @param tableElement
     */
    protected Decollation(TableElement tableElement, Decollator decollator)
    {
        _tableElement = tableElement;
        _decollator = decollator;
    }

    /* 
     * cellPropertyにこのDecollationクラスが与える属性値を個々でセットする
     * セットする値は、Decollatorが持つように、分離する
     */
    public final Properties getCellProperty() {
        return _decollator.addCellProperties(_tableElement.getCellProperty());
    }
    

    public String toString() {
        return _tableElement.toString();
    }

    public void accept(TableVisitor v) {
        // 飾りはthroughして、中身に制御を移す
        _tableElement.accept(v);
    }
    
    public final Properties getProperties() {
        // Decollationは、基本的に属性値を持たない
        return new Properties();
    }
    
        
    public final void setProperty(String property, String value) {
        // Decollationは、基本的に属性値を持たない
        throw new UnsupportedOperationException("cannot set any property to the subclass of AbstractAttribute");
    }

    
    public final void setProperties(Properties properties) {
        // Decollationは、基本的に属性値を持たない
        throw new UnsupportedOperationException("cannot set any property to the subclass of AbstractAttribute");
    }
    
    
    /**
     * @return  飾りの中身のTableElement
     */
    protected final TableElement getTableElement()
    {
        return _tableElement;
    }
}


//--------------------------------------
// $Log: Decollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.3  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
//--------------------------------------