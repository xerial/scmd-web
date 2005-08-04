//--------------------------------------
//SCMD Project
//
//IndivualBoxTest.java 
//Since:  2004/07/28
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.DBConnectException;
import junit.framework.TestCase;

public class IndividualBoxTest extends TestCase {
    public IndividualBoxTest () {
        
    }

    public void testGetXML() {
        ByteArrayOutputStream xmlResultStream = new ByteArrayOutputStream();
        //DBConnect dbconnect = new DBConnect(false);
        DBConnect dbconnect = null;
		try {
			dbconnect = new DBConnect("157.82.250.67", "5432", "lab.cb.scmd_pre_20040714");
		} catch (DBConnectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// é¿å±î‘çÜÇÉZÉbÉg
        IndividualBox individualBox = new IndividualBox("YOR202W");
        individualBox.setCurrentPage(2);

        // sample message
        
        
        try {
            individualBox.setData(dbconnect);
        } catch (SCMDException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        XMLOutputter xmlout = new XMLOutputter(new PrintStream(xmlResultStream));
        try {
            individualBox.outputXML(xmlout);
            xmlout.endOutput();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        String xmlresultString = xmlResultStream.toString();
        System.out.println(xmlresultString);
    }

}
