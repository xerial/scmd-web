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

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.struts.action.ActionForm;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.datagen.MorphologicalSearch;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.util.CGIUtil;


/**
 * 2D Plot用のフォーム
 * @author leo
 *
 */
public class ParamPlotForm extends ActionForm
{
    private int param1 = -1;
    private int param2 = -1;
    private String submit = "";
    
    static private List<MorphParameter> paramList = new LinkedList<MorphParameter>();

    static public void init()
    {
        try
        {
            paramList = (List<MorphParameter>) ConnectionServer.query(new BeanListHandler(MorphParameter.class), 
                    "select id, name, shortname, displayname from $1 where scope='orf' and datatype in ('double', 'cv') order by id",
                    SCMDConfiguration.getProperty("DB_PARAMETERLIST", "parameterlist"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getParamName(int paramID)
    {
        for(MorphParameter p : paramList)
        {
            if(p.getId() == paramID)
                return p.getName();
        }
        return "";
    }
    
    public int getParamID(String paramName)
    {
        for(MorphParameter p : paramList)
        {
            if(p.getName() == paramName)
                return p.getId();
        }
        return -1;       
    }
    
    
    /**
     * 
     */
    public ParamPlotForm() {
        super();
    }
    
    public List<Integer> getOptionIDs()
    {
        LinkedList<Integer> paramID = new LinkedList<Integer>(); 
        for(MorphParameter p : paramList)
        {
            paramID.add(p.getId());
        }
        return paramID;
    }
    
    public List <String> getOptions()
    {        
        LinkedList<String> paramNames = new LinkedList<String>();
        for(MorphParameter p : paramList)
            paramNames.add(p.getName());
        return paramNames;
    }
    

    
    public int getParam1()
    {
        return param1;
    }
    public void setParam1(int param1)
    {
        this.param1 = param1;
    }
    public int getParam2()
    {
        return param2;
    }
    public void setParam2(int param2)
    {
        this.param2 = param2;
    }
    public String getSubmit() {
        return submit;
    }
    public void setSubmit(String submit) {
        this.submit = submit;
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
// batikを追加
//
// Revision 1.1  2004/09/07 16:49:46  leo
// 2D plotを追加
//
//--------------------------------------
