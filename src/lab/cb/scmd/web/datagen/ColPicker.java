//--------------------------------------
// SCMDServer
// 
// ColPicker.java 
// Since: 2004/09/10
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.datagen;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import lab.cb.scmd.algorithm.Algorithm;
import lab.cb.scmd.algorithm.RegexPredicate;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.cui.Option;
import lab.cb.scmd.util.cui.OptionParser;
import lab.cb.scmd.util.cui.OptionWithArgument;
import lab.cb.scmd.util.io.NullPrintStream;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

/**
 * @author leo
 *
 */
public class ColPicker
{
    OptionParser _option = new OptionParser();
    final static int OPT_HELP = 0;
    final static int OPT_VERBOSE = 1;
    final static int OPT_OUTFILE = 2;
    final static int OPT_REGEX = 3;
    final static int OPT_INPUTFILE = 4;
    PrintStream _log = new NullPrintStream();
    
    /**
     * 
     */
    public ColPicker()
    {
        
    }
    
    public static void main(String[] arg)
    {
        ColPicker instance = new ColPicker();
        try
        {
            instance.setUpByArgument(arg);
                        
            instance.pick();
        }
        catch(SCMDException e)
        {
            e.what();
        }
    }
    
    public void setUpByArgument(String[] arg) throws SCMDException
    {
        _option.setOption(new Option(OPT_HELP, "h", "help", "print help message"));
        _option.setOption(new Option(OPT_VERBOSE, "v", "verbose", "print verbose messages"));
        _option.setOption(new OptionWithArgument(OPT_OUTFILE, "o", "output", "FILE", "output file name"));
        _option.setOption(new OptionWithArgument(OPT_INPUTFILE, "i", "input", "FILE", "input file name"));
        _option.setOption(new OptionWithArgument(OPT_REGEX, "e", "regex", "REGEX", "use regular expression to pick target col labels"));
        
        _option.getContext(arg);
        
        if(_option.isSet(OPT_HELP))
        {
            PrintHelpMessage();
            throw new SCMDException(""); // finish
        }
        
        if(_option.isSet(OPT_VERBOSE))
            _log = System.err;
        
    }

    public void pick() throws SCMDException
    {
        Table inputTable = null;
        _log.println("reading table...");
        if(_option.isSet(OPT_INPUTFILE))
        {
            _log.println("read from file: " + _option.getValue(OPT_INPUTFILE));
            inputTable = new Table(_option.getValue(OPT_INPUTFILE));
        }
        else
        {
            _log.println("read from stdin");
            inputTable = new Table(System.in);
            _log.println("rows :=" + inputTable.getRowSize());
        }
        ColLabelIndex colLabelIndex = new ColLabelIndex(inputTable);
        
        LinkedHashSet colLabelSet = new LinkedHashSet();
        if(_option.isSet(OPT_REGEX))
        {
            Collection colNameList = colLabelIndex.getColLabelList();
            Algorithm.select(colNameList, colLabelSet, new RegexPredicate(_option.getValue(OPT_REGEX)));
        }
        else
        {
            List colNameList = _option.getArgumentList();
            colLabelSet.addAll(colNameList);
        }
        _log.println("col list: " + colLabelSet.toString());
        
        int[] colIndex = new int[colLabelSet.size()];
        int index = 0; 
        for (Iterator it = colLabelSet.iterator(); it.hasNext();)
        {
            String label = (String) it.next();
            int ci = colLabelIndex.getColIndex(label);
            if(ci == -1)
                throw new SCMDException("collabel: " + label + " is not found");
            colIndex[index++] = ci;
        }
        
        OutputStream out = null;
        if(_option.isSet(OPT_OUTFILE))
        {
            try
            {
                out = new FileOutputStream(_option.getValue(OPT_OUTFILE));
            }
            catch(FileNotFoundException e)
            {
                throw new SCMDException(e);
            }
        }
        else
            out = System.out;
        
        for(int row=0; row<inputTable.getRowSize(); row++)
        {
            Table rowTable = new Table(1, colIndex.length);
            for(int i=0; i<colIndex.length; i++)
            {
                int col = colIndex[i];
                rowTable.set(0, i, inputTable.get(row, col));
            }
            rowTable.output(out);
        }
        
    }
    
    public void PrintHelpMessage()
    {
        System.out.println("> java -jar ColPicker.jar [option] (colname ...)");
        System.out.println(_option.createHelpMessage());
    }
}


//--------------------------------------
// $Log: ColPicker.java,v $
// Revision 1.1  2004/09/10 08:32:43  leo
// ColPicker‚ð’Ç‰Á
//
//--------------------------------------