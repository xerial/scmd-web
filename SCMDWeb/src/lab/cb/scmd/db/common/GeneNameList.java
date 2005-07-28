//--------------------------------------
//SCMDServer
//
//GeneNameList.java 
//Since: 2004/07/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.ArrayList;

public class GeneNameList {
    private ArrayList _geneNameList = new ArrayList();
    int primary = 1;
    
    public void add(String geneName) {
        _geneNameList.add(geneName);
    }
    
    public String get(int n) {
        Object name = _geneNameList.get(n);
        return (String)name;
    }
    
    public String getPrimaryName() {
        return (String)_geneNameList.get(0);
    }
    
    public int size() {
        return _geneNameList.size();
    }
}
