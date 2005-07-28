/*
 * Created on 2005/03/03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.imageio.ImageIO;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DrawConfidencesGOandMutants {
    private int FWDLOWCOL = 12;
    private int FWDHIGHCOL  = 13;
    private int REVLOWCOL = 14;
    private int REVHIGHCOL  = 15;

//    private int IMGWIDTH = 600;
//    private int IMGHEIGHT = 600;
    private int CELLWIDTH = 10;
    private int CELLHEIGHT = 10;
    private int MARGINWIDTH = 0;
    private int MARGINHEGHT = 0;
    private int RIGHTMARGIN = 100;
    private int TOPMARGIN = 100;
    
    private int LOWTHRES = 3;
    private int HIGHTHRES = 400;
    
    private Color fwdCellColor = new Color(255,255,0);
    private Color revCellColor  = new Color(0,0,255);
    private Color bgColor = new Color(0, 0, 0);
    private Color fgColor = new Color(255, 255, 255);
    private String filename = "";
    
    private double SIGTHRESHOLD = 0.3;
    
    private Table paramlist = new Table();
    private Table goidlist = new Table();
    private Table fwdLowConfTable = new Table();
    private Table fwdHighConfTable = new Table();
    private Table revLowConfTable = new Table();
    private Table revHighConfTable = new Table();
    
    private HashMap<String, Integer> gonum = new HashMap<String, Integer> (); 

    private enum Opt {
        help, verbose, forwardoutfile, reverseoutfile, paramfile, goidfile, booleanfile, threshold, imgoutfile
    }
    private PrintStream log = System.err;
    private PrintStream imgOutFile = System.out;
    private String forwardOutFile = "";
    private String reverseOutFile = "";
    private String boolOutFile = "";

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    
    public DrawConfidencesGOandMutants() throws OptionParserException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.forwardoutfile, "f", "forward", "FILE", "forward genetics output file name. defalut=", "");
        optionParser.addOptionWithArgment(Opt.reverseoutfile, "r", "reverse", "FILE", "reverse genetics output file name. defalut=", "");
        optionParser.addOptionWithArgment(Opt.paramfile, "p", "param", "FILE", "parameter list file name. defalut=param.txt", "param.txt");
        optionParser.addOptionWithArgment(Opt.booleanfile, "b", "bool", "FILE", "boolean output file name. defalut=conf.txt", "conf.txt");
        optionParser.addOptionWithArgment(Opt.goidfile, "g", "goid", "FILE", "output file name. defalut=goid.txt", "goid.txt");
        optionParser.addOptionWithArgment(Opt.imgoutfile, "i", "imgfile", "FILE", "output file name. defalut=image.txt", "");
        optionParser.addOptionWithArgment(Opt.threshold, "t", "thres", "VALUE", "value default=0.3", "0.3");
    }

    public static void main(String[] args) {
        DrawConfidencesGOandMutants drawrelations;
        try {
            drawrelations = new DrawConfidencesGOandMutants();
            drawrelations.init(args);
            drawrelations.process();
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SCMDException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void process() {
        RowLabelIndex rowLabelIndex = new RowLabelIndex(fwdLowConfTable);
        ColLabelIndex colLabelIndex = new ColLabelIndex(fwdLowConfTable);
        
        List goLabelsall = goidlist.getColList(0);
        List paramLabels = paramlist.getColList(0);

        List goLabels = new ArrayList();
        for(int row = 0; row < goLabelsall.size(); row++ ) {
            if( gonum.containsKey(goLabelsall.get(row).toString()) && 
                    ( gonum.get(goLabelsall.get(row).toString()) <= LOWTHRES || gonum.get(goLabelsall.get(row).toString()) >= HIGHTHRES ) ) {
                log.println(goLabelsall.get(row).toString() + "\tremove");
                continue;
            }
            goLabels.add(goLabelsall.get(row));
        }

        Table outputfwdTable = new Table(goLabels.size()+1, paramLabels.size()+1);
        Table outputrevTable = new Table(goLabels.size()+1, paramLabels.size()+1);
        Table outputBoolTable = new Table(goLabels.size()+1, paramLabels.size()+1);
        outputfwdTable.set(0, 0, "forward");
        outputrevTable.set(0, 0, "reverse");
        outputBoolTable.set(0, 0, "bool");
        
        BufferedImage arrayImage = new BufferedImage(
                paramLabels.size() * (CELLWIDTH + MARGINWIDTH ) + RIGHTMARGIN, 
                goLabels.size() * (CELLHEIGHT + MARGINHEGHT ) + TOPMARGIN, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) arrayImage.getGraphics();

        int[] assocCount = new int [paramLabels.size()];
        for(int col = 0; col < paramLabels.size(); col++ ) {
            assocCount[col] = 0;
            outputfwdTable.set(0, col+1, paramLabels.get(col));
            outputrevTable.set(0, col+1, paramLabels.get(col));
            outputBoolTable.set(0, col+1, paramLabels.get(col));
            
            double rad = (Math.PI * -90.0) / 180.0;
            AffineTransform at = AffineTransform.getRotateInstance(rad, (CELLWIDTH + MARGINWIDTH) * col + CELLWIDTH / 2, TOPMARGIN - 5);
            g.setTransform(at);
//            g.rotate(rad, (CELLWIDTH + MARGINWIDTH) * col, TOPMARGIN);
            g.setColor(fgColor);
            g.drawString(paramLabels.get(col).toString(), (CELLWIDTH + MARGINWIDTH) * col , TOPMARGIN);
            g.setTransform(new AffineTransform());
        }
        for(int row = 0; row < goLabels.size(); row++ ) {
            log.println(goLabels.get(row).toString() + "\t" + gonum.get(goLabels.get(row).toString()));
            outputfwdTable.set(row+1, 0, goLabels.get(row));
            outputrevTable.set(row+1, 0, goLabels.get(row));
            outputBoolTable.set(row+1, 0, goLabels.get(row));

            g.setColor(fgColor);
            g.drawString(goLabels.get(row).toString(), (CELLWIDTH + MARGINWIDTH) * paramLabels.size(),(CELLHEIGHT + MARGINHEGHT) * ( row + 1) + TOPMARGIN);

            for(int col = 0; col < paramLabels.size(); col++ ) {
                outputBoolTable.set(row+1 , col+1, 0.0);
                String fwdlowcell = fwdLowConfTable.get(rowLabelIndex.getRowIndex(goLabels.get(row).toString()), 
                        colLabelIndex.getColIndex(paramLabels.get(col).toString())).toString();
                double fwdlowvalue = 0.0;
                if( fwdlowcell.length() != 0 ) {
                    fwdlowvalue = Double.parseDouble(fwdlowcell);
                }
                String fwdhighcell = fwdHighConfTable.get(rowLabelIndex.getRowIndex(goLabels.get(row).toString()), 
                        colLabelIndex.getColIndex(paramLabels.get(col).toString())).toString();
                double fwdhighvalue = 0.0;
                if( fwdhighcell.length() != 0  ) {
                    fwdhighvalue = Double.parseDouble(fwdhighcell);
                }
//                log.println(goLabels.get(row).toString() + "\t"
//                        + paramLabels.get(col) + "\t" 
//                        + cell);
                if( fwdlowvalue >= fwdhighvalue) {
                    outputfwdTable.set(row+1, col+1, fwdlowvalue);
                } else {
                    outputfwdTable.set(row+1, col+1, fwdhighvalue);
                }
                if( fwdlowvalue >= SIGTHRESHOLD || fwdhighvalue >= SIGTHRESHOLD ) {
                    g.setColor(fwdCellColor);
                    g.fillRect( (CELLWIDTH + MARGINWIDTH) * col, (CELLHEIGHT + MARGINHEGHT) * row + TOPMARGIN, CELLWIDTH, CELLHEIGHT ); 
                    outputBoolTable.set(row+1, col+1, 1);
                    assocCount[col]++;
                }

                String revlowcell = revLowConfTable.get(rowLabelIndex.getRowIndex(goLabels.get(row).toString()), 
                        colLabelIndex.getColIndex(paramLabels.get(col).toString())).toString();
                double revlowvalue = 0.0;
                if( revlowcell.length() != 0 ) {
                    revlowvalue = Double.parseDouble(revlowcell);
                }
                String revhighcell = revHighConfTable.get(rowLabelIndex.getRowIndex(goLabels.get(row).toString()), 
                        colLabelIndex.getColIndex(paramLabels.get(col).toString())).toString();
                double revhighvalue = 0.0;
                if( revhighcell.length() != 0 ) {
                    revhighvalue = Double.parseDouble(revhighcell);
                }

                if( fwdlowvalue >= fwdhighvalue) {
                    outputrevTable.set(row+1, col+1, revlowvalue);
                } else {
                    outputrevTable.set(row+1, col+1, revhighvalue);
                }
                if ( revlowvalue >= SIGTHRESHOLD || revhighvalue >= SIGTHRESHOLD ) {
                    g.setColor(revCellColor);
                    g.fillRect( (CELLWIDTH + MARGINWIDTH) * col, (CELLHEIGHT + MARGINHEGHT) * row + TOPMARGIN, CELLWIDTH, CELLHEIGHT ); 
                    outputBoolTable.set(row+1, col+1, -1);
                    assocCount[col]++;
                }

                if( ( fwdlowvalue >= SIGTHRESHOLD || fwdhighvalue >= SIGTHRESHOLD ) &&
                        ( revlowvalue >= SIGTHRESHOLD || revhighvalue >= SIGTHRESHOLD ) ) {
                    g.setColor(new Color(255, 0, 0));
                    g.fillRect( (CELLWIDTH + MARGINWIDTH) * col, (CELLHEIGHT + MARGINHEGHT) * row + TOPMARGIN, CELLWIDTH, CELLHEIGHT ); 
                    outputBoolTable.set(row+1, col+1, 2);
                }
//                log.println(fwdlowvalue + "\t" + fwdhighvalue + "\t" + revlowvalue + "\t" + revhighvalue);
            }
        }
        for(int i = 0; i < paramLabels.size(); i++ ) {
            log.println(paramLabels.get(i) + "\t" + assocCount[i]);
        }
        try {
            if(!forwardOutFile.equals("")) {
                outputfwdTable.output(new FileOutputStream(forwardOutFile));
            }
            if(!reverseOutFile.equals("")) {
                outputrevTable.output(new FileOutputStream(reverseOutFile));
            }
            outputBoolTable.output(new FileOutputStream(boolOutFile));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
            
        
        try {
            ImageIO.write(arrayImage, "png", imgOutFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws OptionParserException 
     * @throws FileNotFoundException 
     * @throws SCMDException 
     */
    private void init(String[] args) throws OptionParserException, FileNotFoundException, SCMDException {
        optionParser.parse(args);
        if(optionParser.isSet(Opt.help)) {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if(optionParser.isSet(Opt.verbose)) {
            log = System.out;
        }

        if( optionParser.getValue(Opt.imgoutfile).equals("")) {
            imgOutFile = System.out;
        } else {
            imgOutFile = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.imgoutfile)));
        } 
        forwardOutFile = optionParser.getValue(Opt.forwardoutfile);
        reverseOutFile = optionParser.getValue(Opt.reverseoutfile);
        boolOutFile    = optionParser.getValue(Opt.booleanfile);
        goidlist = new Table(optionParser.getValue(Opt.goidfile));
        paramlist = new Table(optionParser.getValue(Opt.paramfile));
        SIGTHRESHOLD = Double.parseDouble(optionParser.getValue(Opt.threshold));
        
        filename = optionParser.getArgument(0);
        loadPvalueFile(goidlist, paramlist);
    }

    /**
     * @param goidlist
     * @param paramlist
     * @return
     */
    private void loadPvalueFile(Table goidlist, Table paramlist) {
        List params = paramlist.getColList(0);
        List goids  = goidlist.getColList(0);

        fwdLowConfTable = new Table(goids.size() + 1, params.size() + 1);
        fwdHighConfTable = new Table(goids.size() + 1, params.size() + 1);
        revLowConfTable = new Table(goids.size() + 1, params.size() + 1);
        revHighConfTable = new Table(goids.size() + 1, params.size() + 1);

        fwdLowConfTable.set(0, 0, "fwd low confidence matrix");
        fwdHighConfTable.set(0, 0, "fwd high confidence matrix");
        revLowConfTable.set(0, 0, "rev low confidence matrix");
        revHighConfTable.set(0, 0, "rev high confidence matrix");
        // set labels
        for( int i = 0; i < params.size(); i++ ) {
            fwdLowConfTable.set(0, i + 1, params.get(i));
            fwdHighConfTable.set(0, i + 1, params.get(i));
            revLowConfTable.set(0, i + 1, params.get(i));
            revHighConfTable.set(0, i + 1, params.get(i));
        }
        for( int i = 0; i < goids.size(); i++ ) {
            fwdLowConfTable.set(i + 1, 0, goids.get(i));
            fwdHighConfTable.set(i + 1, 0, goids.get(i));
            revLowConfTable.set(i + 1, 0, goids.get(i));
            revHighConfTable.set(i + 1, 0, goids.get(i));
        }

        RowLabelIndex rowLabelIndex = new RowLabelIndex(fwdLowConfTable);
        ColLabelIndex colLabelIndex = new ColLabelIndex(fwdLowConfTable);

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] cols = line.split("\t");
//              if( ( Integer.parseInt(cols[2]) <= LOWTHRES || Integer.parseInt(cols[2]) >= HIGHTHRES ) ||
//              ( Integer.parseInt(cols[3]) <= LOWTHRES || Integer.parseInt(cols[3]) >= HIGHTHRES ) ||
//              ( ( Integer.parseInt(cols[4]) <= LOWTHRES || Integer.parseInt(cols[4]) >= HIGHTHRES ) &&
//              ( Integer.parseInt(cols[6]) <= LOWTHRES || Integer.parseInt(cols[6]) >= HIGHTHRES ) ) ) {
//          
//      }
                int row = rowLabelIndex.getRowIndex(cols[1]);
                int col = colLabelIndex.getColIndex(cols[0]);
                if( row < 0 || col < 0 )
                    continue;
                gonum.put(cols[1], Integer.parseInt(cols[3]));
                fwdLowConfTable.set(row, col, cols[FWDLOWCOL]);
                fwdHighConfTable.set(row, col, cols[FWDHIGHCOL]);
                if( Integer.parseInt(cols[4]) > LOWTHRES  )
                    revLowConfTable.set(row, col, cols[REVLOWCOL]);
                if( Integer.parseInt(cols[6]) > LOWTHRES )
                    revHighConfTable.set(row, col, cols[REVHIGHCOL]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
