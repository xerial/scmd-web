//-----------------------------------
// SCMDWeb Project
// 
// ViewORFParameterForm.java 
// Since: 2005/03/15
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.formbean;

import org.apache.struts.action.ActionForm;



/**
 * 行：ORF、列：ORFパラメータ　のデータシートを表示するためのフォーム
 * @author leo
 *
 */
public class ViewORFParameterForm extends ActionForm
{
    public enum ColumnType 
    { 
        input, // 入力されたパラメータを表示 
        custom // customize画面で選択されたパラメータを表示
    }

    String format = "";
    Integer[] paramID = new Integer[] {}; // データシートに表示するparameter ID
    ColumnType columnType = ColumnType.input;
    
    /**
     * @return Returns the format.
     */
    public String getFormat()
    {
        return format;
    }
    /**
     * @param format The format to set.
     */
    public void setFormat(String format)
    {
        this.format = format;
    }
    
    
    
    
    /**
     * @return Returns the paramID.
     */
    public Integer[] getParamID()
    {
        return paramID;
    }
    /**
     * @param paramID The paramID to set.
     */
    public void setParamID(Integer[] paramID)
    {
        this.paramID = paramID;
    }
    
    
    /**
     * @return Returns the columnType.
     */
    public ColumnType getColumnType()
    {
        return columnType;
    }
    /**
     * @param columnType The columnType to set.
     */
    public void setColumnType(String columnType)
    {
        this.columnType = ColumnType.valueOf(columnType);
    }
}
