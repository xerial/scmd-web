//--------------------------------------
// SCMDServer
//
// ParamPlotForm.java 
// Since: 2004/09/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.action.ActionForm;

import lab.cb.scmd.web.util.CGIUtil;

/**
 * @author leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamPlotForm extends ActionForm
{
    String param1 = "";
    String param2 = "";
    
    final static String[] _params = 
    { "C11-1_A", "C13_A", "C103_A", "C104_A", "C115_A", "CCV11-1_A", "CCV115_A", "A101_A", "A106_A", "A113_A", "ACV101_A", "D14-1_A", "D15-1_A", "D17-1_A", "D102_A", "D105_A", "D117_A", "D147_A", "D154_A", "D176_A", "D179_A", "D182_A", "D188_A", "D194_A", "DCV14-1_A", "DCV105_A", "DCV147_A", "DCV182_A", "C11-1_A1B", "C11-2_A1B", "C13_A1B", "C101_A1B", "C103_A1B", "C104_A1B", "C105_A1B", "C106_A1B", "C107_A1B", "C108_A1B", "C109_A1B", "C110_A1B", "C111_A1B", "C112_A1B", "C113_A1B", "C114_A1B", "C115_A1B", "C116_A1B", "C118_A1B", "C123_A1B", "C124_A1B", "C125_A1B", "C127_A1B", "CCV11-1_A1B", "CCV11-2_A1B", "CCV105_A1B", "CCV109_A1B", "CCV114_A1B", "CCV115_A1B", "CCV118_A1B", "A7-2_A1B", "A9_A1B", "A101_A1B", "A103_A1B", "A107_A1B", "A108_A1B", "A110_A1B", "A112_A1B", "A113_A1B", "D15-3_A1B", "D17-3_A1B", "D104_A1B", "D107_A1B", "D110_A1B", "D114_A1B", "D118_A1B", "D143_A1B", "D145_A1B", "D147_A1B", "D152_A1B", "D154_A1B", "D169_A1B", "D181_A1B", "DCV107_A1B", "DCV114_A1B", "DCV147_A1B", "C11-1_C", "C11-2_C", "C13_C", "C101_C", "C103_C", "C104_C", "C105_C", "C106_C", "C107_C", "C108_C", "C109_C", "C110_C", "C111_C", "C112_C", "C113_C", "C114_C", "C115_C", "C116_C", "C118_C", "C123_C", "C125_C", "CCV11-1_C", "CCV11-2_C", "CCV105_C", "CCV106_C", "CCV109_C", "CCV114_C", "CCV115_C", "CCV118_C", "A7-2_C", "A9_C", "A101_C", "A102_C", "A103_C", "A104_C", "A107_C", "A108_C", "A109_C", "A112_C", "A113_C", "ACV102_C", "D14-1_C", "D14-2_C", "D14-3_C", "D15-1_C", "D15-2_C", "D15-3_C", "D17-1_C", "D17-2_C", "D103_C", "D106_C", "D108_C", "D109_C", "D112_C", "D113_C", "D116_C", "D117_C", "D119_C", "D121_C", "D125_C", "D143_C", "D144_C", "D145_C", "D146_C", "D147_C", "D149_C", "D151_C", "D152_C", "D153_C", "D154_C", "D156_C", "D158_C", "D162_C", "D166_C", "D170_C", "D176_C", "D177_C", "D179_C", "D180_C", "D182_C", "D183_C", "D185_C", "D188_C", "D189_C", "D194_C", "D195_C", "D196_C", "D197_C", "DCV14-1_C", "DCV14-2_C", "DCV14-3_C", "DCV106_C", "DCV112_C", "DCV113_C", "DCV116_C", "DCV147_C", "DCV149_C", "C119", "C120", "C122", "C123", "C124", "C125", "A105", "A106", "A107", "A108", "A110", "A111", "A112", "A113", "A115", "A116", "A117", "A118", "A119", "D203", "D204", "D205", "D206", "D210", "D211", "D213", "D214", "D215", "D216"};

    /**
     * 
     */
    public ParamPlotForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public String[] getOptions()
    {
        return _params;
    }
    
    public String getParam1() {
        return param1;
    }
    public void setParam1(String param1) {
        this.param1 = param1;
    }
    public String getParam2() {
        return param2;
    }
    public void setParam2(String param2) {
        this.param2 = param2;
    }
    
    public Map getArgumentMap()
    {
        TreeMap map = new TreeMap();
        map.put("param1", param1);
        map.put("param2", param2);
        return map;
    }
    
    public String getCgiArgument()
    {
        return CGIUtil.getCGIArgument(getArgumentMap());
    }
}


//--------------------------------------
// $Log: ParamPlotForm.java,v $
// Revision 1.2  2004/09/08 02:30:32  leo
// batik‚ð’Ç‰Á
//
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plot‚ð’Ç‰Á
//
//--------------------------------------
