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
    
    /** 
     * listをカンマで区切った文字列を返す
     * 
     * @param list
     * @param quotationType
     * @return listをカンマで区切った文字列を返す
     */
    static public String commaSeparatedList(Collection list, QuotationType quotationType) 
    {
        StringBuilder buffer = new StringBuilder();
        int quoteType = quotationType.ordinal();
        for(Object o : list)
        {
            buffer.append(quotation[quoteType]);
            buffer.append(o.toString());
            buffer.append(quotation[quoteType]);
            buffer.append(", ");
        }
        if(buffer.length() >= 2 )
            return buffer.substring(0, buffer.length() - 2);
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
    static public <E> String commaSeparatedList(E[] list, QuotationType quotationType)
    {
        Vector<E> v = new Vector<E>(list.length);
        for(E e : list)
            v.add(e);
        
        return commaSeparatedList(v, quotationType);
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




