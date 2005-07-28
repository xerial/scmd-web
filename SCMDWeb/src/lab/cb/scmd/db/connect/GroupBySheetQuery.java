package lab.cb.scmd.db.connect;



import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.xml.XMLOutputter;
import lab.cb.scmd.web.exception.InvalidSQLException;



/** @deprecated
 * @author leo
 *
 */
public class GroupBySheetQuery {
	String _strain 	= "YOR202W";
	int _groupType 	= 0;  
	PageStatus _page = new PageStatus(1,1);
	static String[] categoryName = {"Cgroup", "Dgroup", "Agroup"};
	
	public GroupBySheetQuery(String strain, int groupType) {
		_strain		 = strain;
		_groupType 	= groupType;
	}

	public void setCurrentPage(int page) {
		_page.setCurrentPage(page);
	}
	
	public void setNumberOfCellsInAPage(int numcell) {
		_page.setMaxElementsInAPage(numcell);
	}

	public void setData(SCMDDBConnect connect) throws InvalidSQLException {
		BasicTable count = connect.getNumberOfCellsGroupedBy(_strain, categoryName[_groupType]);
/*		BasicTable photoIDList = connect.getPhotoIDList(_strain); 
		Collection groupNameSet = count.getRowLabelList();
		Iterator it = groupNameSet.iterator();
		Map categoryToPhoto = new HashMap();
		while(it.hasNext()) {
			
		}
		BasicTable photos = connect.getCellsGroupedBy(_strain, categoryName[_groupType]);
		*/
	}


	public void outputXML(XMLOutputter xmlout) {
		// TODO Auto-generated method stub
		
	}

}
