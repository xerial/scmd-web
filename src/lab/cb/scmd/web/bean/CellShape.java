//--------------------------------------
// SCMDServer
// 
// CellShape.java 
// Since: 2004/08/01
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;



/**
 * @author leo
 *
 */
public class CellShape
{
    protected double areaRatio = -1;
    protected  double longAxis = -1;
    protected  double roundness = -1;
    protected double budNeckPosition = -1;
    protected double budGrowthDirection = -1;
    protected int numSamples = 1;

    /**
     * 
     */
    public CellShape()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Map getArgumentMap()
    {
        TreeMap defaultParameterMap = new TreeMap();
        if(numSamples < 1)
            return defaultParameterMap;

        defaultParameterMap.put("areaRatio", new Double(areaRatio));
        defaultParameterMap.put("longAxis", new Double(longAxis));
        defaultParameterMap.put("roundness", new Double(roundness));
        if(areaRatio!=0)
        {
            defaultParameterMap.put("neckPosition", new Double(budNeckPosition));
            defaultParameterMap.put("growthDirection", new Double(budGrowthDirection));
        }
        return defaultParameterMap;
    }

    public double getGrowthDirection() {
        if(areaRatio == 0.0)
        {
            return Double.NaN;
        }
        return budGrowthDirection;
    }
    public void setBudGrowthDirection(double growthDirection) {
        this.budGrowthDirection = growthDirection;
    }
    public double getLongAxis() {
        return longAxis;
    }
    public void setLongAxis(double longAxis) {
        this.longAxis = longAxis;
    }
    public double getBudNeckPosition() {
        if(areaRatio == 0.0)
        {
            return Double.NaN;
        }
        return budNeckPosition;
    }
    public void setBudNeckPosition(double neckPosition) {
        this.budNeckPosition = neckPosition;
    }
    public double getRoundness() {
        return roundness;
    }
    public void setRoundness(double roundness) {
        this.roundness = roundness;
    }
    
    
    public double getAreaRatio() {
        return areaRatio;
    }
    public void setAreaRatio(double areaRatio) {
        if(areaRatio < 0.0)
            this.areaRatio = 0.0;
        else
            this.areaRatio = areaRatio;
    }
    
    public int getNumSamples() {
        return numSamples;
    }
    public void setNumSamples(int numSamples) {
        if(numSamples < 0)
            this.numSamples = 0;
        else
            this.numSamples = numSamples;
    }
}


//--------------------------------------
// $Log: CellShape.java,v $
// Revision 1.3  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.2  2004/09/03 03:31:30  leo
// javascriptでCSSを変更できるようにした
//
// Revision 1.1  2004/08/01 08:20:12  leo
// BasicTableをHTMLに変換するツールを書き始めました
//
//--------------------------------------