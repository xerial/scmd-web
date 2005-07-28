//--------------------------------------
// SCMDServer
//
// GeneNameReaderSAXHandler.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;


import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import lab.cb.scmd.util.xml.BaseHandler;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeneNameReaderSAXHandler extends BaseHandler
{

    /**
     * 
     */
    public GeneNameReaderSAXHandler() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        
        if(qName.equals("orf"))
        {
            String _orfName = attributes.getValue("orfname");
            return;
        }
        if(qName.equals("genename"))
        {
            String geneName = attributes.getValue("name");
            _geneNameList.add(geneName);
        }
    }
    
    public String getOrfname()
    {
        return _orfName;
    }
    
    public List getGeneNameList()
    {
        return  _geneNameList;
    }
    
    String _orfName = "";
    LinkedList _geneNameList = new LinkedList();
}


//--------------------------------------
// $Log: GeneNameReaderSAXHandler.java,v $
// Revision 1.1  2004/07/26 11:19:11  leo
// Yeast Mutants page—p‚ÌƒNƒ‰ƒX‚ð’Ç‰Á
//
//--------------------------------------
