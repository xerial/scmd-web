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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.scripts.bean.Parameter;
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
public class CreateZScoreAnalysisTable {

    private enum Opt {
        help, verbose, outfile, avgoutfile, server, port, user, passwd, dbname
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream(); 

    public static void main(String[] arg) 
    {
        try
        {
            CreateZScoreAnalysisTable instance = new CreateZScoreAnalysisTable();
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
    public CreateZScoreAnalysisTable() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=zscore.txt", "zscore.txt");
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
        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {
                "-1", "-1.0", "." });

        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName(optionParser.getValue(Opt.server));
        dataSource.setPortNumber(optionParser.getIntValue(Opt.port));
        dataSource.setDatabaseName(optionParser.getValue(Opt.dbname));
        dataSource.setUser(optionParser.getValue(Opt.user));
        dataSource.setPassword(optionParser.getValue(Opt.passwd));
        dataSource.setMaxConnections(10);

        QueryRunner queryRunner = new QueryRunner(dataSource);

        Table sheet = new Table("workfolder/avgsd/analysisdata_ver2.xls");
       
        ColLabelIndex colLabelIndex = new ColLabelIndex(sheet);
        List colList = sheet.getRowList(0);
        
        for(Iterator it = colList.iterator(); it.hasNext(); ) {
            String colname = it.next().toString();
            TableIterator avgIterator = colLabelIndex.getVerticalIterator(colname);

            Collection samples = stat.filter(avgIterator);
            if (samples.size() <= 1)
                continue;

            double ave = Statistics.calcMean(samples);
            double SD = Statistics.calcSD(samples);
            double num = samples.size();

            System.out.println(colname + "\t" + ave + "\t" + SD + "\t" + num );
            outFile.println(colname + "\t" + ave + "\t" + SD + "\t" + num );
        }
        outFile.flush();
        outFile.close();
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
