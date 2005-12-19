package lab.cb.scmd.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.web.test.DBConnectionWebTest;
import lab.cb.scmd.web.test.SCMDServerConfigWebTest;
import lab.cb.scmd.web.test.WebTestCase;
import lab.cb.scmd.web.test.WebTestRunner;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class SCMDServerTest extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		WebTestRunner runner = new WebTestRunner();
		runner.addTest(new DBConnectionWebTest());
		runner.addTest(new SCMDServerConfigWebTest());
		runner.run();
		PrintWriter pw = response.getWriter();
		Vector<WebTestCase> list = runner.getTestCaseList();
		for(WebTestCase test : list) {
			pw.println("<table>");
			pw.println("<tr>");
			pw.printf("<td>%s</td><td>%s</td><td>%s</td>\n",test.getName(),test.getDescription(),!test.isError());
			pw.printf("<td>%s</td>\n",test.getReport());
			pw.println("<tr>");
			pw.println("</table>");
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------