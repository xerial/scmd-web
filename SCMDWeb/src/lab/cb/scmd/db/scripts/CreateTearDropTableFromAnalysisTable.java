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
import java.util.HashMap;
import java.util.Iterator;
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
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.Table;

import org.apache.commons.dbutils.QueryRunner;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.xerial.XerialException;

/**
 * @author sesejun
 *
 * 各Parameterと各Groupの組み合わせで、全てのORFに関してZScoreを計算する
 * ToDo: とりあえず、mutant 全体の平均からZ-Scoreを計算するが、WT からの距離を計算するのが正しい
 * 
 */
public class CreateTearDropTableFromAnalysisTable {

    private enum Opt {
        help, verbose, outfile, avgoutfile, server, port, user, passwd, dbname, 
        table, paramlist, update
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream(); 

    public static void main(String[] arg) 
    {
        try
        {
            CreateTearDropTableFromAnalysisTable instance = new CreateTearDropTableFromAnalysisTable();
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
    public CreateTearDropTableFromAnalysisTable() throws OptionParserException, SCMDException
    {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.update, "", "update", "output update script");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=zscore.txt", "zscore.txt");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        optionParser.addOptionWithArgment(Opt.table, "t", "table", "TABLE", "table name", "table");
        optionParser.addOptionWithArgment(Opt.paramlist, "l", "paramlist", "PARAMLIST", "parameter list", "parameterlist_avg.xls");
        
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
        String tablename = optionParser.getValue(Opt.table);
        String paramfile = optionParser.getValue(Opt.paramlist); 

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

        // analysis data
        Table sheet = new Table(tablename);
        // parameterlist。ただし、cellを抜いもの
        Table paramSheet = new Table(optionParser.getValue(Opt.paramlist));
       
        RowLabelIndex rowLabelIndex = new RowLabelIndex(sheet);
        ColLabelIndex colLabelIndex = new ColLabelIndex(sheet);
        RowLabelIndex paramLabelIndex = new RowLabelIndex(paramSheet);
        List rowList = sheet.getRowList(0);
        List colList = sheet.getColList(0);
        List paramList = paramSheet.getColList(1);
        // PARAMNAME, paramid
        HashMap<String, String> paramMap = new HashMap<String, String> ();
        for(int i = 1; i < paramList.size(); i++ ) {
            paramMap.put(paramList.get(i).toString(), paramSheet.get(i, 0).toString());
        }
        int paramsize = paramSheet.getRowSize();
        
        int n = 0;
        Iterator itcol = rowList.iterator();
        itcol.next();
        while( itcol.hasNext() ) {
            String paramname = itcol.next().toString();
            int col = colLabelIndex.getColIndex(paramname);
            String paramid = paramMap.get(paramname);
            if( paramid == null ) {
                System.out.println("ERROR!!! " + paramname);
            }
            Iterator itrow = colList.iterator();
            itrow.next();
            while( itrow.hasNext() ) {
                String orfname = itrow.next().toString();
                int row = rowLabelIndex.getRowIndex(orfname);
                
                if(optionParser.isSet(Opt.update)) {
                    outFile.println("update paramstat set average=" + sheet.get(row, col) 
                            + " where strainname='" + orfname + "' and paramid='" + paramid + "' and groupid='0';");
                } else {
                    outFile.println(orfname + "\t" + paramid + "\t0\t" + sheet.get(row, col) + "\t0\t0\t0\t" + (colList.size() - 1));
                }
            }
            System.out.println( (n++) + "\t" + paramid + "\t" + paramname );
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
