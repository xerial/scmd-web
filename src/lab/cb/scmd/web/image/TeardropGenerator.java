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

import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;
import lab.cb.scmd.util.image.ImageConverter;
import lab.cb.scmd.util.teardrop.TeardropStatistics;

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
	
    public void drawTeardrop(PrintStream out, String param, TeardropPoint[] teardropPoint, TeardropStatistics tds )
	throws UnfinishedTaskException
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
    	

    	String drawCommand = " ";
    	int centerindex = tds.getIndex(tds.getAvg());
    	int avgPos = IMAGEHEIGHT / 2;
    	drawCommand = " -fill \"#A0E0F0\"";
    	// 平均値に線を引く
    	drawCommand += " -draw \"line 0," + avgPos + " " + IMAGEWIDTH + "," + avgPos + "\"";
    	int[] xpos = computeXpositionOfPoints(teardropPoint, tds);
    	// 点を打つ
    	for( int i = 0; i < teardropPoint.length; i++ ) {
    	    int ypos = computeYposition(teardropPoint[i], tds, centerindex);
        	drawCommand += " -fill \"" + teardropPoint[i].getColor() + "\"";
        	drawCommand += " -draw \"circle " + xpos[i] +  "," + ypos + " ";
       		drawCommand += (xpos[i] + DOTRADIUS) + "," + (ypos + DOTRADIUS) + "\"";
    	}
    	// 向きを90度回転させる
  		drawCommand += " -resize 30x128 -rotate 90 -sharpen 1.1";
    	try {
        	String cmd  =  getConvertProgram() + " " + drawCommand + " - -";
    		String url = _teardropURI + "/td_" + param + ".png";
			InputStream in = new URL(url).openStream();
	        ProcessRunner.run(in, out, cmd);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    
	}

    /**
     * @param point
     * @return
     */
    private int computeYposition(TeardropPoint point, TeardropStatistics tds, int centerindex) {
    	int yindex = tds.getIndex(point.getValue());
    	int ypos = ( centerindex - yindex ) * BARHEIGHT + IMAGEHEIGHT /2 ;
    	if( ypos < 0 )
    	    ypos = 0;
    	if( ypos > IMAGEHEIGHT )
    	    ypos = IMAGEHEIGHT - DOTRADIUS;
    	return ypos; 
    }

    private int[] computeXpositionOfPoints(TeardropPoint[] teardropPoint, TeardropStatistics tds) {
        int size = teardropPoint.length;
        int[] xpos = new int [size];
        HashMap indexMap = new HashMap();
        for( int i = 0 ; i < size; i++ ) {
            xpos[i] = IMAGEWIDTH / 2 - DOTRADIUS;
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
                xpos[hindex] += 4 * ( i - midindex ) * DOTRADIUS;
            }
        }
        return xpos;
    }

}
