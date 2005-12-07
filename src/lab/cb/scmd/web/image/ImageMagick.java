//--------------------------------------
// SCMDWeb Project
//
// ImageMagick.java
// Since: 2005/12/07
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.util.image.ImageConverter;
import lab.cb.scmd.util.io.FileUtil;
import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 * ImageMagickの機能を使うためのクラス
 * @author leo
 * TODO ImageConverter からこちらに移行
 */
public class ImageMagick
{

    static public void clipImage(OutputStream out, URL imageURL, BoundingRectangle clipRegion, int scalingPercentage, int border) 
    throws InvalidParameterException, UnfinishedTaskException, IOException
    {
        ImageConverter c = new ImageConverter(new File(SCMDConfiguration.getProperty(SCMDConfiguration.IMAGEMAGICK_CONVERT)));
        c.clipImage(out, imageURL, clipRegion, scalingPercentage, border);
    }    
    
    private ImageMagick()
    {
    }

}




