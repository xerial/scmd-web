//--------------------------------------
// SCMDWeb Project
//
// SCMDThreadManager.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.common;

import lab.cb.common.thread.ThreadManager;

/**
 * @author leo
 *
 */
public class SCMDThreadManager 
{
    private ThreadManager manager = null;
    static private SCMDThreadManager _instance = null;
    
    static public void initialize(int maximumThread, int taskQueueSize)
    {
        if(_instance != null)
            _instance.manager.terminateAll();
        
        _instance = new SCMDThreadManager(maximumThread, taskQueueSize);
    }
    
    /**
     * 
     */
    private SCMDThreadManager(int maximumThread, int taskQueueSize)
    {
        manager = new ThreadManager(maximumThread, taskQueueSize);
    }
    
    
    static public void addTask(Runnable task)
    {
        try
        {
            _instance.manager.addTask(task);
        }
        catch(InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
    
    static public void joinAll()
    {
        try
        {
            _instance.manager.joinAll();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


}




