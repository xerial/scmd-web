//--------------------------------------
//SCMDServer
//
//GeneList.java 
//Since:  2004/07/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.ArrayList;
import java.util.HashMap;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLOutputter;

public class GeneList {
    protected ArrayList _groups = new ArrayList(); //ArrayList of ORFListGroup
    protected HashMap	_groupNameMap = new HashMap();
    
    public GeneList() {
    }
    
    /*
     * Each element of genes is ORF name of a gene. 
     */
    public boolean addGroup(String groupName, String[] geneNames ) {
        GeneGroup geneGroup = new GeneGroup(groupName);
        
        int size = geneNames.length;
        for(int i = 0; i < size; i++ ) {
            GeneInformation geneInfo = new GeneInformation(geneNames[i]);
            if(geneInfo == null) {
                //TODO error handling
                return false; // gene name is invalid 
            } else {
                geneGroup.add(geneInfo);
            }
        }
        _groupNameMap.put(groupName, new Integer(_groups.size()));
        _groups.add(geneGroup);
        return true;
    }
    
    public boolean addGroup(String groupName, String[] geneNames, DBConnect dbconnect ) throws SCMDException {
        String[] systematicNames = convertSystematicName(geneNames, dbconnect);
        return addGroup(groupName, systematicNames);
    }
    
    private String[] convertSystematicName(String[] geneNames, DBConnect dbconnect) throws SCMDException {
        String[] systematicNames = dbconnect.toSystematicNames(geneNames);
        return systematicNames;
    }

    public GeneGroup getGroup(int n) {
        return (GeneGroup)_groups.get(n);
    }
    
    public GeneGroup getGroup(String str) {
        Object groupno = _groupNameMap.get(str);
        if( groupno == null ) {
            //TODO Error or assert handling
            return null;
        }
        return getGroup(((Integer)groupno).intValue());
    }

    public int size() {
        return _groups.size();
    }
    
    public GeneGroup removeGroup(int n) {
        GeneGroup removed = (GeneGroup)_groups.remove(n);
        _groupNameMap.remove(removed.getName());
        return removed;
    }
    
    public GeneGroup removeGroup(String str) {
        Object removedno = _groupNameMap.remove(str);
        GeneGroup removed = (GeneGroup)_groups.remove(((Integer)removedno).intValue());
        return removed;
    }
    
    public void setMaxElementsInAPage(String groupName, int max) {
        getGroup(groupName).setMaxElementsInAPage(max);
    }
    
    public void setCurrentPage(String groupName, int current) {
        getGroup(groupName).setCurrentPage(current);
    }

    public void addParameter(String paramName) {
        for(int i = 0; i < _groups.size(); i++ ) {
            addParameter( ((GeneGroup)_groups.get(i)).getName(), paramName);
        }
    }
    
    public void addParameter(String groupName, String paramName) {
        getGroup(groupName).addParameter(paramName);
    }

    public String outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        xmlout.startTag("phenomeresult");
        xmlout.startTag("genelist"); // orflist in SCMD
        
        int size = _groups.size();
        for(int i = 0; i < size; i++ ) {
            String groupxml = ((GeneGroup)(_groups.get(i))).outputXML(xmlout);
        }
        
        xmlout.closeTag(); // for genelist
        xmlout.closeTag(); // for lab.cb.scmdresult
        return "";
    }

    public void setData(DBConnect dbconnect) throws SCMDException {
        int size = _groups.size();
        // 各グループに対して、データをデータベースから取得し
        // セットする
        for( int i = 0; i < size; i++ ) {
            ((GeneGroup)_groups.get(i)).setData(dbconnect);
        }
    }
    
}
