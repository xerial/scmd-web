//--------------------------------------
// SCMDServer
// 
// AttributeDecollator.java 
// Since: 2004/08/07
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
public class AttributeDecollator extends Decollator
{
    String _attributeName;
    String _value;
    /**
     * 
     */
    public AttributeDecollator(String attributeName, String value)
    {
        _attributeName = attributeName;
        _value = value;
    }
    
    protected Properties addCellProperties(Properties properties) {
        properties.setProperty(_attributeName, _value);
        return properties;
    }


    public TableElement decollate(TableElement element) {
        return new AttributeDecollation(element, this);
    }
    
    protected String getValue()
    {
        return _value;
    }

}


//--------------------------------------
// $Log: AttributeDecollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.4  2004/08/09 12:32:47  leo
// StringAttributeDecollation -> AttributeDecollationに集約
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