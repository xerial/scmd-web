//--------------------------------------
// SCMDServer
// 
// TableElement.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/**
 * @author leo
 *
 */
public interface TableElement
{
    /** v.visit(this)として、TableElementの派生クラスに
     * TableVisitorが訪れたときの動作をVisitor内に記述することができる
     * @param v
     */
    public void accept(TableVisitor v);
    
    /** TableElementの属性値を取得する。 HTMLタグの属性と同義
     * @return
     */
    public Properties getProperties();

    /** 属性を定義する
     * @param property
     * @param value
     */
    public void setProperty(String property, String value);

    /**属性をセットする。既存のPropertiesクラスを再利用したいときに使う
     * @param properties
     */
    public void setProperties(Properties properties);
    
    /** このTableElementそのものの属性値ではなく、
     * このTableElementが配置されるTable内のcellについての属性値を取得する
     * HTMLでのTDタグの属性値と同義
     * この属性をセットするには、TableElementをDecollationクラスで囲う
     * 
     * ex. 
     * TableElement elem = new Style(new StringElement("hello"), "bold");
     * 
     * elem.getCellProperty() で、｛ ("class", "bold") } のProperties が返ってくる
     * 
     * 同じ属性が設定された場合は、外側のDecollationが内側を上書きする
     * @return
     */
    public Properties getCellProperty();
}


//--------------------------------------
// $Log: TableElement.java,v $
// Revision 1.6  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
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