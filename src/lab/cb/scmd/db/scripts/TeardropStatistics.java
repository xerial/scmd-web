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

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

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
    private String BGFILE                   = "TeardropBackground.png";

    private String BASE_COLOR               = "#8888FF";
    private int IMAGEWIDTH                  = 30;
    private int IMAGEHEIGHT                 = 128;
    private int BARHEIGHT                   = 1;
    private int MARGIN                      = 0;

    // program
    //String _converter = "C:/Program Files/ImageMagick-6.0.3-Q16/convert.exe";
    String _converter = "/usr/bin/convert";
    
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
			_count[index]++;
		}
		int maxcount = 0;
		for(int i = 0; i < _count.length; i++ ) {
			if( maxcount < _count[i] )
				maxcount = _count[i];
		}
		_maxcount = maxcount;
	}
    

    public void drawTeardrop(TeardropStatistics tds, PrintStream out) {
        int size = tds.getHistgramSize();
        int centerindex = tds.getIndex(tds.getAvg());
        String drawCommand = "-fill " + BASE_COLOR;
        for( int i = 0; i < size; i++ ) {
            int hpos = ( centerindex - i ) * BARHEIGHT + IMAGEHEIGHT /2 ;
            double barwidth = Math.log(tds.getHistgram(i) + 1) * (IMAGEWIDTH - MARGIN) / Math.log(tds.getMaxCount() + 1);
            double middle = IMAGEWIDTH/2.0;
            String stpos = (int)(middle - barwidth/2.0) + "," + hpos; 
            String edpos = (int)(middle + barwidth/2.0) + "," + (hpos + BARHEIGHT);
            drawCommand += " -draw \"rectangle " + stpos + " " + edpos + "\""; 
        }
        String cmd = _converter + " " + BGFILE + " " + drawCommand + " -"; 
        
        System.out.print(size + "\t" + cmd.length() + "\t...");
        System.out.flush();
        try {
            ProcessRunner.run(out, cmd);
        } catch (UnfinishedTaskException e) {
            e.printStackTrace();
        }
        System.out.println("end");
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
