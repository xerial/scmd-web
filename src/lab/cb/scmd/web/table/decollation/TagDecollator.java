//--------------------------------------
// SCMDServer
// 
// TagDecollator.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table.decollation;

import java.util.Properties;

import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.Tag;

/**
 * @author leo
 *
 */
public class TagDecollator extends Decollator
{
    Properties _properties = new Properties();

    String _tagName;
    
    public TagDecollator(String tagName)
    {
        super();
        _tagName = tagName;
    }
    
    public void setAttribute(String attributeName, String value)
    {
        _properties.setProperty(attributeName, value);
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.Decollator#decollate(lab.cb.scmd.web.table.TableElement)
     */
    public TableElement decollate(TableElement element) {
        Tag tag = new Tag(_tagName, element);
        tag.setProperties(_properties);
        return tag;
    }

}


//--------------------------------------
// $Log: TagDecollator.java,v $
// Revision 1.1  2004/12/10 05:15:31  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.1  2004/08/09 03:36:42  leo
// TagDecollator‚ð’Ç‰Á
//
//--------------------------------------