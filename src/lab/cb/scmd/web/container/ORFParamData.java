//--------------------------------------
// SCMDWeb Project
//
// ORFParamData.java
// Since: 2005/02/15
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.container;

/**
 * ORFに関するデータを格納するクラス
 * @author leo
 *
 */
public class ORFParamData
{
    String orf;    
    double data;
    
    /**
     * 
     */
    public ORFParamData()
    {
    }

    public String getOrf()
    {
        return orf;
    }
    public void setOrf(String orf)
    {
        this.orf = orf;
    }
    
    
    public double getData()
    {
        return data;
    }
    public void setData(double data)
    {
        this.data = data;
    }
}




