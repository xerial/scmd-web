//-----------------------------------
// SCMDWeb Project
// 
// TeardropComposition.java 
// Since: 2005/03/15
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.image.teaddrop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import lab.cb.common.cui.OptionParser;
import lab.cb.common.cui.OptionParserException;
import lab.cb.scmd.algorithm.RegexPredicate;
import lab.cb.scmd.exception.SCMDException;

/**
 * ïœàŸäîÇ∆ñÏê∂äîÇÃteardropÇÇ≠Ç¡Ç¬ÇØÇÈÉvÉçÉOÉâÉÄ
 * @author leo
 *
 */
public class TeardropComposition
{
    enum Opt { help, mutantDir, wilddtypeDir, outDir, mutantColor, wildtypeColor } 
    OptionParser<Opt> _opt = new OptionParser<Opt>();    
    
    public static void main(String[] arg) throws OptionParserException
    {
        TeardropComposition instance = new TeardropComposition();
        instance.process(arg);        
    }

    public TeardropComposition() throws OptionParserException
    {
        _opt.addOption(Opt.help, "h", "help", "display help message");
        _opt.addOptionWithArgment(Opt.mutantDir, "m", "mutant", "DIR", "mutant teardrop directory (default=paramstats)", "paramstats");
        _opt.addOptionWithArgment(Opt.wilddtypeDir, "w", "wildtype", "DIR", "wildtype teardrop directory (default=wildtype)", "wildtype");
        _opt.addOptionWithArgment(Opt.outDir, "o", "out", "DIR", "output directory (default=composite)", "composite");
    }
    
    public void process(String[] arg)
    {
        try
        {
            _opt.parse(arg);
            if(_opt.isSet(Opt.help))
            {
                printHelpMessage();
                return;
            }
            
            File mutantDir = new File(_opt.getValue(Opt.mutantDir));
            File wildtypeDir = new File(_opt.getValue(Opt.wilddtypeDir));
            if(!mutantDir.isDirectory())
                throw new SCMDException(mutantDir.getName() + " is not a directory");
            if(!wildtypeDir.isDirectory())
                throw new SCMDException(wildtypeDir.getName() + " is not a directory");
            
            File outDir = new File(_opt.getValue(Opt.outDir));
            if(!outDir.exists())
            {
                if(!outDir.mkdirs())
                    throw new SCMDException("cannot create a directory: " + outDir.getPath());
            }
            if(!outDir.isDirectory())
                throw new SCMDException(outDir.getName() + " is not a directory");
                
            class PNGFilter implements FilenameFilter
            {
                private RegexPredicate p = new RegexPredicate("td_[0-9]+_[0-9]+\\.png");
                public boolean accept(File dir, String name)
                {                    
                    return p.isTrue(name);
                }
            }

            Set<String> mutantFileSet = createFileSet(mutantDir.listFiles(new PNGFilter()));
            Set<String> wildtypeFileSet = createFileSet(wildtypeDir.listFiles(new PNGFilter()));
            for(String tdFile : mutantFileSet)
            {
                if(!wildtypeFileSet.contains(tdFile))
                    continue;
                
                try
                {
                    System.out.printf("processing " + tdFile + "      \r");
                    BufferedImage a = ImageIO.read(new File(mutantDir, tdFile));
                    BufferedImage b = ImageIO.read(new File(wildtypeDir, tdFile));
                    
                    BufferedImage compositeImage = Teardrop.composite(a, b);
                    ImageIO.write(compositeImage, "png", new File(outDir, tdFile));
                }
                catch(IOException e)
                {
                    System.err.println(e.getMessage());
                }
            }
            
            
        }
        catch(OptionParserException e)
        {
            System.err.println(e);
            printHelpMessage();
        }
        catch(SCMDException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    private void printHelpMessage()
    {
        System.out.println("> TeardropComposition [option]");
        System.out.println(_opt.helpMessage());
    }
    
    private Set<String> createFileSet(File[] files)
    {
        TreeSet<String> s = new TreeSet<String>();
        for(File f : files)
            s.add(f.getName());
        return s;
    }
}
