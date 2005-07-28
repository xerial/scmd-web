//--------------------------------------
// SCMDServer
// 
// CGIUtilTest.java 
// Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.util;

import java.util.TreeMap;

import junit.framework.TestCase;

/**
 * @author leo
 *
 */
public class CGIUtilTest extends TestCase
{

    public void testGetCGIArgument() 
    {
        TreeMap map = new TreeMap();
        map.put("prop1", new Double(1.5));
        map.put("prop2", "hello");
        
        String arg = CGIUtil.getCGIArgument(map);
        assertEquals("prop1=1.5&prop2=hello", arg);
    }

}


//--------------------------------------
// $Log: CGIUtilTest.java,v $
// Revision 1.1  2004/07/30 06:40:29  leo
// Morphology Searchのインターフェース、完成
//
//--------------------------------------