//--------------------------------------
//SCMDServer
//
//TeardropGenerator.java 
//Since: 2004/08/17
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import sun.awt.image.PNGImageDecoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;
import lab.cb.scmd.util.image.ImageConverter;
import lab.cb.scmd.db.scripts.TeardropStatistics;

public class TeardropGenerator extends ImageConverter {

	private int	IMAGEHEIGHT					= 300;
	private int IMAGEWIDTH					= 75;
	private int BARHEIGHT					= 4;
	private int DOTRADIUS						= 4;
	
	private String _teardropURI				= "";

	/**
	 * @param pathToImageMagickConvert
	 * @throws FileNotFoundException
	 */
	public TeardropGenerator(File pathToImageMagickConvert, String uri) throws FileNotFoundException {
		super(pathToImageMagickConvert);
		// TODO Auto-generated constructor stub
		_teardropURI = uri;
	}
	
    public void drawTeardrop(PrintStream out, int paramID, int groupID, TeardropPoint[] teardropPoint, TeardropStatistics tds )
	throws UnfinishedTaskException, IOException
	{
    	// mother #408090 daughtor #70B0B0 (旧配色)
//    	String motherCellDrawCommand =  "-fill \"#72ADC5\" -affine \"1,0,0,1," + xCenter + "," + yCenter + 
//		"\" -draw \"ellipse 0,0 " + sizeRatioOfLongAxis + "," + sizeRatioOfShortAxis + " 0,360\"";
//    	String budCellDrawCommand =  "-fill \"#9FC7D7\" -affine \"1,0,0,1," + xOfBudOffset + "," + yOfBudOffset + 
//		"\" -draw \"rotate -" + budGrowthDirection + " ellipse 0,0 " + sizeRatioOfBudLongAxis + "," + sizeRatioOfBudShortAxis + " 0,360\"";
//    
//    	//File plateFile = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT), "/image/plate.png");
//    	File bgFile = new File(new URL(photoDIRURL, photoPath);)
//    
//    	String cmd  =  getConvertProgram() + " " + motherCellDrawCommand + " " + budCellDrawCommand + " " + plateFile + " -";
    	       
        URL imageURL = new URL(_teardropURI + "/td_" + paramID + "_" + groupID + ".png");
        BufferedImage teardrop = ImageIO.read(imageURL);
        int height = teardrop.getHeight();
        int width = teardrop.getWidth();
        Graphics2D g = (Graphics2D) teardrop.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	int centerindex = tds.getIndex(tds.getAvg());
    	int avgPos = width / 2;
    	//drawCommand = " -fill \"#A0E0F0\"";
    	//drawCommand += " -draw \"line 0," + avgPos + " " + IMAGEWIDTH + "," + avgPos + "\"";
        // 平均値に線を引く
        g.setColor(new Color(0xA0E0F0));
        g.drawLine(avgPos, 0, avgPos, height);
        
        int barWidth = 1;
        int dotRadius = 3;
        
    	int[] ypos = computeYpositionOfPoints(teardropPoint, tds, width, height, dotRadius);
    	// 点を打つ
    	for( int i = 0; i < teardropPoint.length; i++ ) {
    	    int xpos = computeXposition(teardropPoint[i], tds, centerindex, width, barWidth, dotRadius);
            g.setColor(teardropPoint[i].getColor());
            g.fillOval(xpos, ypos[i], dotRadius * 2, dotRadius * 2);
            
//            drawCommand += " -fill \"" + teardropPoint[i].getColor() + "\"";
//        	drawCommand += " -draw \"circle " + xpos[i] +  "," + ypos + " ";
//       		drawCommand += (xpos[i] + DOTRADIUS) + "," + (ypos + DOTRADIUS) + "\"";
    	}
        
        // output
        ImageIO.write(teardrop, "png", out);

        
    	// 向きを90度回転させる
//  		drawCommand += " -resize 30x128 -rotate 90 -sharpen 1.1";
//    	try {
//        	String cmd  =  getConvertProgram() + " " + drawCommand + " - -";
//    		String url = _teardropURI + "/td_" + param + ".png";
//			InputStream in = new URL(url).openStream();
//	        ProcessRunner.run(in, out, cmd);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	*/
    
	}

    /**
     * @param point
     * @return
     */
    private int computeXposition(TeardropPoint point, TeardropStatistics tds, int centerindex, int width, int barWidth, int dotRadius) {
    	int xindex = tds.getIndex(point.getValue());
    	int xpos = ( centerindex - xindex ) * barWidth + width /2 ;
    	if( xpos < 0 )
    	    xpos = 0;
    	if( xpos > width )
    	    xpos = width - dotRadius;
    	return xpos; 
    }

    private int[] computeYpositionOfPoints(TeardropPoint[] teardropPoint, TeardropStatistics tds, int width, int height, int dotRadius) {
        int size = teardropPoint.length;
        int[] ypos = new int [size];
        HashMap indexMap = new HashMap();
        for( int i = 0 ; i < size; i++ ) {
            ypos[i] = height / 2 - dotRadius;
            Integer hindex = new Integer(tds.getIndex(teardropPoint[i].getValue()));
            if( indexMap.containsKey(hindex) ) {
                List indexes = (List)indexMap.get(hindex);
                indexes.add(new Integer(i));
            } else {
                List indexes = new LinkedList();
                indexes.add(new Integer(i));
                indexMap.put(hindex, indexes);
            }
        }
        Iterator it = indexMap.keySet().iterator();
        while(it.hasNext()) {
            Object nextobj = it.next();
            List indexes = (List)indexMap.get(nextobj);
            if( indexes.size() < 2 )
                continue;
            double midindex = (indexes.size() - 1) * 0.5;
            for( int i = 0; i < indexes.size(); i++ ) {
                int hindex = ((Integer)indexes.get(i)).intValue();
                ypos[hindex] += 4 * ( i - midindex ) * dotRadius;
            }
        }
        return ypos;
    }

}
