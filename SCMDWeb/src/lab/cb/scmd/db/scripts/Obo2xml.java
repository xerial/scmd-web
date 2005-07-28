//--------------------------------------
//SCMDWeb Project
//
//Obo2xml
//Since: 2005/02/23
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/db/scripts/CreateTearDropTable.java $ 
//$Author: leo $
//--------------------------------------
// GO のOBO形式のものを、xmlに変更する
// Ref. http://scmd-staff.gi.k.u-tokyo.ac.jp/developer/index.php?SCMD%2FDB%2FGO

package lab.cb.scmd.db.scripts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.xerial.util.xml.InvalidXMLException;
import org.xerial.util.xml.bean.XMLBeanException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.db.scripts.go.*;

public class Obo2xml {

    private OptionParser<Opt> optionParser = new OptionParser<Opt>();
    private String obofilename = "";

    private enum Opt {
        help, verbose, outfile, obsolete
    }

    public static void main(String[] args) {
        Obo2xml toXML = null;
        try {
            toXML = new Obo2xml();
            toXML.process(args);
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (SCMDException e) {
            e.printStackTrace();
        }
    }
    
    private void process(String[] args) throws OptionParserException {
        optionParser.parse(args);
        obofilename = optionParser.getArgument(0);

        PrintStream outFile = System.out;
        Obo obo = new Obo();
        try {
            if( optionParser.isSet(Opt.outfile)) {
                outFile = new PrintStream(new FileOutputStream(optionParser
                        .getValue(Opt.outfile)));
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(obofilename));

            String line = "";
            
            Pattern patternTerm = Pattern.compile("^\\[Term\\]$");
            Pattern patternId = Pattern.compile("^id: (.+)");
            
            boolean isHeader = true;
            boolean isTypedef = false;
            
            Term term = new Term();
            ArrayList<String> is_aList = new ArrayList<String>();
            ArrayList<Term> termList = new ArrayList<Term>();
            ArrayList<Relationship> relshipList = new ArrayList<Relationship>();
            ArrayList<Synonym> synonymList = new ArrayList<Synonym>();
            while ((line = fileReader.readLine()) != null) {
                line = line.replaceFirst("!.+", "");
                if( line.matches("^\\[Term\\]") || line.matches("\\[Typedef\\]") ) {
                    if( isHeader ) {
                        isHeader = false;
                        continue;
                    }
                    String[] is_aarray = new String[is_aList.size()];
                    for(int i = 0; i < is_aList.size(); i++ ) {
                        is_aarray[i] = is_aList.get(i);
                    }
                    is_aList.clear();
                    term.setIs_a(is_aarray);
                    Relationship[] relshiparray = new Relationship[relshipList.size()];
                    for(int i = 0; i < relshipList.size(); i++ ) {
                        relshiparray[i] = relshipList.get(i);
                    }
                    term.setRelationship(relshiparray);
                    relshipList.clear();

                    Synonym[] synonymarray = new Synonym[synonymList.size()];
                    for(int i = 0; i < synonymList.size(); i++ ) {
                        synonymarray[i] = synonymList.get(i);
                    }
                    term.setSynonym(synonymarray);
                    synonymList.clear();
                    
                    // remove obsolete terms
                    if( term.getIs_obsolete() == null || !term.getIs_obsolete().equals("true") || optionParser.isSet(Opt.obsolete)) {
                        term.setIs_a(is_aarray);
                        termList.add(term);
                    }
                    if( line.matches("\\[Typedef\\]")) {
                        isTypedef = true;
                    } else {
                        isTypedef = false;
                    }
                    term = new Term();
                    continue;
                }
                if ( line.matches("^id: +.+") ) {
                    String id = line.replaceFirst("id: +", "");
                    term.setId(id);
                } else if ( line.matches("^name: +.+") ) {
                    String name = line.replaceFirst("name: +", "");
                    term.setName(name);
                } else if ( line.matches("^namespace: +.+")) {
                    String namespace = line.replaceFirst("namespace: +", "");
                    term.setNamespace(namespace);
                } else if ( line.matches("^def: +.+")) {
                    String defstr = line.replaceFirst("def: +", "");
                    Def def = parseDefstr(defstr);
                    term.setDef(def);
                } else if ( line.matches("^is_a: +.+")) {
                    String is_a = line.replaceFirst("is_a: +", "");
                    is_aList.add(is_a);
                } else if ( line.matches("^is_obsolete: +.+")) {
                    String is_obsolete = line.replaceFirst("is_obsolete: +", "");
                    term.setIs_obsolete(is_obsolete);
                } else if ( line.matches("^relationship: +.+")) {
                    String rel = line.replaceFirst("relationship: +", "");
                    Relationship relship = parseRelShip(rel);
                    relshipList.add(relship);
                } else if ( line.matches("^(.+_)*synonym: +.+")) {
                    Pattern pattern = Pattern.compile("^(.+)_synonym: +(.+)$");
                    Matcher match = pattern.matcher(line);
                    Synonym synonym = new Synonym();
                    if( match.matches()) {
                        synonym.setScope(match.group(1));
                        synonym.setSynonym_text(match.group(2));
                    } else {
                        String text = line.replaceFirst(".*synonym: +", "");
                        synonym.setSynonym_text(text);
                    }
                    synonymList.add(synonym);
                }
            }
            if( !isTypedef ) {
                String[] is_aarray = new String[is_aList.size()];
                for(int i = 0; i < is_aList.size(); i++ ) {
                    is_aarray[i] = is_aList.get(i);
                }
                term.setIs_a(is_aarray);
                termList.add(term);
            }
            Term[] termarray = new Term[termList.size()];
            for(int i = 0; i < termList.size(); i++ ) {
                termarray[i] = termList.get(i);
            }
            obo.setTerm(termarray);
            
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            XMLBeanUtil.outputAsXML(obo, outFile);
        } catch (XMLBeanException e) {
            e.printStackTrace();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param rel
     * @return
     */
    private Relationship parseRelShip(String rel) {
        String[] term = rel.split(" ");
        Relationship relship = new Relationship();
        relship.setType(term[0]);
        relship.setTo(term[1]);
        return relship;
    }

    /**
     * @param defstr
     * @param def
     */
    private Def parseDefstr(String defstr) {
        Def def = new Def();
        Pattern pattern = Pattern.compile("\"(.+)\" \\[(.+)\\]");
        Matcher matcher = pattern.matcher(defstr);
        if( !matcher.matches() ) {
            return new Def();
        }
        def.setDefstr(matcher.group(1));
        String match = matcher.group(2);
        // "\, " でsplitしないように、置き換え。
        String m = match.replaceAll("\\,", "___");
        String[] xref = m.split(", ");
        ArrayList<Dbxref> dbxrefList = new ArrayList<Dbxref>(); 
        for(String s: xref) {
            Dbxref dbxref = new Dbxref();
            String[] str = s.split(":");
            dbxref.setDbname(str[0].replaceAll("___", "\\,"));
            dbxref.setAcc(str[1].replaceAll("___", "\\,"));
            dbxrefList.add(dbxref);
        }
        Dbxref[] dbxrefArray = new Dbxref[dbxrefList.size()];
        def.setDbxref(dbxrefArray);
        return def;
    }

    Obo2xml() throws OptionParserException, SCMDException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.outfile, "o", "output", "FILE", "output file name. defalut=teardrop.txt", "");
        optionParser.addOption(Opt.obsolete, "b", "obsolete", "output obsolete terms");
    }

}
