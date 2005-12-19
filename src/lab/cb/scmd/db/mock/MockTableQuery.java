//--------------------------------------
// SCMDServer
// 
// MockTableQuery.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.mock;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import lab.cb.scmd.db.common.QueryRange;
import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.FlatTable;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.datagen.ParamPair;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.Table;

/**
 * @author leo
 *  
 */
public class MockTableQuery extends MockQueryAPI implements TableQuery
{

    /**
     *  
     */
    public MockTableQuery()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.db.common.TableQuery#getShapeDataSheet(java.lang.String, int)
     */
    public Table getShapeDataSheet(String orf, int photoID, int sheetType) {
        
        if(sheetType == 0)
        {
            Table appendableTable = new Table();
            appendableTable.addRow(new String[] { "Phase", "Nucleus", "Actin", "Size", "Bud", "Mother", "L-Axis", "S-Axis", "(Ratio)", "Bud Dir.", "Neck Pos.", "Fitness"});
            Random random = new Random();
            for (int i = 0; i < 17; i++)
            {
                appendableTable.addRow(createRow(random, i));
            }
            
            return appendableTable;
        }
        else
        {
            Random random = new Random();
            Table appendableTable = new Table();
            appendableTable.addRow(new String[] { "A1034", "C103", "E342", "D23", "D24"}); 
            for(int i=0; i<17; i++)
            {
                Object[] row = new Object[6];
                row[0] = new Integer(i);
                for(int j=0; j<5; j++)
                {
                    row[j+1] = new Integer(random.nextInt(1000));
                }
                appendableTable.addRow(row);
            }
            return appendableTable;
        }
    }

    public Table getShapeDataSheet(String orf, int photoID, int cellID, int sheetType) {
        Table appendableTable = new Table();
        appendableTable.addRow(new String[] { "Phase", "Nucleus",
                "Actin", "Size", "Bud", "Mother", "L-Axis", "S-Axis", "(Ratio)", "Bud Dir.", "Neck Pos.", "Fitness"});

        Random random = new Random();
        appendableTable.addRow(createRow(random, cellID));
        return appendableTable;
    }

    Object[] createRow(Random random, int cellID) {
        Object[] row = new Object[13];
        row[0] = new Integer(cellID);
        ;
        row[1] = (new String[] { "no", "small", "medium", "large"})[random.nextInt(4)];
        row[2] = (new String[] { "A", "A1", "B", "C"})[random.nextInt(4)];
        row[3] = (new String[] { "A", "B", "api", "iso", "C", "D"})[random.nextInt(6)];
        row[4] = new Integer(random.nextInt(2000));
        row[5] = new Integer(random.nextInt(1000));
        row[6] = new Integer(random.nextInt(1500));
        row[7] = new Integer(random.nextInt(1500));
        row[8] = new Integer(random.nextInt(1500));
        row[9] = new Integer(random.nextInt(1500));
        row[10] = new Integer(random.nextInt(1500));
        row[11] = new Integer(random.nextInt(1500));
        row[12] = new Integer(random.nextInt(1500));
        return row;
    }

    public Table getShapeDataSheet(String orf, int photoNum, String[] columnName){
        return null;
    }

    Double getRandom(double min, double max) {
        return new Double(min + (rand.nextDouble() * (max - min)));
    }

//    public BasicTable getShapeStatSheet(String orf) {
//        AppendableTable table = new AppendableTable("statTable", new String[] { "no", "small", "medium", "large"}, true);
//
//        String[] param = { "longAxis", "roundness", "neckPosition", "budGrowthDirection"};
//        int i = 0;
//        table.append(new Object[] { param[i++], getRandom(30, 80), getRandom(30, 80), getRandom(30, 80),
//                getRandom(30, 80)});
//        table.append(new Object[] { param[i++], getRandom(1.0, 2.0), getRandom(1.0, 2.0), getRandom(1.0, 2.0),
//                getRandom(1.0, 2.0)});
//        table
//                .append(new Object[] { param[i++], getRandom(0, 90), getRandom(0, 90), getRandom(0, 90),
//                        getRandom(0, 90)});
//        table
//                .append(new Object[] { param[i++], getRandom(0, 90), getRandom(0, 90), getRandom(0, 90),
//                        getRandom(0, 90)});
//        return table;
//    }

    static Random rand = new Random();

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.db.common.TableQuery#getShapeStatLine(java.lang.String)
     */
    public Map getShapeStatLine(String orf) throws SCMDException {

        BasicTable table = new FlatTable(new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT),
                "scmd-server/table/sample/average_shape.xls"), true, true);
        TreeMap map = new TreeMap();
        TableIterator it = table.getHorisontalIterator(orf);
        for (; it.hasNext();)
        {
            Cell cell = it.nextCell();
            map.put(table.getColLabel(it.col()), cell.toString());
        }
        return map;
    }
    
    public Table getShapeStatOfParameter(String param) {
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String, int, int, java.lang.String)
     */
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupByDatasheet(java.lang.String, int, int, java.lang.String, int, int)
     */
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName, int page, int numElementInAPage) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getGroupCount(java.lang.String, int)
     */
    public Table getGroupCount(String orf, int stainType) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, java.lang.String)
     */
    public Table getAveragePlot(String param1, String param2) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see lab.cb.scmd.db.common.TableQuery#getAveragePlot(java.lang.String, lab.cb.scmd.db.common.QueryRange, java.lang.String, lab.cb.scmd.db.common.QueryRange)
     */
    public Table getAveragePlot(String param1, QueryRange range1, String param2, QueryRange range2) {
        // TODO Auto-generated method stub
        return null;
    }

    public Table getShapeZScoreTable(ParamPair[] paramSets) {
        return null;
    }
    public Table getGroupAvgSDTable(ParamPair[] paramSets) {
        return null;
    }

    public Table getAnalysisAVGandSD() {
        return null;
    }
    public Table getSelectedAnalysisValue(String[] orf) {
        return null;
    }
    public List<MorphParameter> getParameterInfo(Set<Integer> parameter){
        return null;
    }

    public MorphParameter getOneParameterInfo(String param, String scope) {
        return null;
    }

	public Table getAssociatedGO(String[] keywordList) {
		// TODO Auto-generated method stub
		return null;
	}

	public Table getForwardReverseAssociations(String goid) {
		// TODO Auto-generated method stub
		return null;
	}
}

//--------------------------------------
// $Log: MockTableQuery.java,v $
// Revision 1.10  2004/09/07 16:49:46  leo
// 2D plotを追加
//
// Revision 1.9  2004/09/06 01:52:01  leo
// mockのエラーを修正
//
// Revision 1.8  2004/08/24 06:57:14  leo
// web.tableに対応
//
// Revision 1.7  2004/08/24 01:51:53  sesejun
// TeardropのためのDB操作を追加
//
// Revision 1.6  2004/08/14 13:51:27  leo
// sheetTypeを変えたときに出力が切り替わるように
//
// Revision 1.5  2004/08/14 13:26:43  leo
// MockTableの修正
//
// Revision 1.4 2004/08/14 12:34:50 leo
// cellの表示を修正
//
// Revision 1.3 2004/08/13 13:57:12 leo
// Statページの更新
//
// Revision 1.2 2004/08/13 05:50:32 leo
// XMLQuery, TableQuery, ValueQueryのinstanceに
//
// Revision 1.1 2004/08/01 14:58:22 leo
// 移動
//
// Revision 1.3 2004/08/01 08:20:12 leo
// BasicTableをHTMLに変換するツールを書き始めました
//
// Revision 1.2 2004/07/27 06:50:25 leo
// CellInfoページを追加
//
// Revision 1.1 2004/07/27 05:17:50 leo
// Datasheetのサンプル表示
//
//--------------------------------------
