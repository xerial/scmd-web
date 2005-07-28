//--------------------------------------
//SCMDServer
//
//ORFInformation.java 
//Since:  2004/07/16
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.connect;

import lab.cb.scmd.db.common.GeneInformation;

public class ORFInformation extends GeneInformation {

    public ORFInformation (String systematicName) {
        super(systematicName);

        _GeneTag = "orf";
    	_GeneIDAttribute = "orfname";
    	_StandardNameTag = "standardname";
    	_StandardNameAttribute = "name";
    	_GeneNameTag = "alias";
    	_GeneNameAttribute = "name";
    	_AnnotationTag = "annotation";
    	_AnnotationAttribute = "name";
    	_ParamNameAttribute = "name";
    	_ParamValueTag = "value";
    }
//
//    public void outputXML(XMLOutputter xmlout) throws InvalidXMLException {
//        XMLAttribute geneIdNameAtt	= new XMLAttribute("orfname", this.getSystematicName());
//        xmlout.startTag("orf", geneIdNameAtt); // orfname in SCMD
//
//        int size = this.getGeneNames().size();
//        for(int i = 0; i < size; i++ ) {
//            XMLAttribute geneNameAtt = new XMLAttribute("name", this.getGeneNames().get(i));
//            xmlout.startTag("genename", geneNameAtt);
//            xmlout.closeTag(); // genename
//        }
//        if( this.getAnnotation().length() > 0 ) {
//            xmlout.startTag("annotation");
//            try {
//                xmlout.textContent(this.getAnnotation());
//            } catch (InvalidXMLException e) {
//                //TODO error handling
//                e.printStackTrace();
//            }
//            xmlout.closeTag();
//        }
//        Iterator it = _parameterOrder.iterator();
//        while( it.hasNext() ) {
//            Object param = it.next();
//            Object cell = _parameters.get(param);
//            if( cell != null ) {
//                xmlout.startTag("value", new XMLAttribute("parameter", param.toString()));
//                xmlout.textContent(((Cell)cell).toString());
//                xmlout.closeTag();
//            }
//        }
//        xmlout.closeTag(); // for gene
//    }


}
