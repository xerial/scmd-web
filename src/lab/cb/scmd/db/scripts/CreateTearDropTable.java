//--------------------------------------
// SCMDWeb Project
//
// CreateTearDropTable.java
// Since: 2005/01/31
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.scripts;

import java.sql.SQLException;

import java.util.Collection;
import java.util.List;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.stat.Statistics;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.web.exception.DatabaseException;
import lab.cb.scmd.web.table.Table;

/**
 * TearDrop用のテーブルを作成 schemaは、(orf, param_id, group_id, num, average, sd, min,
 * max)
 * 
 * PostgreSQL専用
 * 
 * @author leo
 * 
 */
public class CreateTearDropTable
{
    private enum Opt {
        help, verbose
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();

    
    public static void main(String[] arg) 
    {
        try
        {
            CreateTearDropTable instance = new CreateTearDropTable();
            instance.process(arg);
        }
        catch (OptionParserException e)
        {
            System.err.println(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 
     */
    public CreateTearDropTable() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose",
                "display verbose messages");
        initDB();
    }

    public void process(String[] arg) throws OptionParserException,
            SCMDException, SQLException, XerialException
    {
        optionParser.parse(arg);
        
        // read group types
        QueryRunner queryRunner = new QueryRunner(dataSource);
        List<GroupType> groupTypeList = (List<GroupType>) queryRunner.query("select id, stain, name from groups  where id >= 1 order by id", new BeanListHandler(GroupType.class));
//        for(GroupType t : groupTypeList)
//           XMLBeanUtil.outputAsXML(t, System.out);

        List<Parameter> cellParamList = (List<Parameter>) queryRunner.query("select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id", new BeanListHandler(Parameter.class));
//        for(Parameter p : cellParamList)
//            XMLBeanUtil.outputAsXML(p, System.out);
        
        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {"-1", "-1.0", "."});
        
        // for each orf
        List orfList = (List) queryRunner.query("select distinct systematicname from genename_20040719 order by systematicname", new ColumnListHandler());
        
        for(Object orfObj : orfList)
        {
            String orf = orfObj.toString();
            System.out.println("[" + orf + "]");
            // for each param
            for(Parameter param : cellParamList)
            {
                // for each group
                for(GroupType group : groupTypeList)
                {                    
                    String groupName = group.getStain() + "group";
                    List dataList = (List) queryRunner.query(
                           "select " + doublequote(param.getName()) + " from individual_20050131 where strainname=" +quote(orf) + " and " + doublequote(groupName) + "=" + quote(group.getName()),
                            new ColumnListHandler());
                    
                    Table table = new Table();
                    table.addRow(dataList);
                    Collection samples = stat.filter(table.getHorizontalIterator(0));
                    
                    if(samples.size() == 0) 
                        continue;
                    double SD = Statistics.calcSD(samples);
                    double ave = Statistics.calcMean(samples);
                    double min = Statistics.getMinValue(samples);
                    double max = Statistics.getMaxValue(samples);
                    double num = samples.size();
                    System.out.println(orf + "\t" + param.getId() + "\t" +  group.getId() + "\t" +  num + "\t" +  ave + "\t" +  SD + "\t" +  min + "\t" +  max);
                }
            }
        }
        
    }

    String quote(String s)
    {
        return "'" + s + "'"; 
    }
    String doublequote(String s)
    {
        return "\"" + s + "\""; 
    }
    
    void initDB() throws DatabaseException
    {
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new DatabaseException(e);
        }

        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName("scmd.gi.k.u-tokyo.ac.jp");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("scmd");
        dataSource.setUser("leo");
        dataSource.setPassword("");
        dataSource.setMaxConnections(10);
    }

    public void printHelpMessage()
    {
        System.err.println(optionParser.helpMessage());
    }

}


