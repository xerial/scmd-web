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

import java.util.Vector;

import lab.cb.scmd.web.container.Enrichments;

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
	Vector<Enrichments> fwdrev = new Vector<Enrichments>();
	
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
	public void addFwdRev(Enrichments fwdrev) {
		this.fwdrev.add(fwdrev);
	}
	public Vector<Enrichments> getFwdRev() {
		return fwdrev;
	}
}
