//--------------------------------------
//SCMDServer
//
//GeneInformation.java 
//Since: 2004/07/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class GeneInformation {
    private String _systematicName = "";
    private GeneNameList _geneNames = new GeneNameList();
    private String _annotation 	= "";
    protected LinkedList _parameterOrder = new LinkedList();
    protected HashMap _parameters = new HashMap();
    
	protected String _GeneTag = "gene";
	protected String _GeneIDAttribute = "id";
	protected String _StandardNameTag = "primaryname";
	protected String _StandardNameAttribute = "name";
	protected String _GeneNameTag = "aliasname";
	protected String _GeneNameAttribute = "name";
	protected String _AnnotationTag = "annotation";
	protected String _AnnotationAttribute = "name";
	protected String _ParamNameAttribute = "name";
	protected String _ParamValueTag = "parameter";

    public GeneInformation (String systematicName) {
        _systematicName = systematicName;
    }
    
    public String getSystematicName () {
        return _systematicName;
    }
    
    public void setAnnotation(String annotation) {
        _annotation = annotation;
    }
    
    public String getAnnotation() {
        return _annotation;
    }
    
    public void addGeneNames(String geneName) {
        _geneNames.add(geneName);
    }
    
    public GeneNameList getGeneNames() {
        return _geneNames;
    }
    
    public String getGeneNames(int n) {
        return _geneNames.get(n);
    }
    
    public Cell getValue(String param) {
        Object value = _parameters.get(param);
        if( value == null )
            return new Cell();
        return (Cell)value;
    }
    
    public void putValue(String param, Cell value) {
        _parameterOrder.add(param);
        _parameters.put(param, value);
    }

    public void outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        XMLAttribute geneIdNameAtt	= new XMLAttribute(_GeneIDAttribute, _systematicName);
        xmlout.startTag(_GeneTag, geneIdNameAtt); // orfname in SCMD

        int size = _geneNames.size();
        if( size > 0 ) {
        	XMLAttribute stdNameAtt = new XMLAttribute(_StandardNameAttribute, _geneNames.get(0));
        	xmlout.selfCloseTag(_StandardNameTag, stdNameAtt);
        }
        for(int i = 1; i < size; i++ ) {
            XMLAttribute geneNameAtt = new XMLAttribute(_GeneNameAttribute, _geneNames.get(i));
            xmlout.selfCloseTag(_GeneNameTag, geneNameAtt);
        }
        if( _annotation.length() > 0 ) {
            xmlout.startTag(_AnnotationTag);
            xmlout.textContent(_annotation);
            xmlout.closeTag();
        }
        Iterator it = _parameterOrder.iterator();
        while( it.hasNext() ) {
            Object param = it.next();
            Object cell = _parameters.get(param);
            if( cell != null ) {
                xmlout.startTag(_ParamValueTag, new XMLAttribute(_ParamNameAttribute, param.toString()));
                xmlout.textContent(((Cell)cell).toString());
                xmlout.closeTag();
//                XMLAttribute paramAtt = new XMLAttribute(_ParamNameAttribute, param.toString());
//                paramAtt.add(_ParamValueAttribute, ((Cell)cell).toString());
//                xmlout.startTag(_ParamTag, paramAtt);
//                xmlout.closeTag();
            }
        }
        xmlout.closeTag(); // for gene
    }
}
