//--------------------------------------
// SCMDServer
// 
// GroupType.java 
// Since: 2004/09/03
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

/**
 * @author leo
 *
 */
public class GroupType
{
    public final static String[][] GROUP_NAME = new String[][]
                                                {
            {"no", "small", "medium", "large"},
            {"A", "A1", "B", "C", "D", "E", "F"},
            {"A", "B", "api", "iso", "E", "F"}
                                                };
    
    public final static String[] STAIN_GROUP 
    = {"Bud Size", "Nucleus Localization", "Actin Distribution"}; 
                                                    
    
    public static String[] getGroupNameArray(int stainType)
    {
        return GROUP_NAME[stainType];
    }
    
    
}


//--------------------------------------
// $Log: GroupType.java,v $
// Revision 1.1  2004/09/03 07:31:53  leo
// �f�U�C���̒���
// standardname��\��
//
//--------------------------------------