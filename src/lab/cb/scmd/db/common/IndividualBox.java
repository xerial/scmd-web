//--------------------------------------
//SCMDServer
//
//IndividualBox.java 
//Since:  2004/07/28
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.Vector;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

public class IndividualBox {
	protected Strain	_strain			= null; // strain information
	protected Image		_photo			= null; // photo its individual information
	protected Vector	_parameters		= new Vector();
	protected int		_currentPage	= 1;
	protected int		_maxPage		= 1;
	protected int		_photoID		= 0;
	
	protected String _phenomeTag 	= "phenomeresult";
	protected String _celllistTag =  "celllist";
	protected String _strainTag		= "strain";
	protected String _strainAtt		= "name";
	
	public IndividualBox(String strainName) {
		_strain = new Strain(strainName);
	}
	
	public void setCurrentPage(int photoID) {
		_currentPage = photoID; 
		_photo  	 = new Image(photoID); 
	}
	
	public void setData(DBConnect dbconnect) throws SCMDException {
		if(_strain.sizeOfGenes() == 0) {
			GeneInformation gi = new GeneInformation(_strain.getName());
			_strain.addGeneInformation(gi);
		}
        String[] columns = {"strainname", "image_number", "cell_local_id", "x1", "y1", "x2", "y2"};
        int columnslength = columns.length;

        String[] params = new String [_parameters.size()];
        for( int i = 0; i < _parameters.size(); i++ ) {
            params[i] = _parameters.get(i).toString();
        }

        BasicTable bt = dbconnect.individualBoxQuery(_strain, _photo, columns, params);
        int size = bt.getRowSize();
        for( int i = 0; i < size; i++ ) {
        	Cell id = bt.getCell(i, "cell_id");
        	Individual individual = new Individual(id.toString());
        	Cell x1 = bt.getCell(i, "x1");
        	individual.setX1(Integer.parseInt(x1.toString()));
        	Cell y1 = bt.getCell(i, "y1");
        	individual.setY1(Integer.parseInt(y1.toString()));
        	Cell x2 = bt.getCell(i, "x2");
        	individual.setX2(Integer.parseInt(x2.toString()));
        	Cell y2 = bt.getCell(i, "y2");
        	individual.setY2(Integer.parseInt(y2.toString()));
        	Cell photoid = bt.getCell(i, "image_number");
        	individual.setPhotoID(Integer.parseInt(photoid.toString()));
        	Cell localid = bt.getCell(i, "cell_local_id");
        	individual.setLocalID(Integer.parseInt(localid.toString()));
        	
        	_photo.add(individual);
        }
        
	}

	public void outputXML(XMLOutputter xmlout) throws InvalidXMLException {
        xmlout.startTag(_phenomeTag);
        xmlout.startTag(_celllistTag);
        xmlout.startTag(_strainTag, new XMLAttribute(_strainAtt, _strain.getName()));
        _photo.outputXML(xmlout);
        xmlout.closeTag(); // for strain
        xmlout.closeTag(); // for celllist
        xmlout.closeTag(); // for phenomeresult
        return;

        }
}
