/*
 * Created on 2005/03/21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValueComparator implements Comparator {
    
    public ValueComparator() {
        super();
    }
    
    public boolean equals(Object obj) {
        return (super.equals(obj));
    }

    public int compare(Object v0, Object v1) {
        double d0 = ((ORFValueSet)v0).getValue();
        double d1 = ((ORFValueSet)v1).getValue();
        
        if( d0 - d1 < 0 )
            return -1;
        else if (d0 - d1 > 0)
            return 1;
        return 0;
    }
}
