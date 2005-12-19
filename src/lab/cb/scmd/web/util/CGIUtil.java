//--------------------------------------
// SCMDServer
// 
// CGIUtil.java 
// Since: 2004/07/30
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author leo
 * 
 */
public class CGIUtil {
	/**
	 * URL�̃p�����[�^��Map������Key,Value���g���Đ������� key=value&key=value&key=value
	 * 
	 * @param argumentMap
	 * @return
	 */
	public static String getCGIArgument(Map argumentMap) {
		String cgiArgument = "";

		Set keySet = argumentMap.keySet();
		for (Iterator it = keySet.iterator(); it.hasNext();) {
			Object key = (it.next());
			Object value = argumentMap.get(key);
			cgiArgument += "&" + key.toString() + "="
					+ (value != null ? value.toString() : "");
		}

		if (cgiArgument.length() > 1)
			cgiArgument = cgiArgument.substring(1);
		return cgiArgument;
	}

}

// --------------------------------------
// $Log: CGIUtil.java,v $
// Revision 1.4 2004/08/11 06:24:14 leo
// �o�O���C��
//
// Revision 1.3 2004/08/11 05:29:44 leo
// �C��
//
// Revision 1.2 2004/08/11 05:27:30 leo
// null�΍�
//
// Revision 1.1 2004/07/30 06:40:29 leo
// Morphology Search�̃C���^�[�t�F�[�X�A����
//
// --------------------------------------
