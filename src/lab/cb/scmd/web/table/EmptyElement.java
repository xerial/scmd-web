//--------------------------------------
// SCMDServer
// 
// EmptyElement.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/** Table�̗̈���̋󔒂́A���ׂĂ��̗v�f�Ŗ��߂���
 * @author leo
 *
 */
public class EmptyElement extends LeafTableElementBase
{
    /**
     * 
     */
    public EmptyElement()
    {
        super();
    }

    public void accept(TableVisitor v) {
        v.visit(this);
    }


    public Properties getProperties() {
        return new Properties();
    }
    public void setProperty(String property, String value) {
        // do nothing
    }
    
    public String toString()
    {
        return "";
    }
}


//--------------------------------------
// $Log: EmptyElement.java,v $
// Revision 1.1  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.4  2004/08/09 09:32:00  leo
// EmptyCell��toString() ��ݒ�
//
// Revision 1.3  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
//--------------------------------------