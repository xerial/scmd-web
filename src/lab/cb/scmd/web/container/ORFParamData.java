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

import lab.cb.scmd.web.bean.YeastGene;

/**
 * ORFに関するデータを格納するクラス
 * @author leo
 *
 */
public class ORFParamData
{
    String orf; 
    String standardname;
    String annotation;
    String aliases;
    double data;
    
    /**
     * 
     */
    public ORFParamData()
    {
    }

    public String getOrf()
    {
        return YeastGene.formatedOrf(orf);
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
    
    
    public String getStandardname()
    {
        return standardname;
    }
    public void setStandardname(String standardname)
    {
        this.standardname = standardname;
    }

    public String getAnnotation() {
        return annotation;
    }
    

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    
    
    
}




