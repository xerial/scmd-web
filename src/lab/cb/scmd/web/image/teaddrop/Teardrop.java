//--------------------------------------
// SCMDWeb Project
//
// Teardrop.java
// Since: 2005/02/13
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image.teaddrop;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.xerial.algorithm.Algorithm;
import org.xerial.util.Pair;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.image.TeardropPoint;

/**
 * ���Teardrop��\���N���X
 * @author leo
 *
 */
public class Teardrop
{
    static public enum Orientation { vertical, horizontal}   
    
    int paramID;
    int groupID;
    Orientation orientation = Orientation.vertical;
    
    double min;
    double max;
    double average;
    double SD;    
    
    /**
     * @param paramID
     * @param groupID
     * @param orientation
     * @param average
     * @param SD
     * @param min
     * @param max
     */
    public Teardrop(int paramID, int groupID, double average, double SD, double min, double max)
    {
        this.groupID = groupID;
        this.paramID = paramID;
        this.average = average;
        this.SD = SD;
        this.min = min;
        this.max = max;
    }
    
    public Teardrop()
    {
        
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }
    
    public double getAverage()
    {
        return average;
    }
    public void setAverage(double average)
    {
        this.average = average;
    }
    public int getGroupID()
    {
        return groupID;
    }
    public void setGroupID(int groupID)
    {
        this.groupID = groupID;
    }
    public double getMax()
    {
        return max;
    }
    public void setMax(double max)
    {
        this.max = max;
    }
    public double getMin()
    {
        return min;
    }
    public void setMin(double min)
    {
        this.min = min;
    }
    public int getParamID()
    {
        return paramID;
    }
    public double getSD()
    {
        return SD;
    }
    public void setSD(double sd)
    {
        SD = sd;
    }
    /**
     * ImageCache�ɒ~����ۂ̉摜ID�𓾂�
     * @return ImageCache�ɒ~����ۂ̉摜ID��Ԃ�
     */
    public String getImageID()
    {
        return "T_" + paramID + "_" + groupID + "_" + orientation.name(); 
    }
    

    public BufferedImage drawImage(List<TeardropPoint> plotList) throws SCMDException
    {   
        String teardropURI;
        switch(orientation)
        {
        case horizontal:
            teardropURI = SCMDConfiguration.getProperty("TEARDROP_H_URI");
            break;
        case vertical:
        default:
            teardropURI = SCMDConfiguration.getProperty("TEARDROP_URI");                
            break;
        }
        
        URL imageURL;
        BufferedImage teardrop;
        try
        {
            imageURL = new URL(teardropURI + "/td_" + paramID + "_" + groupID + ".png");
            teardrop = ImageIO.read(imageURL);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            throw new SCMDException(e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new SCMDException(e);
        }
        Graphics2D g = (Graphics2D) teardrop.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int dotRadius = 3;
        int avgLineColor = 0xA0E0F0;
        int pollLength, perpendicularLength;
        int avgPos;
        int i;
        switch(orientation)
        {
        case horizontal:
            pollLength = teardrop.getWidth();
            perpendicularLength = teardrop.getHeight();
            avgPos = pollLength / 2;     
            // ���ϒl�ɐ�������
            g.setColor(new Color(avgLineColor));
            g.drawLine(avgPos, 0, avgPos, perpendicularLength);
             
            int[] ypos = computePositionInPerpendicularDirection(plotList, perpendicularLength, pollLength, dotRadius);
            // �_��ł�
            i=0;
            for(TeardropPoint tp : plotList)
            {
                int x = computePositionOnThePoll(tp.getValue(), pollLength) - dotRadius;
                g.setColor(tp.getColor());
                g.fillOval(x, ypos[i], dotRadius * 2, dotRadius * 2);
                i++;
            }
            break;
        case vertical:
        default:
            pollLength = teardrop.getHeight();
            perpendicularLength = teardrop.getHeight();
            avgPos = pollLength / 2;     

            // ���ϒl�ɐ�������
            g.setColor(new Color(0xA0E0F0));
            g.drawLine(0, avgPos, perpendicularLength, avgPos);
            int[] xpos = computePositionInPerpendicularDirection(plotList, perpendicularLength, pollLength, dotRadius);
            // �_��ł�
            i=0;
            for(TeardropPoint tp : plotList)
            {
                int y = computePositionOnThePoll(tp.getValue(), pollLength) - dotRadius;
                g.setColor(tp.getColor());
                g.fillOval(xpos[i], y, dotRadius * 2, dotRadius * 2);
                i++;
            }
            break;
        }
        
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_OVER);
        g.setComposite(ac);
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, teardrop.getWidth(), teardrop.getHeight());
        return teardrop;        
    }

    public void drawRange(BufferedImage teardrop, double rangeBegin, double rangeEnd)
    {
        Graphics2D g = (Graphics2D) teardrop.getGraphics();
        g.setColor(new Color(0xF0B0AF));
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(ac);
        
        int begin, end, pollLength, perpendicularLength, range;
        switch(orientation)
        {
        case horizontal:
            pollLength = teardrop.getWidth();
            perpendicularLength = teardrop.getHeight();
            begin = computePositionOnThePoll(rangeBegin, pollLength);
            end = computePositionOnThePoll(rangeEnd, pollLength);
            range = (end - begin > 0) ? end - begin : 1;
            
            g.fillRect(begin, 0, range, perpendicularLength);
            break;
        case vertical:
        default:
            pollLength = teardrop.getHeight();
            perpendicularLength = teardrop.getWidth();
            begin = computePositionOnThePoll(rangeBegin, pollLength);
            end = computePositionOnThePoll(rangeEnd, pollLength);
            range = (end - begin > 0) ? end - begin : 1;
            
            g.fillRect(0, begin, perpendicularLength, range);                        
            break;                   
        }
    }
    

    /** 
     * value��Teardrop��ł̑��Έʒu��Ԃ�
     * @param value [min, max]�͈̔͂̒l
     * @return value��Teardrop��ł̑��Έʒu 0����1�͈̔́B���ϒl��0.5
     */
    public double getIndex(double value) 
    {
        double imageRadius = Algorithm.<Double>minmax(max - average, average - min).max();
        return (value - average) / (imageRadius * 2)  + 0.5;
    }
    
    /**
     * @param value �C�ӂ̓_�̒l
     * @param pollLength teardrop�̉摜�̏c�i�������̓�����K���܂Łj�̒���
     * @return
     */
    private int computePositionOnThePoll(double value, int pollLength) {
        double index = getIndex(value);
        int ypos =  (int) (index * pollLength);

        // �摜����͂ݏo���_�̕␳
        if( ypos < 0 )
            ypos = 0;
        if( ypos > pollLength )
            ypos = pollLength;
        return ypos; 
    }

    class Range extends Pair<Integer, Integer>
    {

        /**
         * @param begin
         * @param end
         */
        public Range(Integer begin, Integer end)
        {
            super(begin, end);
        }
        
        public boolean contains(Integer value)
        {
            return (getFirst() <= value && value <= getSecond());
        }
        
        public int getBegin()
        {
            return getFirst();
        }
        public int getEnd()
        {
            return getSecond();
        }
        
    }

    private int[] computePositionInPerpendicularDirection(List<TeardropPoint> teardropPoint, int perpendicularLength, int pollLength, int dotRadius)
    {
        int size = teardropPoint.size();
        int[] xpos = new int [size];
         HashMap indexMap = new HashMap();
         for( int i = 0 ; i < size; i++ ) 
         {
             xpos[i] = perpendicularLength / 2 - dotRadius;
             Integer hindex = computePositionOnThePoll(teardropPoint.get(i).getValue(), pollLength);
             if( indexMap.containsKey(hindex) ) {
                 List indexes = (List)indexMap.get(hindex);
                 indexes.add(new Integer(i));
             } else {
                 List indexes = new LinkedList();
                 indexes.add(new Integer(i));
                 indexMap.put(hindex, indexes);
             }
         }
         Iterator it = indexMap.keySet().iterator();
         while(it.hasNext()) {
             Object nextobj = it.next();
             List indexes = (List)indexMap.get(nextobj);
             if( indexes.size() < 2 )
                 continue;
             double midindex = (indexes.size() - 1) * 0.5;
             for( int i = 0; i < indexes.size(); i++ ) {
                 int hindex = ((Integer)indexes.get(i)).intValue();
                 xpos[hindex] += 4 * ( i - midindex ) * dotRadius;
             }
         }
         return xpos;
     }
 
    /**
     * 0�̕����珇�ɓ_���l�߂čs��
     * 
     * 
     * 1. �_��y�̏��������\�[�g����
     * 2. 
     * 
     * 
     * @param teardropPoint
     * @param width
     * @param teardropImageHeight
     * @param dotRadius
     * @return
     */
    private int[] computeXposition2(TeardropPoint[] teardropPoint, int teardropImageWidth, int teardropImageHeight, int dotRadius) 
    {
        int[] xpos = new int [teardropPoint.length];

        Vector<PlotPoint> pointList = new Vector<PlotPoint>();
        for(TeardropPoint tp : teardropPoint)
        {
            int x = teardropImageWidth / 2;
            int y = computePositionOnThePoll(tp.getValue(), teardropImageHeight);
            pointList.add(new PlotPoint(x, y));
        }
        Collections.sort(pointList); // y�̏���������sort
        
        // ����Y���W�����_��set ��TreeMap�����
        TreeMap<Integer, Vector<PlotPoint>> mapOfThePointsHavingTheSameY = new TreeMap<Integer, Vector<PlotPoint>>(); 
        for(PlotPoint p : pointList)
        {
            if(mapOfThePointsHavingTheSameY.containsKey(p.getY()))
                mapOfThePointsHavingTheSameY.get(p.getY()).add(p);
            else
            {
                Vector<PlotPoint> newContainer = new Vector<PlotPoint>();
                newContainer.add(p);
                mapOfThePointsHavingTheSameY.put(p.getY(), newContainer);
            }
        }

        final int xCenter = teardropImageWidth / 2;
        
        Vector<PlotPoint> allocatedPoints = new Vector<PlotPoint>();
        for(Integer y : mapOfThePointsHavingTheSameY.keySet())
        {
            Vector<PlotPoint> pointsHavingTheSameY = mapOfThePointsHavingTheSameY.get(y);
            // ���݂̂����W�}dot�̔��a�Əd�Ȃ�_���
            Vector<PlotPoint> overlappingForwardPoints = new Vector<PlotPoint>();
            int searchRangeBegin = y - dotRadius;
            for(PlotPoint p : allocatedPoints)
            {
                if(p.getY() + dotRadius < searchRangeBegin)
                    overlappingForwardPoints.add(p);
            }
            
            Vector<Range> availableRange = new Vector<Range>();
            // �^�񒆂���͈͂�V������
            
            
            // x������scanline ���āA�_��z�u�\�Ȕ͈͂̏W�������߂�
            Collections.sort(overlappingForwardPoints, new PlotPoint.XComparator());
            int beginX = 0;
            for(PlotPoint p : overlappingForwardPoints)
            {
                int range = p.getX() - dotRadius - beginX;
                if(range > (dotRadius * 2))
                    availableRange.add(new Range(beginX + dotRadius, p.getX() - dotRadius * 2));
                beginX = p.getX() + dotRadius;
            }
            
            // �󂫗̈��^�񒆂���߂�����sort
            Collections.sort(availableRange, new Comparator<Range>() {
                public int compare(Range r1, Range r2)
                {
                    return  distFromCenter(r1) -  distFromCenter(r2);
                }
                public int distFromCenter(Range r)
                {
                    return r.contains(xCenter) ? 0 : (r.getBegin() < xCenter ? r.getEnd() - xCenter : xCenter - r.getBegin());
                }
            });

            // ��X�y�[�X�̐^�񒆂ɋ߂�������z�u���Ă���            
            for(Range r : availableRange)
            {
                int capacity = (int) Math.floor((r.getEnd() - r.getBegin()) / dotRadius * 2);
                // �ϓ�����t��
                
            }
            
        }
        
        // TODO impl
        return null;
    }

   
    
    public void setParamID(int paramID)
    {
        this.paramID = paramID;
    }
}




