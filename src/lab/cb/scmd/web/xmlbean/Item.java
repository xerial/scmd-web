//--------------------------------------
// SCMDWeb Project
//
// Orf.java
// Since: 2005/02/22
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.xmlbean;

/**
 * Selection���̊eORF(Item�j������킷�N���X XMLBean�Ƃ��Ĉ���
 * @author leo
 *
 */
public class Item
{
    String orf = null;
    String color = null;
    
    
    
    public Item()
    {
    }
    
    public Item(String orf, String color)
    {
        this.orf = orf;
        this.color = color;
    }
    
    public String[] attributes()
    {
        return new String[] {"orf", "color"};
    }

    public void setOrf(String orf)
    {
        this.orf = orf;
    }
    public void setColor(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }
    public String getOrf()
    {
        return orf;
    }
}




