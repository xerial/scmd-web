//--------------------------------------
//SCMDServer
//
//Individual.java 
//Since:  2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

public class Individual {
	private String _id = "";
	private int _x1 = 0;
	private int _x2 = 0;
	private int _y1 = 0;
	private int _y2 = 0;
	private int _localid = 0;
	private int _photoid = 0;
	
	public Individual (String id) {
		_id = id;
	}
	
	public void setPosition(int x1, int x2, int y1, int y2) {
		_x1 = x1;
		_x2 = x2;
		_y1 = y1;
		_y2	= y2;
	}
	
	public int getX1() {
		return _x1;
	}

	public void setX1(int x1) {
		this._x1 = x1;
	}

	public int getX2() {
		return _x2;
	}

	public void setX2(int x2) {
		this._x2 = x2;
	}

	public int getY1() {
		return _y1;
	}

	public void setY1(int y1) {
		this._y1 = y1;
	}

	public int getY2() {
		return _y2;
	}

	public void setY2(int y2) {
		this._y2 = y2;
	}

	public String getId() {
		return _id;
	}

	public int getLocalID() {
		return _localid;
	}

	public void setLocalID(int localid) {
		this._localid = localid;
	}

	public int getPhotoID() {
		return _photoid;
	}

	public void setPhotoID(int photoid) {
		this._photoid = photoid;
	}
	
}
