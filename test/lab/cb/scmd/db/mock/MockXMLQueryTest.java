//--------------------------------------
// SCMDServer
// 
// MockXMLQueryTest.java 
// Since: 2004/07/21
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

//import java.io.IOException;
//import java.util.HashMap;
//
//import lab.cb.scmd.algorithm.Algorithm;
//import lab.cb.scmd.algorithm.Functor;

//import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.util.xml.DTDDeclaration;
//import lab.cb.scmd.web.xml.XMLElement;
import junit.framework.TestCase;
import lab.cb.scmd.db.common.XMLQuery;
/**
 * @author leo
 *  
 */
public class MockXMLQueryTest extends TestCase
{
	XMLQuery	mock;

	protected void setUp() throws Exception {
		super.setUp();
		mock = new MockXMLQuery(true); // XMLQuery interfaceの実装をセット
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testGetAvailableORF() throws InterruptedException, IOException, SCMDException {
//		Validator validator = new Validator(mock) {
//			public void run() {
//				getXMLOuttputter().setDTDDeclaration(
//						new DTDDeclaration("lab.cb.scmdresult", "http://localhost:8080/lab.cb.scmd-server/xml/orflist.dtd"));
//				getXMLQuery().getAvailableORF(getOutputStream(), new HashMap());
//			}
//		};
//		boolean isValid = validator.validate();
//		assertTrue(validator.getErrorMessage(), isValid);
//
//		Algorithm.foreach(validator.getXMLElementList("orfgroup"), new Functor() {
//			public void apply(Object input) {
//				XMLElement elem = (XMLElement) input;
//				assertTrue("orfgroup type", elem.testAttributeValue("type", new String[] {
//						"wildtype", "mutant" }));
//			}
//		});
//		Algorithm.foreach(validator.getXMLElementList("page"), new Functor() {
//			public void apply(Object input) {
//				XMLElement elem = (XMLElement) input;
//				assertTrue("test for current page <= max",
//						elem.getAttributeIntValue("current") <= elem.getAttributeIntValue("max"));
//			}
//		});
//	}
//
//	public void testGetORFInfo() throws IOException, SCMDException, InterruptedException {
//		Validator validator = new Validator(mock) {
//			public void run() {
//				getOutputStream().setDTDDeclaration(
//						new DTDDeclaration("orf", "http://localhost:8080/lab.cb.scmd-server/xml/orflist.dtd"));
//				getXMLQuery().getORFInfo(getOutputStream(), "yor202w", XMLQuery.ORFINFO_GENENAME);
//			}
//		};
//		boolean isValid = validator.validate();
//		assertTrue(validator.getErrorMessage(), isValid);
//
//	}
}

//--------------------------------------
// $Log: MockXMLQueryTest.java,v $
// Revision 1.8  2004/08/11 07:28:37  leo
// 設定ファイルで、XMLQueryのinstanceを変更できるようにした
//
// Revision 1.7  2004/08/09 03:39:23  sesejun
// ORFListをDBから生成
//
// Revision 1.6  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.5  2004/07/24 14:35:31  leo
// *** empty log message ***
//
// Revision 1.4  2004/07/22 14:42:39  leo
// testの仕方を若干変更
//
// Revision 1.3 2004/07/22 07:11:47 leo
// xerces対応
//
// Revision 1.2 2004/07/21 14:49:12 leo
// Validationの部分を分離
//
// Revision 1.1 2004/07/21 08:07:05 leo
// temp commit
//
//--------------------------------------
