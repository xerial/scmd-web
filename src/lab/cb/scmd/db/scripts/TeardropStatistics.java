//--------------------------------------
//SCMDProject
//
//TeardropBackgroupnd.java 
//Since: 2004/08/16
//
// $URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMD/src/lab/cb/scmd/util/teardrop/TeardropStatistics.java $ 
// $LastChangedBy: leo $ 
//--------------------------------------

package lab.cb.scmd.db.scripts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.ImageIO;

import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;

public class TeardropStatistics {
	private int LIMIT = 1000;
	
	private double _max;
	private double _min;
	private double _bucketSize;
	private double _avg;
	private double _sd;
    private double _farthestDistance;
    private int _num;
	private int[]  _count;
	private int		_maxcount;
	
	//private double DIVISIONNUMOFSD = 5.0;

    private int BASE_COLOR               = 0x8888FF;
    private int IMAGEWIDTH                  = 30;
    private int IMAGEHEIGHT                 = 128;
    private int BARHEIGHT                   = 1;
    private int MARGIN                      = 0;

    // program
    
    public TeardropStatistics()
    {
      
    }
    
	/**
	 * @return Returns the avg.
	 */
	public double getAvg() {
		return _avg;
	}
	/**
	 * @param avg The avg to set.
	 */
	public void setAvg(double avg) {
		this._avg = avg;
	}
	/**
	 * @return Returns the max.
	 */
	public double getMax() {
		return _max;
	}
	/**
	 * @param max The max to set.
	 */
	public void setMax(double max) {
		this._max = max;
	}
	/**
	 * @return Returns the min.
	 */
	public double getMin() {
		return _min;
	}
	/**
	 * @param min The min to set.
	 */
	public void setMin(double min) {
		this._min = min;
	}
	/**
	 * @return Returns the sd.
	 */
	public double getSD() {
		return _sd;
	}
	/**
	 * @param sd The sd to set.
	 */
	public void setSD(double sd) {
		this._sd = sd;
	}
	/**
	 * @param column
	 */
	public void calcHistgram(Collection column) {
        // 中心(avg)から、minとmaxで遠いほうを選ぶ
		_farthestDistance = (getMax() - getAvg() > getAvg() - getMin())?(getMax() - getAvg()):(getAvg() - getMin());
        // 選んだ方に併せてバケットの個数と幅を計算する
//		_bucketSize = getSD() / DIVISIONNUMOFSD;
        _bucketSize = _farthestDistance / IMAGEHEIGHT; 
//      int numOfBuckets = (int)Math.ceil( diff / _bucketSize );
        int numOfBuckets = IMAGEHEIGHT;
//        if( numOfBuckets > LIMIT ) {
//            _count = new int [0];
//            return;
//        }
		_count = new int[numOfBuckets];
        for(int i = 0; i < numOfBuckets; i++ ) {
            _count[i] = 0;
        }
		
		for( Iterator it = column.iterator();  it.hasNext(); ) {
			double v = ((Double)it.next()).doubleValue();
			int index = getIndex(v);
            if(index >= _count.length || index < 0)
                continue;  // はみ出た場合
			_count[index]++;
		}
		int maxcount = 0;
		for(int i = 0; i < _count.length; i++ ) {
			if( maxcount < _count[i] )
				maxcount = _count[i];
		}
		_maxcount = maxcount;
	}
    

    public void drawTeardrop(TeardropStatistics tds, PrintStream out)
        throws IOException
    {
        int size = tds.getHistgramSize();
        int centerindex = tds.getIndex(tds.getAvg());
        
        BufferedImage image = new BufferedImage(IMAGEWIDTH, IMAGEHEIGHT, ColorSpace.TYPE_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, IMAGEWIDTH, IMAGEHEIGHT);        
        
        g.setColor(new Color(BASE_COLOR));
        for( int i = 0; i < size; i++ ) {
            int hpos = ( centerindex - i ) * BARHEIGHT + IMAGEHEIGHT /2 ;
            if( tds.getHistgram(i) != 0 ) {
                double barwidth = Math.log(tds.getHistgram(i) + 1) * (IMAGEWIDTH - MARGIN) / Math.log(tds.getMaxCount() + 1);
                double middle = IMAGEWIDTH/2.0;
                g.fillRect((int)(middle - barwidth/2.0), hpos, (int)barwidth, BARHEIGHT);                
            }
        }
        ImageIO.write(image, "png", out);
    }

	
	public int getHistgram(int n) {
		return _count[n];
	}
	
	public int getHistgramSize() {
		return _count.length;
	}
	
	public int getMaxCount() {
		return _maxcount;
	}
	
	public int getIndex(double value) {
        if( _farthestDistance == 0.0 ) {
            _farthestDistance = (getMax() - getAvg() > getAvg() - getMin())?(getMax() - getAvg()):(getAvg() - getMin());
        }
//        if( _bucketSize == 0.0 ) {
//            _bucketSize = _farthestDistance / IMAGEHEIGHT;
//        }
        // when the value is maximum, its index takes out of bounds. 
        if( value == getMax()  && _farthestDistance == getMax() - getAvg())
            return (int)(Math.floor( (( value - getAvg() ) / (_farthestDistance * 2) + 0.5 ) * IMAGEHEIGHT ) - 1 );
        return (int)Math.floor( (( value - getAvg() ) / (_farthestDistance * 2) + 0.5 ) * IMAGEHEIGHT );
	}

    public void setNum(int n) {
        _num = n;
    }
    /**
     * @return
     */
    public int getNum() {
        return _num;
    }
}
