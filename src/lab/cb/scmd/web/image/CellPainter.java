//--------------------------------------
// SCMDServer
// 
// CellPainter.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
//import java.io.PrintStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import lab.cb.scmd.exception.UnfinishedTaskException;

/**
 * 細胞の画像を描画するservlet
 * 
 * @author leo
 * 
 */
public class CellPainter extends HttpServlet
{
    HashMap budSizeToSizeRatioMap = new HashMap();

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        budSizeToSizeRatioMap.put("no", new Double(0));
        budSizeToSizeRatioMap.put("small", new Double(0.3));
        budSizeToSizeRatioMap.put("medium", new Double(0.5));
        budSizeToSizeRatioMap.put("large", new Double(0.75));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        try
        {

            String budSize = request.getParameter("budSize");
            double sizeRatio = getParameter(request, "areaRatio");

            double longAxis = getParameter(request, "longAxis");
            String roundnessStr = request.getParameter("roundness");
            double roundness = Double.parseDouble(roundnessStr == null ? "-1" : roundnessStr);
            double neckPosition = -1.0;
            double growthDirection = -1.0;
            if (sizeRatio != 0.0)
            {
                neckPosition = getParameter(request, "neckPosition");
                growthDirection = getParameter(request, "growthDirection");
            }
            if (longAxis < 0
                    || longAxis > 80
                    || roundness < 1.0
                    || roundness > 5.0
                    || (sizeRatio != 0.0 && (neckPosition < 0 || neckPosition > 90 || growthDirection < 0 || growthDirection > 90))
                    || sizeRatio < 0.0 || sizeRatio > 1.0)
            {
                printWhiteBoard(request, response);
                return;
            }


            try
            {
                response.setContentType("image/png");
                drawCellImage(response.getOutputStream(), sizeRatio, longAxis, roundness, neckPosition, growthDirection);
            }
            catch (IOException e)
            {
                log(e.getMessage());
            }
        }
        catch (NumberFormatException e)
        {
            log(e.getMessage());
            printWhiteBoard(request, response);
        }

    }

    void printWhiteBoard(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("image/png");
        request.getRequestDispatcher("/png/na.png").forward(request, response);
        return;
    }

    double getParameter(HttpServletRequest request, String parameterName)
    {
        double val = -1;
        String input = request.getParameter(parameterName);
        if (input != null)
        {
            if (!input.equals("")) val = Double.parseDouble(input);
        }
        return val;
    }

    static public void drawCellImage(OutputStream out, double budSizeRatio, double longAxis, double roundness,
            double neckPosition, double budGrowthDirection) throws IOException
    {
        double shortAxis = longAxis / roundness;
        double radian = Math.PI / 180;

        double longAxisOfBud = longAxis * budSizeRatio;
        double shortAxisOfBud = shortAxis * budSizeRatio;

        double xRange = (longAxis / 2.0) * (Math.cos(neckPosition * radian) + 1) + longAxisOfBud
                * Math.cos(budGrowthDirection * radian);
        double yRange = (shortAxis / 2.0) * (Math.sin(neckPosition * radian) + 1) + longAxisOfBud
                * Math.sin(budGrowthDirection * radian);

        int xCenter = (int) (50 - (xRange / 2.0) + (longAxis / 2.0));
        int yCenter = (int) (118 - (shortAxis / 2.0 + 0.5));

        int xOfBudOffset = xCenter
                + (int) ((longAxis / 2.0) * Math.cos(neckPosition * radian) + (longAxisOfBud / 2.0)
                        * Math.cos(budGrowthDirection * radian) + 0.5);
        int yOfBudOffset = yCenter
                - (int) ((shortAxis / 2.0) * Math.sin(neckPosition * radian) + (longAxisOfBud / 2.0)
                        * Math.sin(budGrowthDirection * radian) + 0.5);

        int sizeRatioOfBudLongAxis = (int) (longAxisOfBud / 2.0 + 0.5);
        int sizeRatioOfBudShortAxis = (int) (shortAxisOfBud / 2.0 + 0.5);

        int sizeRatioOfLongAxis = (int) (longAxis / 2.0 + 0.5);
        int sizeRatioOfShortAxis = (int) (shortAxis / 2.0 + 0.5);

        BufferedImage cellImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) cellImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D motherCellEllipse = new Ellipse2D.Double(-sizeRatioOfLongAxis, -sizeRatioOfShortAxis,
                sizeRatioOfLongAxis * 2, sizeRatioOfShortAxis * 2);
        Ellipse2D budCellEllipse = new Ellipse2D.Double(-sizeRatioOfBudLongAxis, -sizeRatioOfBudShortAxis,
                sizeRatioOfBudLongAxis * 2, sizeRatioOfBudShortAxis * 2);

        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, 128, 128);

        g.setTransform(AffineTransform.getTranslateInstance(xCenter, yCenter));
        g.setColor(new Color(0x72ADC5));
        g.fill(motherCellEllipse);

        AffineTransform rotateTransformation = AffineTransform.getTranslateInstance(xOfBudOffset, yOfBudOffset);
        rotateTransformation.rotate(-budGrowthDirection * radian);
        g.setTransform(rotateTransformation);
        g.setColor(new Color(0x9FC7D7));
        g.fill(budCellEllipse);

        ImageIO.write(cellImage, "png", out);
    }

}

// --------------------------------------
// $Log: CellPainter.java,v $
// Revision 1.11 2004/09/21 06:13:05 leo
// warning fix
//
// Revision 1.10 2004/09/09 02:15:10 leo
// bufferedimageで、clippingをするようにしてみた
//
// Revision 1.9 2004/09/06 07:04:28 leo
// PrintStream -> outputStream
//
// Revision 1.8 2004/09/03 07:31:53 leo
// デザインの調整
// standardnameを表示
//
// Revision 1.7 2004/09/02 08:49:48 leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.6 2004/08/13 19:17:56 leo
// *** empty log message ***
//
// Revision 1.5 2004/08/13 13:57:12 leo
// Statページの更新
//
// Revision 1.4 2004/08/09 12:26:42 leo
// Commentを追加
//
// Revision 1.3 2004/07/30 06:40:29 leo
// Morphology Searchのインターフェース、完成
//
// Revision 1.2 2004/07/29 07:50:45 leo
// MorphologySearchのパーツ作成中
//
// Revision 1.1 2004/07/27 07:41:04 leo
// 作成途中
//
// --------------------------------------
