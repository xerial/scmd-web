//--------------------------------------
// SCMDServer
// 
// TableQuery.java 
// Since: 2004/07/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.db.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xerial.util.Pair;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.datagen.ParamPair;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.Table;

/**
 * @author leo
 *  
 */
public interface TableQuery 
{
    /*
     * 写真に含まれるcellの形態パラメータのテーブルを返す (cellID, budSize, nucleusGroup, actin group,
     * bud size, mother size, etc.)
     *  
     */
    public Table getShapeDataSheet(String strain, int photoID, int sheetType);

    /*
     * 各細胞の形態パラメータのテーブルを得る
     */
    public Table getShapeDataSheet(String strain, int photoID, int cellID, int sheetType);
    public Table getShapeDataSheet(String orf, int photoNum, String[] columnName);

    /*
     * bud size毎の平均値 budSize no small medium large longAxis 30 34 ... roundness
     * ... neckPosition budGrowthDirection
     * 
     * ....
     */
    //public BasicTable getShapeStatSheet(String strain);

    /*
     * orfName C-num-no C-num-small C-num-medium C-num-large D-num-A D-num-A1
     * D-num-A1|B D-num-B D-num-C D-num-D D-num-E D-num-F A-num-A A-num-B
     * A-num-apiA-num-iso A-num-E A-num-F C-longAxis_no C-longAxis_small
     * C-longAxis_mediumC-longAxis_large C-roundness_no C-roundness_small
     * C-roundness_medium C-roundness_large C-budNeckPosition_no
     * C-budNeckPosition_small C-budNeckPosition_medium C-budNeckPosition_large
     * C-budGrowthDirection_no C-budGrowthDirection_small
     * C-budGrowthDirection_medium C-budGrowthDirection_large D-longAxis_A
     * D-longAxis_A1 D-longAxis_A1|B D-longAxis_B D-longAxis_C D-longAxis_D
     * D-longAxis_E D-longAxis_F D-roundness_A D-roundness_A1 D-roundness_A1|B
     * D-roundness_B D-roundness_C D-roundness_D D-roundness_E D-roundness_F
     * D-budNeckPosition_A D-budNeckPosition_A1 D-budNeckPosition_A1|B
     * D-budNeckPosition_B D-budNeckPosition_C D-budNeckPosition_D
     * D-budNeckPosition_E D-budNeckPosition_F D-budGrowthDirection_A
     * D-budGrowthDirection_A1 D-budGrowthDirection_A1|B D-budGrowthDirection_B
     * D-budGrowthDirection_C D-budGrowthDirection_D D-budGrowthDirection_E
     * D-budGrowthDirection_F A-longAxis_A A-longAxis_B A-longAxis_api
     * A-longAxis_iso A-longAxis_E A-longAxis_F A-roundness_A A-roundness_B
     * A-roundness_api A-roundness_iso A-roundness_E A-roundness_F
     * A-budNeckPosition_A A-budNeckPosition_B A-budNeckPosition_api
     * A-budNeckPosition_iso A-budNeckPosition_E A-budNeckPosition_F
     * A-budGrowthDirection_A A-budGrowthDirection_B A-budGrowthDirection_api
     * A-budGrowthDirection_iso A-budGrowthDirection_E A-budGrowthDirection_F
     * 
     * というテーブルの一行をMapに格納して返す
     */
    public Map getShapeStatLine(String orf) throws SCMDException;
    
    /*
     * ShapeStat内のparamの
     * averag	sd	maxvalue	minvalue 
     * のテーブルを返す
     */
    public Table getShapeStatOfParameter(String param);
    
    
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName);
    
    public Table getGroupByDatasheet(String orf, int sheetType, int stainType, String groupName, int page, int numElementInAPage);
    public Table getGroupCount(String orf, int stainType);
    
    
    public Table getAveragePlot(String param1, String param2);
    public Table getAveragePlot(String param1, QueryRange range1, String param2, QueryRange range2);

    public Table getShapeZScoreTable(ParamPair[] paramSets);
    public Table getGroupAvgSDTable(ParamPair[] paramSets);

    public Table getAnalysisAVGandSD();
    public Table getSelectedAnalysisValue(String[] orf);

    public List<MorphParameter> getParameterInfo(Set<Integer> parameter);
    

}

//--------------------------------------
// $Log: TableQuery.java,v $
// Revision 1.13  2004/09/07 16:49:46  leo
// 2D plotを追加
//
// Revision 1.12  2004/08/31 08:44:01  leo
// Group By Sheet完成
//
// Revision 1.11  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.10  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.9  2004/08/24 06:56:53  leo
// SQLの結果をlab.cb.scmd.web.tableで返すmethodを追加
//
// Revision 1.8  2004/08/24 01:53:17  sesejun
// *** empty log message ***
//
// Revision 1.7  2004/08/24 01:51:53  sesejun
// TeardropのためのDB操作を追加
//
// Revision 1.6  2004/08/14 11:13:06  sesejun
// DataSheet対応開始。
// getCellCordinates()にcellidを追加
//
// Revision 1.5  2004/08/13 13:57:12  leo
// Statページの更新
//
// Revision 1.4 2004/08/13 05:50:32 leo
// XMLQuery, TableQuery, ValueQueryのinstanceに
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
