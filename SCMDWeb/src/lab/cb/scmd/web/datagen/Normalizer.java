//--------------------------------------
// SCMDServer
// 
// Normalizer.java 
// Since: 2004/08/27
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.datagen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.cui.Option;
import lab.cb.scmd.util.cui.OptionParser;
import lab.cb.scmd.util.cui.OptionWithArgument;
import lab.cb.scmd.util.io.NullPrintStream;
import lab.cb.scmd.util.stat.Statistics;
import lab.cb.scmd.util.stat.StatisticsWithMissingValueSupport;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.Table;

/** 平均０、分散１に標準化するためのクラス 
 * 大きいテーブルだとメモリが足りなくなるので、-Xmx512Mなどのoptionを忘れずに
 * @author leo
 *
 */
public class Normalizer
{
    OptionParser _option = new OptionParser();
    final static int OPT_HELP = 0;
    final static int OPT_VERBOSE = 1;
    final static int OPT_OUTFILE = 2;
    
    
    PrintStream _log = new NullPrintStream();
    /**
     * 
     */
    public Normalizer()
    {
        super();
    }

    public static void main(String[] arg)
    {
        Normalizer normalizer = new Normalizer();
        normalizer.execute(arg);
    }
    
    
    public void execute(String[] arg)
    {	
        try
        {
            _option.setOption(new Option(OPT_HELP, "h", "help", "print help message"));
            _option.setOption(new Option(OPT_VERBOSE, "v", "verbose", "print verbose messages"));
            _option.setOption(new OptionWithArgument(OPT_OUTFILE, "o", "output", "FILE", "output file name"));
            _option.setRequirementForNonOptionArgument();
            
            _option.getContext(arg);
            
            if(_option.isSet(OPT_HELP))
            {
                printHelpMessage();
                return;
            }
                
            if(_option.isSet(OPT_VERBOSE))
            {
                _log = System.out;
            }
                
            List inputFileList = _option.getArgumentList();
            for (Iterator it = inputFileList.iterator(); it.hasNext();)
            {
                String file = (String) it.next();
                _log.println("input file: " + file);
                
                Table inputTable = new Table(file);
                Table[] result = createNormalizedTable(inputTable, _log);
                
                String outputFile;
                if(_option.isSet(OPT_OUTFILE))
                {
                    outputFile = _option.getValue(OPT_OUTFILE);
                }
                else
                    outputFile = "z-" + file;
                _log.println("output file: " + outputFile);
                
                FileOutputStream out = new FileOutputStream(outputFile);
                result[0].output(out);
                out.close();
                
                String statFile = "stat-" + file;
                _log.println("stat file: " + statFile);
                FileOutputStream statOut = new FileOutputStream(statFile);
                result[1].output(statOut);
                statOut.close();
                
            }
        }
        catch(SCMDException e)
        {
            e.what(System.err);
            printHelpMessage();
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    /** 一行目に、パラメータ名 、一列目に、個体名があるテーブルを仮定して、
     * パラメータの値を標準化する
     * @param sourceTable
     * @return z-valueTable, statTable
     */
    public static Table[] createNormalizedTable(Table sourceTable, PrintStream log)
    {
        Statistics stat = new StatisticsWithMissingValueSupport(new String[] {".", "0", "0.0"});
        
        Table resultTable = new Table(sourceTable.getRowSize(), sourceTable.getColSize());
        Table statTable = new Table();

        // 一行、一列目をコピー
        resultTable.paste(0, 0, sourceTable.getRow(0));
        resultTable.paste(0, 0, sourceTable.getCol(0));
        
        // 行、parameter, 列、 (average, SD)
        statTable.addRow(new String[] {"param", "average", "SD", "min", "max"});
                
        // for each parameter(col)
        ColLabelIndex colLabelIndex = new ColLabelIndex(sourceTable);
        Collection colLabel = colLabelIndex.getColLabelList();
        Iterator it = colLabel.iterator();
        for (it.next(); it.hasNext();)
        {
            String param = (String) it.next();
            log.println("[param] " + param);
            TableIterator ti = colLabelIndex.getVerticalIterator(param);            
            double average = stat.calcAverage((TableIterator) ti.clone());
            log.println("average:\t" + average);
            double SD = stat.calcSD((TableIterator) ti.clone());
            log.println("SD     :\t" + SD);
            double min = stat.getMinValue((TableIterator) ti.clone());
            log.println("min     :\t" + min);
            double max = stat.getMaxValue((TableIterator) ti.clone());
            log.println("max     :\t" + max);
            
            statTable.addRow(new String[] {param, Double.toString(average), Double.toString(SD), Double.toString(min), Double.toString(max)});
            
            // for each item(row)
            //int colIndex = colLabelIndex.getColIndex(param);
            for(; ti.hasNext(); )
            {
                double value = ti.nextCell().doubleValue();
                double zvalue = (value - average) / SD; 
                resultTable.set(ti.row(), ti.col(), new Double(zvalue));
            }
        }
        
        return new Table[] {resultTable, statTable};
    }
    
    void printHelpMessage()
    {
        System.out.println("java -jar Normalizer.jar [input table file] ...");
        System.out.println(_option.createHelpMessage());
    }
}


//--------------------------------------
// $Log: Normalizer.java,v $
// Revision 1.3  2004/09/01 06:39:59  leo
// TearDropViewを追加
//
// Revision 1.2  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.1  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
//--------------------------------------