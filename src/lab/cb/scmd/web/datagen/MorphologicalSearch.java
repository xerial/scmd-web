//--------------------------------------
//SCMDServer
//
//MorphologicalSearch.java 
//Since: 2005/02/05
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/datagen/Normalizer.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------

package lab.cb.scmd.web.datagen;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.bean.YeastGeneWithDistance;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;

/**
 * @author sesejun
 * 
 * 与えられた点近傍のORFリストを求めるクラス
 * Note: 途中でXMLをはさまずに、tableQueryでデータを呼び出している
 */
public class MorphologicalSearch {
    TableQuery tableQuery;
    ParamPair[] paramSets;
    
    public MorphologicalSearch(TableQuery tableQuery) {
        this.tableQuery = tableQuery;
    }
    
    public void setParamAndValues(ParamPair[] paramSets) {
        this.paramSets = paramSets;
    }
    
    public List selectByEuclidianDistance(int minRank, int maxRank ) {
        double[] zvalue = new double[paramSets.length];
        double[] weight = new double[paramSets.length];
        ParamPair[] ps = new ParamPair[1];
        // paramid and groupid から　それらの　avg と sd を得る。
        // ただし全てのペアに関して別々にqueryを投げているので、非常に非効率。
        for(int i = 0; i < paramSets.length; i++ ) {
            ps[0] = paramSets[i]; 
            Table groupAvgSDTable = tableQuery.getGroupAvgSDTable(ps);
            double avg = Double.parseDouble(groupAvgSDTable.get(1, 2).toString());
            double sd = Double.parseDouble(groupAvgSDTable.get(1, 3).toString());
            if( avg != 0.0 )
                zvalue[i] = (paramSets[i].getValue() - avg)/sd;
            else
                zvalue[i] = 0.0;
            weight[i] = paramSets[i].getWeight();
        }

        Table zScoreTable = tableQuery.getShapeZScoreTable(paramSets);
        int rowsize = zScoreTable.getRowSize();
        
        // TreeMap<ORFName,Distance>
        Comparator distComparator = new Comparator () {
            public int compare(Object o1, Object o2) {
                double d1 = ((YeastGeneWithDistance)o1).getDistance();
                double d2 = ((YeastGeneWithDistance)o2).getDistance();
                if( d1 > d2 ) {
                    return 1;
                } else if (d1 == d2 )
                    return 0;
                return -1;
            }
        };
        TreeSet distanceSet = new TreeSet(distComparator); 
        for(int row = 1; row < rowsize; row++ ) {
            TableIterator it = zScoreTable.getHorizontalIterator(row);
            String orfname = it.nextCell().toString();
            double dist = 0.0;
            int colnum = 0;
            while(it.hasNext()) {
                Cell cell = it.nextCell();
                double value = ( cell.doubleValue() - zvalue[colnum] ) * weight[colnum];
                dist += value*value;
                colnum++;
            }
            YeastGeneWithDistance odset = new YeastGeneWithDistance();
            odset.setOrf(orfname);
            odset.setDistance(Math.sqrt(dist));
            distanceSet.add(odset);
        }
        LinkedList distanceList = new LinkedList();
        int rank = 0;
        Iterator iterator = distanceSet.iterator();
        while(iterator.hasNext()) {
            Object obj = iterator.next();
            if( rank >= minRank && rank < maxRank) {
                distanceList.add(obj);
            }
            rank++;
        }
        return distanceList;
    }
}
