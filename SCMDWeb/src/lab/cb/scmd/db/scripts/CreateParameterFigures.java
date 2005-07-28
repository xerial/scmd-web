//--------------------------------------
//SCMDWeb Project
//
//CreateParameterFigures.java
//Since: 2005/02/14
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/db/scripts/CreateTearDropTable.java $ 
//$Author: leo $
//--------------------------------------
package lab.cb.scmd.db.scripts;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import lab.cb.scmd.util.xml.DTDDeclaration;
import lab.cb.scmd.util.xml.InvalidXMLException;
import lab.cb.scmd.util.xml.XMLAttribute;
import lab.cb.scmd.util.xml.XMLOutputter;

/**
 * @author sesejun
 *
 * 各パラメータのイラストを出力する。
 * 
 * 
 */
public class CreateParameterFigures {
    PrintStream out = System.out;
    String dir = "";

    /**
     * @param string
     */
    public CreateParameterFigures(String outdir) {
        dir = outdir;
    }

    public static void main(String[] args) {
        CreateParameterFigures paramFigs = new CreateParameterFigures(args[0]);
        paramFigs.process();
    }

    /**
     * 
     */
    private void process() {
        initCellWallPoints();
        initNucleusPositions();
        try {
            cellFigure();
            actinFigure();
            dnaFigure();
        } catch (InvalidXMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private Point canvasSize;
    private Point motherCenter;
    private Point motherR;
    private Point C1_neck; // long axis point in mother cell (neck side)
    private Point C1_hip;  // long axis point in mother cell (hip side)
    private Point C2_neck; // short axis point in mother cell (neck side)
    private Point C2_hip;  // short axis point in mother cell (hip side)
    private Point C3_right; // neck point (right side)
    private Point C3_left;  // neck point (left side)
    private Point budCenter;
    private Point budR;
    private Point C4_neck;  // bud long axis point (neck side)
    private Point C4_tip;   // bud long axis point (tip side)
    private Point C5_right;
    private Point C5_left;
    private double buddegree = -60;
    private String cellWallColor = "rgb(94,194,192)";
    private Point CM;
    private Point CP106;
    private Point C10;

    String paramRegionColor = "rgb(222,241,237)";
    String paramLineColor = "red";
    
    static int WIDTH = 500;
    static int HEIGHT = 350;

    private void initCellWallPoints() {
        canvasSize = new Point(WIDTH, HEIGHT);
        motherCenter = new Point(200, 225);
        motherR = new Point(150, 100);
        C1_neck = new Point((int)(motherCenter.getX() + motherR.getX()), (int)motherCenter.getY());
        C1_hip  = new Point((int)(motherCenter.getX() - motherR.getX()), (int)motherCenter.getY());
        C2_neck = new Point((int)motherCenter.getX(), (int)(motherCenter.getY() - motherR.getY()));
        C2_hip  = new Point((int)motherCenter.getX(), (int)(motherCenter.getY() + motherR.getY()));
        C3_right = new Point((int)motherCenter.getX() + 133, (int)motherCenter.getY() - 47);
        C3_left  = new Point((int)motherCenter.getX() + 93, (int)motherCenter.getY() - 79);
        budCenter = new Point((int)motherCenter.getX() + 150, (int)motherCenter.getY() - 120);
        budR = new Point(80, 50);
        C4_tip  = new Point((int)(budCenter.getX() + budR.getX() * Math.cos((double)buddegree / 180.0 * Math.PI)),
                (int)(budCenter.getY() + budR.getX() * Math.sin((double)buddegree / 180.0 * Math.PI)));
        C4_neck = new Point((int)(budCenter.getX() - budR.getX() * Math.cos((double)buddegree / 180.0 * Math.PI)),
                (int)(budCenter.getY() - budR.getX() * Math.sin((double)buddegree / 180.0 * Math.PI)));
        CM  = new Point( (int)((C3_right.getX() + C3_left.getX())/2.0), (int)((C3_right.getY() + C3_left.getY())/2.0));
        C5_right = new Point((int)(budCenter.getX() - budR.getY() * Math.sin((double)buddegree / 180.0 * Math.PI )), 
                (int)(budCenter.getY() + budR.getY() * Math.cos((double)buddegree / 180.0 * Math.PI)));
        C5_left = new Point((int)(budCenter.getX() + budR.getY() * Math.sin((double)buddegree / 180.0 * Math.PI )), 
                (int)(budCenter.getY() - budR.getY() * Math.cos((double)buddegree / 180.0 * Math.PI )));
        C10 = new Point((int)motherCenter.getX() - 142, (int)motherCenter.getY() + 30 );
        CP106 = new Point((int)motherCenter.getX() + 80, (int)motherCenter.getY());
    }

    /**
     * @throws InvalidXMLException 
     * 
     */
    private void cellFigure() throws InvalidXMLException {
//        <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 20010904//EN"
//        "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd">

        String[] parameter = {
                "C11-1", "C11-2", "C12-1", "C12-2",
                "C101", "C102", "C103", "C104", "C105", "C106",
                "C107", "C108", "C109", "C110", "C111", "C112",
                "C113", "C114", "C115", "C128"
        };
        for(String param: parameter ) {
            XMLAttribute svgatt = new XMLAttribute();
            svgatt.add("width", canvasSize.getX());
            svgatt.add("height", canvasSize.getY());
            svgatt.add("xmlns", "http://www.w3.org/2000/svg");

            String filename = dir + "/" + param + ".svg";
            XMLOutputter xmlout = null;
            try {
                xmlout = new XMLOutputter(new FileOutputStream(filename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            xmlout.setDTDDeclaration(new DTDDeclaration("svg", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd" ));
            xmlout.startTag("svg", svgatt);
            paintBackGround(xmlout);
            writeCellWall(param, xmlout);
            xmlout.closeTag(); // svg
            xmlout.closeStream();
            xmlout.endOutput();
        }
    }
    
    private void paintBackGround(XMLOutputter xmlout) {
        XMLAttribute att = new XMLAttribute();
        att.add("x", 0);
        att.add("y", 0);
        att.add("width", WIDTH);
        att.add("height", HEIGHT);
        att.add("fill", "rgb(255,255,255)");
        att.add("stroke", "none");
        xmlout.selfCloseTag("rect", att);
    }

    private void writeCellWall(String param, XMLOutputter xmlout) throws InvalidXMLException {
        writeCellWall(true, xmlout);
        writeCellWallAxis(true, xmlout);
        if(param.equals("C11-1")) {
            drawMotherCellRegion(xmlout, true);
        } else if (param.equals("C11-2")) {
            drawBudRegion(xmlout, true);
        } else if (param.equals("C12-1")) {
            drawMotherCellRegion(xmlout, false);
        } else if (param.equals("C12-2")) {
            drawBudRegion(xmlout, false);
        } else if (param.equals("C101")) {
            drawMotherCellRegion(xmlout, true);
            drawBudRegion(xmlout, true);
        } else if (param.equals("C102")) {
            drawMotherCellRegion(xmlout, false);
            drawBudRegion(xmlout, false);
        } else if (param.equals("C103")) {
            drawParamLine(xmlout, C1_neck, C1_hip);
        } else if (param.equals("C104")) {
            drawParamLine(xmlout, C2_neck, C2_hip);
        } else if (param.equals("C105")) {
            drawAngle(xmlout, CM, motherCenter, C1_neck);
        } else if (param.equals("C106")) {
            drawAngle(xmlout, C4_tip, CP106, C1_neck);
        } else if (param.equals("C107")) {
            drawParamLine(xmlout, C4_neck, C4_tip);
        } else if (param.equals("C108")) {
            drawParamLine(xmlout, C5_left, C5_right);
        } else if (param.equals("C109")) {
            drawParamLine(xmlout, C3_left, C3_right);
        } else if (param.equals("C110")) {
            drawDottedLine(xmlout, C1_neck, new Point((int)canvasSize.getX(), (int)motherCenter.getY()));
            drawParamLine(xmlout, C4_tip, new Point((int)C4_tip.getX(), (int)motherCenter.getY()));
            drawParamLine(xmlout, 
                    new Point((int)C4_tip.getX() - 3, (int)C4_tip.getY()),
                    new Point((int)C4_tip.getX() + 3, (int)C4_tip.getY()));
            drawParamLine(xmlout, 
                    new Point((int)C4_tip.getX() - 3, (int)motherCenter.getY()),
                    new Point((int)C4_tip.getX() + 3, (int)motherCenter.getY()));
        } else if (param.equals("C111")) {
            drawDottedLine(xmlout, C2_neck, new Point((int)motherCenter.getX(), 0));
            drawParamLine(xmlout, C4_tip, new Point((int)motherCenter.getX(), (int)C4_tip.getY()));
            drawParamLine(xmlout, 
                    new Point((int)C4_tip.getX(), (int)C4_tip.getY() - 5),
                    new Point((int)C4_tip.getX(), (int)C4_tip.getY() + 5));
            drawParamLine(xmlout, 
                    new Point((int)motherCenter.getX(), (int)C4_tip.getY() - 5),
                    new Point((int)motherCenter.getX(), (int)C4_tip.getY() + 5));
        } else if (param.equals("C112")) {
            drawParamLine(xmlout, CM, motherCenter);
        } else if (param.equals("C113")) {
            drawParamLine(xmlout, C4_tip, CP106);
        } else if (param.equals("C114")) {
            drawDottedParamLine(xmlout, C4_neck, C4_tip);
            drawDottedParamLine(xmlout, C5_left, C5_right);
        } else if (param.equals("C115")) {
            drawDottedParamLine(xmlout, C1_hip, C1_neck);
            drawDottedParamLine(xmlout, C2_hip, C2_neck);
        } else if (param.equals("C128") ) {
            drawParamLine(xmlout, CM, C10);
        } else {
            System.out.println("ERROR! " + param);
        }
    }

    /**
     * @param xmlout
     */
    private void drawParamLine(XMLOutputter xmlout, Point p1, Point p2) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("stroke", paramLineColor);
        xmlatt.add("stroke-width", 5);
        drawline(xmlout, p1, p2, xmlatt);
    }

    private void drawNarrowLine(XMLOutputter xmlout, Point p1, Point p2) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("stroke", paramLineColor);
        xmlatt.add("stroke-width", 2);
        drawline(xmlout, p1, p2, xmlatt);
    }
    
    private void drawDottedLine(XMLOutputter xmlout, Point p1, Point p2) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("stroke", paramLineColor);
        xmlatt.add("stroke-width", 2);
        xmlatt.add("stroke-dasharray", "5, 10");
        xmlatt.add("fill", "none");
        drawline(xmlout, p1, p2, xmlatt);
    }

    private void drawDottedParamLine(XMLOutputter xmlout, Point p1, Point p2) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("stroke", paramLineColor);
        xmlatt.add("stroke-width", 5);
        xmlatt.add("stroke-dasharray", "10, 5");
        xmlatt.add("fill", "none");
        drawline(xmlout, p1, p2, xmlatt);
    }

    private void drawline(XMLOutputter xmlout, Point p1, Point p2) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("stroke", "rgb(94,194,192)");
        xmlatt.add("stroke-width", 1);
        drawline(xmlout, p1, p2, xmlatt);
    }
    /**
     * @param c1_neck2
     * @param c1_hip2
     */
    private void drawline(XMLOutputter xmlout, Point p1, Point p2, XMLAttribute xmlatt) {
        if( xmlatt == null ) {
            xmlatt = new XMLAttribute();
        }
        xmlatt.add("x1", (int)p1.getX());
        xmlatt.add("y1", (int)p1.getY());
        xmlatt.add("x2", (int)p2.getX());
        xmlatt.add("y2", (int)p2.getY());
        xmlout.selfCloseTag("line", xmlatt);
    }


    /**
     * @param budCenter2
     * @param c4_tip2
     * @param cp1062
     * @param c1_neck2
     */
    private void drawAngle(XMLOutputter xmlout, Point p1, Point pc, Point p2) {
        drawNarrowLine(xmlout, p1, pc);
        drawNarrowLine(xmlout, pc, p2);
        double length = 30.0;
        double degStart = Math.atan2(p1.getY() - pc.getY(), p1.getX() - pc.getX());
        Point startPoint = new Point((int)(pc.getX() + length * Math.cos(degStart)),
                (int)(pc.getY() + length * Math.sin(degStart)));
        double degEnd = Math.atan2(p2.getY() - pc.getY(), p2.getX() - pc.getX());
        Point endPoint   = new Point((int)(pc.getX() + length * Math.cos(degEnd)),
                (int)(pc.getY() + length * Math.sin(degEnd)));
        double degM = ( degStart + degEnd ) / 2.0;
        if( degEnd - degStart >= Math.PI )
            degM += Math.PI;
        Point midPoint = new Point((int)(pc.getX() + length * Math.cos(degM)),
                (int)(pc.getY() + length * Math.sin(degM)));
        String path = "M " + (int)startPoint.getX() + "," + (int)startPoint.getY();
        path += " " + "Q " + (int)midPoint.getX() + "," + (int)midPoint.getY();
        path += " " + (int)endPoint.getX() + "," + (int)endPoint.getY();

        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("d", path);
        xmlatt.add("fill", "none");
        xmlatt.add("stroke", paramLineColor);
        xmlatt.add("stroke-width", 5);
        xmlout.selfCloseTag("path", xmlatt);
    }

    /**
     * @param xmlout
     * @throws InvalidXMLException
     */
    private void drawBudRegion(XMLOutputter xmlout, boolean isFilled) throws InvalidXMLException {
        xmlout.startTag("g", new XMLAttribute("transform",
                "translate(" +
                ((int)budCenter.getX()) + "," + 
                ((int)budCenter.getY()) + ")"));
        xmlout.startTag("g", new XMLAttribute("transform", 
                "rotate(" + ((int)buddegree) + ")"));
        XMLAttribute xmlatt = getBudCellAttribute();
        if( isFilled ) {
            xmlatt.add("fill", paramRegionColor);
            xmlatt.add("stroke", "none");
        } else {
            xmlatt.add("fill", "none");
            xmlatt.add("stroke", paramLineColor);
            xmlatt.add("stroke-width", 5);
        }
        xmlout.startTag("ellipse", xmlatt);
        xmlout.closeTag(); // ellipse
        xmlout.closeTag(); // g rotate
        xmlout.closeTag(); // g translate
    }

    /**
     * @param xmlout
     * @throws InvalidXMLException
     */
    private void drawSmallBudWall(XMLOutputter xmlout) throws InvalidXMLException {
        xmlout.startTag("g", new XMLAttribute("transform",
                "translate(" +
                ((int)( 0.2 * budCenter.getX() + 0.8 * CM.getX()) ) + "," + 
                ((int)( 0.2 * budCenter.getY() + 0.8 * CM.getY()) ) + ")"));
        xmlout.startTag("g", new XMLAttribute("transform", 
                "rotate(" + ((int)buddegree) + ")"));
        XMLAttribute xmlatt = getSmallBudCellAttribute();
        xmlatt.add("fill", "none");
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "3");
        xmlout.startTag("ellipse", xmlatt);
        xmlout.closeTag(); // ellipse
        xmlout.closeTag(); // g rotate
        xmlout.closeTag(); // g translate
    }

    /**
     * @param xmlout
     * @throws InvalidXMLException
     */
    private void drawMotherCellRegion(XMLOutputter xmlout, boolean isFilled) throws InvalidXMLException {
        XMLAttribute xmlatt = getMotherCellAttribute();
        if( isFilled ) {
            xmlatt.add("fill", paramRegionColor);
            xmlatt.add("stroke", "none");
        } else {
            xmlatt.add("fill", "none");
            xmlatt.add("stroke", paramLineColor);
            xmlatt.add("stroke-width", 5);
        }
        xmlout.startTag("ellipse", xmlatt); // mother cell
        xmlout.closeTag(); // ellipse
    }
    
    private XMLAttribute getMotherCellAttribute() {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", motherCenter.getX());
        xmlatt.add("cy", motherCenter.getY());
        xmlatt.add("rx", motherR.getX());
        xmlatt.add("ry", motherR.getY());
        return xmlatt;
    }
    
    private XMLAttribute getBudCellAttribute() {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", "0");
        xmlatt.add("cy", "0");
        xmlatt.add("rx", ((int)budR.getX()));
        xmlatt.add("ry", ((int)budR.getY()));
        return xmlatt;
    }

    private XMLAttribute getSmallBudCellAttribute() {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", "0");
        xmlatt.add("cy", "0");
        xmlatt.add("rx", ((int)budR.getX() / 2));
        xmlatt.add("ry", ((int)budR.getY() / 1.5));
        return xmlatt;
    }

    /**
     * @param i
     * @throws InvalidXMLException 
     */
    private void writeCellWall(boolean hasBud, XMLOutputter xmlout) throws InvalidXMLException {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", motherCenter.getX());
        xmlatt.add("cy", motherCenter.getY());
        xmlatt.add("rx", motherR.getX());
        xmlatt.add("ry", motherR.getY());
        xmlatt.add("fill", "none");
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "3");
        
        xmlout.selfCloseTag("ellipse", xmlatt); // mother cell
        
        if( hasBud ) {
            xmlout.startTag("g", new XMLAttribute("transform",
                    "translate(" +
                    ((int)budCenter.getX()) + "," + 
                    ((int)budCenter.getY()) + ")"));
            
            xmlout.startTag("g", new XMLAttribute("transform", 
                    "rotate(" + ((int)buddegree) + ")"));

            XMLAttribute xmlattbud = new XMLAttribute();
            xmlattbud.add("cx", "0");
            xmlattbud.add("cy", "0");
            xmlattbud.add("rx", ((int)budR.getX()));
            xmlattbud.add("ry", ((int)budR.getY()));
            xmlattbud.add("fill", "none");
            xmlattbud.add("stroke", cellWallColor);
            xmlattbud.add("stroke-width", "3");
            xmlout.startTag("ellipse", xmlattbud);
            xmlout.closeTag(); // ellipse

            xmlout.closeTag(); // g rotate
            xmlout.closeTag(); // g translate
        }
    }

    private void writeCellWallAxis(boolean hasBud, XMLOutputter xmlout) throws InvalidXMLException {
        drawline(xmlout, C1_hip, C1_neck);
        drawline(xmlout, C2_neck, C2_hip);
        if(hasBud) { 
            drawline(xmlout, C4_neck, C4_tip);
            drawline(xmlout, C5_left, C5_right);
        }
    }

    private Point D1_mother;
    private Point D1_mother_R;
    private Point D1_bud;
    private Point D1_bud_R;
    private Point D1_A1B;
    private Point D1_A1B_R;
    private double D1_A1B_degree;
    private Point D1_A1B_mother;
    private Point D1_A1B_bud;
    private Point D3_A1B_tip;
    private Point D3_A1B_hip;
    private Point D4_mother_hip;
    private Point D4_mother_neck;
    private Point D4_bud_tip;
    private Point D4_bud_neck;
    private Point D5_mother_hip;
    private Point D5_mother_neck;
    private Point D5_bud_tip;
    private Point D5_bud_neck;
    private Point D5_A1B_hip;
    private Point D5_A1B_tip;
    private Point D6_mother;
    private Point D6_bud;
    private Point D7;
    
    String nucleusColor = "rgb(222,174,213)";

    private void initNucleusPositions() {
        D1_mother_R = new Point(50, 30); 
        D1_mother = new Point((int)(motherCenter.getX() - D1_mother_R.getX() - 5),
                (int)(motherCenter.getY() - D1_mother_R.getY() - 2));
        D1_bud_R = new Point(25, 20); 
        D1_bud = new Point((int)(budCenter.getX() + D1_bud_R.getX()),
                (int)(budCenter.getY() - D1_bud_R.getY()));
        D1_A1B_R = new Point(120, 20);
        D1_A1B   = CM;
        D1_A1B_degree = -45;
        D1_A1B_mother = new Point(
                (int)(D1_A1B.getX() - Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX() * 0.5),
                (int)(D1_A1B.getY() - Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX() * 0.5)); 
        D1_A1B_bud = new Point(
                (int)(D1_A1B.getX() + Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX() * 0.5),
                (int)(D1_A1B.getY() + Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX() * 0.5)); 
        D3_A1B_tip =  new Point(
                (int)(D1_A1B.getX() + Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX()),
                (int)(D1_A1B.getY() + Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX()));
        D3_A1B_hip =  new Point(
                (int)(D1_A1B.getX() - Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX()),
                (int)(D1_A1B.getY() - Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getX()));
        D5_A1B_tip = new Point(
                (int)( D1_A1B.getX() + Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getY()),
                (int)( D1_A1B.getY() - Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getY()));
        D5_A1B_hip = new Point(
                (int)( D1_A1B.getX() - Math.cos(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getY()),
                (int)( D1_A1B.getY() + Math.sin(D1_A1B_degree / 180.0 * Math.PI) * D1_A1B_R.getY()));
        D4_mother_hip = new Point(
                (int)(D1_mother.getX() - D1_mother_R.getX()), (int)D1_mother.getY() );
        D4_mother_neck = new Point(
                (int)(D1_mother.getX() + D1_mother_R.getX()), (int)D1_mother.getY() );
        D4_bud_tip = new Point(
                (int)(D1_bud.getX() + D1_bud_R.getX()), (int)D1_bud.getY() );
        D4_bud_neck = new Point(
                (int)(D1_bud.getX() - D1_bud_R.getX()), (int)D1_bud.getY() );
        D5_mother_hip = new Point(
                (int)D1_mother.getX(), (int)( D1_mother.getY() + D1_mother_R.getY()));
        D5_mother_neck = new Point(
                (int)D1_mother.getX(), (int)( D1_mother.getY() - D1_mother_R.getY()));
        D5_bud_tip = new Point(
                (int)D1_bud.getX(), (int)( D1_bud.getY() - D1_bud_R.getY()));
        D5_bud_neck = new Point(
                (int)D1_bud.getX(), (int)( D1_bud.getY() + D1_bud_R.getY()));
        D6_mother = new Point (
                (int)(D1_mother.getX() + (CM.getX() - D1_mother.getX()) * 0.28),
                (int)(D1_mother.getY() + (CM.getY() - D1_mother.getY()) * 0.28)
                );
        D6_bud = new Point (
                (int)(D1_bud.getX() + (CM.getX() - D1_bud.getX()) * 0.2),
                (int)(D1_bud.getY() + (CM.getY() - D1_bud.getY()) * 0.2)
                );
        D7 = new Point ( 
                (int)(D1_mother.getX() + (C10.getX() - D1_mother.getX()) * 0.37),
                (int)(D1_mother.getY() + (C10.getY() - D1_mother.getY()) * 0.37) );
    }

    private void writeNucleus(int type, XMLOutputter xmlout) throws InvalidXMLException {
        // type 0: nucleus only in mother cell
        //      1: nucleus between mother and bud
        //      2: nucleus in mother and bud
        //      3: nucleus only in bud cell
        if( type == 0 || type == 2 ) { // draw nucleus in mother cell
            XMLAttribute xmlatt = new XMLAttribute();
            xmlatt.add("cx", D1_mother.getX());
            xmlatt.add("cy", D1_mother.getY());
            xmlatt.add("rx", D1_mother_R.getX());
            xmlatt.add("ry", D1_mother_R.getY());
            xmlatt.add("fill", nucleusColor);
            xmlatt.add("stroke", cellWallColor);
            xmlatt.add("stroke-width", "1");
            xmlout.selfCloseTag("ellipse", xmlatt);
        }
        if( type == 2 || type == 3 ) { // draw nucleus in bud
            XMLAttribute xmlattbud = new XMLAttribute();
            xmlattbud.add("cx", D1_bud.getX());
            xmlattbud.add("cy", D1_bud.getY());
            xmlattbud.add("rx", D1_bud_R.getX());
            xmlattbud.add("ry", D1_bud_R.getY());
            xmlattbud.add("fill", nucleusColor);
            xmlattbud.add("stroke", cellWallColor);
            xmlattbud.add("stroke-width", "1");
            xmlout.selfCloseTag("ellipse", xmlattbud);
        }
        if( type == 1 ) { // draw nucleus between mother and bud
            xmlout.startTag("g", new XMLAttribute("transform",
                    "translate(" +
                    ((int)D1_A1B.getX()) + "," + 
                    ((int)D1_A1B.getY()) + ")"));
            
            xmlout.startTag("g", new XMLAttribute("transform", 
                    "rotate(" + ((int)D1_A1B_degree) + ")"));

            XMLAttribute xmlattbud = new XMLAttribute();
            xmlattbud.add("cx", "0");
            xmlattbud.add("cy", "0");
            xmlattbud.add("rx", ((int)D1_A1B_R.getX()));
            xmlattbud.add("ry", ((int)D1_A1B_R.getY()));
            xmlattbud.add("fill", nucleusColor);
            xmlattbud.add("stroke", cellWallColor);
            xmlattbud.add("stroke-width", "2");
            xmlout.selfCloseTag("ellipse", xmlattbud);
            xmlout.closeTag(); // g rotate
            xmlout.closeTag(); // g translate
            
        }
        if ( type == 10 ) {
            XMLAttribute xmlatt = new XMLAttribute();
            //xmlatt.add("cx", 2*D1_mother.getX() - motherCenter.getX());
            //xmlatt.add("cy", 2*D1_mother.getY() - motherCenter.getY());
            xmlatt.add("cx", (int)(CM.getX() + motherCenter.getX())/2.0);
            xmlatt.add("cy", (int)(CM.getY() + motherCenter.getY())/2.0);
            xmlatt.add("rx", D1_mother_R.getX());
            xmlatt.add("ry", D1_mother_R.getY());
            xmlatt.add("fill", nucleusColor);
            xmlatt.add("stroke", cellWallColor);
            xmlatt.add("stroke-width", "1");
            xmlout.selfCloseTag("ellipse", xmlatt);
        }
        writeNucleusAxis(type, xmlout);
    }

    private void writeNucleusType(String type, XMLOutputter xmlout) throws InvalidXMLException {
        if( type.equals("A")) {
            writeNucleus(0, xmlout);
        } else if ( type.equals("A1")) {
            writeNucleus(10, xmlout);
        } else if ( type.equals("B")) {
            writeNucleus(1, xmlout);
        } else if ( type.equals("C")) {
            writeNucleus(2, xmlout);
        } else if ( type.equals("D")) {
            writeNucleus(0, xmlout);
            writeNucleus(10, xmlout);
        } else if ( type.equals("E")) {
            // no nucleus
        } else if ( type.equals("F")) {
            writeNucleus(3, xmlout);
        }
    }
    
    private void writeNucleusAxis(int type, XMLOutputter xmlout) throws InvalidXMLException {
        if( type == 0 || type == 2 ) {
            drawline(xmlout, D4_mother_hip, D4_mother_neck);
            drawline(xmlout, D5_mother_neck, D5_mother_hip);
        }
        if( type == 2 ) { 
            drawline(xmlout, D4_bud_neck, D4_bud_tip);
            drawline(xmlout, D5_bud_neck, D5_bud_tip);
        }
        if( type == 1 ) {
        }
    }
    
    private void writePoint(XMLOutputter xmlout, Point p, String color) {
        XMLAttribute xmlattbud = new XMLAttribute();
        xmlattbud.add("cx", (int)p.getX());
        xmlattbud.add("cy", (int)p.getY());
        xmlattbud.add("rx", "5");
        xmlattbud.add("ry", "5");
        xmlattbud.add("fill", color);
        xmlattbud.add("stroke", cellWallColor);
        xmlattbud.add("stroke-width", "1");
        xmlout.selfCloseTag("ellipse", xmlattbud);
    }

    private void writeNucleus(String param, XMLOutputter xmlout) throws InvalidXMLException {
        writeCellWall(true, xmlout);
        writeCellWallAxis(true, xmlout);
        if( param.equals("D102") || param.equals("D105")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, C1_neck);
        } else if (param.equals("D103") || param.equals("D106")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, C1_hip);
        } else if (param.equals("D104") || param.equals("D107")) {
            writeCellWallAxis(true, xmlout);
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B, C1_hip);
        } else if ( param.equals("D108" ) || param.equals("D110")
                || param.equals("D112") || param.equals("D114")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, CM);
        } else if ( param.equals("D109" ) || param.equals("D113")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D1_bud, CM);
        } else if ( param.equals("D111") || param.equals("D115")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D1_bud, CM);
        } else if ( param.equals("D116")) {
            writeNucleus(2, xmlout);
            drawParamLine(xmlout, D1_mother, D1_bud);
        } else if ( param.equals("D117") || param.equals("D147")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, motherCenter);
        } else if ( param.equals("D118")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B_bud, motherCenter);
        } else if ( param.equals("D119") || param.equals("D149")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D1_bud, budCenter);
        } else if ( param.equals("D120")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B_bud, budCenter);
        } else if ( param.equals("D121") || param.equals("D123")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D1_bud, C4_tip);
        } else if ( param.equals("D122") || param.equals("D124")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B, C4_tip);
        } else if ( param.equals("D125")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, C10);
        } else if ( param.equals("D126")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B, C10);
        } else if ( param.equals("D143")) {
            writeNucleus(0, xmlout);
            drawDottedLine(xmlout, D1_mother, CM);
            drawParamLine(xmlout, D6_mother, CM);
        } else if ( param.equals("D144")) {
            writeNucleus(3, xmlout);
            drawDottedLine(xmlout, D1_bud, CM);
            drawParamLine(xmlout, D6_bud, CM);
        } else if ( param.equals("D145")) {
            writeNucleus(0, xmlout);
            drawDottedLine(xmlout, D1_mother, C10);
            drawDottedLine(xmlout, CM, C10);
            drawParamLine(xmlout, D7, C10);
        } else if ( param.equals("D151")) {
            writeNucleus(2, xmlout);
            drawDottedLine(xmlout, D1_bud, CM);
            drawDottedLine(xmlout, D1_mother, CM);
        } else if ( param.equals("D152")){
            writeNucleus(0, xmlout);
            drawDottedLine(xmlout, D6_mother, CM);
            drawDottedLine(xmlout, D7, C10);
        } else if ( param.equals("D154")) {
            writeNucleus(0, xmlout);
            drawAngle(xmlout, D1_mother, motherCenter, C1_hip);
        } else if ( param.equals("D155")) {
            writeNucleus(3, xmlout);
            drawAngle(xmlout, C1_hip, motherCenter, D1_bud);
        } else if ( param.equals("D156")) {
            writeNucleus(2, xmlout);
            drawAngle(xmlout, C4_tip, budCenter, D1_mother);
        } else if ( param.equals("D157")) {
            writeNucleus(2, xmlout);
            drawAngle(xmlout, C4_tip, budCenter, D1_bud);
        } else if ( param.equals("D169")) {
            writeNucleus(0, xmlout);
            drawAngle(xmlout, D1_mother, CM, motherCenter);
        } else if ( param.equals("D171")) {
            writeNucleus(0, xmlout);
            drawAngle(xmlout, D4_mother_hip, CM, motherCenter);
        } else if ( param.equals("D172")) {
            writeNucleus(1, xmlout);
            drawAngle(xmlout, D3_A1B_hip, CM, motherCenter);
        } else if ( param.equals("D173")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, D4_mother_neck);
        } else if ( param.equals("D174")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D1_bud, D4_bud_tip);
        } else if ( param.equals("D175")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B, D3_A1B_hip);
        } else if ( param.equals("D176")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D4_mother_hip, D4_mother_neck);
        } else if ( param.equals("D177")) {
            writeNucleus(3, xmlout);
            drawParamLine(xmlout, D4_bud_tip, D4_bud_neck);
        } else if ( param.equals("D178")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D3_A1B_tip, D3_A1B_hip);
        } else if ( param.equals("D179")) {
            writeNucleus(0, xmlout);
            drawParamLine(xmlout, D1_mother, D5_mother_neck);
        } else if ( param.equals("D180")) {
            writeNucleus(2, xmlout);
            drawParamLine(xmlout, D1_bud, D5_bud_tip);
        } else if ( param.equals("D181")) {
            writeNucleus(1, xmlout);
            drawParamLine(xmlout, D1_A1B, D5_A1B_hip);
        } else if ( param.equals("D182") || param.equals("D184")) {
            writeNucleus(0, xmlout);
            drawDottedLine(xmlout, D4_mother_neck, D4_mother_hip);
            drawDottedLine(xmlout, D5_mother_hip, D5_mother_neck);
        } else if ( param.equals("D183") ) {
            writeNucleus(3, xmlout);
            drawDottedLine(xmlout, D4_bud_neck, D4_bud_tip);
            drawDottedLine(xmlout, D5_bud_tip, D5_bud_neck);
        } else if ( param.equals("D197")) {
            writeNucleus(2, xmlout);

            XMLAttribute xmlatt = new XMLAttribute();
            xmlatt.add("cx", D1_mother.getX());
            xmlatt.add("cy", D1_mother.getY());
            xmlatt.add("rx", D1_mother_R.getX());
            xmlatt.add("ry", D1_mother_R.getY());
            xmlatt.add("fill", "none");
            xmlatt.add("stroke", paramLineColor);
            xmlatt.add("stroke-width", "3");
            xmlout.selfCloseTag("ellipse", xmlatt);

            XMLAttribute xmlattbud = new XMLAttribute();
            xmlattbud.add("cx", D1_bud.getX());
            xmlattbud.add("cy", D1_bud.getY());
            xmlattbud.add("rx", D1_bud_R.getX());
            xmlattbud.add("ry", D1_bud_R.getY());
            xmlattbud.add("fill", "none");
            xmlattbud.add("stroke", paramLineColor);
            xmlattbud.add("stroke-width", "3");
            xmlout.selfCloseTag("ellipse", xmlattbud);
        } else if ( param.equals("D199") 
                || param.equals("D206") || param.equals("D210") ) {
            writeNucleusType("A", xmlout);
        } else if ( param.equals("D200") 
                || param.equals("D207") || param.equals("D211") 
                || param.equals("D214") ) {
            writeNucleusType("A1", xmlout);
        } else if ( param.equals("D201") 
                || param.equals("D208") || param.equals("D212") 
                || param.equals("D215")) {
            writeNucleusType("B", xmlout);
        } else if ( param.equals("D202") 
                || param.equals("D209") || param.equals("D213") 
                || param.equals("D216")) {
            writeNucleusType("C", xmlout);
        } else if ( param.equals("D203") ) {
            writeNucleusType("D", xmlout);
        } else if ( param.equals("D204") ) {
            writeNucleusType("E", xmlout);
        } else if ( param.equals("D205") ) {
            writeNucleusType("F", xmlout);
        } else {
            System.out.println("ERROR! " + param);
        }
    }

    private void dnaFigure() throws InvalidXMLException {
        String[] parameter = {
                "D102", "D103", "D104", "D105", "D106",
                "D107", "D108", "D109", "D110", "D111",
                "D112", "D113", "D114", "D115", "D116",
                "D117", "D118", "D119", "D120", "D121",
                "D122", "D123", "D124", "D125", "D126",
                "D143", "D144", "D145", "D147", "D149",
                "D151", "D152", "D154", "D155", "D156", 
                "D157", "D169", "D171", "D172", "D173", 
                "D174", "D175", "D176", "D177", "D178",
                "D179", "D180", "D181", "D182", "D183",
                "D184", "D197", 
                "D199", "D200", "D201", "D202", "D203",
                "D204", "D205", "D206", "D207", "D208",
                "D209", "D210", "D211", "D212", "D213",
                "D214", "D215", "D216"
        };
        for(String param: parameter ) {
            XMLAttribute svgatt = new XMLAttribute();
            svgatt.add("width", canvasSize.getX());
            svgatt.add("height", canvasSize.getY());
            svgatt.add("xmlns", "http://www.w3.org/2000/svg");

            String filename = dir + "/" + param + ".svg";
            XMLOutputter xmlout = null;
            try {
                xmlout = new XMLOutputter(new FileOutputStream(filename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            xmlout.setDTDDeclaration(new DTDDeclaration("svg", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"));
            xmlout.startTag("svg", svgatt);
            paintBackGround(xmlout);
            writeNucleus(param, xmlout);
            xmlout.closeTag(); // svg
            xmlout.closeStream();
            xmlout.endOutput();
        }
    }
    
    /**
     * @param param
     * @param xmlout
     * @throws InvalidXMLException 
     */
    private void writeActin(String param, XMLOutputter xmlout) throws InvalidXMLException {
        if( param.equals("A1-1") || param.equals("A1-2")
                || param.equals("A1-3") ) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinOnMother(xmlout);
            drawActinLocalizedinBud(xmlout);
        } else if( param.equals("A2-1") ) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinBudRegion(xmlout);
            drawCenterOfActinInBud(xmlout);
        } else if ( param.equals("A2-2") ) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinMotherRegion(xmlout);
            drawCenterOfActinInMother(xmlout);
        } else if ( param.equals("A2-3")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinWholeRegion(xmlout);
            drawCenterOfActin(xmlout);
        } else if ( param.equals("A3-1")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinOnMother(xmlout);
            drawParamPoint(xmlout, motherCenter);
        } else if ( param.equals("A3-2")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinLocalizedinBud(xmlout);
            drawParamPoint(xmlout, budCenter);
        } else if ( param.equals("A3-3")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinOnMother(xmlout);
            drawActinLocalizedinBud(xmlout);
            drawParamPoint(xmlout, CM);
        } else if ( param.equals("A6-1")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinMotherRegion(xmlout);
            Point farthestPoint = new Point((int)( 0.65 * C1_hip.getX() + 0.35 * C2_hip.getX() ), 
                    (int)( 0.25 * C1_hip.getY() + 0.75 * C2_hip.getY() )); 
            drawDottedLine(xmlout, CM, farthestPoint );
            drawParamPoint(xmlout, farthestPoint);
        } else if ( param.equals("A6-2")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinBudRegion(xmlout);
            Point farthestPoint = new Point((int)( C4_tip.getX() - 5 ), (int)( C4_tip.getY() + 5 )); 
            drawDottedLine(xmlout, CM, farthestPoint );
            drawParamPoint(xmlout, farthestPoint);
        } else if ( param.equals("A6-3")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinWholeRegion(xmlout);
            Point farthestPoint = new Point((int)( 0.65 * C1_hip.getX() + 0.35 * C2_hip.getX() ), 
                    (int)( 0.25 * C1_hip.getY() + 0.75 * C2_hip.getY() )); 
            drawDottedLine(xmlout, CM, farthestPoint );
            drawParamPoint(xmlout, farthestPoint);
        } else if( param.equals("A101")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinWholeRegion(xmlout);
        } else if ( param.equals("A105")) {
            writeCellWall(false, xmlout);
            writeCellWallAxis(false, xmlout);
            drawActinOnMother(xmlout);
        } else if ( param.equals("A106")) {
            writeCellWall(false, xmlout);
            writeCellWallAxis(false, xmlout);
            drawActinLocalizedOnNeck(xmlout);
        } else if ( param.equals("A107")) {
            writeCellWall(false, xmlout);
            writeCellWallAxis(false, xmlout);
            drawSmallBudWall(xmlout);
            drawActinOnApi(xmlout);
        } else if ( param.equals("A108")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinLocalizedinBud(xmlout);
        } else if ( param.equals("A109")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinLocalizedinBud(xmlout);
        } else if ( param.equals("A110")) {
            writeCellWall(true, xmlout);
            writeCellWallAxis(true, xmlout);
            drawActinOnApi(xmlout);
            drawActinLocalizedOnNeck(xmlout);
        } 
    }

    private void actinFigure() throws InvalidXMLException {
        String [] parameter = {
                "A1-1", "A1-2", "A1-3", "A2-1", "A2-2", "A2-3",
                "A3-1", "A3-2", "A3-3", "A6-1", "A6-2", "A6-3",
                "A101", "A105", "A106", "A107", "A108", "A109", "A110",
        };
        for(String param: parameter) {
            XMLAttribute svgatt = new XMLAttribute();
            svgatt.add("width", canvasSize.getX());
            svgatt.add("height", canvasSize.getY());
            svgatt.add("xmlns", "http://www.w3.org/2000/svg");

            String filename = dir + "/" + param + ".svg";
            XMLOutputter xmlout = null;
            try {
                xmlout = new XMLOutputter(new FileOutputStream(filename));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            xmlout.setDTDDeclaration(new DTDDeclaration("svg", "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"));
            xmlout.startTag("svg", svgatt);
            paintBackGround(xmlout);
            writeActin(param, xmlout);
            xmlout.closeTag(); // svg
            xmlout.closeStream();
            xmlout.endOutput();
        }
    }
    
    private void drawParamPoint(XMLOutputter xmlout, Point c) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", c.getX() );
        xmlatt.add("cy", c.getY() );
        xmlatt.add("r", 7);
        xmlatt.add("fill", paramLineColor);
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "1");
        xmlout.selfCloseTag("circle", xmlatt);
    }

    private void drawCenterOfActinInBud(XMLOutputter xmlout) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", CM.getX() + 5  );
        xmlatt.add("cy", CM.getY() - 5);
        xmlatt.add("r", 7);
        xmlatt.add("fill", paramLineColor);
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "1");
        xmlout.selfCloseTag("circle", xmlatt);
    }

    private void drawCenterOfActinInMother(XMLOutputter xmlout) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", CM.getX() - 15  );
        xmlatt.add("cy", CM.getY() + 15);
        xmlatt.add("r", 7);
        xmlatt.add("fill", paramLineColor);
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "1");
        xmlout.selfCloseTag("circle", xmlatt);
        
    }

    private void drawCenterOfActin(XMLOutputter xmlout) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", CM.getX() - 3);
        xmlatt.add("cy", CM.getY() + 3);
        xmlatt.add("r", 7);
        xmlatt.add("fill", paramLineColor);
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "1");
        xmlout.selfCloseTag("circle", xmlatt);
    }

    private void drawActinOnMother(XMLOutputter xmlout) {
        for ( int i = 0 ; i < 40 ; i++ ) {
            double r = Math.sqrt(Math.random());
            double deg = Math.random() * 2 * Math.PI;
            drawActinPatch(xmlout, patchPointInMother(r * Math.cos(deg), r * Math.sin(deg)));
        }
    }
    /**
     * @param xmlout
     */
    private void drawActinOnApi(XMLOutputter xmlout) {
        for ( int i = 0 ; i < 20 ; i++ ) {
            double r = Math.sqrt(Math.random());
            double deg = Math.random() * 2 * Math.PI;
            drawActinPatch(xmlout, patchPointAroundNeck(r * Math.cos(deg), r * Math.sin(deg)));
        }
    }

    /**
     * @param xmlout
     */
    private void drawActinLocalizedOnNeck(XMLOutputter xmlout) {
        for ( int i = 0 ; i < 20 ; i++ ) {
            double r = Math.sqrt(Math.random());
            double deg = Math.random() * 2 * Math.PI;
            drawActinPatch(xmlout, patchPointClosetoNeck(r * Math.cos(deg), r * Math.sin(deg)));
        }
    }

    /**
     * @param xmlout
     */
    private void drawActinLocalizedinBud(XMLOutputter xmlout) {
        for ( int i = 0 ; i < 20 ; i++ ) {
            double r = Math.sqrt(Math.random());
            double deg = Math.random() * 2 * Math.PI;
            drawActinPatch(xmlout, patchPointInBud(r * Math.cos(deg), r * Math.sin(deg)));
        }
    }

    /**
     * @param xmlout
     * @throws InvalidXMLException 
     */
    private void drawActinBudRegion(XMLOutputter xmlout) throws InvalidXMLException {
        xmlout.startTag("g", new XMLAttribute("transform",
                "translate(" +
                ((int)budCenter.getX()) + "," + 
                ((int)budCenter.getY()) + ")"));
        
        xmlout.startTag("g", new XMLAttribute("transform", 
                "rotate(" + ((int)buddegree) + ")"));

        XMLAttribute xmlattbud = new XMLAttribute();
        xmlattbud.add("cx", "0");
        xmlattbud.add("cy", "0");
        xmlattbud.add("rx", ((int)( budR.getX() * 0.9 )));
        xmlattbud.add("ry", ((int)( budR.getY() * 0.9 )));
        xmlattbud.add("fill", "rgb(221,229,190)");
        xmlattbud.add("stroke", "none");
        xmlout.selfCloseTag("ellipse", xmlattbud);
        xmlout.closeTag();
        xmlout.closeTag();
    }

    private void drawActinMotherRegion(XMLOutputter xmlout) {
        {
            String path = "M " + (int)CM.getX() + " " + (int)CM.getY();
            path += " L " + (int)(0.7 * C1_neck.getX() + 0.3 * CM.getX()) + " " + (int)(0.5 * C1_neck.getY() + 0.5 * CM.getY());
            path += " L " + (int)( C1_neck.getX() - 2 ) + " " + (int)C1_neck.getY() ;
            path += " L " + (int)(0.7 * C1_neck.getX() + 0.3 * CM.getX()) + " " + (int)(1.5 * C1_neck.getY() - 0.5  * CM.getY());
            path += " L " + (int)CM.getX() + " " + (int)(C1_neck.getY() - (CM.getY() - C1_neck.getY()));
            path += " Q " + (int)( C1_neck.getX() - 15 ) + " " + (int)C1_neck.getY() ;
            path += " " + (int)CM.getX() + " " + (int)CM.getY();
            path += " Z";
        
            XMLAttribute xmlatt = new XMLAttribute();
            xmlatt.add("d", path);
            xmlatt.add("fill", "rgb(221,229,190)");
            xmlatt.add("stroke", "none");
            xmlout.selfCloseTag("path", xmlatt);
        }
        {
            String path = "M " + (int)CM.getX() + " " + (int)CM.getY();
            path += " L" + (int)( 0.4 * C2_neck.getX() + 0.6 * CM.getX() ) + " " + (int)( 0.7 * C2_neck.getY() + 0.3 * CM.getY() );
            path += " L" + (int)C2_neck.getX() + " " + (int)C2_neck.getY();
            path += " Z";

            XMLAttribute xmlatt = new XMLAttribute();
            xmlatt.add("d", path);
            xmlatt.add("fill", "rgb(221,229,190)");
            xmlatt.add("stroke", "none");
            xmlout.selfCloseTag("path", xmlatt);
        }
        {
            String path = "M " + (int)C1_hip.getX() + " " + (int)C1_hip.getY();
            path += " L" + (int)( 0.65 * C1_hip.getX() + 0.35 * C2_hip.getX() ) + " " + (int)( 0.25 * C1_hip.getY() + 0.75 * C2_hip.getY() );
            path += " L" + (int)C2_hip.getX() + " " + (int)C2_hip.getY();
            path += " Z";

            XMLAttribute xmlatt = new XMLAttribute();
            xmlatt.add("d", path);
            xmlatt.add("fill", "rgb(221,229,190)");
            xmlatt.add("stroke", "none");
            xmlout.selfCloseTag("path", xmlatt);
        }
    }

    private void drawActinWholeRegion(XMLOutputter xmlout) throws InvalidXMLException {
        drawActinMotherRegion(xmlout);
        drawActinBudRegion(xmlout);
    }
    
    private Point patchPointInBud(double r1, double r2) {
        return new Point((int)(r1 * ( C5_left.getX() - budCenter.getX() ) 
                + r2 * ( C4_tip.getX() - budCenter.getX() ) 
                + budCenter.getX()), 
                (int)(r1 * ( C5_left.getY() - budCenter.getY() )
                        + r2 * ( C4_tip.getY() - budCenter.getY() )
                        + budCenter.getY()));
    }

    private Point patchPointInMother(double r1, double r2) {
        return new Point((int)(r1 * ( C1_neck.getX() - motherCenter.getX() ) 
                + r2 * ( C2_neck.getX() - motherCenter.getX() ) 
                + motherCenter.getX()), 
                (int)(r1 * ( C1_neck.getY() - motherCenter.getY() )
                        + r2 * ( C2_neck.getY() - motherCenter.getY() )
                        + motherCenter.getY()));
    }

    private Point patchPointClosetoNeck(double r1, double r2) {
        return new Point((int)(0.3 * Math.abs(r1) * ( C1_neck.getX() - motherCenter.getX() ) + CM.getX() - 30), 
                (int)( 0.3 * r2 * ( C2_neck.getY() - motherCenter.getY() ) + CM.getY() + 20 ));
    }

    private Point patchPointAroundNeck(double r1, double r2) {
        return new Point((int)(0.25 * r1 * ( C1_neck.getX() - motherCenter.getX() ) + CM.getX() + 10), 
                (int)( 0.3 * r2 * ( C2_neck.getY() - motherCenter.getY() ) + CM.getY() - 10 ));
    }

    private void drawActinPatch(XMLOutputter xmlout, Point p) {
        XMLAttribute xmlatt = new XMLAttribute();
        xmlatt.add("cx", p.getX());
        xmlatt.add("cy", p.getY());
        xmlatt.add("r", 7);
        xmlatt.add("fill", "rgb(222,236,125)");
        xmlatt.add("stroke", cellWallColor);
        xmlatt.add("stroke-width", "1");
        xmlout.selfCloseTag("circle", xmlatt);
    }

}
