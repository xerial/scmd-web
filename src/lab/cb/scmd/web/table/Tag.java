//--------------------------------------
// SCMDServer
// 
// Tag.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** Tag‚ÅTableElement‚ğˆÍ‚Ş‚½‚ß‚ÌDecollation
 * @author leo
 *
 */
public class Tag extends NestedTableElementBase
{
    String _tagName; 
    /**
     * @param bodyContent
     */
    public Tag(String tagName, TableElement bodyContent)
    {
        super(bodyContent);
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
}


//--------------------------------------
// $Log: Tag.java,v $
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElement‚È‚ÇATableElement‚Ì—v‘f‚Á‚Û‚­–¼Ì•ÏX
// ColIndex, RowIndex‚È‚Ç‚ğDynamic Update
//
// Revision 1.1  2004/08/09 03:36:42  leo
// TagDecollator‚ğ’Ç‰Á
//
//--------------------------------------