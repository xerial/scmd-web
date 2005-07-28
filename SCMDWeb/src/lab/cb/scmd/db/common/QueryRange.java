//--------------------------------------
// SCMDServer
//
// QueryRange.java 
// Since: 2004/09/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryRange
{

    double _begin;
    double _end;
    /**
     * 
     */
    public QueryRange(double begin, double end) {
        _begin = begin;
        _end = end;
    }
    
    

    public double getBegin() {
        return _begin;
    }
    public void setBegin(double _begin) {
        this._begin = _begin;
    }
    public double getEnd() {
        return _end;
    }
    public void setEnd(double _end) {
        this._end = _end;
    }
}


//--------------------------------------
// $Log: QueryRange.java,v $
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plot‚ð’Ç‰Á
//
//--------------------------------------
