//--------------------------------------
// SCMDServer
// 
// NumberFormatDecollator.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import lab.cb.scmd.web.table.TableElement;


/**
 * @author leo
 *
 */
public class NumberFormatDecollator extends Decollator
{
    int _fractionDigits;
    /**
     * 
     */
    public NumberFormatDecollator(int fractionDigits)
    {
        super();
        _fractionDigits = fractionDigits;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.Decollator#decollate(lab.cb.scmd.web.table.TableElement)
     */
    public TableElement decollate(TableElement element) {
        return new NumberFormatDecollation(this, element);
    }
    
    public int getFractionDigits() 
    {
        return _fractionDigits;
    }

}

//--------------------------------------
// $Log: NumberFormatDecollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, DecollatorÇêÆóù
//
//--------------------------------------