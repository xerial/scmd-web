//--------------------------------------
//SCMDServer
//
//Strain.java 
//Since:  2004/07/28
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.bean;

import lab.cb.scmd.db.common.GeneGroup;
import lab.cb.scmd.db.common.GeneInformation;

public class Strain {
	private int _strainid = 0;
	private String _strainName = "";
	//複数遺伝子が関与したときのために、
	//GeneInformationではなく、GeneGroupを利用している
	private GeneGroup _geneGroup = null;

	public Strain(String strainName) {
		_strainName = strainName;
		_geneGroup = new GeneGroup(_strainName);
	}
	
	public void addGeneInformation(GeneInformation geneInformation) {
		_geneGroup.add(geneInformation);
	}
	
	public GeneInformation getGeneInformation(int n) {
		return _geneGroup.get(n);
	}

	public int sizeOfGenes() {
		return _geneGroup.size();
	}

	public String getName() {
		return _strainName;
	}
	
	public int getID() {
		return _strainid;
	}
	
}
