/*
 * Created on 2005/03/17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.table.Table;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExtractSignificantElements {
    int paramnum = 254;
    int gonum = 1491;
    int mingonum = 3;
    int maxgonum = 400;

    PrintStream log = System.err;
    private enum Opt {
        help, verbose, paramoutfile, gooutfile, both, reverse, threshold
    }
    int[] collist = new int [0];

    private TreeSet<String> siggoids = new TreeSet<String> ();
    private TreeSet<String> sigparaids = new TreeSet<String> ();
    private TreeSet<String> goids = new TreeSet<String> ();
    private TreeSet<String> paraids = new TreeSet<String> ();
    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    String paramOutFile = "";
    String goidOutFile = "";
    double threshold = 0.001;
    
    
    Table pvalueTable = null;

    public ExtractSignificantElements () throws OptionParserException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.both, "b", "both", "compute forward and reverse associations");
        optionParser.addOption(Opt.reverse, "r", "reverse", "compute reverse associations");
        optionParser.addOptionWithArgment(Opt.paramoutfile, "p", "outparam", "FILE", "parameter list file name. defalut=paramoutlist.txt", "paramoutlist.txt");
        optionParser.addOptionWithArgment(Opt.gooutfile, "g", "outgo", "FILE", "output file name. defalut=gooutlist.txt", "gooutlist.txt");
        optionParser.addOptionWithArgment(Opt.threshold, "t", "thres", "VALUE", "value default=0.3", "0.3");
        
    }
    
    public static void main(String[] args) {
        ExtractSignificantElements sigelements;
        try {
            sigelements = new ExtractSignificantElements();
            sigelements.init(args);
            sigelements.process();
        } catch (OptionParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SCMDException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void process() {
        PrintStream paramStream = null;
        PrintStream goidStream = null;
        try {
            paramStream = new PrintStream(new FileOutputStream(paramOutFile));
            goidStream = new PrintStream(new FileOutputStream(goidOutFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        for(String para: sigparaids) {
            paramStream.println(para);
        }
        for(String goid: siggoids) {
            goidStream.println(goid);
        }
    }

    /**
     * @param args
     * @throws OptionParserException 
     * @throws SCMDException 
     */
    private void init(String[] args) throws OptionParserException, SCMDException {
        optionParser.parse(args);
        if(optionParser.isSet(Opt.help)) {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if(optionParser.isSet(Opt.verbose)) {
            log = System.out;
        }
        if(optionParser.isSet(Opt.both)) {
            collist = new int [] {8,9,10,11};
        } else if(optionParser.isSet(Opt.reverse)) {
            collist = new int [] {10,11};
        } else {
            collist = new int [] {8, 9};
        }
        paramOutFile = optionParser.getValue(Opt.paramoutfile);
        goidOutFile  = optionParser.getValue(Opt.gooutfile);
        threshold = Double.parseDouble(optionParser.getValue(Opt.threshold));
        String filename = optionParser.getArgument(0);

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] cols = line.split("\t");
                double pvalue = Double.MAX_VALUE;
                for(int colnum: collist) {
                    if( pvalue > Double.parseDouble(cols[colnum])) {
                        pvalue = Double.parseDouble(cols[colnum]);
                    }
                }
                int num = Integer.parseInt(cols[3]);
                if( num < mingonum || maxgonum < num ) 
                    continue;
                if( pvalue < threshold / gonum ) {
                    sigparaids.add(cols[0]);
                }
                if( pvalue < threshold / paramnum ) {
                    siggoids.add(cols[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
