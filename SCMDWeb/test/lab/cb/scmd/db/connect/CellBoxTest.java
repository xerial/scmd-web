//--------------------------------------
//SCMD Project
//
//IndivualBoxTest.java 
//Since:  2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.connect;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.DBConnectException;
import junit.framework.TestCase;

public class CellBoxTest extends TestCase {
	public CellBoxTest() {
		
	}
	
    public void testGetXML() {
        ByteArrayOutputStream xmlResultStream = new ByteArrayOutputStream();
        //DBConnect dbconnect = new DBConnect(false);
        SCMDDBConnect dbconnect = null;
		try {
			dbconnect = new SCMDDBConnect("157.82.250.67", "5432", "lab.cb.scmd_pre_20040714");
		} catch (DBConnectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// é¿å±î‘çÜÇÉZÉbÉg
        CellBox cellBox = new CellBox("YOR202W");
        cellBox.setCurrentPage(2);

        try {
            cellBox.setData(dbconnect);
        } catch (SCMDException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        XMLOutputter xmlout = new XMLOutputter(new PrintStream(xmlResultStream));
        try {
            cellBox.outputXML(xmlout);
            xmlout.endOutput();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        String xmlresultString = xmlResultStream.toString();
        System.out.println(xmlresultString);
//        <lab.cb.scmdresult>
//		<orf orfname="yor202w">
//			<photo id="1">
//				<cell id="1" x1="223" x2="275" y1="0" y2="54"/>
//	<cell id="2" x1="48" x2="90" y1="34" y2="72"/>
//	<cell id="3" x1="422" x2="482" y1="72" y2="122"/>
//	<cell id="4" x1="115" x2="169" y1="87" y2="135"/>
//	<cell id="5" x1="215" x2="270" y1="93" y2="144"/>
//	<cell id="6" x1="483" x2="538" y1="115" y2="167"/>
//	<cell id="7" x1="352" x2="384" y1="131" y2="165"/>
//	<cell id="8" x1="242" x2="296" y1="164" y2="220"/>
//	<cell id="9" x1="125" x2="175" y1="212" y2="259"/>
//	<cell id="10" x1="530" x2="590" y1="226" y2="267"/>
//	<cell id="11" x1="237" x2="285" y1="230" y2="269"/>
//	<cell id="12" x1="81" x2="114" y1="245" y2="283"/>
//	<cell id="13" x1="533" x2="574" y1="287" y2="327"/>
//	<cell id="14" x1="221" x2="269" y1="293" y2="342"/>
//	<cell id="15" x1="287" x2="349" y1="298" y2="356"/>
//	<cell id="16" x1="96" x2="149" y1="423" y2="472"/>
//	<cell id="17" x1="168" x2="208" y1="454" y2="491"/>
//			</photo>
//		</orf>
//	</lab.cb.scmdresult>
    }


}
