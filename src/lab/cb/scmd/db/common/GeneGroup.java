//--------------------------------------
//SCMDServer
//
//ORFListGroup.java 
//Since:  2004/07/09
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class GeneGroup {
    private static int FIRSTPAGENO		 = 1;
    private static int MAXPAGENO		= 1;
    private static int MAXGENESINAPAGE  = 10; 
    private String 		_groupName = "";
    protected LinkedList 	_genes = new LinkedList(); // ArrayList of GeneInformation
    protected HashMap   	_genesMap = new HashMap();
    private PageStatus 	_page = new PageStatus(FIRSTPAGENO, MAXPAGENO, MAXGENESINAPAGE);
    protected ArrayList	_parameters = new ArrayList();
    
    public GeneGroup(String groupName) {
        _groupName = groupName;
    }
    
    public String getName() {
        return _groupName;
    }
    
    public void add(GeneInformation gene) {
        _genes.add(gene);
        _genesMap.put(gene.getSystematicName(), gene);
    }
    
    public GeneInformation get(int n) {
        return (GeneInformation)_genes.get(n);
    }
    
    public int size() {
        return _genes.size();
    }

    public void setMaxPage(int max) {
        _page.setMaxPage(max);
    }
    
    public void setCurrentPage(int current) {
        _page.setCurrentPage(current);
    }
    
    public void setMaxElementsInAPage(int elements) {
    	_page.setMaxElementsInAPage(elements);
    }
    
    public PageStatus getPageStatus() {
        return _page;
    }

    public String outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        XMLAttribute geneGroupAttribute = new XMLAttribute("type", _groupName);
        xmlout.startTag("genegroup", geneGroupAttribute); // orfgroup in SCMD
        _page.outputXML(xmlout);
        int size = _genes.size();
        for(int i = 0; i < size; i++ ) {
            ((GeneInformation)_genes.get(i)).outputXML(xmlout);
        }
        
        xmlout.closeTag(); // orfgroup
        return null;
    }
    
    protected boolean setGeneInformation(BasicTable bt, GeneInformation gene, String[] params) {
        String systematicName = gene.getSystematicName();
        
        if( bt.getRowIndex(systematicName) < 0 )
            return false;

        // set primaryname
        Cell primaryname = bt.getCell(systematicName, "primaryname");
        if( primaryname != null ) {
            gene.addGeneNames(primaryname.toString());
        }
        // set aliasname
        Cell aliasname = bt.getCell(systematicName, "aliasname");
        if( aliasname != null ) {
            String[] aliases = (aliasname.toString()).split("[|\t]");
            for( int j = 0; j < aliases.length; j++ ) {
                if ( aliases[j].length() > 0 )
                    gene.addGeneNames(aliases[j]);
            }
        }
        // set annotation
        Cell annotation = bt.getCell(systematicName, "annotation");
        if( annotation != null )
            gene.setAnnotation(annotation.toString());
        
        for( int i = 0; i < params.length; i++ ) {
            Cell value = bt.getCell(systematicName, params[i]);
            gene.putValue( params[i], value );
        }
        return true;
    }

    public void addParameter(String paramName) {
        _parameters.add(paramName);
    }
    
    public void setData(DBConnect dbconnect) throws SCMDException {
        String[] columns = {"systematicname", "primaryname", "aliasname", "annotation"};
        int columnslength = columns.length;

        String[] params = new String [_parameters.size()];
        for( int i = 0; i < _parameters.size(); i++ ) {
            params[i] = _parameters.get(i).toString();
        }
        
        BasicTable bt = dbconnect.geneGroupQuery(_genes, _page, columns, params);
        Iterator it = _genes.iterator();
        while( it.hasNext() ) {
            GeneInformation gene = (GeneInformation)it.next();
            if( !setGeneInformation(bt, gene, params) )
                it.remove();
            
        }
    }
}
