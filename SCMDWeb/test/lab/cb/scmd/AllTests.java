//--------------------------------------
// SCMDServer
// 
// AllTests.java 
// Since: 2004/07/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd;

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
        TestSuite suite = new TestSuite("Test for lab.cb.scmd");
        //$JUnit-BEGIN$
        suite.addTest(lab.cb.scmd.web.common.AllTests.suite());
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