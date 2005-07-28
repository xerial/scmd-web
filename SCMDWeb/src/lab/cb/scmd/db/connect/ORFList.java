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

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;

public class ORFList extends lab.cb.scmd.db.common.GeneList {
    
    public String outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        xmlout.startTag("lab.cb.scmdresult");
        xmlout.startTag("orflist"); // orflist in SCMD
        
        int size = this.size();
        for(int i = 0; i < size; i++ ) {
            String groupxml = this.getGroup(i).outputXML(xmlout);
        }
        
        xmlout.closeTag(); // for genelist
        xmlout.closeTag(); // for lab.cb.scmdresult
        return "";
    }
    
    public boolean addGroup(String groupName, String[] geneNames ) {
        ORFGroup orfGroup = new ORFGroup(groupName);
        
        int size = geneNames.length;
        for(int i = 0; i < size; i++ ) {
            ORFInformation geneInfo = new ORFInformation(geneNames[i]);
            if(geneInfo == null) {
                //TODO error handling
                return false; // gene name is invalid 
            } else {
                orfGroup.add(geneInfo);
            }
        }
        _groupNameMap.put(groupName, new Integer(_groups.size()));
        _groups.add(orfGroup);
        return true;
    }

	// ORFListGroup ではなく、ORFGroup のデータをセットするように関数を上書き
    public void setData(SCMDDBConnect dbconnect) throws SCMDException {
        int size = this.size();
        for( int i = 0; i < size; i++ ) {
            ((ORFGroup)this.getGroup(i)).setData(dbconnect);
        }
    }

}
