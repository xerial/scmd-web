//--------------------------------------
//SCMDWeb Project
//
//CreateZScoreTable.java
//Since: 2005/02/06
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/db/scripts/CreateTearDropTable.java $ 
//$Author: leo $
//--------------------------------------
package lab.cb.scmd.db.scripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.io.NullPrintStream;
import lab.cb.scmd.util.stat.EliminateOnePercentOfBothSidesStrategy;
import lab.cb.scmd.util.stat.Statistics;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.exception.DatabaseException;
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;

/**
 * @author sesejun
 *
 * 各Parameterと各Groupの組み合わせで、全てのORFに関してZScoreを計算する
 * ToDo: とりあえず、mutant 全体の平均からZ-Scoreを計算するが、WT からの距離を計算するのが正しい
 * 
 */
public class CreateZScoreTable {

    private enum Opt {
        help, verbose, outfile, avgoutfile, server, port, user, passwd, dbname, wildtype
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream(); 

    public static void main(String[] arg) 
    {
        try
        {
            CreateZScoreTable instance = new CreateZScoreTable();
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
    public CreateZScoreTable() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.wildtype, "w", "wildtype", "use wildtype data");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=zscore.txt", "zscore.txt");
        optionParser.addOptionWithArgment(Opt.avgoutfile, "a", "output", "FILE", "average output file name. defalut=zscoreavg.txt", "zscoreavg.txt");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        initDB();
    }

    public void process(String[] arg) throws OptionParserException,
            SCMDException, SQLException, XerialException, IOException {
        optionParser.parse(arg);
        if (optionParser.isSet(Opt.help)) {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if (optionParser.isSet(Opt.verbose)) {
            log = System.out;
        }

        PrintStream outFile = new PrintStream(new FileOutputStream(optionParser
                .getValue(Opt.outfile)));
        PrintStream avgOutFile = new PrintStream(new FileOutputStream(optionParser
                .getValue(Opt.avgoutfile)));

        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName(optionParser.getValue(Opt.server));
        dataSource.setPortNumber(optionParser.getIntValue(Opt.port));
        dataSource.setDatabaseName(optionParser.getValue(Opt.dbname));
        dataSource.setUser(optionParser.getValue(Opt.user));
        dataSource.setPassword(optionParser.getValue(Opt.passwd));
        dataSource.setMaxConnections(10);

        QueryRunner queryRunner = new QueryRunner(dataSource);

        // read group types
        List<GroupType> groupTypeList = (List<GroupType>) queryRunner
                .query(
                        "select id, stain, name from groups  where id >= 1 order by id",
                        new BeanListHandler(GroupType.class));
        List<MorphParameter> cellParamList = (List<MorphParameter>) queryRunner
                .query(
                        "select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id",
                        new BeanListHandler(MorphParameter.class));

        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {
                "-1", "-1.0", "." });

        // for each orf
        List orfList = (List) queryRunner
                .query(
                        "select distinct systematicname from genename_20040719 order by systematicname",
                        new ColumnListHandler());

        // for each param
        for (MorphParameter param : cellParamList) {
            // for each group
            for(GroupType group : groupTypeList ) {
                log.printf("param %5s : group %10s\r", param.getName(), group.getName());
                log.flush();

                Table sheet = (Table) queryRunner.query(
                        "select strainname, paramid, groupid, average from paramstat where paramid=" + param.getId() + " AND groupid=" + group.getId(), 
                            new ResultSetHandler() {
                            public Object handle(ResultSet rs) throws SQLException {
                                Table orfSheet = new Table();
                                ResultSetMetaData metaData = rs.getMetaData();
                                Vector<String> columnName = new Vector<String>();
                                int colSize = metaData.getColumnCount();
                                for (int i = 1; i <= colSize; i++)
                                    columnName.add(metaData.getColumnName(i));

                                orfSheet.addRow(columnName);

                                while (rs.next()) {
                                    Vector<Object> row = new Vector<Object>();
                                    for (int i = 1; i <= colSize; i++)
                                        row.add(rs.getObject(i));
                                    orfSheet.addRow(row);
                                }
                                return orfSheet;
                            }
                        });
                ColLabelIndex colLabelIndex = new ColLabelIndex(sheet);
                Collection samples = stat.filter(colLabelIndex.getVerticalIterator("average"));

                if (samples.size() <= 1)
                    continue;
                
                double ave = 0.0;
                double SD = 0.0;
                double num = samples.size();
                
                if(optionParser.isSet(Opt.wildtype)) {
                    Table avgsdsheet = (Table) queryRunner.query(
                            "select average, sd, num from paramavgsd_wt where paramid=" + param.getId() + " AND groupid=" + group.getId(), 
                                new ResultSetHandler() {
                                public Object handle(ResultSet rs) throws SQLException {
                                    Table avgSheet = new Table();
                                    ResultSetMetaData metaData = rs.getMetaData();
                                    Vector<String> columnName = new Vector<String>();
                                    int colSize = metaData.getColumnCount();
                                    for (int i = 1; i <= colSize; i++)
                                        columnName.add(metaData.getColumnName(i));

                                    avgSheet.addRow(columnName);

                                    while (rs.next()) {
                                        Vector<Object> row = new Vector<Object>();
                                        for (int i = 1; i <= colSize; i++)
                                            row.add(rs.getObject(i));
                                        avgSheet.addRow(row);
                                    }
                                    return avgSheet;
                                }
                            });
                    if(avgsdsheet.getRowSize() == 2 ) {
                        if(!avgsdsheet.get(1,0).toString().equals("") ) {
                            ave = Double.parseDouble(avgsdsheet.get(1,0).toString());
                        } else {
                            System.err.println("ERROR! number format exception on average where paramid = " + param.getId() + " and groupid = " + group.getId());
                        }
                        if(!avgsdsheet.get(1,1).toString().equals("") ) {
                            SD  = Double.parseDouble(avgsdsheet.get(1,1).toString());
                        } else {
                            System.err.println("ERROR! number format exception on sd where paramid = " + param.getId() + " and groupid = " + group.getId());
                        }
                    } else {
                        System.err.println("ERROR!!! on paramid = " + param.getId() + " and groupid = " + group.getId());
                        System.err.println("Row Size " + avgsdsheet.getRowSize());
                        ave = -1.0;
                        SD = -1.0;
                    }
                } else {
                    ave = Statistics.calcMean(samples);
                    SD = Statistics.calcSD(samples);
                }

                avgOutFile.println(param.getId() + "\t" + group.getId() +  "\t" + ave + "\t" + SD + "\t" + num);

                Vector averageList = new Vector();
                
                for(TableIterator avgIterator = colLabelIndex.getVerticalIterator("average"),
                        orfIterator = colLabelIndex.getVerticalIterator("strainname");
                        avgIterator.hasNext() && orfIterator.hasNext(); ) {
                    String orf = orfIterator.nextCell().toString();
                    Cell v = avgIterator.nextCell();
                    String vstr = v.toString(); 
                    if( vstr.equals("-1") || vstr.equals("-1.0") || vstr.equals("."))
                        continue;
                    double value = v.doubleValue();
                    double zscore = (value - ave) / SD; 
                    outFile.println( orf + "\t" + param.getId() + "\t" + group.getId() + "\t" + zscore);
                }
            }
        }
        outFile.flush();
        outFile.close();
        avgOutFile.flush();
        avgOutFile.close();
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
