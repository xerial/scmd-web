//--------------------------------------
// SCMDWeb Project
//
// Parameter.java
// Since: 2005/01/31
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.scripts.bean;

/**
 * Parameterを表現するbean
 * @author leo
 *
 */
public class Parameter
{
    private int id;
    private String name;
    
    /**
     * 
     */
    public Parameter()
    {
        super();
    }
    
    public String[] attributes() 
    {
        return new String[] { "id", "name" };
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
}




