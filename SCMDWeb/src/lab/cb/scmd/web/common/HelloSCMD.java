//--------------------------------------
// SCMDServer
// 
// HelloSCMD.java 
// Since: 2004/07/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;

/**
 * @author leo
 *
 */
public class HelloSCMD extends HttpServlet
{

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
       	PrintStream out = new PrintStream(response.getOutputStream());
       	response.setContentType("text/html");
       	
       	try
       	{
       	    XMLOutputter htmlout = new XMLOutputter(out);
       	    htmlout.startTag("html");
       	    htmlout.startTag("body");
       	    htmlout.textContent("Hello SCMD!");
       	    
       	    htmlout.endOutput();
       	}
       	catch(InvalidXMLException e)
       	{	
       	    e.what(System.err);
       	}
    }
}


//--------------------------------------
// $Log: HelloSCMD.java,v $
// Revision 1.2  2004/07/07 07:53:02  leo
// compile pass
//
// Revision 1.1  2004/07/07 04:10:10  leo
// temp
//
//--------------------------------------