//--------------------------------------
// SCMDServer
// 
// Table.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


import lab.cb.scmd.algorithm.Algorithm;
import lab.cb.scmd.algorithm.Functor;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.TableIterator;
import lab.cb.scmd.web.table.decollation.Decollator;

/**
 * 長方形のテーブルを与えるクラス （行、列は追加可能） 幅や行が異なるデータを追加するたびにテーブルが拡大されていく。
 * 空白要素には、常にEmptyElementが入っている状態になっている
 * 
 * @author leo
 *  
 */

public class Table implements TableElement
{
    protected static EmptyElement _emptyElement    = new EmptyElement();
    private Properties            _properties      = new Properties();
    private LinkedList            _updateObservers = new LinkedList();

    /**
     *  
     */
    public Table()
    {}

    public Table(int rowSize, int colSize)
    {
        expandColSize(colSize);
        expandRowSize(rowSize);
    }
    
    public Table(String fileName) throws SCMDException
    {
        loadFromFile(new File(fileName));
    }

    public Table(InputStream input) throws SCMDException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        load(reader);
    }
    
    public TableIterator getHorizontalIterator(int row)
    {
        return new HorizontalIterator(this, row);
    }
    public TableIterator getVerticalIterator(int col)
    {
        return new VerticalIterator(this, col);
    }
    
    
    public void loadFromFile(File file) throws SCMDException
    {
        try
        {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            load(fileReader);
        }
        catch(IOException e)
        {
            throw new SCMDException("error occured while reading " + file + "\n" + e.getMessage());
        }
    }
    
    /** タブ区切りのファイルを読んで、テーブルを埋める
     * @param file
     * @throws SCMDException
     */
    protected void load(BufferedReader reader) throws SCMDException {
        // TODO  observerへの通知 （初期化時のみにこのメソッドを使うなら、addRowから通知されるので問題なし）
        
        String DELIMITER = "\t";
        // load from tab-delimited file
        try
        {
            String line = "";
            int curcolumn = 0;

            // read data
            while ((line = reader.readLine()) != null)
            {
                StringTokenizer dataTokenizer = new StringTokenizer(line, DELIMITER);
                String[] tokens = line.split(DELIMITER);

                Vector row = new Vector();
                int columnno = 0;
                for (int i = columnno; i < tokens.length; i++)
                {
                    row.add(tokens[i]);
                }
                while (row.size() < _colSize)
                {
                    row.add(_emptyElement);
                }
                addRow(row);
            }
            reader.close();
        }
        catch (IOException e)
        {
            throw new SCMDException(e);
        }

    }
    
    /** タブ区切りのテーブルに変換して出力する
     * @param outputStream
     */
    public void output(OutputStream outputStream)
    {
        PrintStream out = new PrintStream(outputStream); 
        for(int row=0; row<getRowSize(); row++)
        {
            for(int col=0; col<getColSize()-1; col++)
            {
                out.print(get(row, col).toString());
                out.print("\t");
            }
            if(getColSize() >= 1)
                out.print(get(row, getColSize()-1));
            out.println();
        }
        out.flush();
    }

    protected void addObserver(UpdateObserver observer) {
        _updateObservers.add(observer);
    }

    public void removeAllObservers() {
        _updateObservers.clear();
    }

    protected void removeObserver(UpdateObserver targetObserver) {
        _updateObservers.remove(targetObserver);
    }
    
    public Table getRow(int row)
    {
        Table table = new Table();
        table.addRow((Vector) _rows.get(row));
        return table;
    }
    
    public Table getRow(int row, int colBegin)
    {
        return getRow(row, colBegin, getColSize() - 1);
    }
    public Table getRow(int row, int colBegin, int colEnd)
    {
        Table table = new Table();
        Vector line = (Vector) _rows.get(row);
        table.addRow(line.subList(colBegin, colEnd));
        return table;
    }
    public Object[] getRowArray(int row)
    {
        Vector line = (Vector) _rows.get(row);
        return line.toArray();
    }
    public List getRowList(int row)
    {
        return (Vector) _rows.get(row);
    }
    public List getColList(int col)
    {
        Vector colList = new Vector(getRowSize());
        for(int row=0; row<getRowSize(); row++)
        {
            colList.add(get(row, col));
        }
        return colList;
    }
    
    public Table getCol(int col)
    {
        Table table = new Table();
        Vector colList = new Vector(getColSize());
        for(int i=0; i<getRowSize(); i++)
        {
            colList.add(get(i, col));
        }
        table.addCol(colList);
        return table;
    }

    public TableElement get(int row, int col) throws ArrayIndexOutOfBoundsException {
        if(!withinTableRange(row, col)) throw new ArrayIndexOutOfBoundsException("(" + row + ", " + col + ")");
        return (TableElement) ((Vector) _rows.get(row)).get(col);
    }

    /**
     * テーブルにTableComponentをセットする。
     * 行列の範囲を超えて挿入した場合はArrayIndexOutOfBoundsExceptionが発生
     * 
     * @param row
     * @param col
     * @param component
     */
    public void set(int row, int col, TableElement component) throws ArrayIndexOutOfBoundsException {
        if(!withinTableRange(row, col)) throw new ArrayIndexOutOfBoundsException("(" + row + ", " + col + ")");

        Vector cols = (Vector) _rows.get(row);
        Algorithm.foreach(_updateObservers, new NotifyBeforeUpdate(this, row, col, component));
        cols.set(col, component);
        Algorithm.foreach(_updateObservers, new NotifyAfterUpdate(this, row, col, component));
    }

    public void paste(int row, int col, Table table) {
        int newRowSize = row + table.getRowSize();
        int newColSize = col + table.getColSize();
        expandTableSize(newRowSize, newColSize);
        for (int i = 0; i < table.getRowSize(); i++)
        {
            for (int j = 0; j < table.getColSize(); j++)
            {
                set(row + i, col + j, table.get(i, j));
            }
        }
    }
    
    public void appendToRight(Table table) {
        paste(0, getColSize(), table);
    }

    public void appendToBottom(Table table) {
        paste(getRowSize(), 0, table);
    }
    
    public void appendToTop(Table table)
    {
        // TODO optimize
        for(int row=0; row<table.getRowSize(); row++)
        {
            insertRow(0, table.getRowList(row));
        }
    }
    
    public int getRowSize() {
        return _rows.size();
    }

    public int getColSize() {
        return _colSize;
    }

    protected boolean withinTableRange(int row, int col) {
        return (row >= 0 && row < getRowSize()) && (col >= 0 && col < getColSize());
    }

    /**
     * elemがableElementの要素ならそのまま返し、それ以外なら、elem.toStringの結果をStringElementに格納して返す
     * 
     * @param elem
     * @return
     */
    public static TableElement translate(Object elem) {
        if(TableElement.class.isAssignableFrom(elem.getClass()))
        {
            return (TableElement) elem;
        }
        else
        {
            return new StringElement(elem.toString());
        }
    }

    public void addRow(Collection elementList) {
        addRow(elementList.toArray());
    }

    public void addRow(Object[] element) {

        int rowNum = getRowSize();
        Algorithm.foreach(_updateObservers, new NotifyBeforeRowAddition(this, rowNum, element));

        expandColSize(element.length);
        Vector row = new Vector(element.length);
        for (int i = 0; i < element.length; i++)
        {
            row.add(translate(element[i]));
        }
        for (int i = element.length; i < _colSize; i++)
        {
            row.add(_emptyElement);
        }
        _rows.add(row);

        Algorithm.foreach(_updateObservers, new NotifyAfterRowAddition(this, rowNum, element));
    }

    public void addCol(Collection elementList) {
        addCol(elementList.toArray());
    }

    public void addCol(Object[] element) {
        int colNum = getColSize();
        Algorithm.foreach(_updateObservers, new NotifyBeforeColAddition(this, colNum, element));

        expandRowSize(element.length);

        for (int targetRowNum = 0; targetRowNum < _rows.size(); targetRowNum++)
        {
            Vector row = (Vector) _rows.get(targetRowNum);
            row.add(targetRowNum < element.length ? translate(element[targetRowNum]) : _emptyElement);
        }
        _colSize++;
        Algorithm.foreach(_updateObservers, new NotifyAfterColAddition(this, colNum, element));
    }

    public void insertRow(int row, Collection elementList) {
        insertRow(row, elementList.toArray());
    }

    public void insertRow(int row, Object[] element) {
        Algorithm.foreach(_updateObservers, new NotifyBeforeRowInsertion(this, row, element));

        expandColSize(element.length);
        Vector rowLine = new Vector(element.length);
        for (int i = 0; i < element.length; i++)
        {
            rowLine.add(translate(element[i]));
        }
        for (int i = element.length; i < _colSize; i++)
        {
            rowLine.add(_emptyElement);
        }
        _rows.insertElementAt(rowLine, row);

        Algorithm.foreach(_updateObservers, new NotifyAfterRowInsertion(this, row, element));
    }

    public void insertCol(int col, Collection elementList) {
        insertCol(col, elementList.toArray());
    }

    public void insertCol(int col, Object[] element) {
        Algorithm.foreach(_updateObservers, new NotifyBeforeColInsertion(this, col, element));
        expandRowSize(element.length);

        for (int targetRowNum = 0; targetRowNum < _rows.size(); targetRowNum++)
        {
            Vector row = (Vector) _rows.get(targetRowNum);
            row.add(col, targetRowNum < element.length ? translate(element[targetRowNum]) : _emptyElement);
        }

        Algorithm.foreach(_updateObservers, new NotifyAfterColInsertion(this, col, element));
    }

    public void removeRow(int row) {
        Algorithm.foreach(_updateObservers, new NotifyBeforeRowDeletion(this, row));
        _rows.remove(row);
        Algorithm.foreach(_updateObservers, new NotifyAfterRowDeletion(this, row));
    }

    public void removeCol(int col) {
        Algorithm.foreach(_updateObservers, new NotifyBeforeColDeletion(this, col));
        for (int row = 0; row < _rows.size(); row++)
        {
            Vector rowLine = (Vector) _rows.get(row);
            rowLine.remove(col);
        }
        _colSize--;
        Algorithm.foreach(_updateObservers, new NotifyAfterColDeletion(this, col));
    }

    public void expandRowSize(int newRowSize) {
        if(newRowSize < _rows.size()) return; // do nothing

        int numRowsToAdd = newRowSize - _rows.size();
        for (int i = 0; i < numRowsToAdd; i++)
        {
            Vector newRow = new Vector(_colSize);
            for (int k = 0; k < _colSize; k++)
                newRow.add(_emptyElement);
            _rows.add(newRow);
        }
    }

    public void expandColSize(int newColSize) {
        if(newColSize < _colSize) return; // do nothing
        int numColsToAdd = newColSize - _colSize;
        for (Iterator it = _rows.iterator(); it.hasNext();)
        {
            Vector row = (Vector) it.next();
            for (int i = 0; i < numColsToAdd; i++)
            {
                row.add(_emptyElement);
            }
        }
        _colSize = newColSize;
    }

    public void expandTableSize(int newRowSize, int newColSize) {
        expandColSize(newColSize);
        expandRowSize(newRowSize);
    }

    public void accept(TableVisitor v) {
        v.visit(this);
    }



    public Properties getProperties() {
        return _properties;
    }

    public void setProperty(String property, String value) {
        _properties.setProperty(property, value);
    }

    public Properties getCellProperty() {
        return new Properties();
    }

    public void decollateCol(int col, Decollator decollator) {
        for (int row = 0; row < getRowSize(); row++)
        {
            set(row, col, decollator.decollate(get(row, col)));
        }
    }

    public void decollateRow(int row, Decollator decollator) {
        for (int col = 0; col < getColSize(); col++)
        {
            set(row, col, decollator.decollate(get(row, col)));
        }
    }

    public TableCursor cursor() {
        return new TableCursorImpl(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.web.table.AbstractTable#decorate(int, int,
     *      lab.cb.scmd.web.table.Decollator)
     */
    public void decollate(int row, int col, Decollator decollator) {
        set(row, col, decollator.decollate(get(row, col)));
    }

    Vector _rows    = new Vector();

    int    _colSize = 0;

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.web.table.AbstractTable#decorate(lab.cb.scmd.util.image.BoundingRectangle,
     *      lab.cb.scmd.web.table.Decollator)
     */
    public void decollate(TableRange region, Decollator decollator) {
        for (int row = region.getRowBegin(); row <= region.getRowEnd(); row++)
        {
            for (int col = region.getColBegin(); col <= region.getColEnd(); col++)
            {
                decollate(row, col, decollator);
            }
        }
    }

    public void setProperties(Properties properties) {
        _properties = properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see lab.cb.scmd.web.table.AbstractTable#set(int, int, java.lang.Object)
     */
    public void set(int row, int col, Object obj) throws ArrayIndexOutOfBoundsException {
        set(row, col, new StringElement(obj.toString()));
    }

    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowColIndexSupport#get(java.lang.String,
    // java.lang.String)
    //     */
    //    public TableElement get(String rowLabel, String colLabel) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowColIndexSupport#set(java.lang.String,
    // java.lang.String, lab.cb.scmd.web.table.TableElement)
    //     */
    //    public void set(String rowLabel, String colLabel, TableElement element) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowIndexSupport#set(java.lang.String, int,
    // lab.cb.scmd.web.table.TableElement)
    //     */
    //    public void set(String rowLabel, int col, TableElement component) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowIndexSupport#get(java.lang.String, int)
    //     */
    //    public TableElement get(String rowLabel, int col) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowIndexSupport#getRowIndex(java.lang.String)
    //     */
    //    public int getRowIndex(String rowLabel) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.ColIndexSupport#get(int, java.lang.String)
    //     */
    //    public TableElement get(int row, String colLabel) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.ColIndexSupport#set(int, java.lang.String,
    // lab.cb.scmd.web.table.TableElement)
    //     */
    //    public void set(int row, String colLabel, TableElement tableElement) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.ColIndexSupport#getColIndex(java.lang.String)
    //     */
    //    public int getColIndex(String colLabel) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.RowIndexSupport#decollateRow(java.lang.String,
    // lab.cb.scmd.web.table.Decollator)
    //     */
    //    public void decollateRow(String rowLabel, Decollator decollator) {
    //        throw new UnsupportedOperationException();
    //    }
    //
    //    /* (non-Javadoc)
    //     * @see lab.cb.scmd.web.table.ColIndexSupport#decollateCol(java.lang.String,
    // lab.cb.scmd.web.table.Decollator)
    //     */
    //    public void decollateCol(String colLabel, Decollator decollator) {
    //        throw new UnsupportedOperationException();
    //    }
}

abstract class NotifierBase implements Functor
{
    public NotifierBase(Table table)
    {
        _table = table;
    }

    protected NotifierBase(Table table, int row, int col, TableElement element, Object[] elementList)
    {
        _table = table;
        _row = row;
        _col = col;
        _element = element;
        _elementList = elementList;
    }

    protected Table        _table;
    protected int          _row;
    protected int          _col;
    protected TableElement _element;
    protected Object[]     _elementList;
}

class NotifyBeforeUpdate extends NotifierBase
{
    public NotifyBeforeUpdate(Table table, int row, int col, TableElement element)
    {
        super(table, row, col, element, null);
    }

    public void apply(Object input) {
        UpdateObserver observer = (UpdateObserver) input;
        observer.beforeUpdate(_table, _row, _col, _element);
    }
}

class NotifyAfterUpdate extends NotifierBase
{
    public NotifyAfterUpdate(Table table, int row, int col, TableElement element)
    {
        super(table, row, col, element, null);
    }

    public void apply(Object input) {
        UpdateObserver observer = (UpdateObserver) input;
        observer.afterUpdate(_table, _row, _col, _element);
    }
}

class NotifyBeforeRowAddition extends NotifierBase
{
    public NotifyBeforeRowAddition(Table table, int row, Object[] elementList)
    {
        super(table, row, -1, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeRowAddition(_table, _row, _elementList);
    }
}

class NotifyAfterRowAddition extends NotifierBase
{
    public NotifyAfterRowAddition(Table table, int row, Object[] elementList)
    {
        super(table, row, -1, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterRowAddition(_table, _row, _elementList);
    }
}

class NotifyBeforeRowDeletion extends NotifierBase
{
    public NotifyBeforeRowDeletion(Table table, int row)
    {
        super(table, row, -1, null, null);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeRowDeletion(_table, _row);
    }
}

class NotifyAfterRowDeletion extends NotifierBase
{
    public NotifyAfterRowDeletion(Table table, int row)
    {
        super(table, row, -1, null, null);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterRowDeletion(_table, _row);
    }
}

class NotifyBeforeColDeletion extends NotifierBase
{
    public NotifyBeforeColDeletion(Table table, int col)
    {
        super(table, -1, col, null, null);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeColDeletion(_table, _col);
    }
}

class NotifyAfterColDeletion extends NotifierBase
{
    public NotifyAfterColDeletion(Table table, int col)
    {
        super(table, -1, col, null, null);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterColDeletion(_table, _col);
    }
}

class NotifyBeforeColAddition extends NotifierBase
{
    public NotifyBeforeColAddition(Table table, int col, Object[] elementList)
    {
        super(table, -1, col, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeColAddition(_table, _col, _elementList);
    }
}

class NotifyAfterColAddition extends NotifierBase
{
    public NotifyAfterColAddition(Table table, int col, Object[] elementList)
    {
        super(table, -1, col, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterColAddition(_table, _col, _elementList);
    }
}

class NotifyBeforeRowInsertion extends NotifierBase
{
    public NotifyBeforeRowInsertion(Table table, int row, Object[] elementList)
    {
        super(table, row, -1, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeRowInsertion(_table, _row, _elementList);
    }
}

class NotifyAfterRowInsertion extends NotifierBase
{
    public NotifyAfterRowInsertion(Table table, int row, Object[] elementList)
    {
        super(table, row, -1, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterRowInsertion(_table, _row, _elementList);
    }
}

class NotifyBeforeColInsertion extends NotifierBase
{
    public NotifyBeforeColInsertion(Table table, int col, Object[] elementList)
    {
        super(table, -1, col, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).beforeColInsertion(_table, _col, _elementList);
    }
}

class NotifyAfterColInsertion extends NotifierBase
{
    public NotifyAfterColInsertion(Table table, int col, Object[] elementList)
    {
        super(table, -1, col, null, elementList);
    }

    public void apply(Object input) {
        ((UpdateObserver) input).afterColInsertion(_table, _col, _elementList);
    }
}

//--------------------------------------
// $Log: Table.java,v $
// Revision 1.18  2004/12/10 05:15:49  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.17  2004/09/10 08:32:43  leo
// ColPickerを追加
//
// Revision 1.16  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.15  2004/08/27 03:15:12  leo
// Tableにtab区切り出力機能を追加
// Statisticsクラスとの連携のためのTableIteratorを提供
// Normalizerの追加
//
// Revision 1.14  2004/08/24 07:36:15  leo
// AbstractTableを廃棄
//
// Revision 1.13  2004/08/24 06:58:09  leo
// テーブルを切り出してviewを得るメソッドを追加
//
// Revision 1.12  2004/08/24 04:23:18  leo
// merge
//
// Revision 1.11.2.4  2004/08/24 04:04:42  leo
// Tableのindex周り、pasteが完成
//
// Revision 1.11.2.3 2004/08/24 02:36:12 leo
// ColIndexが完成
//
// Revision 1.11.2.2 2004/08/23 15:21:28 leo
// Observerへの通知を追加
//
// Revision 1.11.2.1 2004/08/23 07:08:26 leo
// branchを作って、ObserverによるTableラベルの作成開始
//
// Revision 1.11 2004/08/09 12:26:15 leo
// StringCell -> StringElementなど、TableElementの要素っぽく名称変更
// ColIndex, RowIndexなどをDynamic Update
//
// Revision 1.10 2004/08/09 09:17:11 leo
// Tableの範囲指定にTableRangeを使うようにした
//
// Revision 1.9 2004/08/09 05:25:17 leo
// タグないに複数のタグを持てるように改良
//
// Revision 1.8 2004/08/09 03:36:42 leo
// TagDecollatorを追加
//
// Revision 1.7 2004/08/09 02:10:04 leo
// Decollation, Decollatorを整理
//
// Revision 1.6 2004/08/07 14:30:07 leo
// TableCursorを追加
//
// Revision 1.5 2004/08/07 11:48:43 leo
// Web用のTableクラス
//
// Revision 1.1 2004/08/06 12:17:22 leo
// Decolator Pattern で、rowIndex, colIndexを実現
// Visitor Patternで、Table内探索を行う
//
// Revision 1.3 2004/08/05 14:10:45 leo
// ImageServerをsessionを読むのではなく、GETで取得するように変更
//
// Revision 1.2 2004/08/02 07:57:16 leo
// 未完成
//
// Revision 1.1 2004/08/02 07:56:25 leo
// 未完成
//
//--------------------------------------
