//--------------------------------------
//SCMDServer
//
//Photo.java 
//Since:  2004/07/28
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.connect;

import lab.cb.scmd.db.common.Image;

public class CellImage extends Image {
	
	public CellImage (int page) {
		super(page);
		
		_photoTag   = "photo";
		_photoIDAtt = "id";
		_cellIdAtt  = "id";
		_cellX1Att  = "x1";
		_cellY1Att  = "y1";
		_cellX2Att  = "x2";
		_cellY2Att  = "y2";
		_indivTag   = "cell";

	}
	
	public void add(CellIndividual cell) {
		_individual.add(cell);
	}
	
	
}
