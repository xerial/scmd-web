//--------------------------------------
// SCMDServer
// 
// UserSelection.java 
// Since: 2004/08/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author leo
 *
 */
public class UserSelection
{

    TreeSet _selection = new TreeSet();
    /**
     * 
     */
    public UserSelection()
    {
        super();
    }
    
    public void addSeletion(String orf)
    {
        _selection.add(orf.toLowerCase());
    }
    
    public Set getSelection()
    {
        return _selection;
    }

    
    public void removeSelection(String orf)
    {
        _selection.remove(orf);
    }
}


//--------------------------------------
// $Log: UserSelection.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// QueryÇÃí«â¡ÅB selectionÇÃèCê≥
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionÇÃí«â¡
//
//--------------------------------------