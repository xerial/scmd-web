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

import java.util.List;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.exception.DatabaseException;

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
        CreateTearDropTable instance = new CreateTearDropTable();
        try
        {
            instance.process(arg);
        }
        catch (OptionParserException e)
        {
            System.err.println(e.getMessage());
            instance.printHelpMessage();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 
     */
    public CreateTearDropTable()
    {}

    public void process(String[] arg) throws OptionParserException,
            SCMDException, SQLException, XerialException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose",
                "display verbose messages");

        optionParser.parse(arg);
        
        initDB();
        
        // read group types
        QueryRunner queryRunner = new QueryRunner(dataSource);
        List<GroupType> groupTypeList = (List<GroupType>) queryRunner.query("select id, stain, name from groups order by id", new BeanListHandler(GroupType.class));
//        for(GroupType t : groupTypeList)
//           XMLBeanUtil.outputAsXML(t, System.out);

        List<Parameter> cellParamList = (List<Parameter>) queryRunner.query("select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id", new BeanListHandler(Parameter.class));
        for(Parameter p : cellParamList)
            XMLBeanUtil.outputAsXML(p, System.out);
        
        
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


