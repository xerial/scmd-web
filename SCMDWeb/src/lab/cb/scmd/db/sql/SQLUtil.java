//--------------------------------------
// SCMDWeb Project
//
// SQLUtil.java
// Since: 2005/02/13
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.sql;

import java.util.Collection;
import java.util.Vector;

/**
 * SQL文を生成するのに役立つライブラリ
 * @author leo
 *
 */
public class SQLUtil
{
    static public enum QuotationType { none, singleQuote, doubleQuote }     
    static private String[] quotation = {"", "'", "\""}; 
    
    
    static private String separate(Collection list, String separator, QuotationType quotationType)
    {
        StringBuilder buffer = new StringBuilder();
        int quoteType = quotationType.ordinal();
        for(Object o : list)
        {
            buffer.append(quotation[quoteType]);
            buffer.append(o.toString());
            buffer.append(quotation[quoteType]);
            buffer.append(separator);
        }
        if(buffer.length() >= separator.length())
            return buffer.substring(0, buffer.length() - separator.length());
        else
            return "";        
    }
    
    /** 
     * listをカンマで区切った文字列を返す
     * 
     * @param list
     * @param quotationType
     * @return listをカンマで区切った文字列を返す
     */
    static public String commaSeparatedList(Collection list, QuotationType quotationType) 
    {
        return separate(list, ", ", quotationType);
    }
    
    /** 
     * listをカンマで区切った文字列を返す
     * 
     * @param list
     * @param quotationType
     * @return listをカンマで区切った文字列を返す
     */
    static public <E> String commaSeparatedList(E[] list, QuotationType quotationType)
    {
        Vector<E> v = new Vector<E>(list.length);
        for(E e : list)
            v.add(e);
        
        return commaSeparatedList(v, quotationType);
    }
    
    /** listをseparatorで区切った文字列を返す。
     * @param list
     * @param separator
     * @param quotationType
     * @return listをseparatorで区切った文字列
     */
    static public String separatedList(Collection list, String separator, QuotationType quotationType)
    {
        return separate(list, separator, quotationType);
    }
    
    static public String doubleQuote(String input)
    {
        return "\"" + input + "\""; 
    }
    static public String singleQuote(String input)
    {
        return "'" + input + "'";
    }
    /**
     * 
     */
    private SQLUtil()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
}




