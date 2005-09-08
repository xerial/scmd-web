//--------------------------------------
//SCMDServer
//
//ParamUserSelection.java 
//Since: 2005/02/08
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/bean/ORFSelectionForm.java $ 
//$LastChangedBy: sesejun $ 
//--------------------------------------
package lab.cb.scmd.web.sessiondata;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 * parameterの選択状態を記すクラス
 * @author sesejun
 * 
 */
public class ParamUserSelection implements Serializable{
    private TreeSet<Integer> _orfParam  = new TreeSet<Integer>();
    private TreeSet<Integer> _cellParam = new TreeSet<Integer>();
    
    public ParamUserSelection()
    {
        super();
    }
    
    public void addOrfParamSelection(int param)
    {
        _orfParam.add(new Integer(param));
    }
    
    public Set<Integer> getOrfParamSelection() 
    {
        return (Set<Integer>) _orfParam.clone();
    }
    
    public void removeOrfParamSelection(int param)
    {
        _orfParam.remove(new Integer(param));
    }

    public void resetOrfParam() {
        _orfParam.clear();
    }

    public void addCellParamSelection(int param)
    {
        _cellParam.add(new Integer(param));
    }
    
    public Set<Integer> getCellParamSelection() 
    {
        return (Set<Integer>) _cellParam.clone();
    }
    
    public void removeCellParamSelection(int param)
    {
        _cellParam.remove(new Integer(param));
    }

    public void resetCellParam() {
        _cellParam.clear();
    }

    public List<MorphParameter> getCellParamInfo() {
        return getParamInfo(_cellParam);
    }

    public List<MorphParameter> getOrfParamInfo() {
        return getParamInfo(_orfParam);
    }

    private List<MorphParameter> getParamInfo(TreeSet<Integer> param) {
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        List<MorphParameter> paramlist = null;
        if(param.size() > 0 ) {
            paramlist = query.getParameterInfo(param);
        } else {
            paramlist = new LinkedList<MorphParameter> ();
        }            
        return paramlist;
    }

    public String isChecked(Integer n, String category) {
        if( category.equals("cell") ) {
            if(_cellParam.contains(n))
                return "check";
            else
                return "";
        } else { // orf
            if(_orfParam.contains(n))
                return "check";
            else
                return "";
        }
    }

}
