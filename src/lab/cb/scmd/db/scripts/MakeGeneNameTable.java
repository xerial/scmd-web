//--------------------------------------
//SCMD Server 
//
//MakeGeneNameTable.java 
//Since:  2004/07/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

// à‚ì`éqñº, primary name, alias ÇÃï\ÇçÏê¨
// MakeGeneNameTable <input file> <strain table> <geneid to gene name table>
// input file ÇÕÅAftp://ftp.yeastgenome.org/yeast/data_download/chromosomal_feature/chromosomal_feature.tab


package lab.cb.scmd.db.scripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MakeGeneNameTable {
    PrintStream fOutStrainId = System.out;
    PrintStream fOutGeneName = System.err;

    String DELIMITER = "\t";

    int GENENAMECOLUMN 		= 0;
    int PRIMNAMECOLUMN 		= 1;
    int ALIASNAMECOLUMN 	= 2;
    int ANNOTATIONCOLUMN 	= 10;

    ArrayList geneNameArray = new ArrayList();
    ArrayList primNameArray = new ArrayList();
    ArrayList aliasNameArray = new ArrayList();
    ArrayList annotationNameArray = new ArrayList();

    private void loadGeneNameTable(String filename) {
        try {
            String line = "";
            BufferedReader fileReader = new BufferedReader(new FileReader(filename));
            while ((line = fileReader.readLine()) != null) {
                String[] cells = line.split(DELIMITER);
                String geneName = cells[GENENAMECOLUMN];
                String primName = cells[PRIMNAMECOLUMN];
                String aliasName = cells[ALIASNAMECOLUMN];
                String annotationName = cells[ANNOTATIONCOLUMN];
                geneNameArray.add(geneName);
                primNameArray.add(primName);
                aliasNameArray.add(aliasName);
                annotationNameArray.add(annotationName);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void setStrainFile(String filename) throws FileNotFoundException {
        fOutStrainId = new PrintStream(new FileOutputStream(filename));
    }

    private void setGeneNameFile(String filename) throws FileNotFoundException {
        fOutGeneName = new PrintStream(new FileOutputStream(filename));
    }

    private void printTable() {
        int size = geneNameArray.size();
        int strainId = 0;
        int geneId = 0;
        Pattern pattern = Pattern.compile("^Y.[LR][0-9]+[CW].*");

        for(int i = 0; i < size; i++ ) {
            strainId++;
            geneId++;

            String geneName = (String)geneNameArray.get(i);
            String primName = (String)primNameArray.get(i);
            String aliasName = (String)aliasNameArray.get(i);
            String annotationName = (String)annotationNameArray.get(i);

            fOutStrainId.print(strainId + DELIMITER + geneId);
            fOutStrainId.print(DELIMITER + geneName);
            if( pattern.matcher(geneName).matches() ) {
                fOutStrainId.print(DELIMITER + "true" + "\n");
            } else {
                fOutStrainId.print(DELIMITER + "false" + "\n");
            }

            if( !pattern.matcher(geneName).matches() )
                continue; // 'cause not valid ORF name

            fOutGeneName.print(geneId + DELIMITER);
            fOutGeneName.print(geneName + DELIMITER);
            fOutGeneName.print(primName + DELIMITER);
            fOutGeneName.print(aliasName + DELIMITER);
            fOutGeneName.println(annotationName);
            
            String[] aliases = aliasName.split("[|]");
            for( int j = 0; j < aliases.length; j++ ) {
                if( pattern.matcher(aliases[j]).matches() ) {
                    strainId++;
                    geneId++;
                    fOutStrainId.print(strainId + DELIMITER + geneId);
                    fOutStrainId.print(DELIMITER + aliases[j]);
                    fOutStrainId.print(DELIMITER + "true" + "\n");

                    fOutGeneName.print(geneId + DELIMITER);
                    fOutGeneName.print(aliases[j] + DELIMITER);
                    fOutGeneName.print("" + DELIMITER);
                    fOutGeneName.print(geneName + DELIMITER);
                    fOutGeneName.println("This ORF is a part of " + geneName);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            MakeGeneNameTable makeGeneNameTable = new MakeGeneNameTable();
            makeGeneNameTable.loadGeneNameTable(args[0]);
            if( args.length > 1 ) {
                makeGeneNameTable.setStrainFile(args[1]);
                makeGeneNameTable.setGeneNameFile(args[2]);
            }
            makeGeneNameTable.printTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
