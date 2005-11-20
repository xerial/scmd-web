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

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.ParamPlotForm;
import lab.cb.scmd.web.formbean.ViewCustomizeForm;
import lab.cb.scmd.web.log.SCMDLogging;

/**
 * web application�̋N�����i�ċN�����ɂ��j�ŏ��Ɉ�񂾂����s���鏈�����܂Ƃ߂��N���X
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
//        SCMDManager.initialize();
//        ConnectionServer.initialize();

        try{
        	SCMDLogging.Initialize(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("LOG_FILEPATH"),Level.ALL,true);
            System.out.println("[scmd-server] SCMDLogging is initialized");
        } catch(IOException e) {
        	
        }
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
     * ���������ɕK�v�ȑ���������ɂ܂Ƃ߂ď���
     * @throws SCMDException
     */
    private void setup() throws SCMDException
    {
        try
        {
            SCMDThreadManager.initialize(Integer.parseInt(SCMDConfiguration.getProperty("NUM_WORKER_THREAD", "50")),
                    Integer.parseInt(SCMDConfiguration.getProperty("TASK_QUEUE_SIZE", "100")));
            ViewCustomizeForm.loadParameters();
            ParamPlotForm.init();
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
        SCMDThreadManager.joinAll();
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