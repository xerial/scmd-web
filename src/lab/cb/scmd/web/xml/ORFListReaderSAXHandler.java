//--------------------------------------
// SCMDServer
//
// ORFListReaderSAXHandler.java 
// Since: 2004/07/26
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.xml;

//import java.util.Collection;
//
//import java.util.*;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//
//import lab.cb.scmd.db.common.PageStatus;
//import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.util.xml.BaseHandler;
//import lab.cb.scmd.web.bean.YeastGene;

/** @deprecated
 * @author leo
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
//public class ORFListReaderSAXHandler extends BaseHandler
//{
//
//    /**
//     *  
//     */
//    public ORFListReaderSAXHandler()
//    {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.xml.sax.ContentHandler#startDocument()
//     */
//    public void startDocument() throws SAXException {
//        // TODO Auto-generated method stub
//        super.startDocument();
//        _contextGroup = "global";
//        _yeastGeneListMap.put(_contextGroup, new LinkedList());
//        _groupToPageStatusMap.put(_contextGroup, new PageStatus(1, 1, 10));
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
//     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
//     */
//    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        super.startElement(uri, localName, qName, attributes);
//
//        if(qName.equals("orfgroup"))
//        {
//            _contextGroup = attributes.getValue("type");
//            _yeastGeneListMap.put(_contextGroup, new LinkedList());
//        }
//
//        if(qName.equals("orf"))
//        {
//            String id = attributes.getValue("orfname");
//            //((LinkedList) _yeastGeneListMap.get(_contextGroup)).add(id);
//            _contextYeastGene = new YeastGene(id);
//            return;
//        }
//
//        if(qName.equals("standardname"))
//        {
//            String geneName = attributes.getValue("name");
//            if(_contextYeastGene == null) throw new SAXException(new SCMDException("standardname has no parent orf"));
//            _contextYeastGene.setStandardName(geneName);
//        }
//        if(qName.equals("alias"))
//        {
//            String geneName = attributes.getValue("name");
//            if(_contextYeastGene == null) throw new SAXException(new SCMDException("alias has no parent orf"));
//            _contextYeastGene.addAlias(geneName);
//        }
//        if(qName.equals("annotation"))
//        {
//        	String annotation = attributes.getValue("name");
//        	if(_contextYeastGene == null) throw new SAXException(new SCMDException("annotation has no parent orf"));
//        	_contextYeastGene.setAnnotation(annotation);
//        }
//        
//        if(qName.equals("page"))
//        {
//            String currentPage = attributes.getValue("current");
//            String maxPage = attributes.getValue("max");
//            String elements = attributes.getValue("elements");
//            _groupToPageStatusMap.put(_contextGroup, new PageStatus(Integer.parseInt(currentPage), Integer
//                    .parseInt(maxPage), Integer.parseInt(elements)));
//        }
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
//     *      java.lang.String, java.lang.String)
//     */
//    public void endElement(String uri, String localName, String qName) throws SAXException {
//        super.endElement(uri, localName, qName);
//
//        if(qName.equals("orf"))
//        {
//            ((LinkedList) _yeastGeneListMap.get(_contextGroup)).add(_contextYeastGene);
//        }
//        if(qName.equals("orfgroup"))
//        {
//            if(_groupToPageStatusMap.get(_contextGroup) == null)
//            {
//                _groupToPageStatusMap.put(_contextGroup, new PageStatus(1, 1, 10));
//            }
//            _contextGroup = "global";
//        }
//
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see lab.cb.scmd.util.xml.BaseHandler#textContent(java.lang.String)
//     */
//    public void textContent(String content) throws SAXException {
//        // TODO Auto-generated method stub
//        super.textContent(content);
//    }
//
//    /**
//     * @return Returns the _yeastGeneListMap.
//     */
//    public List getYeastGeneList(String groupName) {
//        return (List) _yeastGeneListMap.get(groupName);
//    }
//
//    public Collection getGroupNameList() {
//        java.util.Set keySet = _yeastGeneListMap.keySet();
//
//        LinkedList groupNameList = new LinkedList();
//        String[] groupName = { "wildtype", "mutant"};
//        for (int i = 0; i < groupName.length; i++)
//        {
//            groupNameList.add(groupName[i]);
//        }
//        for(Iterator it = keySet.iterator(); it.hasNext(); )
//        {
//            String key = (String) it.next();
//            if(!groupNameList.contains(key))
//                groupNameList.add(key);
//        }
//        groupNameList.remove("global"); // groupが指定されていない場合を除く
//        return groupNameList;
//    }
//
//    public PageStatus getPageStatus(String groupName) {
//        return (PageStatus) _groupToPageStatusMap.get(groupName);
//    }
//
//    String    _contextGroup         = "global";
//    YeastGene _contextYeastGene     = null;
//    HashMap   _yeastGeneListMap     = new HashMap();
//    HashMap   _groupToPageStatusMap = new HashMap();
//
//}

//--------------------------------------
// $Log: ORFListReaderSAXHandler.java,v $
// Revision 1.8  2004/08/26 08:45:52  leo
// Queryの追加。 selectionの修正
//
// Revision 1.7  2004/08/15 02:48:48  sesejun
// standardnameが無い場合に別名が表示されないバグを修正。
// ORFListで、annotationを表示できるように修正。
//
// Revision 1.6  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.5  2004/08/09 13:47:03  leo
// 写真がないとき、代替画像を表示するようにした
//
// Revision 1.4  2004/08/09 03:39:23  sesejun
// ORFListをDBから生成
//
// Revision 1.3  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.2  2004/07/26 14:20:24  leo
// *** empty log message ***
//
// Revision 1.1 2004/07/26 11:19:11 leo
// Yeast Mutants page用のクラスを追加
//
//--------------------------------------
