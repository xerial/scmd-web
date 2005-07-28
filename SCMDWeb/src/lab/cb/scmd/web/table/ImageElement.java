//--------------------------------------
// SCMDServer
// 
// ImageElement.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Map;
import java.util.Properties;

import lab.cb.scmd.web.util.CGIUtil;


/**
 * @author leo
 *  
 */
public class ImageElement extends SelfClosedTag
{

    /**
     *  
     */
    public ImageElement(String src)
    {
        super("img");
        _src = src;
    }

    public ImageElement(String src, Map queryMap)
    {
        super("img");
        _src = src;
        _queryMap = queryMap;
    }


    public Properties getProperties() {
        Properties prop = super.getProperties();
        if(_width != UNDEFINED)
            prop.setProperty("width", Integer.toString(_width));
        if(_height != UNDEFINED)
            prop.setProperty("height", Integer.toString(_height));
        prop.put("src", _src + (_queryMap == null ? "" : "?" + CGIUtil.getCGIArgument(_queryMap)));
        return prop;
    }
    
    
    public void setQueryMap(Map queryMap) {
        _queryMap = queryMap;
    }

    public Map getQueryMap() {
        return _queryMap;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public void setWidth(int width) {
        _width = width;
    }

    public void setHeight(int height) {
        _height = height;
    }

    Map               _queryMap = null;
    String 			  _src;
    int               _width    = UNDEFINED;
    int               _height   = UNDEFINED;

    public static int UNDEFINED = -1;
}

//--------------------------------------
// $Log: ImageElement.java,v $
// Revision 1.1  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.7  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
// Revision 1.6  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.5  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.4  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
// Revision 1.3 2004/08/06 14:43:15 leo
// �摜�\�����A�N�V�������o�R����悤�ɂ���
//
// Revision 1.2 2004/08/06 12:17:22 leo
// Decolator Pattern �ŁArowIndex, colIndex������
// Visitor Pattern�ŁATable���T�����s��
//
// Revision 1.1 2004/08/02 07:56:25 leo
// ������
//
//--------------------------------------
