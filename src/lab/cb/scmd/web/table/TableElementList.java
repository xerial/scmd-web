//--------------------------------------
// SCMDServer
// 
// TableElementList.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

/** TableElement�̃��X�g���ЂƂ̃Z���ɓ��ꂽ���Ƃ��Ɏg���B

 * @author leo
 *
 */
public class TableElementList extends LinkedList implements TableElement
{
    /**
     * 
     */
    public TableElementList()
    {
    }

    
    
    /* 
     * TableElement�̔h���N���X�ӊO�̏ꍇ�́A
     * �����I�ɁAStringElement(obj)�ɕϊ������
     */
    public boolean add(Object obj) {
        return super.add(new StringElement(obj.toString()));
    }
    public boolean add(TableElement elem)
    {
        return super.add(elem);
    }
    
    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableElement#accept(lab.cb.scmd.web.table.TableVisitor)
     */
    public void accept(TableVisitor v) {
        v.visit(this);
    }

    public Properties getProperties() {
        return new Properties();
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableElement#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(String property, String value) {
        // do nothing
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableElement#setProperties(java.util.Properties)
     */
    public void setProperties(Properties properties) {
        // do nothing;
    }


    /* (non-Javadoc)
     * @see lab.cb.scmd.web.table.TableElement#getCellProperty()
     */
    public Properties getCellProperty() {
        Properties prop = new Properties();
        for(Iterator it = this.iterator(); it.hasNext(); )
        {
            TableElement elem = (TableElement) it.next();
            Properties p = elem.getCellProperty();
            for(Iterator keyIt = p.keySet().iterator(); keyIt.hasNext(); )
            {
                // copy descendants' properties
                String key = (String) keyIt.next();
                prop.setProperty(key, p.getProperty(key));
            }
        }
        return prop;
    }
    
    /*
    public String toString()
    {
        String str ="";
        for(Iterator it = this.iterator(); it.hasNext(); )
        {
            str += ((TableElement) it.next()).toString();
        }
        return str;
    }
    */

}


//--------------------------------------
// $Log: TableElementList.java,v $
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.1  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
//--------------------------------------