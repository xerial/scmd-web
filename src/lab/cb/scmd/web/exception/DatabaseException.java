//--------------------------------------
// SCMDServer
//
// DatabaseException.java
// Since: 2004/12/10
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
public class DatabaseException extends SCMDException {

    /**
     * 
     */
    public DatabaseException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public DatabaseException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public DatabaseException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public DatabaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}

//--------------------------------------
// $Log: DatabaseException.java,v $
// Revision 1.1  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
//--------------------------------------