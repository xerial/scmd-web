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
 * ユーザーからのORFの入力を記録するフォーム
 * @author leo
 *
 */
public class UserSelection
{

    private TreeSet<String> _selection = new TreeSet<String>();
    /**
     * 
     */
    public UserSelection()
    {
        super();
    }
    
    public void addSelection(String orf)
    {
        _selection.add(orf.toLowerCase());
    }
    
    public Set getSelection()
    {
        return _selection;
    }

    
    public void removeSelection(String orf)
    {
        _selection.remove(orf.toLowerCase());
    }
    
    public Set<String> orfSet()
    {
        return (Set<String>) _selection.clone();
    }

    /**
     *  
     */
    public void clear()
    {
       _selection.clear();
    }
}


//--------------------------------------
// $Log: UserSelection.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionの追加
//
//--------------------------------------