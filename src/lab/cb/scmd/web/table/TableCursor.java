//--------------------------------------
// SCMDServer
// 
// TableCursor.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

/**
 * @author leo
 *
 */
public interface TableCursor
{
//    public void beginningOfRow();
//    public void beginningOfCol();
//    public void endOfRow();
//    public void endOfCol();
    
    public TableElement get();
    public void set(TableElement element);

    public void nextRowHead();
    public void nextColHead();
    
    public TableElement right();
    public TableElement left();
    public TableElement up();
    public TableElement down();

    public boolean hasRight();
    public boolean hasLeft();
    public boolean hasUp();
    public boolean hasDown();
}


//--------------------------------------
// $Log: TableCursor.java,v $
// Revision 1.2  2004/08/07 14:30:07  leo
// TableCursorÇí«â¡
//
// Revision 1.1  2004/08/02 07:56:25  leo
// ñ¢äÆê¨
//
//--------------------------------------