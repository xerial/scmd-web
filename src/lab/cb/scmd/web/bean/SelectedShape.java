//--------------------------------------
// SCMDServer
// 
// SelectedShape.java 
// Since: 2004/07/29
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.web.util.CGIUtil;


/**
 * @author leo
 *  
 */
public class SelectedShape extends ActionForm
{
    static final String[] _selectionPhase        = { "Bud Size", "Long axis", "Roundness", "Neck Position",
            "Bud growth direction", "Search"    };
    static final String[] _parameterName = {"areaRatio", "longAxis", "roundness", "neckPosition", "growthDirection"
    };
    static final String[] selectionMessage = {
            "the bud area ratio",
            "the long axis length", 
            "the roundness",
            "the angle of the bud-neck position",
            "the angle of the bud-growth direction",
            ""
    };
    static final String[] selectionMessage2 = {
            "",
            "of the mother cell",
            "of the mother cell",
            "",
            "",
            ""
    };
    static final String[] selectionSubMessage = {
            "bud area ratio = daughtor cell area / mother cell area",
            "",
            "roundness := long axis length / short axis length",
            "Bud-neck is a point on the coutour of the mother cell, where the bud grows.",
            "",
            ""
    };
    
    static final double[][] defaultOfHIS3 = new double[][] 
    {
            {33.451, 1.214, 0, 0},
            {36.358, 1.209, 37.310, 51.971},
            {35.956, 1.207, 37.140, 51.870},
            {35.973, 1.205, 37.039, 53.146}
    };
    /*
    public double getDefaultValue()
    {
        String[] budSizeList = new String[] {"no", "small", "medium", "large"};
                
        int i;
        for(i=0; i<budSizeList.length; i++)
        {
            if(budSizeList[i].equals(budSize))
                break;
        }
        if(i>= budSizeList.length)
            i=2;
        return defaultOfHIS3[i][phase - 1];
    }
    */

    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        //inputValue = getParameterValue();
    }
    
    //String budSize = "medium";
    double areaRatio = 0.5;
    double longAxis = 37.0;
    double roundness = 1.26;
    double neckPosition = 38.0;
    double growthDirection = 49.5;
    
    int weightAreaRatio = 3;
    int weightLongAxis = 3;
    int weightRoundness = 3;
    int weightNeckPosition = 3;
    int weightGrowthDirection = 3;
    String inputValue = "0.5";
    String selectedValue = "0.5";
    String searchArgument = "";
    
    public double getAreaRatio() {
        return areaRatio;
    }
    public void setAreaRatio(double areaRatio) {
        this.areaRatio = areaRatio;
    }
    public String getSelectedValue() {
        return selectedValue;
    }
    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
    public String[] getParameterNameList()
    {
        return _parameterName;
    }
    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
    public String getSearchArgument() {
        return searchArgument;
    }
    public void setSearchArgument()
    {
        searchArgument = getImageArgument();
    }
    
    
    public void setParameter()
    {
        String value = selectedValue.equals("manual") ? inputValue : selectedValue;
        switch(targetPhase)
        {
        	case SELECT_BUDSIZE:
        	    setAreaRatio(Double.parseDouble(value));
        	    break;
        	case SELECT_LONGAXIS:
        		setLongAxis(Double.parseDouble(value));
        	    break;
        	case SELECT_ROUNDNESS:
        		setRoundness(Double.parseDouble(value));
        	    break;
        	case SELECT_BUDNECK:
        		setNeckPosition(Double.parseDouble(value));
        	    break;
        	case SELECT_BUDGROWTH:
        		setGrowthDirection(Double.parseDouble(value));
        	    break;
        	default:
        	    break;
        }
    }
    
    
    public String getWeightParameter()
    {
        String param = getParameterName();
        return "weight" + param.substring(0, 1).toUpperCase() + param.substring(1);
    }

    
    public int getWeightAreaRatio() {
        return weightAreaRatio;
    }
    public void setWeightAreaRatio(int weightAreaRatio) {
        this.weightAreaRatio = weightAreaRatio;
    }
    public int getWeightGrowthDirection() {
        return weightGrowthDirection;
    }
    public void setWeightGrowthDirection(int weightGrowthDirection) {
        this.weightGrowthDirection = weightGrowthDirection;
    }
    public int getWeightLongAxis() {
        return weightLongAxis;
    }
    public void setWeightLongAxis(int weightLongAxis) {
        this.weightLongAxis = weightLongAxis;
    }
    public int getWeightNeckPosition() {
        return weightNeckPosition;
    }
    public void setWeightNeckPosition(int weightNeckPosition) {
        this.weightNeckPosition = weightNeckPosition;
    }
    public int getWeightRoundness() {
        return weightRoundness;
    }
    public void setWeightRoundness(int weightRoundness) {
        this.weightRoundness = weightRoundness;
    }
    public String getParameterValue()
    {
        switch(phase)
        {
        	case SELECT_BUDSIZE:
        	    return new Double(areaRatio).toString();
        	case SELECT_LONGAXIS:
        	    return new Double(longAxis).toString();
        	case SELECT_ROUNDNESS:
        	    return new Double(roundness).toString();
        	case SELECT_BUDNECK:
        	    return new Double(neckPosition).toString();
        	case SELECT_BUDGROWTH:
        	    return new Double(growthDirection).toString();
        	default:
        	    return "";
        }
    }
    
    
    public double getGrowthDirection() {
        return growthDirection;
    }
    public void setGrowthDirection(double growthDirection) {
        this.growthDirection = growthDirection;
    }
    public double getLongAxis() {
        return longAxis;
    }
    public void setLongAxis(double longAxis) {
        this.longAxis = longAxis;
    }
    public double getNeckPosition() {
        return neckPosition;
    }
    public void setNeckPosition(double neckPosition) {
        this.neckPosition = neckPosition;
    }
    /*
    public String getBudSize() {
        return budSize;
    }
    public void setBudSize(String phase) {
        this.budSize = phase;
    }
    */
    public double getRoundness() {
        return roundness;
    }
    public void setRoundness(double roundness) {
        this.roundness = roundness;
    }
    public static final int      SELECT_BUDSIZE        = 0;
    public static final int      SELECT_LONGAXIS       = 1;
    public static final int      SELECT_ROUNDNESS      = 2;
    public static final int      SELECT_BUDNECK        = 3;
    public static final int      SELECT_BUDGROWTH      = 4;
    public static final int      SELECT_SEARCH         = 5;
    int                   phase = SELECT_BUDSIZE;
    int 				  targetPhase = SELECT_BUDSIZE;

    public int getTargetPhase() {
        return targetPhase;
    }
    public void setTargetPhase(int targetPhase) {
        this.targetPhase = targetPhase;
    }
    HashMap _paramNameToValueRangeMap = new HashMap();
    
    
    public String getShortAxis()
    {
        return (new DecimalFormat("#.##")).format(longAxis / roundness);
    }
    
    /**
     *  
     */
    public SelectedShape()
    {
        super();
        _paramNameToValueRangeMap.put(new Integer(SELECT_BUDSIZE), new Range(0, 1.0));
        _paramNameToValueRangeMap.put(new Integer(SELECT_LONGAXIS), new Range(1, 80));
        _paramNameToValueRangeMap.put(new Integer(SELECT_ROUNDNESS), new Range(1.0, 3.0));
        _paramNameToValueRangeMap.put(new Integer(SELECT_BUDNECK), new Range(0, 90));
        _paramNameToValueRangeMap.put(new Integer(SELECT_BUDGROWTH), new Range(0, 90));

    }

    public double getRangeMin()
    {
        return ((Range) _paramNameToValueRangeMap.get(new Integer(phase))).getMin();
    }
    public double getRangeMax()
    {
        return ((Range) _paramNameToValueRangeMap.get(new Integer(phase))).getMax();
    }
    
    
    
    public String getSelectionMessage()
    {
        return selectionMessage[phase];
    }
    
    public int getPhase() {
        return phase;
    }
    public void setPhase(int currentSelectionPhase) {
        this.phase = currentSelectionPhase;
    }
    public String getSelectionMessage2()
    {
        return selectionMessage2[phase];
    }
    
    public String getSelectionSubMessage()
    {
        return selectionSubMessage[phase];
    }
    
    public String getCurrentPhase()
    {
        return _selectionPhase[phase];
    }
    
    public String getParameterName()
    {
        return _parameterName[phase];
    }
    
    public int getCurrentPhaseID()
    {
        return phase;
    }
    
    public String[] getSelectionPhaseList()
    {
        return _selectionPhase;
    }
    
    static String [][] dataArray = new String[][]{
            {"0.0", "0.3", "0.5", "0.7"},
            {"30", "40", "50", "60"},
            {"1.0", "1.4", "1.8", "2.2"},
            {"15", "35", "55", "75"},
            {"15", "35", "55", "75"}
    };
    
    public String getImageArgument(int imageNum)
    {
        Map argMap = getCurrentImageArgumentMap();
        argMap.put(_parameterName[phase], getSelectionValue(imageNum));
        return CGIUtil.getCGIArgument(argMap);
    }   
    public Map getCurrentImageArgumentMap()
    {
        TreeMap defaultParameterMap = new TreeMap();
        defaultParameterMap.put("areaRatio", new Double(areaRatio));
        defaultParameterMap.put("longAxis", new Double(longAxis));
        defaultParameterMap.put("roundness", new Double(roundness));
        defaultParameterMap.put("neckPosition", new Double(neckPosition));
        defaultParameterMap.put("growthDirection", new Double(growthDirection));
        defaultParameterMap.put("areaRatio", new Double(areaRatio));
        return defaultParameterMap;
    }
    
    public String getImageArgument()
    {
        return CGIUtil.getCGIArgument(getCurrentImageArgumentMap());
    }
    
    public String getImageArgumentWithout(String paramName)
    {
        Map map = getCurrentImageArgumentMap();
        map.remove(paramName);
        return CGIUtil.getCGIArgument(map);
    }
    
    public String getSelectionValue(int imageNum)
    {
        return dataArray[phase][imageNum];
    }

    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        if(phase < 0 || phase > SELECT_SEARCH)
        {
            phase = SELECT_BUDSIZE;
        }
        
        // round-up
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        
        areaRatio = Double.parseDouble(format.format(areaRatio));
        longAxis = Double.parseDouble(format.format(longAxis));
        roundness= Double.parseDouble(format.format(roundness));
        neckPosition = Double.parseDouble(format.format(neckPosition));
        growthDirection = Double.parseDouble(format.format(growthDirection));

        return super.validate(mapping, request);
    }
}

//--------------------------------------
// $Log: SelectedShape.java,v $
// Revision 1.6  2004/08/29 13:15:11  leo
// morphology search。bud size -> bud area ratioに変更
//
// Revision 1.5  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.4  2004/08/14 14:32:24  leo
// MorphologySearchのイメージが表示されるように調整
// bud sizeによる選択部分を、数値で調整させるように変更する必要あり
//
// Revision 1.3  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.2  2004/07/30 06:40:29  leo
// Morphology Searchのインターフェース、完成
//
// Revision 1.1 2004/07/29 07:50:45 leo
// MorphologySearchのパーツ作成中
//
//--------------------------------------
