//--------------------------------------
// SCMDServer
// 
// CellImageGenerator.java 
// Since: 2004/07/29
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

//import lab.cb.scmd.exception.UnfinishedTaskException;
//import lab.cb.scmd.util.ProcessRunner;
import lab.cb.scmd.util.image.ImageConverter;
//import lab.cb.scmd.web.common.SCMDConfiguration;

/**
 * @author leo
 *
 */
public class CellImageGenerator extends ImageConverter
{

    /**
     * @param pathToImageMagickConvert
     * @throws FileNotFoundException
     */
    public CellImageGenerator(File pathToImageMagickConvert) throws FileNotFoundException
    {
        super(pathToImageMagickConvert);
        // TODO Auto-generated constructor stub
    }

    public void drawCellImage(OutputStream out, double budSizeRatio, double longAxis, double roundness, double neckPosition, double budGrowthDirection)
    	throws IOException
    {
        double shortAxis = longAxis / roundness;
        double radian = Math.PI / 180;

        double longAxisOfBud = longAxis * budSizeRatio;
        double shortAxisOfBud = shortAxis * budSizeRatio;

        double xRange = (longAxis / 2.0) * (Math.cos(neckPosition * radian) + 1) + longAxisOfBud
                * Math.cos(budGrowthDirection * radian);
        double yRange = (shortAxis / 2.0) * (Math.sin(neckPosition * radian) + 1) + longAxisOfBud
                * Math.sin(budGrowthDirection * radian);

        int xCenter = (int) (50 - (xRange / 2.0) + (longAxis / 2.0));
        int yCenter = (int) (118 - (shortAxis / 2.0 + 0.5));

        int xOfBudOffset = xCenter
                + (int) ((longAxis / 2.0) * Math.cos(neckPosition * radian) + (longAxisOfBud / 2.0)
                        * Math.cos(budGrowthDirection * radian) + 0.5);
        int yOfBudOffset = yCenter
                - (int) ((shortAxis / 2.0) * Math.sin(neckPosition * radian) + (longAxisOfBud / 2.0)
                        * Math.sin(budGrowthDirection * radian) + 0.5);

        int sizeRatioOfBudLongAxis = (int) (longAxisOfBud / 2.0 + 0.5);
        int sizeRatioOfBudShortAxis = (int) (shortAxisOfBud / 2.0 + 0.5);

        int sizeRatioOfLongAxis = (int) (longAxis / 2.0 + 0.5);
        int sizeRatioOfShortAxis = (int) (shortAxis / 2.0 + 0.5);

        // mother #408090 daughtor #70B0B0 (旧配色)
        /*
        String motherCellDrawCommand =  "-fill \"#72ADC5\" -affine \"1,0,0,1," + xCenter + "," + yCenter + 
        "\" -draw \"ellipse 0,0 " + sizeRatioOfLongAxis + "," + sizeRatioOfShortAxis + " 0,360\"";
        String budCellDrawCommand = "";
        if(budSizeRatio != 0)
        {
            budCellDrawCommand =  "-fill \"#9FC7D7\" -affine \"1,0,0,1," + xOfBudOffset + "," + yOfBudOffset + 
            "\" -draw \"rotate -" + budGrowthDirection + " ellipse 0,0 " + sizeRatioOfBudLongAxis + "," + sizeRatioOfBudShortAxis + " 0,360\"";
        }
        */
		/*
		 * BufferedImage cellImage = new BufferedImage(128, 128,
		 * BufferedImage.TYPE_INT_RGB); Graphics2D graphics =
		 * cellImage.createGraphics(); Ellipse2D motherCellEllipse = new
		 * Ellipse2D.Double(-sizeRatioOfLongAxis, -sizeRatioOfShortAxis,
		 * sizeRatioOfLongAxis * 2, sizeRatioOfShortAxis * 2); Ellipse2D
		 * budCellEllipse = new Ellipse2D.Double(-sizeRatioOfBudLongAxis,
		 * -sizeRatioOfBudShortAxis, sizeRatioOfBudLongAxis * 2,
		 * sizeRatioOfBudShortAxis * 2);
		 * 
		 * graphics.setColor(new Color(0xFFFFFF)); graphics.fillRect(0, 0,
		 * 128, 128);
		 * graphics.setTransform(AffineTransform.getTranslateInstance(xCenter,
		 * yCenter)); graphics.setColor(new Color(0x408090));
		 * graphics.fill(motherCellEllipse); AffineTransform transform =
		 * AffineTransform.getTranslateInstance(xOfBudOffset, yOfBudOffset);
		 * transform.rotate(-growthDirection * radian);
		 * graphics.setTransform(transform); graphics.setColor(new
		 * Color(0x70B0B0)); graphics.fill(budCellEllipse);
		 */

        /*
        File plateFile = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT), "/image/plate.png");
        
        String cmd  =  getConvertProgram() + " " + motherCellDrawCommand + " " + budCellDrawCommand + " " + plateFile + " -";
        
        ProcessRunner.run(out, cmd);
        */
        
        BufferedImage cellImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) cellImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D motherCellEllipse = new Ellipse2D.Double(-sizeRatioOfLongAxis, -sizeRatioOfShortAxis, sizeRatioOfLongAxis * 2, sizeRatioOfShortAxis * 2);
        Ellipse2D budCellEllipse = new Ellipse2D.Double(-sizeRatioOfBudLongAxis, -sizeRatioOfBudShortAxis, sizeRatioOfBudLongAxis * 2, sizeRatioOfBudShortAxis * 2);
        
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, 128, 128);
        
        g.setTransform(AffineTransform.getTranslateInstance(xCenter,yCenter));
        g.setColor(new Color(0x72ADC5));
        g.fill(motherCellEllipse);
        
        AffineTransform rotateTransformation = AffineTransform.getTranslateInstance(xOfBudOffset, yOfBudOffset);
        rotateTransformation.rotate(-budGrowthDirection * radian);
        g.setTransform(rotateTransformation);
        g.setColor(new Color(0x9FC7D7));
        g.fill(budCellEllipse);
        
        
        ImageIO.write(cellImage,"png",out);
    }
    
}


//--------------------------------------
// $Log: CellImageGenerator.java,v $
// Revision 1.8  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.7  2004/09/09 02:15:10  leo
// bufferedimageで、clippingをするようにしてみた
//
// Revision 1.6  2004/09/09 01:34:50  leo
// antialiasingをして、画像を描くようにした
//
// Revision 1.5  2004/09/06 07:04:28  leo
// PrintStream -> outputStream
//
// Revision 1.4  2004/09/06 06:27:14  leo
// no budのときのバグを修正
//
// Revision 1.3  2004/08/13 13:57:12  leo
// Statページの更新
//
// Revision 1.2  2004/07/30 06:40:29  leo
// Morphology Searchのインターフェース、完成
//
// Revision 1.1  2004/07/29 07:50:45  leo
// MorphologySearchのパーツ作成中
//
//--------------------------------------