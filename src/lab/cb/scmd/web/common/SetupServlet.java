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

import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.formbean.ViewCustomizeForm;

/**
 * Tomcat
 * @author leo
 *
 */
public class SetupServlet extends HttpServlet {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256725074139427635L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        try
        {
            SCMDConfiguration.Initialize();
            System.out.println("[scmd-server] SCMDConfiguration is initialized");
                        
            ConnectionServer.initialize();
            
            
            // initialize some beans
            ViewCustomizeForm.loadParameters();
        }
        catch(SCMDException e)
        {
            throw new ServletException(e);
        }
        catch(SQLException e)
        {
            throw new ServletException(e);
        }
    }

}


//--------------------------------------
// $Log: SetupServlet.java,v $
// Revision 1.4  2004/08/23 04:07:39  leo
// 例外処理を、SCMDConfigurationにまかす
//
// Revision 1.3  2004/08/12 17:33:41  leo
// reloadボタンを追加
//
// Revision 1.2  2004/07/17 05:25:56  leo
// InvalidPathExceptionを取り除きました
//
// Revision 1.1  2004/07/16 16:35:51  leo
// web moduleを、servから、lab.cb.scmd-serverに変更しました
//
//--------------------------------------