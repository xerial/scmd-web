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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.db.sql.TableHandler;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.io.NullPrintStream;
import lab.cb.scmd.util.stat.EliminateOnePercentOfBothSidesStrategy;
import lab.cb.scmd.util.stat.Statistics;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.exception.DatabaseException;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;

/**
 * TearDrop用のテーブルを作成 schemaは、(orf, param_id, group_id, num, average, sd, min, max, num)
 * 
 * PostgreSQL専用
 * 
 * @author leo
 * 
 */
public class CreateTearDropTable
{
    private enum Opt {
        help, verbose, outfile, server, port, user, passwd, dbname, wildtype
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream();
    private String individualTableName = "individual";

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
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=teardrop.txt", "teardrop.txt");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");
        optionParser.addOption(Opt.wildtype, "w", "wildtype", "analyze wildtype data");
        initDB();
    }

    public void process(String[] arg) throws OptionParserException,
            SCMDException, SQLException, XerialException, IOException
    {
        optionParser.parse(arg);
        if(optionParser.isSet(Opt.help))
        {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if(optionParser.isSet(Opt.verbose))
        {
            log = System.out;
        }
        
        PrintStream outFile = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.outfile)));

        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName(optionParser.getValue(Opt.server));
        dataSource.setPortNumber(optionParser.getIntValue(Opt.port));
        dataSource.setDatabaseName(optionParser.getValue(Opt.dbname));
        dataSource.setUser(optionParser.getValue(Opt.user));
        dataSource.setPassword(optionParser.getValue(Opt.passwd));
        dataSource.setMaxConnections(10);

        QueryRunner queryRunner = new QueryRunner(dataSource);

        List orfList = null;
        if(optionParser.isSet(Opt.wildtype))
        {
            individualTableName = "individual_wt";
            orfList = (List) queryRunner.query("select distinct strainname from individual_wt order by strainname", new ColumnListHandler());
        }
        else
        {
            orfList = (List) queryRunner.query("select distinct systematicname from genename_20040719 order by systematicname", new ColumnListHandler());    
        }        
        calcProcess(queryRunner, outFile, orfList);        
    }
    
    private void calcProcess(QueryRunner queryRunner, PrintStream outFile, List orfList)
        throws SQLException
    {        
        // read group types
        List<GroupType> groupTypeList = (List<GroupType>) queryRunner.query("select id, stain, name from groups  where id >= 1 order by id", new BeanListHandler(GroupType.class));
        List<MorphParameter> cellParamList = (List<MorphParameter>) queryRunner.query("select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id", new BeanListHandler(MorphParameter.class));

        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {"-1", "-1.0", "."}, new EliminateOnePercentOfBothSidesStrategy());
        
        // for each orf        
        for(Object orfObj : orfList)
        {
            String orf = orfObj.toString();
            Table sheet = (Table) queryRunner.query(
                    SQLExpression.assignTo("select * from $1 where strainname='$2'", individualTableName, orf),
                    new TableHandler());
            
            ColLabelIndex colLabelIndex = new ColLabelIndex(sheet);
            
            // for each param
            for(MorphParameter param : cellParamList)
            {
                // for each group
                for(GroupType group : groupTypeList)
                {   
                    log.printf("[%7s] param %5s : group %10s\r", orf, param.getName(), group.getName());
                    log.flush();
                    String groupName = group.getStain() + "group";

                    LinkedList<TableElement> dataList = new LinkedList<TableElement>();
                    if( group.getId() == 9 ) 
                    { 
                        // Dapi A1B is "A1" or "B"
                        for(TableIterator colIt = colLabelIndex.getVerticalIterator(groupName); colIt.hasNext(); )
                        {
                            Cell cell = colIt.nextCell();
                            if(cell.toString().equals("A1") || cell.toString().equals("B"))
                                dataList.add(colLabelIndex.get(colIt.row(), param.getName()));
                        }
                    } 
                    else 
                    {
                        for(TableIterator colIt = colLabelIndex.getVerticalIterator(groupName); colIt.hasNext(); )
                        {
                            Cell cell = colIt.nextCell();
                            if(cell.toString().equals(group.getName()))
                                dataList.add(colLabelIndex.get(colIt.row(), param.getName()));
                        }
                    }
                    
//                    List dataList = (List) queryRunner.query(
//                           "select " + doublequote(param.getName()) + " from individual_20050131 where strainname=" +quote(orf) + " and " + doublequote(groupName) + "=" + quote(group.getName()),
//                            new ColumnListHandler());
                    
                    Table table = new Table();
                    table.addRow(dataList);
                    // sampleの両端1%分を除去
                    Collection samples = stat.filter(table.getHorizontalIterator(0)); 
                    
                    if(samples.size() <= 1) 
                        continue;

                    double SD = Statistics.calcSD(samples);
                    double ave = Statistics.calcMean(samples);
                    double min = Statistics.getMinValue(samples);
                    double max = Statistics.getMaxValue(samples);
                    int num = samples.size();                    
                    //outFile.println(orf + "\t" + param.getId() + "\t" +  group.getId() + "\t" + ave + "\t" +  SD + "\t" +  min + "\t" +  max);
                    outFile.println(orf + "\t" + param.getId() + "\t" +  group.getId() + "\t" + ave + "\t" +  SD + "\t" +  min + "\t" +  max + "\t" + num );
                }
            }
        }
        outFile.flush();
        outFile.close();
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

    }

    public void printHelpMessage()
    {
        System.err.println(optionParser.helpMessage());
    }

}


