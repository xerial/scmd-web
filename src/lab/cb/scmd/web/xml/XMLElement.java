//--------------------------------------
// SCMDServer
// 
// XMLElement.java 
// Since: 2004/07/22
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;

import lab.cb.scmd.util.xml.XMLAttribute;



/**
 * @author leo
 *
 */
public class XMLElement
{
    /**
     * 
     */
    public XMLElement(XMLElement parent, String startTag, Attributes attrib)
    {
        _parent = parent; 
        _startTag = startTag;
        for(int i=0; i<attrib.getLength(); i++)
        {
            _attributes.add(attrib.getQName(i), attrib.getValue(i));
        }
    }
   
    public String getTagName()
    {
        return _startTag;
    }
    
    public XMLAttribute getAttributes()
    {
        return _attributes;
    }
    public XMLElement getParent()
    {
        return _parent;
    }
    
    public String getAttributeValue(String attributeName)
    {
        String value = _attributes.getValue(attributeName);
        return value == null ? "" : value;
    }
    public int getAttributeIntValue(String attributeName)
    {
        String value = getAttributeValue(attributeName);
        return Integer.parseInt(value);
    }
    
    public String getTextContent()
    {
        return _textContent;
    }
    
    public int getTextContentAsInt()
    {
        return Integer.parseInt(_textContent);
    }
    
    public void setTextContent(String textContent)
    {
        _textContent = textContent;
    }
    
    public boolean testAttributeValue(String attributeName, String value)
    {
        String attributeValue = _attributes.getValue(attributeName);
        return (attributeValue == null) ? false : attributeValue.equals(value);
    }
    
    public boolean testAttributeValueRange(String attributeName, int min, int max)
    {
        int value;
        try
        {
            value = getAttributeIntValue(attributeName);
        }
        catch(NumberFormatException e)
        {
            return false; // not a number
        }
        return (min <= value) && (value <= max);        
    }
    
    public boolean testAttributeValue(String attributeName, String[] possibleValue)
    {
        String value = _attributes.getValue(attributeName);
        if(value == null)
            return possibleValue.length == 0; 
        for(int i=0; i<possibleValue.length; i++)
        {
            if(value.equals(possibleValue[i]))
                return true;
        }
        return false;
    }

    public boolean testTextContent(String content)
    {
        return _textContent.equals(content);
    }
    
    public boolean testTextContentWithRegex(String regularExpression)
    {
        Pattern p = Pattern.compile(regularExpression);
        Matcher m = p.matcher(regularExpression);
        return m.matches();
    }
    
    
    String _startTag;
    XMLAttribute _attributes = new XMLAttribute();
    String _textContent = "";
    
    XMLElement _parent;
}


//--------------------------------------
// $Log: XMLElement.java,v $
// Revision 1.1  2004/07/22 07:11:46  leo
// xerces‘Î‰ž
//
//--------------------------------------