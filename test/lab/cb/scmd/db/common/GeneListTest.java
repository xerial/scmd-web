//--------------------------------------
//SCMD Project
//
//GeneListTest.java 
//Since:  2004/07/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.DBConnectException;
import junit.framework.TestCase;

public class GeneListTest extends TestCase {

    public GeneListTest () {
        
    }
    
    public void testGetXML() {
        ByteArrayOutputStream xmlResultStream = new ByteArrayOutputStream();
        GeneList geneList = new GeneList();
        //DBConnect dbconnect = new DBConnect(false);
        DBConnect dbconnect = null;
		try {
			dbconnect = new DBConnect("157.82.250.67", "5432", "lab.cb.scmd_pre_20040714");
		} catch (DBConnectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] group1 = {"YOR202W"};
        String[] group2 = {"YAL001C", "YAL005C","YAL007C","YAL008W","YAL010W"};
        String	 group1Name = "Wild Type";
        String	 group2Name	= "Mutant";
        
        geneList.addGroup(group1Name, group1);	// 第一引数は、グループ名
        geneList.addGroup(group2Name, group2);	// 第二引数は、gene nameを配列で入力
        geneList.setMaxElementsInAPage(group1Name, 10);		// 1ページの最大数。省略可。
        geneList.setMaxElementsInAPage(group2Name, 20);		// 省略値 10
        geneList.setCurrentPage(group1Name, 1);		// ページ番号。省略の際は、1
        geneList.setCurrentPage(group2Name, 1);		// 
        geneList.addParameter("C11-1_A");
        //geneList.addParameter(group1Name, "C11-1_A");
        //geneList.addParameter(group2Name, "C11-1_A");
        //geneList.addParameter("roundness");
        
        try {
            geneList.setData(dbconnect);
        } catch (SCMDException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        XMLOutputter xmlout = new XMLOutputter(new PrintStream(xmlResultStream));
        try {
            geneList.outputXML(xmlout);
            xmlout.endOutput();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        String xmlresultString = xmlResultStream.toString();
        System.out.println(xmlresultString);
        
//        assertEquals(xmlresultString, "");
//        <!DOCTYPE lab.cb.scmdresult SYSTEM "null">
//        <lab.cb.scmdresult>
//          <genelist>
//            <genegroup type="Wild Type">
//              <page current="1" max="10"></page>
//              <gene id="YOR202W">
//                <genename name="HIS3"></genename>
//                <genename name="HIS10"></genename>
//                <annotation>imidazoleglycerol-phosphate dehydratase</annotation>
//              </gene>
//            </genegroup>
//            <genegroup type="Mutant">
//              <page current="1" max="20"></page>
//              <gene id="YAL001C">
//                <genename name="TFC3"></genename>
//                <genename name="FUN24"></genename>
//                <genename name="TSV115"></genename>
//                <annotation>transcription factor tau (TFIIIC) subunit</annotation>
//              </gene>
//              <gene id="YAL005C">
//                <genename name="SSA1"></genename>
//                <genename name="YG100"></genename>
//                <annotation>heat shock protein of HSP70 family</annotation>
//              </gene>
//              <gene id="YAL007C">
//                <genename name="ERP2"></genename>
//                <annotation>p24 protein involved in membrane trafficking</annotation>
//              </gene>
//              <gene id="YAL008W">
//                <genename name="FUN14"></genename>
//                <annotation>Protein of unknown function</annotation>
//              </gene>
//              <gene id="YAL010W"></gene>
//            </genegroup>
//          </genelist>
//        </lab.cb.scmdresult>
//

    }
}
