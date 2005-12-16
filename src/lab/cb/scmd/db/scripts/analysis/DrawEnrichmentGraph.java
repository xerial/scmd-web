//--------------------------------------
//SCMDWeb
//
//DrawSignificantChart.java 
//Since: 2005/03/20
//
//$URL: http://phenome.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/action/CellViewerAction.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------
package lab.cb.scmd.db.scripts.analysis;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
 * DrawSignificantChartから、fork
 * パラーメータの名称などを、数値の通し番号にするため。
 * また、DB用に特化。
 * forward genetics, reverse genetics の図を描くプログラム
 * forward とreverseの定義が逆らしいので注意。
 * (変数名が全て逆になっている) 
 */
public class DrawEnrichmentGraph {

    private enum Opt {
        help, verbose, outfile, outputmethod, server, port, user, passwd, dbname, 
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
    HashMap<String, String> parameterMap = new HashMap<String, String> ();
    
    // for excel sheet
    private enum Method { db, excel, html };
	Method outputMethod = Method.db; 

    WritableWorkbook workbook = null;
    WritableSheet fwdlowsheet = null;
    WritableSheet fwdhighsheet = null;
    WritableSheet revlowsheet = null;
    WritableSheet revhighsheet = null;
    int fwdlowcount = 0;
    int fwdhighcount = 0;
    int revlowcount = 0;
    int revhighcount = 0;

    Color lowCellColor = new Color(102, 153, 0); // 996600
    Color middleCellColor = Color.BLACK;
    Color highCellColor =  new Color(240, 48, 128);// old -- Color.MAGENTA;
    Color selectedColor = Color.YELLOW;

    public static void main(String[] args) {
        DrawEnrichmentGraph dsc = null;
        try {
            dsc = new DrawEnrichmentGraph();
            dsc.parse(args);
            dsc.process();
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
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
        	if( outputMethod == Method.db ) {
                try {
					fwdlowout = new PrintStream(new FileOutputStream("fwdlow.tab"));
	                fwdhighout = new PrintStream(new FileOutputStream("fwdhigh.tab"));
	                revlowout = new PrintStream(new FileOutputStream("revlow.tab"));
	                revhighout = new PrintStream(new FileOutputStream("revhigh.tab"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
        	} else if( outputMethod == Method.excel ) {
        		try {
					workbook = Workbook.createWorkbook(new File("Supplementary_Table.xls"));
					fwdlowsheet = workbook.createSheet("reverse_low", 2);
                    makeSheetIndex(fwdlowsheet, true, true, fwdlowcount++);
					fwdhighsheet = workbook.createSheet("reverse_high", 3);
                    makeSheetIndex(fwdhighsheet, true, false, fwdhighcount++);
					revlowsheet = workbook.createSheet("forward_low", 0);
                    makeSheetIndex(revlowsheet, false, true, revlowcount++);
					revhighsheet = workbook.createSheet("forward_high", 1);
                    makeSheetIndex(revhighsheet, false, false, revhighcount++);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	} else {
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
        	}

            // get go list
            try {
                List<GOTerm> goannot = (List<GOTerm>) queryRunner.query("select goid, name from term",
                        new BeanListHandler(GOTerm.class));
                for(GOTerm g: goannot) {
                    if(!gotermMap.containsKey(g.getName())) {
                        gotermMap.put(g.getGoid(), g.getName());
                    }
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            // get parameter list
            List<ParameterList> paramList;
            try {
                paramList = (List<ParameterList>) queryRunner.query(
                        "select id as pid, name from parameterlist where scope='orf'", 
                        new BeanListHandler(ParameterList.class));
                for(ParameterList p: paramList) {
                    parameterMap.put(p.getName(), p.getPid() + "");
                }
            } catch (SQLException e3) {
                e3.printStackTrace();
            }
            
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
                if( outputMethod == Method.db ) {
                    fwdlowout.println();
                    fwdhighout.println();
                    revlowout.println();
                    revhighout.println();

                    fwdlowout.close();
                    fwdhighout.close();
                    revlowout.close();
                    revhighout.close();
                	
                } else if( outputMethod == Method.excel ) {
        			workbook.write();
        			try {
						workbook.close();
					} catch (WriteException e1) {
						e1.printStackTrace();
					}
                } else {
                    fwdlowout.println("</table>");
                    fwdhighout.println("</table>");
                    revlowout.println("</table>");
                    revhighout.println("</table>");

                    fwdlowout.close();
                    fwdhighout.close();
                    revlowout.close();
                    revhighout.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } else {
//            drawChart();
        }
    }

    /**
     * @param fwdlowsheet2
     * @param b
     * @param i
     */
    private void makeSheetIndex(WritableSheet sheet, boolean isFwd, boolean isLow, int i) {
        int col = 0;
        try {
            sheet.addCell(new Label(col++, 0, "Parameter"));
            sheet.addCell(new Label(col++, 0, "GO ID"));
            sheet.addCell(new Label(col++, 0, "GO Term"));
            sheet.addCell(new Label(col++, 0, "Significant Morphology Graph"));
            if( isFwd ) {
                sheet.addCell(new Label(col++, 0, "Genes having significant low value and associated with the GO Term"));
                sheet.addCell(new Label(col++, 0, "Genes having no significnat values but associated with the GO Term"));
                sheet.addCell(new Label(col++, 0, "Genes having significant high value and associated with the GO Term"));
                sheet.addCell(new Label(col++, 0, "Lethal Genes associated with the GO Term"));
            } else {
                if( isLow ) {
                    sheet.addCell(new Label(col++, 0, "Pichart of genes having both the GO term and significant low value (yellow) to significant low genes (green)"));
                    sheet.addCell(new Label(col++, 0, "Pichart of genes having both the GO term and significant low value (yellow) to all genes (gray)"));
                } else {
                    sheet.addCell(new Label(col++, 0, "Pichart of genes having both the GO term and significant high value (yellow) to significant high genes (red)"));
                    sheet.addCell(new Label(col++, 0, "Pichart of genes having both the GO term and significant high value (yellow) to all genes (gray)"));
                }
            }
            sheet.addCell(new Label(col++, 0, "# of genes associated with the GO term"));
            if( isLow ) {
                sheet.addCell(new Label(col++, 0, "# of genes having significant low value in this parameter"));
                sheet.addCell(new Label(col++, 0, "# of genes associated withe the GO term adn having significant low value"));
            } else {
                sheet.addCell(new Label(col++, 0, "# of genes having significant high value in this parameter"));
                sheet.addCell(new Label(col++, 0, "# of genes associated withe the GO term adn having significant high value"));
            }
            sheet.addCell(new Label(col++, 0, "P-value (<= 0.01, Bonferroni-corrected)"));
            if( isFwd ) {
                if( isLow ) {
                    sheet.addCell(new Label(col++, 0, "Ratio of # of genes having both the GO term and significant low value to number of genes having the GO term"));
                } else {
                    sheet.addCell(new Label(col++, 0, "Ratio of # of genes having both the GO term and significant high value to number of genes having the GO term"));
                }
                
            } else {
                if( isLow ) {
                    sheet.addCell(new Label(col++, 0, "Ratio of # of genes having both the GO term and significant low value to number of genes having significant low value"));
                } else {
                    sheet.addCell(new Label(col++, 0, "Ratio of # of genes having both the GO term and significant high value to number of genes having significant high value"));
                }
            }
            
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void addRow(String param, String goid, double value, double ratio, boolean fwd, boolean high, 
    		WritableSheet sheet, int count, 
    		int gonum, int signum, int targetnum) {
        addRow(param, goid, value, ratio, null, fwd, high, sheet, count, gonum, signum, targetnum);
    }
    
    private void addRow(String param, String goid, double value, double ratio, 
    		HashMap<String, ArrayList<String>> orfnames, boolean fwd, boolean high, 
    		WritableSheet sheet, int count, 
    		int gonum, int signum, int targetnum) {
        if( value < 2.2e-16 ) {
            value = 2.2e-16;
        }
		try {
			int col = 0;

            if( parameterMap.get(param) == null ) {
                System.err.println("NO VALID PARAM! --" + param + "--");
            }
            URL url = new URL("http://scmd.gi.k.u-tokyo.ac.jp/datamine/ViewORFParameter.do?columnType=input&paramID="+ parameterMap.get(param));
            WritableHyperlink link = new WritableHyperlink(col++, count, url);
            link.setDescription(param);
            sheet.addHyperlink(link);
            
            URL gourl = new URL("http://scmd.gi.k.u-tokyo.ac.jp/datamine/Search.do?keyword=" + goid);
            WritableHyperlink golink = new WritableHyperlink(col++, count, gourl);
            golink.setDescription(goid);
            sheet.addHyperlink(golink);
            
			//sheet.addCell(new Label(col++, count, param));
			//sheet.addCell(new Label(col++, count, goid));
			sheet.addCell(new Label(col++, count, gotermMap.get(goid)));
            
	        if(fwd) {
	            String filename = paramname + "-" + goid.replaceFirst("GO:", "") + "_fg.png"; 

                sheet.setRowView(count, 500);
                sheet.setColumnView(col, 100);

                File imageFile = new File(filename);
	            WritableImage img = new WritableImage(col++, count, 1, 1, imageFile);
				sheet.addImage(img);
				
				String lowOrfStr = getOrfStr(orfnames, "low");
                WritableFont lowCellFont = new WritableFont(WritableFont.ARIAL, 10);
                lowCellFont.setColour(Colour.BRIGHT_GREEN);
				sheet.addCell(new Label(col++, count, lowOrfStr, new WritableCellFormat(lowCellFont)));
				String middleOrfStr = getOrfStr(orfnames, "middle");
				sheet.addCell(new Label(col++, count, middleOrfStr));

                String highOrfStr = getOrfStr(orfnames, "high");
                WritableFont highCellFont = new WritableFont(WritableFont.ARIAL, 10);
                highCellFont.setColour(Colour.RED);
                sheet.addCell(new Label(col++, count, highOrfStr, new WritableCellFormat(highCellFont)));

                String lethalOrfStr = getOrfStr(orfnames, "lethal");
                WritableFont lethalCellFont = new WritableFont(WritableFont.ARIAL, 10);
                lethalCellFont.setColour(Colour.GRAY_80);
				sheet.addCell(new Label(col++, count, lethalOrfStr, new WritableCellFormat(lethalCellFont)));
	        } else {
	            String filename = paramname + "_rg.png";
	            String pifile = "";
	            if( high ) {
	                pifile = paramname + "-" + goid.replaceFirst("GO:", "") + "_high.png";
	            } else {
	                pifile = paramname + "-" + goid.replaceFirst("GO:", "") + "_low.png";
	            }
	            String piwholefile = paramname + "-" + goid.replaceFirst("GO:", "") + "_whole.png";
                sheet.setColumnView(col, 50);
                sheet.setRowView(count, 1150);
	            WritableImage barChart = new WritableImage(col++, count, 1, 1, new File(filename));
                sheet.setColumnView(col, 10);
	            WritableImage pichart  = new WritableImage(col++, count, 1, 1, new File(pifile));
                sheet.setColumnView(col, 10);
	            WritableImage piwholechart = new WritableImage(col++, count, 1, 1, new File(piwholefile));
	            sheet.addImage(barChart);
	            sheet.addImage(pichart);
	            sheet.addImage(piwholechart);
	        }
	        sheet.addCell(new Number(col++, count, gonum));
	        sheet.addCell(new Number(col++, count, signum));
	        sheet.addCell(new Number(col++, count, targetnum));
	        sheet.addCell(new Number(col++, count, value ));
	        sheet.addCell(new Number(col++, count, ratio ));
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
            e.printStackTrace();
        }
		
	}

    private String getOrfStr(HashMap<String, ArrayList<String>> orfnames, String index) {
    	String orfstr = "";
		for(String s: orfnames.get(index)) {
			orfstr += s + ", ";
		}
		return orfstr.substring(0, Math.max(0, orfstr.length() - 2));
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
        outHtml(param, goid, value, ratio, null, fwd, high, out, gonum, signum, targetnum);
    }

    private void outHtml(String param, String goid, double value, double ratio, HashMap<String, ArrayList<String>> orfnames, boolean fwd, boolean high, PrintStream out,
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
            //TODO: hashmap 対応にする
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

    private void outTabRow(String param, String goid, double value, double ratio, boolean fwd, boolean high, PrintStream out,
            int gonum, int signum, int targetnum) {
    	outTabRow(param, goid, value, ratio, null, fwd, high, out, gonum, signum, targetnum);
    }

    private void outTabRow(String param, String goid, double value, double ratio, HashMap<String, ArrayList<String>> orfnames, boolean fwd, boolean high, PrintStream out,
            int gonum, int signum, int targetnum) {
        if( value < 2.2e-16 ) {
            value = 2.2e-16;
        }
        HashMap<String,String> values = new HashMap<String,String>();
        values.put("param",param);
        values.put("goid",goid);
        values.put("goterm", gotermMap.get(goid));
        // orf名は必要なら dbのgoassocから得る 
        //values.put("orfs", orfnames);
        if( fwd ) {
            values.put("fwd", "1");
        } else {
        	values.put("fwd", "0");
        }
        if( high ) {
        	values.put("high", "1");
        } else {
        	values.put("high", "0");
        }
        values.put("gonum", Integer.valueOf(gonum).toString());
        values.put("signum", Integer.valueOf(signum).toString());
        values.put("targetnum", Integer.valueOf(targetnum).toString());
        values.put("pvalue", Double.valueOf(value).toString());
        values.put("ratio", Double.valueOf(ratio).toString());
        
        String[] keys = {"param", "goid", "fwd", "high", "gonum", "signum", "targetnum", "pvalue", "ratio"};
        for(int i = 0; i < keys.length; i++ ) {
        	if( i != 0 )
        		out.print("\t");
        	out.print(values.get(keys[i]));
        }
        out.println();
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
                	if( outputMethod == Method.db ) {
                        outTabRow(paramname, goid, curpval, curratio, false, true, 
                        		revhighout, 
                                gonum, signum, targetnum);
                		
                	} else if( outputMethod == Method.excel ) {
                        addRow(paramname, goid, curpval, curratio, false, true, 
                        		revhighsheet, revhighcount++,
                                gonum, signum, targetnum);
                	} else {
                        outHtml(paramname, goid, curpval, curratio, false, true, 
                        		revhighout,
                                gonum, signum, targetnum);
                	}
                } else {
                	if( outputMethod == Method.db ) {
                        outTabRow(paramname, goid, curpval, curratio, false, false, 
                        		revlowout, 
                                gonum, signum, targetnum);
                	
                	} else if ( outputMethod == Method.excel ) {
                        addRow(paramname, goid, curpval, curratio, false, false, 
                        		revlowsheet, revlowcount++,
                                gonum, signum, targetnum);
                	} else {
                        outHtml(paramname, goid, curpval, curratio, false, false, revlowout,
                                gonum, signum, targetnum);
                	}
                }
            } else {
            	HashMap<String, ArrayList<String>> orfnames = new HashMap<String, ArrayList<String>>();
                orfnames = drawForwardDistribution(goid, paramname, anavalueList, orfMap);
                if(drawHigh) {
                	if( outputMethod == Method.db ) {
                        outTabRow(paramname, goid, curpval, curratio, orfnames, true, true, 
                        		fwdhighout, 
                                gonum, signum, targetnum);
                	} else if( outputMethod == Method.excel ) {
                        addRow(paramname, goid, curpval, curratio, orfnames, true, true, 
                        		fwdhighsheet, fwdhighcount++,
                                gonum, signum, targetnum);
                	} else {
                        outHtml(paramname, goid, curpval, curratio, orfnames, true, true, 
                        		fwdhighout, gonum, signum, targetnum);
                	}
                } else {
                	if( outputMethod == Method.db ) {
                        outTabRow(paramname, goid, curpval, curratio, orfnames, true, false, 
                        		fwdlowout, 
                                gonum, signum, targetnum);
                	} else if( outputMethod == Method.excel ) {
                        addRow(paramname, goid, curpval, curratio, orfnames, true, false, 
                        		fwdlowsheet, fwdlowcount++,
                                gonum, signum, targetnum);
                	} else {
                        outHtml(paramname, goid, curpval, curratio, orfnames, true, false, 
                        		fwdlowout, gonum, signum, targetnum);
                	}
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
        g.fillRect( (int)Math.ceil(CELLWIDTH * (anavalueList.length - highm)), 0, (int)(CELLWIDTH * highm), HEIGHT);

        
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
        	PrintStream ps = new PrintStream(new FileOutputStream(paramname + "_rg.png"));
            ImageIO.write(arrayImage, "png", ps);
            ps.close();
            if(drawHigh) {
            	ps = new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_high.png"));
                ImageIO.write(highPiImage, "png", ps);
                ps.close();
            } else {
            	ps = new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_low.png"));
                ImageIO.write(lowPiImage, "png", ps);
                ps.close();
            }
        	ps = new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_whole.png"));
            ImageIO.write(wholePiImage, "png", ps);
            ps.close();
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
    private HashMap<String, ArrayList<String>> drawForwardDistribution(String goid, String paramname, ORFValueSet[] anavalueList, TreeMap<String, Strainname> orfMap) {
        BufferedImage arrayImage = new BufferedImage( (int)(CELLWIDTH * anavalueList.length), HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) arrayImage.getGraphics();
        int lowcount = 0;
        int highcount = 0;
        
        // draw back ground
        HashMap<String, ArrayList<String>> orfnames = new HashMap<String, ArrayList<String>>();
        orfnames.put("low", new ArrayList<String>());
        orfnames.put("middle", new ArrayList<String>());
        orfnames.put("high", new ArrayList<String>());
        orfnames.put("lethal", new ArrayList<String>());
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
                    orfnames.get("low").add(orfMap.get(anavalueList[i].getOrf()).getPrimaryname());
            } else if( anavalueList[i].getPVal() >= HIGHTHRESHOLD ) {
                highcount++;
                if( flag == true )
                    orfnames.get("high").add(orfMap.get(anavalueList[i].getOrf()).getPrimaryname());
            } else if ( flag == true ) {
                orfnames.get("middle").add(orfMap.get(anavalueList[i].getOrf()).getPrimaryname());
            }
        }
        for(String o: orfset) {
        	orfnames.get("lethal").add(orfMap.get(o).getPrimaryname());
        }
        g.setColor(lowCellColor);
        g.fillRect( 0, 0, (int)(CELLWIDTH * lowcount), HEIGHT);
        g.setColor(middleCellColor);
        g.fillRect( (int)(CELLWIDTH * lowcount), 0, (int)(CELLWIDTH * (anavalueList.length - lowcount - highcount)), HEIGHT);
        g.setColor(highCellColor);
        g.fillRect( (int)Math.ceil(CELLWIDTH * (anavalueList.length - highcount)), 0, (int)(CELLWIDTH * highcount), HEIGHT);
        
        // draw foreground
        g.setColor(selectedColor);
        for(int i = 0; i < anavalueList.length; i++ ) {
            String orf = anavalueList[i].getOrf();
            if( orfMap.containsKey(orf) ) {
                g.fillRect( (int)(CELLWIDTH * (i - 5)), 0, (int)(CELLWIDTH * 10), HEIGHT);
                orfset.remove(orf);
            }
        }
        //log.println(orfnames.replaceFirst(", $", ""));

        try {
            ImageIO.write(arrayImage, "png", new PrintStream(new FileOutputStream(paramname + "-" + goid.replaceFirst("GO:", "") + "_fg.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orfnames;
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
        if( optionParser.isSet(Opt.outputmethod) ) {
        	outputMethod = Method.valueOf(optionParser.getValue(Opt.outputmethod));
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

    public DrawEnrichmentGraph() throws OptionParserException, DatabaseException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.reverse, "r", "reverse", "reverse genetics chart");
        optionParser.addOptionWithArgment(Opt.outputmethod, "m", "outputmethod", "METHOD", "output excel sheet", "db");
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
