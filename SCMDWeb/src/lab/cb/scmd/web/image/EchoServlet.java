//--------------------------------------
// SCMDServer
// 
// EchoServlet.java 
// Since: 2004/09/06
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;

/**
 * @author leo
 *
 */
public class EchoServlet extends HttpServlet
{

    /**
     * 
     */
    public EchoServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String cmd = request.getParameter("cmd");
		response.setContentType("text/plain");		
		
		try
        {
            ProcessRunner.run(response.getOutputStream(), cmd);
        }
        catch (UnfinishedTaskException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}


//--------------------------------------
// $Log: EchoServlet.java,v $
// Revision 1.1  2004/09/06 07:26:57  leo
// *** empty log message ***
//
//--------------------------------------