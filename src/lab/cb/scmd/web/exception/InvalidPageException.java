//--------------------------------------
// SCMDServer
// 
// InvalidPageException.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.exception;


import lab.cb.scmd.exception.SCMDException;

/**
 * @author leo
 *
 */
public class InvalidPageException extends SCMDException
{
    int page = 1;
    int maxPage = 1; 

    public int getPage() 
    {
        return page;
    }
    public int getMaxPage()
    {
        return maxPage;
    }
    
    /**
     * 
     */
    public InvalidPageException(int invalidPage, int maxPage)
    {
        super();
        page = invalidPage;
        this.maxPage = maxPage;
    }
    /**
     * @param arg0
     */
    public InvalidPageException(String arg0)
    {
        super(arg0);
    }
    /**
     * @param arg0
     * @param arg1
     */
    public InvalidPageException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }
    /**
     * @param arg0
     */
    public InvalidPageException(Throwable arg0)
    {
        super(arg0);
    }

}


//--------------------------------------
// $Log: InvalidPageException.java,v $
// Revision 1.3  2004/08/14 11:09:08  leo
// Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
// Revision 1.2  2004/08/13 09:29:58  leo
// DBから読み取ったmaxPageを返すようにした
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------