//--------------------------------------
//SCMD Project
//
//ORFListTest.java 
//Since:  2004/07/16
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.connect;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.DBConnectException;

public class ORFListTest extends TestCase {
    public void testGetXML() {
        ByteArrayOutputStream xmlResultStream = new ByteArrayOutputStream();
        ORFList orfList = new ORFList();
        // use data in flat file
        //SCMDDBConnect dbconnect = new SCMDDBConnect("flatfile", "5432", "summary_joined_test.tab");
        // use data in DB 
        SCMDDBConnect dbconnect = null;
		try {
			dbconnect = new SCMDDBConnect("157.82.250.67", "5432", "lab.cb.scmd_pre_20040714");
		} catch (DBConnectException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String	 group1Name = "Wild Type";
        String[] group1orf = {"YOR202W"};

        String	 group2Name	= "Mutant";
        //String[] group2orf = {"YAL001C","YAL005C","YAL007C","YAL008W","YAL010W"};
        String[] group2orf = {};
        
        //TODO gene nameや、aliasを与えられたと気に
        //ORF名に変換して検索をする
        orfList.addGroup(group1Name, group1orf);	// 第一引数は、グループ名
        orfList.addGroup(group2Name, group2orf);	// 第二引数は、gene nameを配列で入力
        orfList.setMaxElementsInAPage(group1Name, 10);		// 1ページの最大数。省略可。
        orfList.setMaxElementsInAPage(group2Name, 10);		// 省略値 10
        orfList.setCurrentPage(group1Name, 1);		// ページ番号。省略の際は、1
        orfList.setCurrentPage(group2Name, 1);		// 
        //TODO パラメータの機械的な名前から、意味のある名前への変換、及び
        // その逆変換を行う。
        //orfList.addParameter("roundness");
        orfList.addParameter("C11-1_A");
        
        try {
            orfList.setData(dbconnect);
        } catch (SCMDException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        XMLOutputter xmlout = new XMLOutputter(new PrintStream(xmlResultStream));
        try {
            orfList.outputXML(xmlout);
            xmlout.endOutput();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        String xmlresultString = xmlResultStream.toString();
        System.out.println(xmlresultString);
        //TODO assertで判断できるようにする
        //assertEqual(xmlresultString, "");
        
//        <!DOCTYPE lab.cb.scmdresult SYSTEM "null">
//        <lab.cb.scmdresult>
//          <orflist>
//            <orfgroup type="Wild Type">
//              <page current="1" max="10"></page>
//              <orf id="YOR202W">
//                <genename name="HIS3"></genename>
//                <genename name="HIS10"></genename>
//                <annotation>imidazoleglycerol-phosphate dehydratase</annotation>
//              </orf>
//            </orfgroup>
//            <orfgroup type="Mutant">
//              <page current="1" max="20"></page>
//              <orf id="YAL001C">
//                <genename name="TFC3"></genename>
//                <genename name="FUN24"></genename>
//                <genename name="TSV115"></genename>
//                <annotation>transcription factor tau (TFIIIC) subunit</annotation>
//              </orf>
//              <orf id="YAL005C">
//                <genename name="SSA1"></genename>
//                <genename name="YG100"></genename>
//                <annotation>heat shock protein of HSP70 family</annotation>
//              </orf>
//              <orf id="YAL007C">
//                <genename name="ERP2"></genename>
//                <annotation>p24 protein involved in membrane trafficking</annotation>
//              </orf>
//              <orf id="YAL008W">
//                <genename name="FUN14"></genename>
//                <annotation>Protein of unknown function</annotation>
//              </orf>
//              <orf id="YAL010W"></orf>
//            </orfgroup>
//          </orflist>
//        </lab.cb.scmdresult>

    }

}
