//--------------------------------------
// SCMDServer
// 
// DataSheetType.java 
// Since: 2004/07/31
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

/**
 * @author leo
 *  
 */
public class DataSheetType
{
    public final static int      SHEET_SHAPE   = 0;
    public final static int      SHEET_CELL    = 1;
    public final static int      SHEET_NUCLEUS = 2;
    public final static int      SHEET_ACTIN   = 3;
    public final static int      SHEET_CUSTOM  = 4;
    public final static int 		SHEET_PHOTO_INFO_ONLY = 5;
    public final static int      SHEET_MAX     = 6;

    public final static String[] TAB_NAME      = { "Shape", "Cell", "Nucleus", "Actin", "Custom"};
    public final static String[] PHOTO_INFO_PARAM = {"image_number", "x1", "x2", "y1", "y2"};

    public static boolean isValidDataSheetType(int dataSheetType)
    {
        return (dataSheetType >=0) && (dataSheetType < SHEET_MAX);
    }
    
    //パラメータ名から、パラメータのIDに変更 => getParameterIds(int n)
    public static String [] getParameters(int n) {
        switch (n) {
            case SHEET_SHAPE:
                return shapeParameters;
            case SHEET_CELL:
                return cellParameters;
            case SHEET_NUCLEUS:
                return nucleusParameters;
            case SHEET_ACTIN:
                return actinParameters;
        }
        return new String [0];
    }

    public static int [] getParameterIds(int n) {
        switch (n) {
            case SHEET_SHAPE:
                return shapeParameterIds;
            case SHEET_CELL:
                return cellParameterIds;
            case SHEET_NUCLEUS:
                return nucleusParameterIds;
            case SHEET_ACTIN:
                return actinParameterIds;
        }
        return new int [0];
    }

    //パラメータ名から、パラメータのIDに変更 => getDetailParameterIds(int n)
    public static String [] getDetailParameters(int n) {
        switch (n) {
            case SHEET_SHAPE:
                return shapeDetailParameters;
            case SHEET_CELL:
                return cellDetailParameters;
            case SHEET_NUCLEUS:
                return nucleusDetailParameters;
            case SHEET_ACTIN:
                return actinDetailParameters;
        }
        return new String [0];
    }

    public static int [] getDetailParameterIds(int n) {
        switch (n) {
            case SHEET_SHAPE:
                return shapeDetailParameterIds;
            case SHEET_CELL:
                return cellDetailParameterIds;
            case SHEET_NUCLEUS:
                return nucleusDetailParameterIds;
            case SHEET_ACTIN:
                return actinDetailParameterIds;
        }
        return new int [0];
    }

    private final static String [] shapeParameters = 
    {
            "Cgroup",
            "Dgroup",
            "Agroup",
            "C101",
            "C11-2",
            "C11-1",
            "C103",
            "C104",
            "C105",
            "C106"
    };
    
    private final static String [] cellParameters = {
            "Cgroup",
            "C107",
            "C108",
            "C109",
            "C110",
            "C111",
            "C112",
            "C113",
            "C114",
            "C115"
    };
    
    private final static String [] nucleusParameters = {
            "Dgroup",
            "D104",
            "D105",
            "D106",
            "D107",
            "D108",
            "D109",
            "D110",
            "D111",
            "D112",
    };
    
    private final static String [] actinParameters = {
            "Agroup",
            "A101",
            "A102",
            "A103",
            "A104",
            "A120",
            "A121",
            "A122",
            "A123"
    };

    private final static int [] shapeParameterIds = 
    {
        8, // "Cgroup",
        79, //"Dgroup",
        50, // "Agroup",
        29, // "C101",
        25, // "C11-2",
        24, // "C11-1",
        31, // "C103",
        32, // "C104"
        33, // "C105",
        34 // "C106"
    };
    
    private final static int [] cellParameterIds = {
        8, // "Cgroup",
        35,//    "C107",
        36,//    "C108",
        37,//    "C109",
        38,//    "C110",
        39,//    "C111",
        40,//    "C112",
        41,//    "C113",
        42,//    "C114",
        43 //    "C115"
    };
    
    private final static int [] nucleusParameterIds = {
        79,//    "Dgroup",
        126,//    "D104",
        127,//    "D105",
        128,//    "D106",
        129,//    "D107",
        130,//    "D108",
        131,//    "D109",
        132,//    "D110",
        133,//    "D111",
        134 //    "D112",
    };
    
    private final static int [] actinParameterIds = {
        50,//    "Agroup",
        71,//    "A101",
        72,//    "A102",
        73,//    "A103",
        74,//    "A104",
        75,//    "A120",
        76,//    "A121",
        77,//    "A122",
        78 //    "A123"
    };

    private final static String [] shapeDetailParameters = 
    {
    		"Cgroup",
			"Dgroup",
			"Agroup",
			"C101",
			"C11-2",
			"C11-1",
			"C103",
			"C104"
    };
    
    private final static String [] cellDetailParameters = {
    		"Cgroup",
			"Dgroup",
			"Agroup"
    		
    };
    
    private final static String [] nucleusDetailParameters = {
    		"Cgroup",
			"Dgroup",
			"Agroup"

    };
    
    private final static String [] actinDetailParameters = {
    		"Cgroup",
			"Dgroup",
			"Agroup"

    };

    private final static int [] shapeDetailParameterIds = 
    {
        8, // "Cgroup",
        79, //"Dgroup",
        50, // "Agroup",
        29, // "C101",
        25, //"C11-2",
        24, //"C11-1",
        31, //"C103",
        32  // "C104"
    };
    
    private final static int [] cellDetailParameterIds = {
        8, //"Cgroup",
        79, //"Dgroup",
        50 // "Agroup"
            
    };
    
    private final static int [] nucleusDetailParameterIds = {
        8, //"Cgroup",
        79, //"Dgroup",
        50 // "Agroup"
    };
    
    private final static int [] actinDetailParameterIds = {
        8, //"Cgroup",
        79, //"Dgroup",
        50 // "Agroup"
    };

}

//--------------------------------------
// $Log: DataSheetType.java,v $
// Revision 1.7  2004/09/02 08:49:48  leo
// no budや個数０個のときの処理を追加
// datasheetのデザインを調整
//
// Revision 1.6  2004/08/31 08:44:01  leo
// Group By Sheet完成
//
// Revision 1.5  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.4  2004/08/15 07:14:42  sesejun
// GroupByのDB接続書き始め(未完成)
//
// Revision 1.3  2004/08/14 11:13:06  sesejun
// DataSheet対応開始。
// getCellCordinates()にcellidを追加
//
// Revision 1.2  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.1 2004/07/31 05:11:36 leo
// *** empty log message ***
//
//--------------------------------------
