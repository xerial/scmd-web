//--------------------------------------
// SCMDServer
// 
// BeanUtil.java 
// Since: 2004/08/31
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author leo
 *
 */
public class BeanUtil
{

    /**
     * 
     */
    public BeanUtil()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static Map getMap(PageContext pageContext, String name, String property)
    {
        Map map = new TreeMap();
        Object instance = null;
        if(name != null)
            instance = pageContext.findAttribute(name);
        if(instance != null && property != null)
        {
            try
            {
                Map inputMap = (Map) PropertyUtils.getSimpleProperty(instance, property);
                map = inputMap;
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        return map;
    }

}


//--------------------------------------
// $Log: BeanUtil.java,v $
// Revision 1.1  2004/08/31 04:46:21  leo
// �O���[�v���̃f�[�^�V�[�g�̍쐬�I��
// �����A�f�[�^�V�[�g�̃y�[�W�ړ����I��
//
//--------------------------------------