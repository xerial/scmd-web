//--------------------------------------
// SCMDServer
// 
// NestedTableElementBase.java 
// Since: 2004/08/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/**
 * @author leo
 *
 */
public abstract class NestedTableElementBase implements TableElement
{
    TableElement _bodyContent;
    Properties _properties = new Properties();
    /**
     * 
     */
    public NestedTableElementBase(TableElement bodyContent)
    {
        super();
        _bodyContent = bodyContent;
    }

    public TableElement getBodyContent()
    {
        return _bodyContent;
    }
    
    public Properties getCellProperty() {
        return _bodyContent.getCellProperty();
    }
    
    
    public Properties getProperties() {
        return _properties;
    }
    


    public void setProperty(String property, String value) {
        _properties.setProperty(property, value);
    }
    
    
    public void setProperties(Properties properties) {
        _properties = properties;
    }

}


//--------------------------------------
// $Log: NestedTableElementBase.java,v $
// Revision 1.4  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.3  2004/08/09 05:25:17  leo
// タグないに複数のタグを持てるように改良
//
// Revision 1.2  2004/08/09 03:36:42  leo
// TagDecollatorを追加
//
// Revision 1.1  2004/08/09 02:10:04  leo
// Decollation, Decollatorを整理
//
//--------------------------------------