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
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.formbean.ViewCustomizeForm;

/**
 * web applicationの起動時（再起動時にも）最初に一回だけ実行する処理をまとめたクラス
 * @author leo
 *
 */
public class SetupServlet extends HttpServlet implements ConfigObserver, ServletContextListener {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256725074139427635L;

    /*
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        
        try
        {
            SCMDConfiguration.Initialize();
            System.out.println("[scmd-server] SCMDConfiguration is initialized");
                         
            ConnectionServer.initialize();

            setup();
            
            // register myself
            SCMDConfiguration.addObserver(this);
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
    */

    public void initialSetup() throws SCMDException, SQLException
    {
        SCMDConfiguration.Initialize();
        System.out.println("[scmd-server] SCMDConfiguration is initialized");
        
        ConnectionServer.initialize();
        
        setup();
        
        // register myself
        SCMDConfiguration.addObserver(this);
    }
    
    
    
    // @see lab.cb.scmd.web.common.ConfigObserver#reloaded()
    public void reloaded() 
    {
        try
        {
            setup();
        }
        catch(SCMDException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 初期化時に必要な操作をここにまとめて書く
     * @throws SCMDException
     */
    private void setup() throws SCMDException
    {
        try
        {
            ViewCustomizeForm.loadParameters();
        }
        catch(SQLException e)
        {   
            throw new SCMDException(e);
        }
    }

    // @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
    public void contextInitialized(ServletContextEvent arg0)
    {
        try
        {
            initialSetup();
        }
        catch (SCMDException e)
        {
            e.printStackTrace(System.out);
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
    }

    // @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
    public void contextDestroyed(ServletContextEvent arg0)
    {
        ConnectionServer.dispose();        
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