//--------------------------------------
// SCMDWeb Project
//
// GroupType.java
// Since: 2005/01/31
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.scripts.bean;

/**
 * @author leo
 *
 */
public class GroupType
{
    private int id;
    public String stain;
    private String name;

    public GroupType() {}
    
    public String[] attributes()
    {
        return new String[] {"id", "name", "stain"};
    }
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getStain()
    {
        return stain;
    }
    public void setStain(String stain)
    {
        this.stain = stain;
    }
}

