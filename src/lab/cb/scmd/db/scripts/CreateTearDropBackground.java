//--------------------------------------
//SCMDWeb Project
//
//CreateTearDropBackground.java
//Since: 2005/02/02
//
//$URL: $ 
//$Author: $
//--------------------------------------
package lab.cb.scmd.db.scripts;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
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

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;


import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.scripts.bean.Parameter;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.exception.UnfinishedTaskException;
import lab.cb.scmd.util.ProcessRunner;
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
 * TearDrop用の背景(ヒストグラム)を作成 
 * 
 * PostgreSQL専用
 * データ読み出し部分のコードは、CreateTeardropTableから一部流用
 * 
 * @author sesejun
 * 
 */
public class CreateTearDropBackground {

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream(); 

    private String PREFIX                   = "td_";
    private String SUFFIX                   = ".png";

    private enum Opt {
        help, verbose, outfile, server, port, user, passwd, dbname
    }

    public static void main(String[] args) {
        try {
            CreateTearDropBackground instance = new CreateTearDropBackground();
            instance.process(args);
        } catch (OptionParserException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Parameter> cellParamList = (List<Parameter>) queryRunner
                .query(
                        "select id, name, scope, datatype from parameterlist where scope='cell' and datatype='num' order by id",
                        new BeanListHandler(Parameter.class));

        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {
                "-1", "-1.0", "." });

        // for each orf
        List orfList = (List) queryRunner
                .query(
                        "select distinct systematicname from genename_20040719 order by systematicname",
                        new ColumnListHandler());

        // for each param
        for (Parameter param : cellParamList) {
            // for each group
            for (GroupType group : groupTypeList) {
                log.printf("param %5s : group %10s\n", param.getName(), group.getName());
//                log.flush();
//                String groupName = group.getStain() + "group";

                Table sheet = (Table) queryRunner.query(
                        "select strainname, average from paramstat where paramid=" + param.getId() 
                        + " and groupid=" + group.getId(), new ResultSetHandler() {
                            public Object handle(ResultSet rs) throws SQLException {
                                Table paramGroupSheet = new Table();
                                ResultSetMetaData metaData = rs.getMetaData();
                                Vector<String> columnName = new Vector<String>();
                                int colSize = metaData.getColumnCount();
                                for (int i = 1; i <= colSize; i++)
                                    columnName.add(metaData.getColumnName(i));
                                
                                paramGroupSheet.addRow(columnName);
                                
                                while (rs.next()) {
                                    Vector<Object> row = new Vector<Object>();
                                    for (int i = 1; i <= colSize; i++)
                                        row.add(rs.getObject(i));
                                    paramGroupSheet.addRow(row);
                                }  
                                return paramGroupSheet;
                            }
                        });

                ColLabelIndex colLabelIndex = new ColLabelIndex(sheet);
                LinkedList<TableElement> dataList = new LinkedList<TableElement>();
                for (TableIterator colIt = colLabelIndex
                        .getVerticalIterator("average"); colIt.hasNext();) {
                    Cell cell = colIt.nextCell();
                    dataList.add(colLabelIndex.get(colIt.row(), "average"));
                }
                Table table = new Table();
                table.addRow(dataList);
                Collection samples = stat.filter(table.getHorizontalIterator(0));

                if (samples.size() <= 1)
                    continue;

                TeardropStatistics tds = new TeardropStatistics();
                tds.setSD(Statistics.calcSD(samples));
                tds.setAvg(Statistics.calcMean(samples));
                tds.setMin(Statistics.getMinValue(samples));
                tds.setMax(Statistics.getMaxValue(samples));
                tds.setNum(samples.size());
                
                drawTeardropBackground(param.getId(), group.getId(), samples, tds);

                outFile.println(param.getId() + "\t"
                        + group.getId() + "\t" + tds.getAvg() + "\t" + tds.getSD() + "\t"
                        + tds.getMin() + "\t" + tds.getMax() + "\t" + tds.getNum());
            }
        }

        outFile.flush();
        outFile.close();
    }

    /**
     * @param id
     * @param id2
     * @param samples
     * @param tds
     */
    private void drawTeardropBackground(int paramid, int groupid, Collection samples, TeardropStatistics tds) {
        tds.calcHistgram(samples);
        String filename = PREFIX + paramid + "_" + groupid + SUFFIX;
        try {
            PrintStream out = new PrintStream( new BufferedOutputStream( new FileOutputStream(filename)));
            tds.drawTeardrop(tds, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 
     */
    public CreateTearDropBackground() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=teardrop.txt", "teardrop.txt");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        initDB();
    }

    private String quote(String s)
    {
        return "'" + s + "'"; 
    }
    private String doublequote(String s)
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
}
