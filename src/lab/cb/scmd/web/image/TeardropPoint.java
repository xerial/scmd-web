//--------------------------------------
//SCMDServer
//
//TeardropValue.java 
//Since: 2004/08/23
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.Color;

public class TeardropPoint {

	private String _name = "";
	private double _value;
	private Color color = new Color(0xFF8888);
	
	public TeardropPoint(String param, double point, String color) {
		_name = param;
		_value = point;
        setColor(color);
	}

	/**
	 * @return Returns the _color.
	 */
	public Color getColor() {
	    return color;
	}
	/**
	 * @param _color The _color to set.
	 */
	public void setColor(String _color) {
        try
        {
           color = new Color(Integer.parseInt(_color, 16));
        }
        catch(NumberFormatException e)
        {
            // use default color
        }
	}
	/**
	 * @return Returns the _param.
	 */
	public String getParamName() {
		return _name;
	}
	/**
	 * @param _param The _param to set.
	 */
	public void setParamName(String param) {
		this._name = param;
	}
	/**
	 * @return Returns the _point.
	 */
	public double getValue() {
		return _value;
	}
	/**
	 * @param _point The _point to set.
	 */
	public void setValue(double value) {
		this._value = value;
	}
}
