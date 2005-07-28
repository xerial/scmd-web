//--------------------------------------
// SCMDServer
// 
// ORFGroup.java 
// Since: 2004/08/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.exception.XMLParseErrorException;

/**
 * @author leo
 *
 */
public class ORFGroup
{
    String groupName;
    Vector orfList = new Vector();
    PageStatus pageStatus = new PageStatus();
    
    /**
     * 
     */
    public ORFGroup()
    {
        super();
    }
    
    public ORFGroup(Element orfgroupElement) throws SCMDException
    {
        parse(orfgroupElement);
    }
    
    public void parse(Element orfgroupElement) throws XMLParseErrorException
    {
        if(!orfgroupElement.getTagName().equals("orfgroup"))
            throw new XMLParseErrorException("invalid XML element");
    
        setGroupName(orfgroupElement.getAttribute("type"));
        
        NodeList nodeList = orfgroupElement.getElementsByTagName("page");
        if(nodeList.getLength() > 0)
        {
            Element pageElement = (Element) nodeList.item(0);
            pageStatus = new PageStatus(pageElement);            
        }
        
        NodeList orfElementList = orfgroupElement.getElementsByTagName("orf");
        for(int i=0; i<orfElementList.getLength(); i++)
        {
            Element orf = (Element) orfElementList.item(i);
            YeastGene gene = new YeastGene(orf);
            orfList.add(gene);
        }
    }
    

    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    
    public Vector getOrfList() {
        return orfList;
    }
    public void setOrfList(Vector orfList) {
        this.orfList = orfList;
    }
    public PageStatus getPageStatus() {
        return pageStatus;
    }
    public void setPageStatus(PageStatus pageStatus) {
        this.pageStatus = pageStatus;
    }
}


//--------------------------------------
// $Log: ORFGroup.java,v $
// Revision 1.1  2004/08/26 08:45:52  leo
// Query‚Ì’Ç‰ÁB selection‚ÌC³
//
//--------------------------------------