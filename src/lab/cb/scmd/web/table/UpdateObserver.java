//--------------------------------------
// SCMDServer
// 
// UpdateObserver.java 
// Since: 2004/08/23
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/** Tableに更新操作が行われたときに、実行される操作を記述する
 * @author leo
 *
 */
public interface UpdateObserver
{
    /**
     * tableから、observerを切り離すときに使う
     */
    void invalidate();
    
    void beforeRowInsertion(Table table, int row, Object[] rowElement);
    void afterRowInsertion(Table table, int row, Object[] rowElement);
    
    void beforeColInsertion(Table table, int col, Object[] colElement);
    void afterColInsertion(Table table, int col, Object[] colElement);

    void beforeRowAddition(Table table, int row, Object[] rowElement);
    void afterRowAddition(Table table, int row, Object[] rowElement);
    
    void beforeColAddition(Table table, int col, Object[] colElement);
    void afterColAddition(Table table, int col, Object[] colElement);

    void beforeRowDeletion(Table table, int row);
    void afterRowDeletion(Table table, int row);
    
    void beforeColDeletion(Table table, int col);
    void afterColDeletion(Table table, int col);
    
    void beforeUpdate(Table table, int row, int col, TableElement element);
    void afterUpdate(Table table, int row, int col, TableElement element);
    
}


//--------------------------------------
// $Log: UpdateObserver.java,v $
// Revision 1.2  2004/08/24 04:23:18  leo
// merge
//
// Revision 1.1.2.3  2004/08/24 04:04:42  leo
// Tableのindex周り、pasteが完成
//
// Revision 1.1.2.2  2004/08/24 02:36:12  leo
// ColIndexが完成
//
// Revision 1.1.2.1  2004/08/23 07:08:26  leo
// branchを作って、ObserverによるTableラベルの作成開始
//
//--------------------------------------