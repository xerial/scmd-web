// --------------------------------------
//SCMD Project
//
//CellBox.java
//Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------
package lab.cb.scmd.db.connect;

import java.util.Arrays;

import lab.cb.scmd.db.common.IndividualBox;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.web.exception.InvalidPageException;

public class CellBox extends IndividualBox
{
	int _cellID	= -1;
	
	public CellBox(String strainname)
	{
		super(strainname);

		_phenomeTag = "lab.cb.scmdresult";
		_strainTag = "orf";
		_strainAtt = "orfname";
	}

	public void setCurrentPage(int currentPage) {
		super.setCurrentPage(currentPage);
		_photo = new CellImage(currentPage);
	}

	public void setData(SCMDDBConnect dbconnect) throws SCMDException, InvalidPageException {
		setPageData(dbconnect);

		if(_strain.sizeOfGenes() == 0)
		{
			ORFInformation gi = new ORFInformation(_strain.getName());
			_strain.addGeneInformation(gi);
		}
		String[] columns = { "strainname", "image_number", "cell_local_id", "x1", "y1", "x2", "y2" };
		int columnslength = columns.length;

		String[] params = new String[_parameters.size()];
		for(int i = 0; i < _parameters.size(); i++)
		{
			params[i] = _parameters.get(i).toString();
		}

		BasicTable bt = dbconnect.cellBoxQuery(_strain, _photo, _cellID, columns, params);
		int size = bt.getRowSize();
		for(int i = 0; i < size; i++)
		{
			Cell id = bt.getCell(i, "cell_id");
			CellIndividual cell = new CellIndividual(id.toString());
			Cell x1 = bt.getCell(i, "x1");
			cell.setX1(Integer.parseInt(x1.toString()));
			Cell y1 = bt.getCell(i, "y1");
			cell.setY1(Integer.parseInt(y1.toString()));
			Cell x2 = bt.getCell(i, "x2");
			cell.setX2(Integer.parseInt(x2.toString()));
			Cell y2 = bt.getCell(i, "y2");
			cell.setY2(Integer.parseInt(y2.toString()));
			Cell photoid = bt.getCell(i, "image_number");
			cell.setPhotoID(Integer.parseInt(photoid.toString()));
			Cell localid = bt.getCell(i, "cell_local_id");
			cell.setLocalID(Integer.parseInt(localid.toString()));

			_photo.add(cell);
		}

	}

	private void setPageData(SCMDDBConnect dbconnect) throws SCMDException, InvalidPageException {
		BasicTable bt = dbconnect.imagePageStatusQuery(_strain.getName());
		int size = bt.getRowSize();
		if(size == 0)
		{		    
		    // invalid な値をセットしておきます (by leo)
			_photo.setID(-1);
			_photo.setMaxPage(0);
			return;
		}

		if(_currentPage < 1 || _currentPage > size)
			throw new InvalidPageException(_currentPage, size);


		Integer[] photoids = new Integer[size];
		for(int i = 0; i < size; i++)
		{
			photoids[i] = new Integer(bt.getCell(i, 0).toString());
		}
		Arrays.sort(photoids);
		//        photoids[_currentPage]
		//        int curPage = 0;
		//        for( int i = 0; i < size; i++ ) {
		//        	if( photoids[i].intValue() == curPage ) {
		//        		curPage = i;
		//        		break;
		//        	}
		//        }
		_photo.setID(photoids[_currentPage - 1].intValue());
		_photo.setMaxPage(size);
		//        _currentPage = curPage;
		//        _photoNum = photoids[_currentPage].intValue();
		//        _maxPage = size;
	}

	/**
	 * @param cellID
	 */
	public void setCellID(int cellID) {
		_cellID = cellID;
	}
}