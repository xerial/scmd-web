//--------------------------------------
// SCMDWeb Project
//
// TDStatParam.java
// Since: 2005/03/10
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.scripts;


/**
 * Teardrop描画用の統計データのホルダー
 * @author leo
 *
 */
public class TDStatParam 
{
    int paramid;
    int groupid;
    double average;
    double sd;
    double min;
    double max;
    int num;
    
    public TDStatParam()
    {
        
    }
    
    public double getAverage()
    {
        return average;
    }
    public void setAverage(double average)
    {
        this.average = average;
    }
    public int getGroupid()
    {
        return groupid;
    }
    public void setGroupid(int groupid)
    {
        this.groupid = groupid;
    }
    public double getMax()
    {
        return max;
    }
    public void setMax(double max)
    {
        this.max = max;
    }
    public double getMin()
    {
        return min;
    }
    public void setMin(double min)
    {
        this.min = min;
    }
    public int getNum()
    {
        return num;
    }
    public void setNum(int num)
    {
        this.num = num;
    }
    public int getParamid()
    {
        return paramid;
    }
    public void setParamid(int paramid)
    {
        this.paramid = paramid;
    }
    public double getSd()
    {
        return sd;
    }
    public void setSd(double sd)
    {
        this.sd = sd;
    }
}
