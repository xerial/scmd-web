/*
 * Created on 2005/03/22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BinomialTest {
    /**
     * @param total2
     * @param d
     * @param e
     * @param f
     * @return
     */
    public static double compute(int n, int m, int x, int y) {
        if( m == 0 )
            return 0.5;
        double probsum = 0.0;
        
        for( int i = 0; i < y; i++ ) {
            probsum += computeBinomProb(n, m, x, i);
        }
        return 1.0 - probsum;
    }

    /**
     * @param n
     * @param m
     * @param x
     * @param y
     * @return
     */
//    private double computeBinomProb(int n, int m, int x, int y) {
//        double p1 = (double)m / (double)n;
//        double p2 = 1.0 - p1;
//
//        double prob = 1.0;
//        double probcomb = 1.0;
//        for( int i = 1; i <= y; i++ ) {
//            probcomb *= (double)(n - i + 1) / (double)i;
//        }
//        prob = probcomb * Math.pow(p1, y) * Math.pow(p2, x - y);
//        return prob;
//    }
    private static double computeBinomProb(int n, int m, int x, int y) {
        double p1 = (double)m / (double)n;
        double logP1 = Math.log(m) - Math.log(n); 
        double p2 = 1.0 - p1;
        double logP2 = Math.log(n - m) - Math.log(n);
        double prob = 0.0;

        double problog = logP1 * (double)y + logP2 * (double)( x - y );
        prob += problog;
        
        double probcomb = 0.0;
        for(int i = 1; i <= y; i++ ) {
            probcomb += Math.log(x - i + 1);
            probcomb -= Math.log(i);
        }
        prob += probcomb;
        return Math.exp(prob);
    }
}
