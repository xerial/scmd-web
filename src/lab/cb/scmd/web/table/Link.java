//--------------------------------------
// SCMDServer
// 
// Link.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Map;

import lab.cb.scmd.web.util.CGIUtil;



/**
 * @author leo
 *
 */
public class Link extends Tag
{
     /**
     * 
     */
    public Link(String href, TableElement bodyContent)
    {
        super("a", bodyContent);
        _bodyContent = bodyContent;
        setProperty("href", href);
    }
    public Link(String href, String body)
    {
        super("a", new StringElement(body));
        setProperty("href", href);
    }
    
    public Link(String href, Map queryMap, TableElement bodyContent)
    {
        super("a", bodyContent);
        setProperty("href", href + "?" + CGIUtil.getCGIArgument(queryMap));
    }

    public Link(String href, Map queryMap, String bodyContent)
    {
        super("a", new StringElement(bodyContent));
        setProperty("href", href + "?" + CGIUtil.getCGIArgument(queryMap));
    }
}


//--------------------------------------
// $Log: Link.java,v $
// Revision 1.5  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.4  2004/08/09 05:25:17  leo
// �^�O�Ȃ��ɕ����̃^�O�����Ă�悤�ɉ���
//
// Revision 1.3  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.1  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
//--------------------------------------