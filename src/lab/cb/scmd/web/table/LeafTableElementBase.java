//--------------------------------------
// SCMDServer
// 
// LeafTableElementBase.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/** 中身にTableElementなど構造を持つものを持たないクラスのベース
 * @author leo
 *
 */
public abstract class LeafTableElementBase implements LeafTableElement
{
    //Properties _properties = new Properties();
    /**
     * 
     */
    public LeafTableElementBase()
    {
    }

    public Properties getProperties() {
        //return _properties;
        return new Properties();
    }
    
    
 
    public void accept(TableVisitor v) {
        v.visit(this);
    }
    
    public void setProperty(String property, String value) {
        throw new UnsupportedOperationException("cannot set property to leaf table element");
        //_properties.setProperty(property, value);
    }
    
    public Properties getCellProperty() {
        return new Properties();
    }
    
    
    public void setProperties(Properties properties) {
        throw new UnsupportedOperationException("cannot set property to leaf table element");
        //_properties = properties;
    }
}


//--------------------------------------
// $Log: LeafTableElementBase.java,v $
// Revision 1.3  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.1  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
//--------------------------------------