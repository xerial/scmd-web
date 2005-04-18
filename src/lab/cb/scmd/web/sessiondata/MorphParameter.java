//--------------------------------------
//SCMDServer
//
//MorphologicalParameter.java 
//Since: 2005/02/08
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/bean/YeastGene.java $ 
//$LastChangedBy: sesejun $ 
//--------------------------------------
package lab.cb.scmd.web.sessiondata;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import lab.cb.scmd.db.connect.ConnectionServer;
import lab.cb.scmd.web.common.SCMDConfiguration;

public class MorphParameter {
    int id = 0; 
    String name = "";
    String scope = "";
    String shortName = "";
    String datatype = "";
    String displayname = "";
    String stain = "";
    String groupid = "";
    String systematicname = "";
    String definition = "";
    
    String groupName = "";

    public MorphParameter() {
        
    }
    
    public MorphParameter(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }
    public String getIdStr() {
        return "" + id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getDatatype() {
        return datatype;
    }
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
    
    public String getNucleusStatus() {
        String[] names = name.split("_");
        if( names.length == 1 )
            return "none";
        return names[1];
    }
    
    public String getParameterType() {
        if(name.matches("^[ACD]CV.+"))
            return "Coefficient of Variation";
        return "Average";
    }
    
    public String getStainType() {
        if(name == null || name.length() < 1)
            return "";
        if(name.charAt(0) == 'A' ) {
            return "Actin";
        } else if(name.charAt(0) == 'D') {
            return "Nucleus";
        }
        return "Cell Wall";
    }
    public String getSystematicname()
    {
        return systematicname;
    }
    public void setSystematicname(String systematicname)
    {
        this.systematicname = systematicname;
    }
    
    public String getGroupName()
    {
        return groupName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    
    public String getStain()
    {
        return stain;
    }
    public void setStain(String stain)
    {
        this.stain = stain;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition(String definition)
    {
        this.definition = definition;
    }
    
    public String getHtmlDefinition()
    {
        final Pattern p = Pattern.compile("[ACD](CV)?[0-9]+(-[0-9])?(_(A|A1B|C))?");
        if(definition == null)
            return "";
        Matcher m = p.matcher(definition);
        StringBuffer buffer = new StringBuffer();
        while(m.find())
        {
            String paramName = definition.substring(m.start(), m.end());
            int paramID = parameterID(paramName);
            if(paramID != -1)
                m.appendReplacement(buffer, "<a href=\"ViewORFParameter.do?columnType=input&paramID=" + paramID + "&sortspec=" + paramID + "\">" + paramName + "</a>");
            else
                m.appendReplacement(buffer, paramName);
        }
        m.appendTail(buffer);
        
        return buffer.toString();
    }

    static public int parameterID(String parameterName) 
    {
        try
        {
            Integer id = (Integer) ConnectionServer.query(new ScalarHandler("id"), 
                    "select id from $1 where name='$2'",
                    SCMDConfiguration.getProperty("DB_PARAMETERLIST", "visible_parameterlist"),
                    parameterName
                    );
            if(id != null)
                return id.intValue();
            else
                return -1;
        }
        catch(SQLException e)
        {
            // cannot find id
            e.printStackTrace();
            return -1;
        }
    }
    
}
