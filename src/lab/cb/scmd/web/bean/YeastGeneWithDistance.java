//--------------------------------------
//SCMDServer
//
//YeastGeneWithDistance.java 
//Since: 2005/02/05
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/datagen/Normalizer.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------
package lab.cb.scmd.web.bean;

import lab.cb.scmd.web.exception.XMLParseErrorException;

import org.w3c.dom.Element;


/**
 * @author sesejun
 *
 * 距離を遺伝子情報に加え保存しておくクラス
 * MorphologicalSearchで使用
 */
public class YeastGeneWithDistance extends YeastGene {
    private double distance = 0.0;

    public YeastGeneWithDistance() {
    }
    public YeastGeneWithDistance(String orf) {
        super(orf);
    }
    public YeastGeneWithDistance(Element element) throws XMLParseErrorException {
        super(element);
    }
    public double getDistance() {
        return distance;
    }
    public String getSlimedDistance() {
        String str = distance + ""; 
        if( str.length() < 5 )
            return str;
        return str.substring(0,5);
    }
    public void setDistance(double dist) {
        this.distance = dist;
    }
}
