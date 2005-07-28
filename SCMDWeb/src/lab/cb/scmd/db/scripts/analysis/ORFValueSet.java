/*
 * Created on 2005/03/21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ORFValueSet {
    private String _orf = "";
    private double _value = 0.0;
    private int _pval = 0;
    
    public ORFValueSet (String orf, double value, int pval) {
        _orf = orf;
        _value = value;
        _pval = pval;
    }
    
    public String getOrf() {
        return _orf;
    }
    
    public double getValue() {
        return _value;
    }
    
    public int getPVal() {
        return _pval;
    }
}
