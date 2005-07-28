//--------------------------------------
// SCMDServer
// 
// XMLContentTestSAXHandler.java 
// Since: 2004/07/22
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import lab.cb.scmd.util.xml.BaseHandler;

/**
 * @author leo
 *
 */
public class XMLContentTestHandler extends BaseHandler
{

    /**
     * 
     */
    public XMLContentTestHandler()
    {
        super();
    }
    

    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        super.endElement(uri, localName, qName);
        XMLElement elem = (XMLElement) _elementStack.pop();
        LinkedList elemList = (LinkedList) _tagNameToXMLElementListMap.get(qName);
        if(elemList == null)
        {
            elemList = new LinkedList();
        }
        elemList.add(elem);
        _tagNameToXMLElementListMap.put(qName, elemList);        
    }
    
    public void startDocument() throws SAXException
    {
        super.startDocument();
        
        _tagNameToXMLElementListMap.clear();
        _elementStack.clear();
        XMLElement root = null;
        _elementStack.push(root);
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        super.startElement(uri, localName, qName, attributes);
    
        XMLElement elem = new XMLElement((XMLElement) _elementStack.peek(), qName, attributes);
        _elementStack.push(elem);
    }
    
    public void textContent(String content) throws SAXException
    {
        XMLElement elem = (XMLElement) _elementStack.pop();
        elem.setTextContent(content);
        _elementStack.push(elem);
    }
    
    
    /** ëŒâûÇ∑ÇÈtagNameÇÃóvëfàÍóóÇï‘Ç∑ÅB ë∂ç›ÇµÇ»Ç¢èÍçáÇÕãÛlistÇ™ï‘Ç¡ÇƒÇ≠ÇÈ
     * @param tagName
     * @return XMLElementÇÃList
     */
    public List getXMLElementList(String tagName)
    {
        List list = (List) _tagNameToXMLElementListMap.get(tagName);
        return list == null ? new LinkedList() : list;
    }
    
    
    HashMap _tagNameToXMLElementListMap = new HashMap();
    Stack _elementStack = new Stack();
}


//--------------------------------------
// $Log: XMLContentTestHandler.java,v $
// Revision 1.1  2004/07/22 07:11:46  leo
// xercesëŒâû
//
//--------------------------------------