//--------------------------------------
// SCMDServer
// 
// ORFSelectionForm.java 
// Since: 2004/08/25
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * ÉÜÅ[ÉUÇ©ÇÁÇÃORFÇÃì¸óÕÇéÛÇØéÊÇÈform
 * @author leo
 *
 */
public class ORFSelectionForm extends ActionForm
{
    String[] _inputList = new String[0];
    String[] _colorList = new String[0];
    String _button = "add selections";
    
    FormFile file = null;
    

    /**
     * 
     */
    public ORFSelectionForm()
    {
        super();
    }
    
    

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        _inputList = new String[0];
        file = null;
    }
    
    public FormFile getFile()
    {
        return file;
    }
    

    public void setFile(FormFile file)
    {
        this.file = file;
    }
    

    
    public String[] getInputList()
    {
        return _inputList;
    }
    
    public String[] getColorList()
    {
        return _colorList;
    }
    
    public void setInputList(String[] inputList)
    {
        _inputList = inputList;
    }
    public void setColorList(String[] colorList) {
        _colorList = colorList;
    }
    
    
    public void setOrfList(String orfListText)
    {
        String[]  orfList = orfListText.trim().split("[ ,\t\n\r]+");
        if(orfList.length > 0) 
        {
            setInputList(orfList);
        }
    }
    
    public void setButton(String buttonName)
    {
        _button = buttonName;
    }
    public String getButton()
    {
        return _button;
    }
}


//--------------------------------------
// $Log: ORFSelectionForm.java,v $
// Revision 1.2  2004/08/26 08:45:52  leo
// QueryÇÃí«â¡ÅB selectionÇÃèCê≥
//
// Revision 1.1  2004/08/25 09:06:00  leo
// userselectionÇÃí«â¡
//
//--------------------------------------