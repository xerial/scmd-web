//-----------------------------------
// SCMDWeb Project
// 
// SQLExpression.java 
// Since: 2005/02/01
//
// $Date$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.db.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 引数を受けとって、SQL文にパラメータを代入できるようにする
 * 
 * SQL文内では、$1, $2, ...　という変数が使える。
 * 
 * <pre>
 * <sql name="select all" description="">
 *   SELECT * from $1
 * </sql>
 * </pre>
 * 
 * 
 * @author leo
 *
 */
public class SQLExpression
{
    private String _sqlExpr;
    
    /**
     * 
     * @param sql SQL文
     */
    public SQLExpression(String sql)
    {
        _sqlExpr = sql;
    }
    
    /**
     * $1, $2, ...という変数を置き換えたSQL文を生成する
     * @param arguments 変数を置き換える文字列
     * @return 　$1, $2, ...という変数を置き換えたSQL文
     */
    public String assign(Object... arguments)
    {
        String assignedSQL = _sqlExpr;
        for(int i=0; i<arguments.length; i++)
        {
            String pattern = "\\$" + (i+1) + "([^0-9]|$)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(assignedSQL);
            String replacement =  arguments[i].toString().replace("$", "\\$") +"$1";
            assignedSQL = m.replaceAll(replacement);
        }
        return assignedSQL;
    }
}
