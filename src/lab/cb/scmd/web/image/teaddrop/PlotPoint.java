//--------------------------------------
// SCMDWeb Project
//
// PlotPoint.java
// Since: 2005/02/14
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image.teaddrop;

import java.util.Comparator;

/**
 * Teardropの画像上のプロットを表すクラス
 * @author leo
 *
 */
public class PlotPoint implements Comparable<PlotPoint>
{
    private int x;
    private int y;

    /**
     * 
     */
    public PlotPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }


    public int compareTo(PlotPoint other)
    {
        return this.y - other.y;
    }

    public static class XComparator implements Comparator<PlotPoint>
    {
        // @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
        public int compare(PlotPoint o1, PlotPoint o2)
        {
            return o1.x - o2.x;
        }
    }
    
    public int getX()
    {
        return x;
    }
    public void setX(int x)
    {
        this.x = x;
    }
    public int getY()
    {
        return y;
    }
}




