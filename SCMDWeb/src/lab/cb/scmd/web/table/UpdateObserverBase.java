//--------------------------------------
// SCMDServer
// 
// UpdateObserverBase.java 
// Since: 2004/08/24
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/**
 * @author leo
 *
 */
public abstract class UpdateObserverBase implements UpdateObserver
{

    /**
     * 
     */
    public UpdateObserverBase()
    {
        super();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeRowInsertion(lab.cb.scmd.web.table.Table, int)
     */
    public void beforeRowInsertion(Table table, int row, Object[] rowElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterRowInsertion(lab.cb.scmd.web.table.Table, int)
     */
    public void afterRowInsertion(Table table, int row, Object[] rowElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeColInsertion(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void beforeColInsertion(Table table, int col, Object[] colElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColInsertion(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterColInsertion(Table table, int col, Object[] colElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeRowAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void beforeRowAddition(Table table, int row, Object[] rowElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterRowAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterRowAddition(Table table, int row, Object[] rowElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeColAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void beforeColAddition(Table table, int col, Object[] colElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColAddition(lab.cb.scmd.web.table.Table, int, java.lang.Object[])
     */
    public void afterColAddition(Table table, int col, Object[] colElement) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeRowDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void beforeRowDeletion(Table table, int row) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterRowDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void afterRowDeletion(Table table, int row) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeColDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void beforeColDeletion(Table table, int col) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterColDeletion(lab.cb.scmd.web.table.Table, int)
     */
    public void afterColDeletion(Table table, int col) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#beforeUpdate(lab.cb.scmd.web.table.Table, int, int, lab.cb.scmd.web.table.TableElement)
     */
    public void beforeUpdate(Table table, int row, int col, TableElement element) {

    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.UpdateObserver#afterUpdate(lab.cb.scmd.web.table.Table, int, int, lab.cb.scmd.web.table.TableElement)
     */
    public void afterUpdate(Table table, int row, int col, TableElement element) {

    }

}


//--------------------------------------
// $Log: UpdateObserverBase.java,v $
// Revision 1.2  2004/08/24 04:23:18  leo
// merge
//
// Revision 1.1.2.2  2004/08/24 04:04:42  leo
// TableÇÃindexé¸ÇËÅApasteÇ™äÆê¨
//
// Revision 1.1.2.1  2004/08/24 02:36:12  leo
// ColIndexÇ™äÆê¨
//
//--------------------------------------