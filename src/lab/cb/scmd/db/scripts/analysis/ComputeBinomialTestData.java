//--------------------------------------
//SCMDWeb Project
//
//BinomialTestBetweenGOAndAbnormalORFs
//Since: 2005/02/28
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/db/scripts/CreateTearDropTable.java $ 
//$Author: leo $
//--------------------------------------

package lab.cb.scmd.db.scripts.analysis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.bean.GroupType;
import lab.cb.scmd.db.scripts.go.Term;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.io.NullPrintStream;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.FlatTable;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.exception.DatabaseException;

/**
 * @author sesejun
 *
 * GOのカテゴリと、変異認定の株の間で2項検定を行う
 * 
 */
public class ComputeBinomialTestData {

    private enum Opt {
        help, verbose, outfile, server, port, user, passwd, dbname, 
        minthreshold, maxthreshold, pvaluefile, log, molecularfunction
    }

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
    private PrintStream log = new NullPrintStream();
    private PrintStream outFile = new NullPrintStream();
    private String pvaluefilename = "";
    private double minthreshold = 0;
    private double maxthreshold = 0;
    private boolean useOnlyMolecularFunction = false;

    public static void main(String[] args) {
        ComputeBinomialTestData binom = null;
        try {
            binom = new ComputeBinomialTestData();
            binom.init(args);
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        binom.process();
    }
    
    public ComputeBinomialTestData() throws OptionParserException, DatabaseException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOption(Opt.molecularfunction, "m", "molecular", "use only molecular functions");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=stdout", "");
        optionParser.addOptionWithArgment(Opt.server, "s", "server", "SERVER", "postgres server name. defalut=localhost", "localhost");
        optionParser.addOptionWithArgment(Opt.port, "p", "port", "PORT", "port number. defalut=5432", "5432");
        optionParser.addOptionWithArgment(Opt.user, "u", "user", "USER", "user name. defalut=postgres", "postgres");        
        optionParser.addOptionWithArgment(Opt.passwd, "", "passwd", "PASSWORD", "password. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.dbname, "d", "db", "NAME", "database name. defalut=scmd", "scmd");        
        optionParser.addOptionWithArgment(Opt.pvaluefile, "f", "file", "FILE", "p-value file. defalut=analysistable.xls", "analysistable.xls");        
        optionParser.addOptionWithArgment(Opt.log, "", "log", "FILE", "log file. defalut=\"\"", "");        
        optionParser.addOptionWithArgment(Opt.minthreshold, "l", "low", "MIN", "minthreshold. defalut=-6", "-6");        
        optionParser.addOptionWithArgment(Opt.maxthreshold, "c", "high", "MAX", "maxthreshold. defalut=6", "6");
        initDB();
    }

    void initDB() throws DatabaseException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * @param args
     * @throws OptionParserException 
     * @throws FileNotFoundException 
     */
    private void init(String[] args) throws OptionParserException, FileNotFoundException {
        optionParser.parse(args);
        if(optionParser.isSet(Opt.help)) {
            System.out.println(optionParser.helpMessage());
            return;
        }
        if(optionParser.isSet(Opt.verbose)) {
            log = System.out;
        }
        if(optionParser.isSet(Opt.molecularfunction)) {
            useOnlyMolecularFunction = true;
        }

        if( optionParser.getValue(Opt.outfile).equals("")) {
            outFile = System.out;
        } else {
            outFile = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.outfile)));
        } 
        if( optionParser.getValue(Opt.log).equals("")) {
            log = System.err;
            log.println("Output File: " + optionParser.getValue(Opt.outfile));
            log.println("Log File: System err");
        } else {
            log = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.log)));
            log.println("Output File: " + optionParser.getValue(Opt.outfile));
            log.println("Log File: " + optionParser.getValue(Opt.log));
        }

        dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName(optionParser.getValue(Opt.server));
        dataSource.setPortNumber(optionParser.getIntValue(Opt.port));
        dataSource.setDatabaseName(optionParser.getValue(Opt.dbname));
        dataSource.setUser(optionParser.getValue(Opt.user));
        dataSource.setPassword(optionParser.getValue(Opt.passwd));
        dataSource.setMaxConnections(10);
        
        pvaluefilename = optionParser.getValue(Opt.pvaluefile);
        minthreshold = Double.parseDouble(optionParser.getValue(Opt.minthreshold));
        maxthreshold = Double.parseDouble(optionParser.getValue(Opt.maxthreshold));
    }
    
    /**
     * 
     */
    private void process() {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        BasicTable table = null;
        
        try {
            table = new FlatTable(pvaluefilename, true, true);
        } catch (SCMDException e) {
            e.printStackTrace();
        }
        
        Calendar cal = Calendar.getInstance();
        log.println("Date: " + cal.getTime().toString());

//        "GO:0045003",
//    "GO:0000228" };
        
        String[] goroots = {
                "GO:0003674",   // molecular_function
                "GO:0005575",   // cellular_component
                "GO:0008150"};  // biological_process
        if( useOnlyMolecularFunction ) {
            goroots = new String [] {
                    "GO:0003674"
            };
        }

        HashMap<String, List<Strainname>> goid2orf = new HashMap<String, List<Strainname>>();
        HashSet<String> availableOrfSet = new HashSet<String> ();
        for(Object label: table.getRowLabelList()) {
            availableOrfSet.add(label.toString());
        }
        try {
            for( int i = 0; i < goroots.length; i++ ) {
                goid2orf = getGoidToOrfMap(queryRunner, goid2orf, goroots[i], availableOrfSet);
            }
            // 子ノードと同じORFしか含まないterm は計算しない
            goid2orf = compressGoidToOrfMap(queryRunner, goid2orf);
        } catch (SQLException e) {
            e.printStackTrace();
        }
         
        computeParamToGoterm(queryRunner, table, goid2orf);
        //computeGotermToParam(table);
        
    }

    /**
     * @param table
     */
    private void computeParamToGoterm(QueryRunner queryRunner, BasicTable table, HashMap<String, List<Strainname>> goid2orf) {
        for(int col = 0; col < table.getColSize(); col++ ) {
            binomialTestToGo(queryRunner, col, table, goid2orf);
        }
    }

    /**
     * @param colLabel
     * @param verticalIterator
     */
    private void binomialTestToGo(QueryRunner queryRunner, int colLabel, BasicTable table, HashMap<String, List<Strainname>> goid2orf) {
        HashSet<String> highContain = new HashSet<String>();
        HashSet<String> lowContain = new HashSet<String>();

        for( int row = 0; row < table.getRowSize(); row++ ) {
            double v = table.getCell(row, colLabel).doubleValue();
            if( v <= minthreshold ) {
                lowContain.add(table.getRowLabel(row));
            } else if ( v >= maxthreshold ) {
                highContain.add(table.getRowLabel(row));
            }
        }
        if( lowContain.size() == 0 && highContain.size() == 0 ) {
            log.println(table.getColLabel(colLabel) + "\t" + "No Significant ORF");
            return;
        }

        for(String goid: goid2orf.keySet()) {
            List<Strainname> orfList = goid2orf.get(goid);
            int lowcount = 0;
            int highcount = 0;
            TreeSet<String> sigorfs = new TreeSet<String>();
            for(Strainname orf: orfList) {
                if( lowContain.contains(orf.getStrainname()) ) {
                    lowcount++;
                    sigorfs.add(orf.getStrainname());
                } else if ( highContain.contains(orf.getStrainname())) {
                    highcount++;
                    sigorfs.add(orf.getStrainname());
                }
            }
            outFile.print(table.getColLabel(colLabel) + "\t" + table.getRowSize() + "\t" + lowContain.size() + "\t" + highContain.size() + "\t");
//            System.out.print(goroots[i] + "\t" + goid + "\t" + orfList.size() + "\t" + lowcount + "\t" + highcount);
            outFile.print(goid + "\t" + orfList.size() + "\t" + lowcount + "\t" + highcount);
            try {
                if( optionParser.isSet(Opt.verbose) ) {
                    System.err.println(table.getColLabel(colLabel) + "\t" + lowContain.size() + "\t" + highContain.size() + "\t");
                    System.err.println(goid + "\t" + orfList.size() + "\t" + lowcount + "\t" + highcount);
                }
            } catch (OptionParserException e) {
                e.printStackTrace();
            }
            for(String o: sigorfs ) {
                outFile.print("\t" + o);
            }
            outFile.println();
        }
    }

    /**
     * @param queryRunner
     * @param goid2orf
     * @return
     * @throws SQLException 
     */
    private HashMap<String, List<Strainname>> compressGoidToOrfMap(QueryRunner queryRunner, HashMap<String, List<Strainname>> goid2orf) throws SQLException {
        TreeSet<String> removeSet = new TreeSet<String>(); 
        for(String goid: goid2orf.keySet()) {
            List<Strainname> orfList = goid2orf.get(goid);
            List<Term> childList = (List<Term>) queryRunner.query(
                    "select cid as id from term2term where pid ='" + goid + "'",
                    new BeanListHandler(Term.class));
            for(Term t: childList) {
                if( goid2orf.containsKey(t.getId()) && orfList.size() == goid2orf.get(t.getId()).size() ) {
                    removeSet.add(goid);
                    log.println(goid + "\t" + "Removed");
                    break;
                }
            }
        }
        for(String goid: removeSet) {
            goid2orf.remove(goid);
        }
        return goid2orf;
    }

    /**
     * @param queryRunner
     * @param gotermList
     * @return
     * @throws SQLException 
     */
    private HashMap<String, List<Strainname>> getGoidToOrfMap(QueryRunner queryRunner, HashMap<String, List<Strainname>> goid2orf, String goroot,
            HashSet<String> availableOrfSet ) throws SQLException {
        List<Term> gotermList = (List<Term>) queryRunner.query(
                "select goid as id, name from term where goid in "
                + "( select cid from term_graph where pid = '" 
                + goroot + "') order by goid", 
                new BeanListHandler(Term.class));
    
        for(Term t: gotermList) {
            List<Strainname> orfList = (List<Strainname>) queryRunner.query(
                    "select distinct strainname from goassociation where goid "
                    + "in (select cid as goid from term_graph where pid = '" 
                    + t.getId() + "')", 
                    new BeanListHandler(Strainname.class));
            // 関連するORFの無い項目は計算しない
            if( orfList.size() == 0 )
                continue;
            Iterator<Strainname> it = orfList.iterator();
            while(it.hasNext()) {
                if( !availableOrfSet.contains(it.next().getStrainname()) )
                    it.remove();
            }
            goid2orf.put(t.getId(), orfList);
            log.println(t.getId() + " contains " + orfList.size() + " orfs");
        }
        return goid2orf;
    }

}
