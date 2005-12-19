//--------------------------------------
//SCMDServer
//
//PageStatus.java 
//Since:  2004/07/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.io.Serializable;

import org.w3c.dom.Element;

import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.XMLParseErrorException;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy$
 */
public class PageStatus implements Serializable {
    private int _current	= 0;
    private int _max		= 0;
    private int _maxelements= 0;
    
    public PageStatus(){}
    
    public PageStatus(int current, int max ) {
        _current = current;
        _max = max;
    }
    
    public PageStatus(int current, int max, int elements) {
    	_current = current;
    	_max	 = max;
    	_maxelements	= elements;
    }
    
    public PageStatus(Element pageElement) throws XMLParseErrorException
    {
        parse(pageElement);
    }
    
    public void parse(Element pageElement) throws XMLParseErrorException
    {
        if(!pageElement.getTagName().equals("page"))
            throw new XMLParseErrorException("invalid xml");
        
        try
        {
            setCurrentPage(Integer.parseInt(pageElement.getAttribute("current")));
            setMaxPage(Integer.parseInt(pageElement.getAttribute("max")));
            setMaxElementsInAPage(Integer.parseInt(pageElement.getAttribute("elements")));
        }
        catch(NumberFormatException e)
        {
            throw new XMLParseErrorException(e);
        }
    }
    
    public int getCurrentPage() {
        return _current;
    }
    
    public void setCurrentPage(int current) {
        _current = current;
    }
    
    public int getMaxPage() {
        return _max;
    }
    
    
    public void setMaxPage(int max) {
        _max = max;
    }
    
    public void setMaxPageByElements(int elements) {
    	_max = (int)Math.ceil(elements / (double)_maxelements);
    }
    
    public void setMaxElementsInAPage(int maxelements) {
    	_maxelements = maxelements;
    }
    
    public int getMaxElementsInAPage() {
    	return _maxelements;
    }
    
    public void nextPage() {
    	_current++;
    	normalization();
    }

    public void nextPage(int n) {
    	_current += n;
    	normalization();
    }
    
    public void previousPage() {
    	_current--;
    	normalization();
    }
    
    public void previousPage(int n) {
    	_current -= n;
    	normalization();
    }
    
    public void normalization() {
    	while( _current < 1 )
    		_current += _max;
    	if ( _current > _max ) 
    		_current = _current % _max;
    }

    public String toSQL() {
        int offset = ( _current - 1 ) * _max;
        String sql = " LIMIT " + _max + " OFFSET " + offset;
        return sql;
    }
    

    public void outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        XMLAttribute pageattribute = new XMLAttribute("current", _current + "");
        pageattribute.add("max", _max + "");
        pageattribute.add("elements", _maxelements + "");
        xmlout.startTag("page", pageattribute);
        xmlout.closeTag();
    }
}
