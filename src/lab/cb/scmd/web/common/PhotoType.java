//--------------------------------------
// SCMDServer
// 
// PhotoType.java 
// Since: 2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import java.security.InvalidParameterException;

/**
 * @author leo
 *
 */
public class PhotoType
{
    public static final int ORIGINAL_PHOTO = 0;
    public static final int ANALYZED_PHOTO = 1;
    public static final int PHOTO_TYPE_MAX = 2;
    
    static final String PHOTO_SUFFIX[] = {"", "a"};
    static final String PHOTO_DIRECTORY[] = {"halfsize", "analysis"};
    
    public static void validatePhotoType(int photoType) throws InvalidParameterException
    {
        if(!isValid(photoType))
            throw new InvalidParameterException("photoType: " + photoType);
    }
    
    public static boolean isValid(int photoType)
    {
        return (photoType >= 0 && photoType < PHOTO_TYPE_MAX);
    }
    
    public static String getPhotoSuffix(int photoType)
    {
        return PHOTO_SUFFIX[photoType];
    }
    
    public static String getPhotoDirectory(int photoType)
    {
        return PHOTO_DIRECTORY[photoType];
    }
    
}


//--------------------------------------
// $Log: PhotoType.java,v $
// Revision 1.4  2004/08/05 14:10:45  leo
// ImageServer��session��ǂނ̂ł͂Ȃ��AGET�Ŏ擾����悤�ɕύX
//
// Revision 1.3  2004/07/15 09:21:20  leo
// jsp �̎g�p�J�n
//
// Revision 1.2  2004/07/15 02:31:39  leo
// interface�ł͂Ȃ��Ahas-a�̊֌W�Ƃ��Ďg�p����悤�ɕύX
//
// Revision 1.1  2004/07/14 07:05:30  leo
// SCMD_ROOT�̐ݒ��Tomcat�ɔC����
//
//--------------------------------------