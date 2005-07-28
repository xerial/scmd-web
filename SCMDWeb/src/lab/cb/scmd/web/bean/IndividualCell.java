//--------------------------------------
// SCMDWeb Project
//
// IndividualCell.java
// Since: 2005/04/30
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import lab.cb.scmd.web.common.StainType;




/**
 * IndividualDataSheetÇÃàÍçsÇÇ†ÇÁÇÌÇ∑ÉNÉâÉX
 * @author leo
 *
 */
public class IndividualCell
{
    public TreeMap<String, String> valueMap = new TreeMap<String, String>();
    public String[] imageIDArray = new String[StainType.STAIN_MAX];
    int width = 0;
    int height = 0;
    
    /**
     * 
     */
    public IndividualCell()
    {
        super();
    }
    
    public String[] getImageID()
    {
        return imageIDArray;
    }
    public void setImageID(int stainType, String imageID)
    {
        assert StainType.isValid(stainType);
        imageIDArray[stainType] = imageID;
    }

    public Map getValue()
    {
        return valueMap;
    }

    public String getValue(String paramName)
    {
        String value = valueMap.get(paramName);
        if(value == null)
            return "";
        else
            return value;
    }
    
    public void setValue(String paramName, Object value)
    {
        if(value != null)
            valueMap.put(paramName, value.toString());
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
    
    

}




