//--------------------------------------
// SCMDWeb Project
//
// SelectedParameters.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.sessiondata;

import java.util.TreeSet;
import java.util.Vector;

/**
 * ユーザーが選択したパラメータの集合を保持するクラス
 * @author leo
 *
 */
public class SelectedParameters
{
    private TreeSet<Integer> _selectedCellParamID = new TreeSet<Integer>();
    private TreeSet<Integer> _selectedORFParamID = new TreeSet<Integer>();

    /**
     * 
     */
    public SelectedParameters()
    {
    }
    
    
    public void addCellParameter(int paramID)
    {
        _selectedCellParamID.add(paramID);
    }
    public void addORFParameter(int paramID)
    {
        _selectedORFParamID.add(paramID);
    }
    
}




