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

/**
 * SQL���𐶐�����̂ɖ𗧂��C�u����
 * @author leo
 *
 */
public class SQLUtil
{
    static public enum QuotationType { none, singleQuote, doubleQuote }     
    static private String[] quotation = {"", "'", "\""}; 
    
    /** 
     * list���J���}�ŋ�؂����������Ԃ�
     * 
     * @param list
     * @param quotationType
     * @return list���J���}�ŋ�؂����������Ԃ�
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
     * 
     */
    private SQLUtil()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
}



