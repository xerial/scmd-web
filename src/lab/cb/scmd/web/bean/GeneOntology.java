//--------------------------------------
//SCMDServer
//
//YeastGene.java 
//Since: 2005/12/12
//
//$URL: $ 
//$LastChangedBy: $ 
//--------------------------------------

package lab.cb.scmd.web.bean;

/**
 * gene ontology‚Ìî•ñ‚ğ‚à‚ÂƒNƒ‰ƒX
 * @author sesejun
 * 
 */
public class GeneOntology {
	String goid = "";
	String name = "";
	String namespace = "";
	String def = "";
	
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public String getGoid() {
		return goid;
	}
	public void setGoid(String goid) {
		this.goid = goid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}	
}
