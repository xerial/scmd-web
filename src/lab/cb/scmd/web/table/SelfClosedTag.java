//--------------------------------------
// SCMDServer
// 
// SelfClosedTag.java 
// Since: 2004/08/09
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
public class SelfClosedTag extends LeafTableElementBase
{
    String _tagName;
    Properties _properties = new Properties();
    /*
     * 
     */
    public SelfClosedTag(String tagName)
    {
        super();
        _tagName = tagName;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableElement#accept(lab.cb.scmd.web.table.TableVisitor)
     */
    public void accept(TableVisitor v) {
        v.visit(this);
    }

    public String getTagName()
    {
        return _tagName;
    }

    
    public Properties getProperties() {
        return _properties;
    }
    public void setProperties(Properties properties) {
        _properties = properties;
    }
    public void setProperty(String property, String value) {
        _properties.setProperty(property, value);
    }
}


//--------------------------------------
// $Log: SelfClosedTag.java,v $
// Revision 1.2  2004/08/30 10:43:13  leo
// GroupBySheetÇÃçÏê¨ pageÇÃà⁄ìÆÇÕÇ‹Çæ
//
// Revision 1.1  2004/08/09 03:36:42  leo
// TagDecollatorÇí«â¡
//
//--------------------------------------