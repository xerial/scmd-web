/*
 * Created on 2005/02/23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
// XML-OBOå`éÆÇÃÇ‡ÇÃÇÅADBÇ…ì¸ÇÍÇÈÇ…ïœçXÇ∑ÇÈ
//Ref. http://scmd-staff.gi.k.u-tokyo.ac.jp/developer/index.php?SCMD%2FDB%2FGO

package lab.cb.scmd.db.scripts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.TreeSet;

import org.xerial.util.XMLParserException;
import org.xerial.util.xml.InvalidXMLException;
import org.xerial.util.xml.bean.XMLBeanException;
import org.xerial.util.xml.bean.XMLBeanUtil;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.db.scripts.go.Obo;
import lab.cb.scmd.db.scripts.go.Relationship;
import lab.cb.scmd.db.scripts.go.Term;
import lab.cb.scmd.exception.SCMDException;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Oboxml2db {
    private OptionParser<Opt> optionParser = new OptionParser<Opt>();

    private enum Opt {
        help, verbose, termfile, t2tfile, graphfile 
    }

    public static void main(String[] args) {
        Oboxml2db oboxml2db;
        try {
            oboxml2db = new Oboxml2db();
            oboxml2db.process(args);
        } catch (OptionParserException e) {
            e.printStackTrace();
        } catch (SCMDException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Oboxml2db() throws OptionParserException, SCMDException {
        optionParser.addOption(Opt.help, "h", "help", "display help messages");
        optionParser.addOption(Opt.verbose, "v", "verbose", "display verbose messages");
        optionParser.addOptionWithArgment(Opt.termfile, "o", "output", "FILE", "output file name.", "term.txt");
        optionParser.addOptionWithArgment(Opt.t2tfile, "a", "term2term", "FILE", "term2term output file name.", "term2term.txt");
        optionParser.addOptionWithArgment(Opt.graphfile, "g", "termgraph", "FILE", "term graph file name.", "graphterm.txt");
    }

    private void process(String[] args) throws OptionParserException, FileNotFoundException, SCMDException {
        optionParser.parse(args);
        Obo obo = loadXMLFile();

        PrintStream termStream = System.out;
        if( optionParser.isSet(Opt.termfile)) {
            termStream = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.termfile)));
        }
        PrintStream t2tStream = System.out;
        if( optionParser.isSet(Opt.t2tfile)) {
            t2tStream = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.t2tfile)));
        }
        PrintStream graphStream = System.out;
        if( optionParser.isSet(Opt.graphfile)) {
            graphStream = new PrintStream(new FileOutputStream(optionParser.getValue(Opt.graphfile)));
        }
        
        Term[] terms = obo.getTerm();
        for(Term t: terms) {
            if( t.getDef() == null ) {
                termStream.println(join("\t", t.getId().toString(), t.getName(),  t.getNamespace(), ""));
            } else {
                termStream.println(join("\t", t.getId().toString(), t.getName(),  t.getNamespace(), t.getDef().getDefstr()));
            }
        }
        HashMap<String, TreeSet<String>> term2termHash = new HashMap<String, TreeSet<String>>(); 
        for(Term t: terms) {
            String[] is_a = t.getIs_a();
            for(String parent: is_a ) {
                t2tStream.println(join("\t", "is_a", parent, t.getId()));
                putparent(term2termHash, t.getId(), parent);
            }
            Relationship[] rel = t.getRelationship();
            for(Relationship r: rel) {
                if( r.getType().equals("part_of")) {
                    t2tStream.println(join("\t", r.getType(), r.getTo(), t.getId()));
                    putparent(term2termHash, t.getId(), r.getTo());
                } else {
                    throw new SCMDException("Illigal format on " + t.getId() + " with type " + r.getType());
                }
            }
        }
        
        for(Term t: terms) {
            HashMap<String, Integer> pathmap = new HashMap<String, Integer> ();
//            drawgraph(graphStream, term2termHash, t.getId(), t.getId(), 0);
          pathmap = addgraph(pathmap, term2termHash, t.getId(), t.getId(), 0);
            for(String key: pathmap.keySet()) {
                graphStream.println(join("\t", key, t.getId(), pathmap.get(key).toString()));
            }
        }
    }

    // deplicated
    private void drawgraph(PrintStream out, HashMap<String, 
            TreeSet<String>> termMap, String parent, String id, int depth) {
        if (parent == null) {
            // add root node
            String rootid = "GO:root";
            out.println(join("\t", rootid, id, depth + ""));
            return;
        }
        out.println(join("\t", parent, id, depth + ""));
        if (!termMap.containsKey(parent)) {
            // add root node
            drawgraph(out, termMap, null, id, depth + 1);
            return;
        }
        TreeSet<String> ts = termMap.get(parent);
        for (String s : ts) {
            drawgraph(out, termMap, s, id, depth + 1);
        }
        return;
    }
    
  /**
  * If the term has differnt paths to root,
  * depth indicates the shortest path.
  * @param id
  * @param parent
  * @param depth
  */
    private HashMap<String,Integer> addgraph(HashMap<String,Integer> pathmap, 
            HashMap<String, TreeSet<String>> termMap, String parent, String id, int depth) {
        if( parent == null ) {
            // add root node
            String rootid = "GO:root";
            if( pathmap.containsKey(rootid)) {
                if ( pathmap.get(rootid) > depth )
                    pathmap.put(rootid, depth);
            } else {
                pathmap.put(rootid, depth);
            }
            return pathmap;
        }
        if( pathmap.containsKey(parent)) {
            if ( pathmap.get(parent) > depth )
                pathmap.put(parent, depth);
        } else {
            pathmap.put(parent, depth);
        }
        
//        out.println(join("\t", parent, id, depth + ""));
        if(!termMap.containsKey(parent)) {
            // add root node
            addgraph(pathmap, termMap, null, id, depth + 1);
            return pathmap;
        }
        TreeSet<String> ts = termMap.get(parent);
        for(String s: ts) {
            addgraph(pathmap, termMap, s, id, depth + 1);
        }
        return pathmap;
    }

    /**
     * @param term2termHash
     * @param id
     * @param parent
     */
    private void putparent(HashMap<String, TreeSet<String>> term2termHash, String id, String parent) {
        TreeSet<String> ts = null;
        if( term2termHash.containsKey(id) ) {
            ts = term2termHash.get(id);
        } else {
            ts = new TreeSet<String>();
            term2termHash.put(id, ts);
        }
        ts.add(parent);
    }

    private String join(String delimiter, String... args) {
        String str = "";
        str = args[0];
        for(int i = 1; i < args.length; i++ ) {
            str += delimiter + args[i];
        }
        return str;
    }

    /**
     * @return
     */
    private Obo loadXMLFile() {
        Obo obo = null;
        String xmlfile = optionParser.getArgument(0);
        try {
            obo = XMLBeanUtil.newInstance(Obo.class, new FileInputStream(xmlfile));
        } catch (XMLParserException e) {
            e.printStackTrace();
        } catch (XMLBeanException e) {
            e.printStackTrace();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obo;
    }
}
