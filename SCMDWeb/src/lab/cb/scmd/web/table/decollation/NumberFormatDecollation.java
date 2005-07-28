//--------------------------------------
// SCMDServer
// 
// NumberFormatDecollation.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.text.NumberFormat;

import lab.cb.scmd.web.table.TableElement;

/** 基本的にDecollationで、cellPropertyは中身を透過して見えるが、
 * visitorはこの要素を通過せずに、この要素でtoStringを呼び出すことを期待される
 * @author leo
 *
 */
public class NumberFormatDecollation extends OpaqueDecollation
{
    /**
     * @param tableElement
     */
    public NumberFormatDecollation(int fractionDigits, TableElement tableElement)
    {
        super(tableElement, new NumberFormatDecollator(fractionDigits)); 
    }
    public NumberFormatDecollation(NumberFormatDecollator decollator, TableElement tableElement)
    {
        super(tableElement, decollator);
    }


    public String toString() {
        String str = getTableElement().toString();
        NumberFormat format = NumberFormat.getNumberInstance();
        int digits = ((NumberFormatDecollator) _decollator).getFractionDigits();
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        String formattedText;
        try
        { 
            formattedText = format.format(Double.parseDouble(str));
            return formattedText;
        }
        catch(NumberFormatException e)
        {
            return str;
        }
    
    }
}


//--------------------------------------
// $Log: NumberFormatDecollation.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.4  2004/08/09 12:26:15  leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.3  2004/08/09 09:17:11  leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.2  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
//--------------------------------------