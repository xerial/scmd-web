//--------------------------------------
// SCMDServer
// 
// AllTests.java 
// Since: 2004/07/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author leo
 *
 */
public class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for lab.cb.scmd.web.common");
        //$JUnit-BEGIN$
        suite.addTestSuite(SCMDHeaderTest.class);
        //$JUnit-END$
        return suite;
    }
}


//--------------------------------------
// $Log: AllTests.java,v $
// Revision 1.1  2004/07/06 09:51:56  leo
// temporary commit
//
//--------------------------------------