/*
 * Created on 2005/03/20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.analysis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.exception.DatabaseException;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DrawSignificantChart {

    private enum Opt {
        help, verbose, outfile, server, port, user, passwd, dbname, 
        pvaluefile, analysisfile, binomialfile, log, goid, paramname, reverse
    }
    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private PrintStream log = System.err;
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private Table pvaluetable = null;
    private Table analysistable = null;
    private String goid = "";
    private String paramname = "";
    private String binomialfile = ""; 
    private boolean drawReverse = false;
    private boolean drawHigh = false;
    private double curpval = 1.0;
    private double curratio = 0.0;

    double CELLWIDTH = 0.15;  
    int HEIGHT = 30;
    int LOWTHRESHOLD = -4;
    int HIGHTHRESHOLD = 4;
    int paramnum = 254;
    int gonum = 1491;

    PrintStream fwdlowout = null;
    PrintStream fwdhighout = null;
    PrintStream revlowout = null;
    PrintStream revhighout = null;
    
    RowLabelIndex pvalRowLabelIndex = null;
    ColLabelIndex pvalColLabelIndex = null;
    RowLabelIndex anaRowLabelIndex  = null;
    ColLabelIndex anaColLabelIndex  = null;

    QueryRunner queryRunner = null;
    HashMap<String, List<Strainname>> orflistMap = new HashMap<String, List<Strainname>> ();
    HashMap<String, String> gotermMap = new HashMap<String, String> ();

    Color lowCellColor = new Color(102, 153, 0); // 996600
    Color middleCellColor = Color.BLACK;
    Color highCellColor =  new Color(240, 48, 128);// old -- Color.MAGENTA;
    Color selectedColor = Color.YELLOW;

    public static void main(String[] args) {
        DrawSignificantChart dsc = null;
        try {
            dsc = new DrawSignificantChart();
            dsc.parse(args);
            dsc.process();
        } catch (OptionParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    private void process() {
        double fwdprobthres = 0.01 / (double)paramnum;
        double revprobthres = 0.01 / (double)gonum;
        
        if(binomialfile.length() != 0 ) {
            try {
                fwdlowout = new PrintStream(new FileOutputStream("fwdlow.html"));
                fwdhighout = new PrintStream(new FileOutputStream("fwdhigh.html"));
                revlowout = new PrintStream(new FileOutputStream("revlow.html"));
                revhighout = new PrintStream(new FileOutputStream("revhigh.html"));
                
                fwdlowout.println("<table>");
                fwdhighout.println("<table>");
                revlowout.println("<table>");
                revhighout.println("<table>");
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                List<GOTerm> goannot = (List<GOTerm>) queryRunner.query("select goid, name from term",
                        new BeanListHandler(GOTerm.class));
                for(GOTerm g: goannot) {
                    if(!gotermMap.containsKey(g.getName())) {
                        gotermMap.put(g.getGoid(), g.getName());
                    }
                }
            } catch (SQLException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            
//            List<Strainname> goannot = (List<Strainname>) queryRunner.query(
//                    "select systematicname as strainname, primaryname from genename_20040719 where systematicname in " +
//                    "( select distinct strainname from goassociation where goid "
//                    + "in (select cid as goid from term_graph where pid = '" 
//                    + goid + "') )", 
//                    new BeanListHandler(Strainname.class));
            
            BufferedReader fileReader = null;
            try {
                fileReader = new BufferedReader(new FileReader(binomialfile));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] cols = line.split("\t");
                    paramname = cols[0];
                    goid = cols[1];
                    gonum = Integer.parseInt(cols[3]);
                    if( Double.parseDouble(cols[8]) < fwdprobthres && gonum >= 3 && gonum <= 50 ) {
                        curpval = Double.parseDouble(cols[8]);
                        curratio = Double.parseDouble(cols[12]);
                        int signum = Integer.parseInt(cols[4]);
                        int specificnum = Integer.parseInt(cols[5]); 
                        drawChart(false, false, gonum, signum, specificnum);
                    }
                    if( Double.parseDouble(cols[9]) < fwdprobthres && gonum >= 3 && gonum <= 50 ) {
                        curpval = Double.parseDouble(cols[9]);
                        curratio = Double.parseDouble(cols[13]);
                        int signum = Integer.parseInt(cols[6]);
                        int specificnum = Integer.parseInt(cols[7]); 
                        drawChart(false, true, gonum, signum, specificnum);
                    }
                    if( Double.parseDouble(cols[10]) < revprobthres && gonum >= 3 && gonum <= 400) {
                        curpval = Double.parseDouble(cols[10]);
                        curratio = Double.parseDouble(cols[14]);
                        int signum = Integer.parseInt(cols[4]);
                        int specificnum = Integer.parseInt(cols[5]); 
                        drawChart(true, false, gonum, signum, specificnum);
                    }
                    if( Double.parseDouble(cols[11])< revprobthres && gonum >= 3 && gonum <= 400 ) {
                        curpval = Double.parseDouble(cols[11]);
                        curratio = Double.parseDouble(cols[15]);
                        int signum = Integer.parseInt(cols[6]);
                        int specificnum = Integer.parseInt(cols[7]); 
                        drawChart(true, true, gonum, signum, specificnum);
                    }
                }
                fwdlowout.println("</table>");
                fwdhighout.println("</table>");
                revlowout.println("</table>");
                revhighout.println("</table>");

                fwdlowout.close();
                fwdhighout.close();
                revlowout.close();
                revhighout.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } else {
//            drawChart();
        }
    }

    /**
     * @param string
     * @param string2
     * @param string3
     * @param b
     * @param c
     */
    private void outHtml(String param, String goid, double value, double ratio, boolean fwd, boolean high, PrintStream out,
            int gonum, int signum, int targetnum) {
        outHtml(param, goid, value, ratio, "", fwd, high, out, gonum, signum, targetnum);
    }

    private void outHtml(String param, String goid, double value, double ratio, String orfnames, boolean fwd, boolean high, PrintStream out,
            int gonum, int signum, int targetnum) {
        if( value < 2.2e-16 ) {
            value = 2.2e-16;
        }
        out.println("<tr>");
        out.println("<td>" + param + "</td>");
        out.println("<td>" + goid + "</td>");
        out.println("<td>" + gotermMap.get(goid) + "</td>");
        if(fwd) {
            String filename = paramname + "-" + goid.replaceFirst("GO:", "") + "_fg.png"; 
            out.println("<td>" + "<img src=\"" +  filename + "\" />" + "</td>");
            out.println("<td>" + orfnames + "</td>");
        } else {
            String filename = paramname + "_rg.png";
            String pifile = "";
            if( high ) {
                pifile = paramname + "-" + goid.replaceFirst("GO:", "") + "_high.png";
            } else {
                pifile = paramname + "-" + goid.replaceFirst("GO:", "") + "_low.png";
            }
            String piwholefile = paramname + "-" + goid.replaceFirst("GO:", "") + "_whole.png";
            out.println("<td>" + "<img src=\"" + filename + "\" />" + " </td>");
            out.println("<td>" + "<img src=\"" + pifile + "\" />" + " </td>");
            out.println("<td>" + "<img src=\"" + piwholefile + "\" />" + " </td>");
        }
        out.println("<td>" + gonum + "</td>");
        out.println("<td>" + signum + "</td>");
        out.println("<td>" + targetnum + "</td>");
        out.println("<td>" + value + "</td>");
        out.println("<td>" + ratio + "</td>");
        out.println("</tr>");
    }

    /**
     * @param b
     * @param c
     */
    private void drawChart(boolean rev, boolean high, int gonum, int signum, int targetnum) {
        drawReverse = rev;
        drawHigh = high;
        drawChart(gonum, signum, targetnum);
    }

    /**
     * 
     */
    private void drawChart(int gonum, int signum, int targetnum) {
        log.println(goid);
        log.println(paramname);

        try {
            List<Strainname> orfList = null;
            if(orflistMap.containsKey(goid)) {
                orfList = orflistMap.get(goid);
            } else {
                orfList = (List<Strainname>) queryRunner.query(
                        "select systematicname as strainname, primaryname from genename_20040719 where systematicname in " 
                        + "( select distinct strainname from goassociation where goid "
                        + "in (select cid as goid from term_graph where pid = '" 
                        + goid + "') )", 
                        new BeanListHandler(Strainname.class));
                orflistMap.put(goid, orfList);
            }
            TreeMap<String, Strainname> orfMap = new TreeMap<String, Strainname>();
            for(Strainname s: orfList) {
                orfMap.put(s.getStrainname(), s);
//                log.println(s.getStrainname());
            }
            
            // make hashed for sorting in terms of analysis data values
            HashMap<String, Double> pvalueHash = new HashMap<String, Double> ();
//            TreeMap<Double, > anavalueHash = new TreeMap<String, Double> (new ValueComparator());
            ORFValueSet[] anavalueList = new ORFValueSet[analysistable.getRowSize()-1]; 
            for(int i = 1; i < analysistable.getRowSize(); i++ )  {
                String orf = analysistable.get(i, 0).toString();
                double value = Double.parseDouble(analysistable.get(i, anaColLabelIndex.getColIndex(paramname)).toString());
                int pval  = Integer.parseInt(pvaluetable.get(pvalRowLabelIndex.getRowIndex(orf), pvalColLabelIndex.getColIndex(paramname)).toString()); 
                ORFValueSet valueSet = new ORFValueSet(orf, value, pval);
                anavalueList[i-1] = valueSet;
            }
            Arrays.sort(anavalueList, new ValueComparator());
            if(drawReverse) {
                drawReserseChart(goid, paramname, anavalueList, orfMap);
                if(drawHigh) {
                    outHtml(paramname, goid, curpval, curratio, false, true, revhighout,
                            gonum, signum, targetnum);
                } else {
                    outHtml(paramname, goid, curpval, curratio, false, false, revlowout,
                            gonum, signum, targetnum);
                }
            } else {
                String orfnames = "";
                orfnames = drawForwardDistribution(goid, paramname, anavalueList, orfMap);
                if(drawHigh) {
                    outHtml(paramname, goid, curpval, curratio, orfnames, true, true, fwdhighout,
                            gonum, signum, targetnum);
                } else {
                    outHtml(paramname, goid, curpval, curratio, orfnames, true, false, fwdlowout,
                            gonum, signum, targetnum);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param goid2
     * @param paramname2
     * @param anavalueList
     * @param orfMap
     */
    private void drawReserseChart(String goid2, String paramname2, ORFValueSet[] anavalueList, TreeMap<String, Strainname> orfMap) {
        BufferedImage arrayImage = new BufferedImage( (int)(CELLWIDTH * anavalueList.length), HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) arrayImage.getGraphics();
        int lowm = 0;
        int highm = 0;
        int y = 0;
        int lowx = 0;
        int highx = 0;
        
        // draw back ground
        TreeSet<String> orfset = new TreeSet<String>();
        for(String o: orfMap.keySet()) {
            orfset.add(o);
        }
        int count = 0;
        for(int i = 0; i < anavalueList.length; i++ ) {
            if( orfMap.containsKey(anavalueList[i].getOrf()) ) {
                y++;
                if( anavalueList[i].getPVal() <= LOWTHRESHOLD ) {
                    lowx++;
                } else if( anavalueList[i].getPVal() >= HIGHTHRESHOLD ) {
                    highx++;
                }
            }
            if( anavalueList[i].getPVal() <= LOWTHRESHOLD ) {
                lowm++;
            } else if( anavalueList[i].getPVal() >= HIGHTHRESHOLD ) {
                highm++;
            }
        }
        g.setColor(lowCellColor);
        g.fillRect( 0, 0, (int)(CELLWIDTH * lowm), HEIGHT);
        g.setColor(middleCellColor);
        g.fillRect( (int)(CELLWIDTH * lowm), 0, (int)(CELLWIDTH * (anavalueList.length - lowm - highm)), HEIGHT);
        g.setColor(highCellColor);
        g.fillRect( (int)(CELLWIDTH * (anavalueList.length - highm)), 0, (int)(CELLWIDTH * highm), HEIGHT);

        
        int PICHARTRAD = 75;
        
        BufferedImage lowPiImage = new BufferedImage( PICHARTRAD * 2, PICHARTRAD * 2, BufferedImage.TYPE_INT_BGR);
        Graphics2D lowPi = (Graphics2D) lowPiImage.getGraphics();
        lowPi.setColor(lowCellColor);
        lowPi.fill(new Ellipse2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2));
        lowPi.setColor(selectedColor);
        double lowrad = (double)lowx / (double)lowm * 360.0;
        lowPi.fill(new Arc2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2, 90 - lowrad, lowrad, Arc2D.PIE));
        //lowPi.fill(new Arc2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2, 0, 90, Arc2D.PIE));

        BufferedImage highPiImage = new BufferedImage( PICHARTRAD * 2, PICHARTRAD * 2, BufferedImage.TYPE_INT_BGR);
        Graphics2D highPi = (Graphics2D) highPiImage.getGraphics();
        highPi.setColor(highCellColor);
        highPi.fill(new Ellipse2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2));
        highPi.setColor(selectedColor);
        double highrad = (double)highx / (double)highm * 360.0;
        highPi.fill(new Arc2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2, 90 - highrad, highrad, Arc2D.PIE));

        BufferedImage wholePiImage = new BufferedImage( PICHARTRAD * 2, PICHARTRAD * 2, BufferedImage.TYPE_INT_BGR);
        Graphics2D wholePi = (Graphics2D) wholePiImage.getGraphics();
        wholePi.setColor(new Color(128,128,128));
        wholePi.fill(new Ellipse2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2));
        wholePi.setColor(selectedColor);
        double wholerad = (double)y / (double)anavalueList.length * 360.0;
        wholePi.fill(new Arc2D.Double(0, 0, PICHARTRAD * 2, PICHARTRAD * 2, 90 - wholerad, wholerad, Arc2D.PIE));

        try {
            ImageIO.write(arrayImage, "png", new PrintStream(new FileOutputStream(paramname + "_rg.png")));
            if(drawHigh) {
                ImageIO.write(highPiImage, "png", new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_high.png")));
            } else {
                ImageIO.write(lowPiImage, "png", new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_low.png")));
            }
            ImageIO.write(wholePiImage, "png", new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_whole.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param goid2
     * @param paramname2
     * @param anavalueList
     * @param orfMap
     */
    private String drawForwardDistribution(String goid, String paramname, ORFValueSet[] anavalueList, TreeMap<String, Strainname> orfMap) {
        BufferedImage arrayImage = new BufferedImage( (int)(CELLWIDTH * anavalueList.length), HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) arrayImage.getGraphics();
        int lowcount = 0;
        int highcount = 0;
        
        // draw back ground
        String orfnames = "";
        TreeSet<String> orfset = new TreeSet();
        for(String o: orfMap.keySet()) {
            orfset.add(o);
        }
        for(int i = 0; i < anavalueList.length; i++ ) {
            boolean flag = false;
            if( orfMap.containsKey(anavalueList[i].getOrf()) ) {
                flag = true;
                orfset.remove(anavalueList[i].getOrf());
            }
            if( anavalueList[i].getPVal() <= LOWTHRESHOLD ) {
                lowcount++;
                if( flag == true )
                    orfnames += "<font color=\"#2E8B57\">" + orfMap.get(anavalueList[i].getOrf()).getPrimaryname() + "</font>, ";
            } else if( anavalueList[i].getPVal() >= HIGHTHRESHOLD ) {
                highcount++;
                if( flag == true )
                    orfnames += "<font color=\"#CC0066\">" + orfMap.get(anavalueList[i].getOrf()).getPrimaryname() + "</font>, ";
            } else if ( flag == true ) {
                orfnames += "<font color=\"#000000\">" + orfMap.get(anavalueList[i].getOrf()).getPrimaryname() + "</font>, ";
            }
        }
        for(String o: orfset) {
            orfnames += "<font color=\"#999999\">" + orfMap.get(o).getPrimaryname() + "</font>, ";
        }
        g.setColor(lowCellColor);
        g.fillRect( 0, 0, (int)(CELLWIDTH * lowcount), HEIGHT);
        g.setColor(middleCellColor);
        g.fillRect( (int)(CELLWIDTH * lowcount), 0, (int)(CELLWIDTH * (anavalueList.length - lowcount - highcount)), HEIGHT);
        g.setColor(highCellColor);
        g.fillRect( (int)(CELLWIDTH * (anavalueList.length - highcount)), 0, (int)(CELLWIDTH * highcount), HEIGHT);
        
        // draw foreground
        g.setColor(selectedColor);
        for(int i = 0; i < anavalueList.length; i++ ) {
            String orf = anavalueList[i].getOrf();
            if( orfMap.containsKey(orf) ) {
                g.fillRect( (int)(CELLWIDTH * (i - 5)), 0, (int)(CELLWIDTH * 10), HEIGHT);
                orfset.remove(orf);
            }
        }
        log.println(orfnames.replaceFirst(", $", ""));

        try {
            ImageIO.write(arrayImage, "png", new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_fg.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orfnames.replaceFirst(", $", "");
    }

    /**
     * @param args
     * @throws OptionParserException 
     */
    private void parse(String[] args) throws OptionParserException {
        optionParser.parse(args);
        if(optionParser.isSet(Opt.help)) {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if(optionParser.isSet(Opt.verbose)) {
            log = System.out;
        }
        if(optionParser.isSet(Opt.reverse)) {
            drawReverse = true;
        }
       
        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName(optionParser.getValue(Opt.server));
        dataSource.setPortNumber(optionParser.getIntValue(Opt.port));
        dataSource.setDatabaseName(optionParser.getValue(Opt.dbname));
        dataSource.setUser(optionParser.getValue(Opt.user));
        dataSource.setPassword(optionParser.getValue(Opt.passwd));
        dataSource.setMaxConnections(10);

        queryRunner = new QueryRunner(dataSource);
        
        String pvaluefilename = optionParser.getValue(Opt.pvaluefile);
        String analysisfilename = optionParser.getValue(Opt.analysisfile);
        binomialfile = optionParser.getValue(Opt.binomialfile);

        goid = optionParser.getValue(Opt.goid);
        paramname = optionParser.getValue(Opt.paramname);
        
        try {
            pvaluetable = new Table(pvaluefilename);
            analysistable = new Table(analysisfilename);

            pvalRowLabelIndex = new RowLabelIndex(pvaluetable);
            pvalColLabelIndex = new ColLabelIndex(pvaluetable);
            anaRowLabelIndex  = new RowLabelIndex(analysistable);
            anaColLabelIndex  = new ColLabelIndex(analysistable);

        } catch (SCMDException e) {
            e.printStackTrace();
        }
    }

    public DrawSignificantChart() throws OptionParserException, DatabaseException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.reverse, "r", "reverse", "reverse genetics chart");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=stdout", "");
        optionParser.addOptionWithArgment(Opt.pvaluefile, "f", "pvalue", "FILE", "pvalue file name. defalut=", "");
        optionParser.addOptionWithArgment(Opt.analysisfile, "a", "analysisfile", "FILE", "analysis file name. defalut=", "");
        optionParser.addOptionWithArgment(Opt.binomialfile, "b", "binomialfile", "FILE", "binomial filename", "");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        optionParser.addOptionWithArgment(Opt.goid, "g", "goid", "ID", "goid. defalut=", "");        
        optionParser.addOptionWithArgment(Opt.paramname, "n", "paramname", "NAME", "parameter name. defalut=", "");        
        optionParser.addOptionWithArgment(Opt.log, "", "log", "FILE", "log file. defalut=\"\"", "");        
        initDB();
    }
    
    void initDB() throws DatabaseException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException(e);
        }
    }

}
