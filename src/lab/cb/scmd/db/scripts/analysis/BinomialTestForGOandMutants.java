/*
 * Created on 2005/03/02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BinomialTestForGOandMutants {
    private int total = 4784;
    private int PARAMCOL = 0;
    private int TOTALCOL = 1;
    private int LOWNUMCOL = 2;
    private int HIGHNUMCOL = 3;
    private int GOCOL = 4;
    private int GONUMCOL = 5;
    private int GOLOWNUMCOL = 6;
    private int GOHIGHNUMCOL = 7;

    public static void main(String[] args) {
        BinomialTestForGOandMutants binom = new BinomialTestForGOandMutants();
        binom.process(args);
    }

    /**
     * @param args
     */
    private void process(String[] args) {
        String filename = args[0];
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] cols = line.split("\t");
                total = Integer.parseInt(cols[TOTALCOL]);
                double lowprob = BinomialTest.compute(total, Integer.parseInt(cols[LOWNUMCOL]), 
                        Integer.parseInt(cols[GONUMCOL]), Integer.parseInt(cols[GOLOWNUMCOL]));
                double highprob = BinomialTest.compute(total, Integer.parseInt(cols[HIGHNUMCOL]), 
                        Integer.parseInt(cols[GONUMCOL]), Integer.parseInt(cols[GOHIGHNUMCOL]));
                double lowrevprob = BinomialTest.compute(total, Integer.parseInt(cols[GONUMCOL]),  
                        Integer.parseInt(cols[LOWNUMCOL]), Integer.parseInt(cols[GOLOWNUMCOL]));
                double highrevprob = BinomialTest.compute(total, Integer.parseInt(cols[GONUMCOL]),  
                        Integer.parseInt(cols[HIGHNUMCOL]), Integer.parseInt(cols[GOHIGHNUMCOL]));
                double lowgoconfprob = 0.0;
                double highgoconfprob = 0.0;
                if( Integer.parseInt(cols[GONUMCOL]) != 0 ) {
                    lowgoconfprob = (double)Integer.parseInt(cols[GOLOWNUMCOL]) / (double)Integer.parseInt(cols[GONUMCOL]);
                    highgoconfprob = (double)Integer.parseInt(cols[GOHIGHNUMCOL]) / (double)Integer.parseInt(cols[GONUMCOL]);
                }
                double lowparaconfprob = 0.0;
                double highparaconfprob = 0.0;
                if( Integer.parseInt(cols[LOWNUMCOL]) != 0 ) {
                    lowparaconfprob = (double)Integer.parseInt(cols[GOLOWNUMCOL]) / (double)Integer.parseInt(cols[LOWNUMCOL]);
                }
                if( Integer.parseInt(cols[HIGHNUMCOL]) != 0 ) {
                    highparaconfprob = (double)Integer.parseInt(cols[GOHIGHNUMCOL]) / (double)Integer.parseInt(cols[HIGHNUMCOL]);
                }
                System.out.println(cols[PARAMCOL] + "\t" + cols[GOCOL] + "\t" + total + "\t" + cols[GONUMCOL] + "\t" 
                                + cols[LOWNUMCOL] + "\t" + cols[GOLOWNUMCOL] + "\t"
                                + cols[HIGHNUMCOL] + "\t" + cols[GOHIGHNUMCOL] + "\t" 
                                + lowprob + "\t" + highprob + "\t" + lowrevprob + "\t" + highrevprob + "\t"
                                + lowgoconfprob + "\t" + highgoconfprob + "\t" 
                                + lowparaconfprob + "\t" + highparaconfprob);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }

}
