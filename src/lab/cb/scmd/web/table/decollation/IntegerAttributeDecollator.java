//--------------------------------------
// SCMDServer
// 
// IntegerAttributeDecollator.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;

/**
 * @author leo
 *
 */
public class IntegerAttributeDecollator extends Decollator
{
    String _attributeName;
    int _value;
    
    public IntegerAttributeDecollator(String attributeName, int value)
    {
        _attributeName = attributeName;
        _value = value;
    }
    
    public TableElement decollate(TableElement element) {
        return new IntegerAttributeDecollation(this, element);
    }

    protected Properties addCellProperties(Properties properties) {
        properties.setProperty(_attributeName, Integer.toString(_value));
        return properties;
    }
}


//--------------------------------------
// $Log: IntegerAttributeDecollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElement‚È‚ÇATableElement‚Ì—v‘f‚Á‚Û‚­–¼Ì•ÏX
// ColIndex, RowIndex‚È‚Ç‚ğDynamic Update
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollator‚ğ®—
//
//--------------------------------------