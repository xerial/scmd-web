//--------------------------------------
// SCMDServer
// 
// DBConnectException.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.exception;


/**
 * @author leo
 *
 */
public class DBConnectException extends DatabaseException
{

    /**
     * 
     */
    public DBConnectException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public DBConnectException(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public DBConnectException(Throwable arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public DBConnectException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}


//--------------------------------------
// $Log: DBConnectException.java,v $
// Revision 1.2  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------