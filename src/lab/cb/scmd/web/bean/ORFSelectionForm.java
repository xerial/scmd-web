//--------------------------------------
// SCMDServer
// 
// ORFSelectionForm.java 
// Since: 2004/08/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author leo
 *
 */
public class ORFSelectionForm extends ActionForm
{

    String _input;
    String[] _inputList;
    //Set _removeList = new TreeSet();
    //Set _previousSet = new TreeSet();
    /**
     * 
     */
    public ORFSelectionForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        _inputList = new String[0];
    }
    
    public String getInput() {
        return _input;
    }
    
    public void setInput(String input) {
        this._input = input.toLowerCase();
    }
    
    public String[] getInputList()
    {
        return _inputList;
    }
    /*
    public Set getRemoveList()
    {
        return _removeList;
    }
    */
    
    public void setInputList(String[] inputList)
    {
        /*
        // _previousSetに既にあるものが、inputListになければ、選択から外す意図として、removeListに追加する
        TreeSet inputSet = new TreeSet();
        for (int i = 0; i < inputList.length; i++)
        {
            inputSet.add(inputList[i]);
        }
        for (Iterator it = _previousSet.iterator(); it.hasNext();)
        {
            String element = (String) it.next();
            if(!inputSet.contains(element))
                _removeList.add(element);
        }
        */
        _inputList = inputList;
    }
    
    public void setSelectedORFList(Set orfSet)
    {
        //_previousSet = orfSet;
        _inputList = new String[orfSet.size()];
        int i =0;
        for(Iterator it = orfSet.iterator(); it.hasNext(); )
        {
            _inputList[i++] = (String) it.next();
        }
    }
}


//--------------------------------------
// $Log: ORFSelectionForm.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionの追加
//
//--------------------------------------