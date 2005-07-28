package lab.cb.scmd.db.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import lab.cb.scmd.autoanalysis.grouping.AttributePosition;
import lab.cb.scmd.autoanalysis.grouping.CalMorphTable;
import lab.cb.scmd.autoanalysis.grouping.TableSchema;
import lab.cb.scmd.autoanalysis.grouping.TableTypeServer;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.cui.Option;
import lab.cb.scmd.util.cui.OptionParser;
import lab.cb.scmd.util.cui.OptionWithArgument;
import lab.cb.scmd.util.io.NullPrintStream;

// DB上のindividual* を作成する

class MakeIndividualTable {

	OptionParser		_optParser			= new OptionParser();
	ClassifierSetting	_settings			= new ClassifierSetting();
	PrintStream			_log				= new NullPrintStream();
	boolean				_autoSeach			= false;
	String				_baseDir			= ".";

	final int			OPT_HELP			= 0;
	final int			OPT_VERBOSE			= 1;
	final int			OPT_SETTING_FILE	= 2;
	final int			OPT_OUTPUTDIR		= 3;
	final int			OPT_AUTOSEARCH		= 4;
	final int			OPT_BASEDIR			= 5;
	
	String				_outputFileName		= "individualcells.tab";
    int 				_count 				= 0;
	
	public MakeIndividualTable()
	{
		TableTypeServer.Initialize();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// initialize
		MakeIndividualTable mit = new MakeIndividualTable();
		LinkedList inputFileList = mit.setupByArguments(args);
		mit.constructTable(inputFileList);
	}

	/**
	 * @param args
	 *            コマンドライン引数
	 * @return 入力ファイルのStringリスト
	 */
	public LinkedList setupByArguments(String[] args)
	{
		LinkedList inputFileList = new LinkedList();
		try
		{
			_optParser.setOption(new Option(OPT_HELP, "h", "help", "print help message"));
			_optParser.setOption(new Option(OPT_VERBOSE, "v", "verbose", "print some verbose messages"));
			_optParser
					.setOption(new OptionWithArgument(OPT_SETTING_FILE, "s", "", "SETTINGFILE", "read a setting file"));
			_optParser
					.setOption(new OptionWithArgument(OPT_OUTPUTDIR, "o", "outdir", "DIR", "specify output directroy"));
			//_optParser.setRequirementForNonOptionArgument();
			_optParser.setOption(new Option(OPT_AUTOSEARCH, "a", "auto", "auto search orf directories"));
			_optParser.setOption(new OptionWithArgument(OPT_BASEDIR, "b", "basedir", "DIR", "set input directory base"));
			if(args.length < 1)
				printUsage(1);

			_optParser.getContext(args);

			if(_optParser.isSet(OPT_HELP))
				printUsage(0);

			// setup
			if(_optParser.isSet(OPT_VERBOSE))
				_log = System.out;
			if(_optParser.isSet(OPT_SETTING_FILE))
			{
				String settingFile = _optParser.getValue(OPT_SETTING_FILE);
				_settings.loadSettings(settingFile);
			}
			if(_optParser.isSet(OPT_OUTPUTDIR))
				_settings.setProperty("OUTPUT_DIR", _optParser.getValue(OPT_OUTPUTDIR));
			if(_optParser.isSet(OPT_BASEDIR))
				_baseDir = _optParser.getValue(OPT_BASEDIR);

			if(_optParser.isSet(OPT_AUTOSEARCH))
			{
				// search orf directories
				File baseDir = new File(_baseDir);
				if(!baseDir.isDirectory())
					throw new SCMDException("base directory " + _baseDir + " doesn't exist");
				File[] file = baseDir.listFiles();
				for (int i = 0; i < file.length; i++)
				{
					if(!file[i].isDirectory())
						continue; // skip non directory files

					String orfName = file[i].getName();
					inputFileList.add(orfName);
				}
			}
			else
			{
				inputFileList = _optParser.getArgumentList();
				if(inputFileList.size() < 1)
					printUsage(1);
			}
		}
		catch (SCMDException e)
		{
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return inputFileList;
	}

    public void constructTable(LinkedList inputFileList)
	{
		try
		{
			final String SEP = File.separator;
			String outDirBase = _settings.getProperty("OUTPUT_DIR");
			outDirBase = outDirBase != null ? outDirBase + SEP : "";
			final int TABLE_A = 0;
			TableSchema individualTable = new TableSchema("individual.txt");

			// open output files
			PrintWriter outFile = new PrintWriter(new FileWriter(_outputFileName));

			// output labels
			individualTable.outputLabel(outFile);

			for (Iterator fi = inputFileList.iterator(); fi.hasNext();)
			{
				String inputDir = _baseDir + File.separator + (String) fi.next();
				String orf = (new File(inputDir)).getName();
				_log.println("input directory: " + inputDir);
				_log.println("orf: " + orf);

				TableMap tableMap = new TableMap();
				String tableFileName = ""; 
				try
				{
					// load tables (yor202w.xls, yor202w_conA_basic.xls, etc.)
					// and
					// add them to the tableMap
					for (int i = 0; i < TableTypeServer.getTypeMax(); i++)
					{
						//String telm = TableTypeServer.getTableElement(i).getFileSuffix();
					    String telm = TableTypeServer.getTableSuffix(i);
						tableFileName = inputDir + SEP + orf + telm;
						CalMorphTable table = new CalMorphTable(tableFileName);
						tableMap.addTable(i, table);
					}
				}
				catch (SCMDException e)
				{
					// cannot read some table file correctly
					_log.println("directory " + inputDir + " is skipped because the file " + tableFileName + " doesn't exist");
					continue;
				}

				// output contents
				CalMorphTable orfTable = tableMap.getTable(0);
				for (int row = 0; row < orfTable.getRowSize(); row++)
				{
					outputRow(row, tableMap, individualTable, outFile, orf);
				}
			}
			outFile.close();
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
		catch (SCMDException e)
		{
			System.err.println(e.getMessage());
		}
	}

	private void outputRow(int row, TableMap sourceTableMap, TableSchema schema, PrintWriter outputTable, String orf)
			throws SCMDException
	{
	    _count++;
		int colMax = schema.numRule();
//		outputSingleAttribute(row, 0, sourceTableMap, schema, outputTable);
//		outputTable.print(_count + "\t");
		
//		for (int col = 1; col < colMax ; col++)
//		outputTable.print(_count + "\t" + _strainMap.get(orf.toUpperCase()) + "\t");
		outputTable.print(orf.toUpperCase() + "\t");
		
		for (int col = 0; col < colMax - 1; col++)
		{
			if( outputSingleAttribute(row, col, sourceTableMap, schema, outputTable) ) {
				outputTable.print("\t");
			}
		}
		outputSingleAttribute(row, colMax - 1, sourceTableMap, schema, outputTable);
		outputTable.println();
	}

	private boolean outputSingleAttribute(int row, int col, TableMap sourceTableMap, TableSchema schema,
			PrintWriter outputTable) throws SCMDException
	{
		AttributePosition attribPos = new AttributePosition(-1, "not defined");
		try
		{
			attribPos = schema.getAttributePosition(col);
			int tableType = attribPos.getTableType();
			if( tableType == 7 )
			    return false;
			CalMorphTable sourceTable = sourceTableMap.getTable(attribPos.getTableType());
			outputTable.print(sourceTable.getCellData(row, attribPos.getAttributeName()));
		}
		catch (SCMDException e)
		{
			System.err.println("error occured while reading "
					+ TableTypeServer.getTableTypeName(attribPos.getTableType()));
			System.err.println("attrib=" + attribPos.getAttributeName() + " row=" + row + " col=" + col);
			throw e;
		}
		return true;
	}

	void printUsage(int exitCode)
	{
		System.out.println("Usage: > java -jar MakeIndividualTable.jar [options] orf_directory");
		System.out.println(_optParser.createHelpMessage());
		System.exit(exitCode);
	}
}

class ClassifierSetting extends Properties
{

	public ClassifierSetting()
	{
		super();
	}

	public void loadSettings(String settingFileName)
	{
		try
		{
			FileInputStream fin = new FileInputStream(settingFileName);
			this.load(fin);
			fin.close();
		}
		catch (IOException e)
		{
			System.err.println("error occured while loading " + settingFileName);
			System.err.println(e.getMessage());
		}
	}

}

class TableMap
{

	public TableMap()
	{}

	public void addTable(int tableType, CalMorphTable table)
	{
		_tableMap.put(new Integer(tableType), table);
	}

	public CalMorphTable getTable(int tableType) throws SCMDException
	{
		CalMorphTable table = (CalMorphTable) _tableMap.get(new Integer(tableType));
		if(table == null)
			throw new SCMDException("cannot find table corresponding to tableType = " + tableType);
		return table;
	}

	HashMap	_tableMap	= new HashMap();
}

