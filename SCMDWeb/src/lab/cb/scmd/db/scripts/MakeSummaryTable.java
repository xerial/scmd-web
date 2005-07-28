//--------------------------------------
//SCMD Server 
//
//MakeSummaryTable.java 
//Since:  2004/07/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

// args[0]: 平均値のデータ
// args[1]: strain_table
// args[2]: (あれば)出力ファイル。省略時は標準出力
// args[3]: DBのcreate table用ファイル。省略時は出力なし。
package lab.cb.scmd.db.scripts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.FlatTable;

public class MakeSummaryTable {
    FlatTable summaryTable;
    FlatTable strainTable;
    HashMap   orfStrainMap = new HashMap();
    PrintStream _fOut = System.out;

    public MakeSummaryTable(String summaryFile, String strainFile) throws SCMDException {
        summaryTable	= new FlatTable(summaryFile);
        strainTable 	= new FlatTable(strainFile);
        for( int i = 0; i < strainTable.getRowSize(); i++ ) {
            orfStrainMap.put(strainTable.getCell(i, 2).toString(), 
                    strainTable.getCell(i, 0).toString());
        }
    }

    private void setOutputFile(String filename) throws FileNotFoundException {
        _fOut= new PrintStream(new FileOutputStream(filename));
    }

    private void printSummaryTable() {
        for( int i = 0; i < summaryTable.getRowSize(); i++ ) {
            String orf = summaryTable.getCell(i, 0).toString().toUpperCase();
            _fOut.print(orfStrainMap.get(orf));
            for( int j = 1; j < summaryTable.getColSize(); j++ ) {
                _fOut.print("\t" + summaryTable.getCell(i, j).toString());
            }
            _fOut.println();
        }
    }

    private void printCreateTableFile(String filename) throws FileNotFoundException {
        PrintStream _createTable = new PrintStream(new FileOutputStream(filename));
        int size = summaryTable.getColSize();
        String[] path = filename.split("/");
        String tableName = path[path.length - 1];
        _createTable.println("CREATE TABLE "+  tableName + "(");
        _createTable.println("\tstrainid    integer NOT NULL,");
        for( int i = 1; i < size; i++ ) {
            _createTable.println("\t\"" + summaryTable.getColLabel(i) + "\"    " + "float8 NOT NULL,");
        }
        _createTable.println(")");
    }

    public static void main(String[] args) {
        try {
            MakeSummaryTable mst = new MakeSummaryTable(args[0], args[1]);
            if( args.length > 1 ) {
                mst.setOutputFile(args[2]);
            }
            mst.printSummaryTable();
            if( args.length > 2 ) {
                mst.printCreateTableFile(args[3]);
            }
        } catch (SCMDException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
