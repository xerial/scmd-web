//--------------------------------------
// SCMDServer
// 
// StainType.java 
// Since: 2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import lab.cb.scmd.exception.InvalidParameterException;

/**
 * @author leo
 *
 */
public class StainType
{
    public static final int STAIN_ConA = 0;
    public static final int STAIN_DAPI = 1;
    public static final int STAIN_RhPh = 2;
    public static final int STAIN_MAX = 3;
    
    public static String getStainTypeName(int stainType)
    {
        return STAIN_TYPE[stainType]; 
    }
    
    public static boolean isValid(int stainType)
    {
        return (stainType >= 0 && stainType < STAIN_MAX);
    }
    public static void validateStainType(int stainType) throws InvalidParameterException
    {
        // validation
        if(!isValid(stainType))
                throw new InvalidParameterException("staintype: " + stainType);
    }
    
    public final static String[] STAIN_TYPE = { "C", "D", "A" };
    public final static String[] TAB_NAME = { "Cell", "Nucleus", "Actin" };
    public final static String[] IMAGE_NAME = { "conA", "dapi", "actin" };
}


//--------------------------------------
// $Log: StainType.java,v $
// Revision 1.5  2004/08/13 05:50:32  leo
// XMLQuery, TableQuery, ValueQuery��instance��
//
// Revision 1.4  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.3  2004/08/05 14:10:45  leo
// ImageServer��session��ǂނ̂ł͂Ȃ��AGET�Ŏ擾����悤�ɕύX
//
// Revision 1.2  2004/07/15 02:31:39  leo
// interface�ł͂Ȃ��Ahas-a�̊֌W�Ƃ��Ďg�p����悤�ɕύX
//
// Revision 1.1  2004/07/14 07:05:30  leo
// SCMD_ROOT�̐ݒ��Tomcat�ɔC����
//
//--------------------------------------