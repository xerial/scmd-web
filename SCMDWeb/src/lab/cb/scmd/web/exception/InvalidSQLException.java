

package lab.cb.scmd.web.exception;

import java.io.PrintStream;

public class InvalidSQLException extends DatabaseException {

	/* (non-Javadoc)
	 * @see lab.cb.scmd.exception.SCMDException#what()
	 */
	public void what() {
		// TODO Auto-generated method stub
		super.what();
	}
	/* (non-Javadoc)
	 * @see lab.cb.scmd.exception.SCMDException#what(java.io.PrintStream)
	 */
	public void what(PrintStream outputStream) {
		// TODO Auto-generated method stub
		super.what(outputStream);
	}
	
    /**
     * 
     */
    public InvalidSQLException()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param arg0
     */
    public InvalidSQLException(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param arg0
     * @param arg1
     */
    public InvalidSQLException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param arg0
     */
    public InvalidSQLException(Throwable arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
}
