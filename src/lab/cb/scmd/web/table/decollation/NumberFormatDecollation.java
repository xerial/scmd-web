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

/** ��{�I��Decollation�ŁAcellProperty�͒��g�𓧉߂��Č����邪�A
 * visitor�͂��̗v�f��ʉ߂����ɁA���̗v�f��toString���Ăяo�����Ƃ����҂����
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
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.3  2004/08/09 09:17:11  leo
// Table�͈͎̔w���TableRange���g���悤�ɂ���
//
// Revision 1.2  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
//--------------------------------------