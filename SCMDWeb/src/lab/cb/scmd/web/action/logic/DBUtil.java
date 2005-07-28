//--------------------------------------
// SCMDServer
// 
// DBUtil.java 
// Since: 2004/09/03
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action.logic;

import java.util.LinkedList;
import java.util.List;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.YeastGene;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.sessiondata.MorphParameter;

/**
 * @author leo
 *
 */
public class DBUtil
{

    /**
     * 
     */
    public DBUtil()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static YeastGene getGeneInfo(String orf)
    {
        XMLQuery xmlQuery = SCMDConfiguration.getXMLQueryInstance();
        YeastGene gene = null;
        try
        {
            gene = new YeastGene(orf, xmlQuery);
        }
        catch (SCMDException e1)
        {
            // fail to load the ORF inforamtion
            e1.printStackTrace();
            gene = new YeastGene(orf);
        }
        return gene;
    }
    
    public static MorphParameter getParamInfo(String param, String type)
    {
        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        MorphParameter morphParam = query.getOneParameterInfo(param, type);
        return morphParam;
    }

}


//--------------------------------------
// $Log: DBUtil.java,v $
// Revision 1.1  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
//--------------------------------------