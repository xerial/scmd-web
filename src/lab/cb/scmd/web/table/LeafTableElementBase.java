//--------------------------------------
// SCMDServer
// 
// LeafTableElementBase.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/** ���g��TableElement�ȂǍ\���������̂������Ȃ��N���X�̃x�[�X
 * @author leo
 *
 */
public abstract class LeafTableElementBase implements LeafTableElement
{
    //Properties _properties = new Properties();
    /**
     * 
     */
    public LeafTableElementBase()
    {
    }

    public Properties getProperties() {
        //return _properties;
        return new Properties();
    }
    
    
 
    public void accept(TableVisitor v) {
        v.visit(this);
    }
    
    public void setProperty(String property, String value) {
        throw new UnsupportedOperationException("cannot set property to leaf table element");
        //_properties.setProperty(property, value);
    }
    
    public Properties getCellProperty() {
        return new Properties();
    }
    
    
    public void setProperties(Properties properties) {
        throw new UnsupportedOperationException("cannot set property to leaf table element");
        //_properties = properties;
    }
}


//--------------------------------------
// $Log: LeafTableElementBase.java,v $
// Revision 1.3  2004/08/30 10:43:13  leo
// GroupBySheet�̍쐬 page�̈ړ��͂܂�
//
// Revision 1.2  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.1  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
//--------------------------------------