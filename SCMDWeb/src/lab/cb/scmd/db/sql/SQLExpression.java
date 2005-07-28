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
 * �������󂯂Ƃ��āASQL���Ƀp�����[�^�����ł���悤�ɂ���
 * 
 * SQL�����ł́A$1, $2, ...�@�Ƃ����ϐ����g����B
 * 
 * <pre>
 * String assignedSQL = SQLExpression.assignTo("select * from $1", "t1");
 * // assignedSQL = "select * from t1"�@�ƂȂ�
 * </pre>
 * @author leo
 *
 */
public class SQLExpression
{
    private String _sqlExpr;
    
    /**
     * 
     * @param sql SQL��
     */
    public SQLExpression(String sql)
    {
        _sqlExpr = sql;
    }
    
    /**
     * $1, $2, ...�Ƃ����ϐ���u��������SQL���𐶐�����
     * @param arguments �ϐ���u�������镶����
     * @return �@$1, $2, ...�Ƃ����ϐ���u��������SQL��
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
    
    /**
     * SQL���Ɉ����������ĕԂ�
     * @param sqlExpr SQL��
     * @param arguments�@����������
     * @return �����̒l��������ꂽSQL��
     */
    public static String assignTo(String sqlExpr, Object... arguments)
    {
        return new SQLExpression(sqlExpr).assign(arguments);
    }
}
