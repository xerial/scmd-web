//--------------------------------------
// SCMD Project
// 
// SetupServlet.java 
// Since:  2004/07/17
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import lab.cb.scmd.exception.SCMDException;

/**
 * @author leo
 *
 */
public class SetupServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try
        {
            SCMDConfiguration.Initialize();
            System.out.println("SCMDConfiguration is initialized");
        }
        catch(SCMDException e)
        {
            throw new ServletException(e);
        }
    }

}


//--------------------------------------
// $Log: SetupServlet.java,v $
// Revision 1.4  2004/08/23 04:07:39  leo
// ��O�������ASCMDConfiguration�ɂ܂���
//
// Revision 1.3  2004/08/12 17:33:41  leo
// reload�{�^����ǉ�
//
// Revision 1.2  2004/07/17 05:25:56  leo
// InvalidPathException����菜���܂���
//
// Revision 1.1  2004/07/16 16:35:51  leo
// web module���Aserv����Alab.cb.scmd-server�ɕύX���܂���
//
//--------------------------------------