//--------------------------------------
// SCMDServer
// 
// Range.java 
// Since: 2004/07/29
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

/**
 * @author leo
 *
 */
public class Range
{
    double _min;
    double _max;
    /**
     * 
     */
    public Range(double min, double max)
    {
        _min = min;
        _max = max;
    }
    
    public Range(int min, int max)
    {
        _min = new Double(min).doubleValue();
        _max = new Double(max).doubleValue();
    }
    
    public boolean IsWithinRange(double value)
    {
        return _min <= value  && value <= _max;
    }

    public boolean IsWithinRange(int value)
    {
        return _min <= value  && value <= _max;
    }

    public double getMax() { return _max; }
    public double getMin() { return _min; }
    
}


//--------------------------------------
// $Log: Range.java,v $
// Revision 1.1  2004/07/29 07:50:45  leo
// MorphologySearch‚Ìƒp[ƒcì¬’†
//
//--------------------------------------