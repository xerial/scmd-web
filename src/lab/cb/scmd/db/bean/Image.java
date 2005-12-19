//--------------------------------------
//SCMDServer
//
//Photo.java 
//Since:  2004/07/28
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.bean;

import java.util.Vector;

import lab.cb.scmd.db.common.Individual;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class Image {
	private int _currentPage;
	private int _maxPage;
	private int _id;
	protected Vector _individual 	= new Vector();
	//protected PageStatus _page		= new PageStatus();
	
	protected String _photoTag   		= "photo";
	protected String _photoCurrentPageAtt = "page";
	protected String _photoMaxPageAtt 	= "maxpage";
	protected String _photoIDAtt 		= "id";
	protected String _numberOfCells 	= "numberofcells";
	protected String _cellIdAtt  = "id";
	protected String _cellX1Att  = "x1";
	protected String _cellY1Att  = "y1";
	protected String _cellX2Att  = "x2";
	protected String _cellY2Att  = "y2";
	protected String _indivTag   = "individual";
	
	public Image(int currentPage) {
		_currentPage = currentPage;
	}
	
	public int currentPage() {
		return _currentPage;
	}
	
	public void setID(int id) {
		_id = id;
	}
	
	public int getID() {
		return _id;
	}
	
	public void setMaxPage(int max) {
		_maxPage = max;
	}
	
	public int getMaxPage() {
		return _maxPage;
	}
	
//	public void setPageStatus(int current, int max) {
//		_page.setCurrentPage(current);
//		_page.setMaxPage(max);
//	}
	
	public void add(Individual cell) {
		_individual.add(cell);
	}
	
	public Individual get(int n) {
		Object cell = _individual.get(n);
		if( cell != null )
			return (Individual)cell;
		return null;
	}

	public void outputXML(XMLOutputter xmlout) throws InvalidXMLException {
		XMLAttribute photoAtt = new XMLAttribute();
		photoAtt.add(_photoIDAtt, _id + "");
		photoAtt.add(_photoCurrentPageAtt, _currentPage + "");
		photoAtt.add(_photoMaxPageAtt, _maxPage + "");
		photoAtt.add(_numberOfCells, _individual.size() + "");
		xmlout.startTag(_photoTag, photoAtt);
		for(int i = 0; i < _individual.size(); i++ ) {
			XMLAttribute att = new XMLAttribute();
			Individual 	cell = (Individual)_individual.get(i);  
			att.add(_cellIdAtt, cell.getLocalID() + "");
			att.add(_cellX1Att, cell.getX1() + "");
			att.add(_cellX2Att, cell.getX2() + "");
			att.add(_cellY1Att, cell.getY1() + "");
			att.add(_cellY2Att, cell.getY2() + "");
			xmlout.startTag(_indivTag, att);
			xmlout.closeTag();
		}
        xmlout.closeTag(); // photo
	}

}
