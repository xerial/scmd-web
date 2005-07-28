//--------------------------------------
//SCMDServer
//
//GeneList.java 
//Since:  2004/07/16
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.connect;

import java.util.Iterator;

import lab.cb.scmd.db.common.GeneInformation;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class ORFGroup extends lab.cb.scmd.db.common.GeneGroup {

    public ORFGroup(String groupName) {
        super(groupName);
    }
    
    public void add(ORFInformation gene) {
        _genes.add(gene);
        _genesMap.put(gene.getSystematicName(), gene);
    }

    public String outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        XMLAttribute geneGroupAttribute = new XMLAttribute("type", this.getName());
        xmlout.startTag("orfgroup", geneGroupAttribute); // orfgroup in SCMD
        this.getPageStatus().outputXML(xmlout);
        int size = this.size();
        for(int i = 0; i < size; i++ ) {
            ((ORFInformation)this.get(i)).outputXML(xmlout);
        }
        
        xmlout.closeTag(); // orfgroup
        return null;
    }

    public void setData(SCMDDBConnect dbconnect) throws SCMDException {
        String[] columns = {"systematicname", "primaryname", "aliasname", "annotation"};
        int columnslength = columns.length;

        String[] params = new String [_parameters.size()];
        for( int i = 0; i < _parameters.size(); i++ ) {
            params[i] = _parameters.get(i).toString();
        }
        
        BasicTable bt = dbconnect.geneGroupQuery(_genes, this.getPageStatus(), columns, params);
        this.getPageStatus().setMaxPageByElements(bt.getRowSize());
        if( _genes.size() == 0 ) {
        	int curpage = this.getPageStatus().getCurrentPage();
        	int pageStartOrfNo 	= (curpage - 1)* this.getPageStatus().getMaxElementsInAPage();
        	int pageEndOrfNo 	= curpage* this.getPageStatus().getMaxElementsInAPage();
        	for( int i = pageStartOrfNo; i < pageEndOrfNo && i < bt.getRowSize(); i++ ) {
                ORFInformation geneInfo = new ORFInformation(bt.getCell(i, "systematicname").toString());
                add(geneInfo);
        	}
        }
        Iterator it = _genes.iterator();
        while( it.hasNext() ) {
            GeneInformation gene = (GeneInformation)it.next();
            setGeneInformation(bt, gene, params);
//            if( !setGeneInformation(bt, gene, params) )
//                it.remove(); // remove invisible or no information orfs
        }
    }
}
