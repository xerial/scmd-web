//--------------------------------------
//SCMD Project
//
//GeneNameORFTable.java 
//Since:  2004/07/16
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

//ORF, alias名の全対応を作成
//alias -> ORF の逆引きに利用
//DB上では、orfaliasname_$DATA のデータ

package lab.cb.scmd.db.scripts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.Cell;
import lab.cb.scmd.util.table.FlatTable;

public class MakeORFGeneNameTable {
    private FlatTable 	_ft;
    private HashMap		_geneMap = new HashMap();
    private static final int ORFCOLUMN 			= 0;
    private static final int PRIMARYNAMECOLUMN 	= 1;
    private static final int ALIASNAMECOLUMN 	= 2;
    PrintStream fOut = System.out;

    public MakeORFGeneNameTable (String filename) throws SCMDException {
        _ft = new FlatTable(filename);
    }
    
    public void setOutputFile (String filename) throws FileNotFoundException {
        fOut= new PrintStream(new FileOutputStream(filename));
    }
    
    private void makeMap() {
        Pattern pattern = Pattern.compile("^Y.[LR][0-9]+[CW].*");
        for( int i = 0; i < _ft.getRowSize(); i++ ) {
            ArrayList genes = new ArrayList();

            Cell orf = _ft.getCell(i, ORFCOLUMN);
            if( orf == null )
                continue;
            genes.add(orf.toString());
            Cell prm = _ft.getCell(i, PRIMARYNAMECOLUMN);
            if( !(prm == null || prm.toString().equals("")) )
                genes.add( prm.toString() );
            Cell als = _ft.getCell(i, ALIASNAMECOLUMN); 
            if( als != null ) {
                String[] alsary = als.toString().split("[|]");
                for( int j = 0; j < alsary.length; j++ ) {
                    if( alsary[j] != null && !alsary[j].equals("") )
                        genes.add(alsary[j]);
                    if( pattern.matcher(alsary[j]).matches() ) {
                        ArrayList g = new ArrayList();
                        g.add(alsary[j]);
                        _geneMap.put(alsary[j], g);
                    }
                }
            }
            _geneMap.put(orf.toString(), genes);
        }
    }
    
    private void printMap() {
        Set keys = _geneMap.keySet();
        Iterator it = keys.iterator();
        while( it.hasNext() ) {
            String orf = (String)it.next();
            ArrayList genes = (ArrayList)_geneMap.get(orf);
            for( int i = 0 ; i < genes.size() ; i++ ) {
                fOut.println(genes.get(i) + "\t" + orf);
            }
        }
    }


    public static void main(String[] args) {
        try {
            MakeORFGeneNameTable orftable = new MakeORFGeneNameTable(args[0]);
            if( args.length > 1 ) {
                orftable.setOutputFile(args[1]);
            }
            orftable.makeMap();
            orftable.printMap();
        } catch (SCMDException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
