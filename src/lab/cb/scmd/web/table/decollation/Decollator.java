//--------------------------------------
// SCMDServer
// 
// Decollator.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;

/** TableElementにDecollationを被せるには、new Decollation(TableElement)とする必要がある。
 * しかし、毎回、同じCell属性値を持つDecollationを生成するのは、効率が悪いので、
 * このDecollatorクラスのインスタンスをはじめに生成し、
 * 
 * @author leo
 *
 */
public abstract class Decollator
{
    /** elementの上に、Decollationをかぶせたクラス（これもTableElementの派生クラスになる）を返す
     * @param element
     * @return
     */
    public abstract TableElement decollate(TableElement element);
    
    /** Tableのセルに与えるべき属性値を追加する
     * @param properties
     * @return
     */
    protected Properties addCellProperties(Properties properties)
    {
        // デフォルトでは何もセットせずに返す
        return properties;
    }

}


//--------------------------------------
// $Log: Decollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.4  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.3  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web用のTableクラス
//
//--------------------------------------