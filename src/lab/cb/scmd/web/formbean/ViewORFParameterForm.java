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
 * �s�FORF�A��FORF�p�����[�^�@�̃f�[�^�V�[�g��\�����邽�߂̃t�H�[��
 * @author leo
 *
 */
public class ViewORFParameterForm extends ActionForm
{
    public enum ColumnType 
    { 
        input, // ���͂��ꂽ�p�����[�^��\�� 
        custom // customize��ʂőI�����ꂽ�p�����[�^��\��
    }

    String format = "";
    Integer[] paramID = new Integer[] {}; // �f�[�^�V�[�g�ɕ\������parameter ID
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
