//--------------------------------------
// SCMDWeb Project
//
// CreateTearDropBackground.java
// Since: 2005/02/02
//
// $URL$ 
// $Author$
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
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;


import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.sql.SQLExpression;
import lab.cb.scmd.db.sql.TableHandler;
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
import lab.cb.scmd.web.sessiondata.MorphParameter;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;

/**
 * TearDrop用の背景(ヒストグラム)を作成 
 * 
 * PostgreSQL専用
 * データ読み出し部分のコードは、CreateTeardropTableから一部流用
 * 
 * paramavgsd テーブル用の表も同時に生成
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
    
    private String _paramstatTable   = "paramstat";
    private String _converter;
    private boolean _wildTypeMode = false;
    

    private enum Opt {
        help, verbose, outfile, server, port, user, passwd, dbname, wildtype, group0
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
        if(optionParser.isSet(Opt.wildtype))
        {
            _paramstatTable = "paramstat_wt";
            _wildTypeMode = true;
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
        List<GroupType> groupTypeList = null;
        if(optionParser.isSet(Opt.group0))
        {
            groupTypeList = (List<GroupType>) queryRunner
            .query(
                    "select id, stain, name from groups where id = 0 order by id",
                    new BeanListHandler(GroupType.class));
        }
        else
        {
            groupTypeList = (List<GroupType>) queryRunner
            .query(
                    "select id, stain, name from groups order by id",
                    new BeanListHandler(GroupType.class));
        }
        
        drawProcess(queryRunner, outFile, groupTypeList);
    }
    
    private void drawProcess(QueryRunner queryRunner, PrintStream outFile, List<GroupType> groupTypeList)
        throws SQLException
    {
        List<MorphParameter> cellParamList = (List<MorphParameter>) queryRunner
                .query(
                        "select id, name, scope, datatype from parameterlist where scope in ('cell', 'orf') and datatype in ('num', 'double', 'cv') order by id",
                        new BeanListHandler(MorphParameter.class));

        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {
                "-1", "-1.0", "." });

        // for each param
        for (MorphParameter param : cellParamList) {
            // for each group
            for (GroupType group : groupTypeList) {
                log.printf("param %5s : group %10s", param.getId(), group.getName());
                log.println();
                log.flush();
//                String groupName = group.getStain() + "group";

                Table sheet = (Table) queryRunner.query(
                        "select strainname, average from " + _paramstatTable + " where paramid=" + param.getId() 
                        + " and groupid=" + group.getId(), new TableHandler());

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
                
                double mean = Statistics.calcMean(samples);
                double sd = Statistics.calcSD(samples);
                double min = Statistics.getMinValue(samples);
                double max = Statistics.getMaxValue(samples);                
                
                if(_wildTypeMode)
                {
                    String sql = SQLExpression.assignTo
                    ("select paramid, groupid, average, sd, min, max from $1 where groupid=$2 and paramid=$3",
                     "paramavgsd",
                     group.getId(),
                     param.getId());
                    TDStatParam tdsp = (TDStatParam) queryRunner.query(sql, new BeanHandler(TDStatParam.class));                
                    // TODO mutantの分布の幅より野生株の分布の幅が大きいときに困る
                    tds.setSD(tdsp.getSd());
                    tds.setAvg(tdsp.getAverage());
                    tds.setMin(tdsp.getMin());
                    tds.setMax(tdsp.getMax());                    
                }
                else
                {
                    tds.setSD(sd);
                    tds.setAvg(mean);
                    tds.setMin(min);
                    tds.setMax(max);                    
                }
                
                drawTeardropBackground(param.getId(), group.getId(), samples, tds);

                outFile.println(param.getId() + "\t"
                        + group.getId() + "\t" + mean + "\t" + sd + "\t"
                        + min + "\t" + max + "\t" + samples.size());
            }
        }

        log.println("finished.");
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
        catch(IOException e)
        {
            log.println("failed: " + e.getMessage());
        }
    }

    /**
     * 
     */
    public CreateTearDropBackground() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=paramavnsd.txt", "paramavgsd.txt");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        optionParser.addOption(Opt.wildtype, "w", "wildtype", "create teardrops for the wildtypes");
        optionParser.addOption(Opt.group0, "g", "g0", "create teardrops for parameters whose group id = 0");
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


