//--------------------------------------
//SCMDServer
//
//YeastGene.java 
//Since: 2004/07/26
//
//$URL$ 
//$LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.exception.XMLParseErrorException;
import lab.cb.scmd.web.xml.DOMParser;
import lab.cb.scmd.web.xml.XMLReaderThread;

/**
* @author leo
* 
* TODO To change the template for this generated type comment go to Window -
* Preferences - Java - Code Style - Code Templates
*/
public class YeastGene
{

 String     orf = null;
 String     standardName = null;
 List alias = new LinkedList();
 String     annotation = null;
 String      color = "pink";

 /**
  *  
  */
 public YeastGene()
 {

 }

 public YeastGene(String orf)
 {
     setOrf(orf);
 }

 public YeastGene(String orf, XMLQuery queryInstance) throws SCMDException
 {
     setOrf(orf);
     
     DOMParser parser = new DOMParser();
     XMLReaderThread reader = new XMLReaderThread(parser);
     try
     {
         reader.start();
         queryInstance.getORFInfo(reader.getOutputStream(), orf, 0);
         reader.join();
     }
     catch(InterruptedException e)
     {
         throw new SCMDException(e);
     }
     
     Document document = parser.getDocument();
     NodeList nodeList = document.getElementsByTagName("orf");
     if(nodeList.getLength() > 0)
         load((Element) nodeList.item(0));
 }
 
 public YeastGene(Element orfElement) throws XMLParseErrorException
 {
     load(orfElement);
 }

 public YeastGene(String orf, String standardName, List geneNameList)
 {
     setOrf(orf);
     this.standardName = standardName;
     this.alias = geneNameList;
 }

 public void load(Element orfElem) throws XMLParseErrorException {
     if(orfElem.getTagName() != "orf")
         throw new XMLParseErrorException("orf tag is not found");
     
     setOrf(orfElem.getAttribute("orfname"));

     NodeList children = orfElem.getChildNodes();
     for (int i = 0; i < children.getLength(); i++)
     {
         Node node = children.item(i);
         if(node.getNodeType() == Node.ELEMENT_NODE)
         {
             Element elem = (Element) node;
             String tagName = elem.getTagName();
             String name = elem.getAttribute("name");
             if(tagName.equals("standardname"))
             {  
                 setStandardName(name);
             }
             else if(tagName.equals("alias"))
             {
                 addAlias(name);
             }
             else if(tagName.equals("annotation"))
             {
                 NodeList contentList = elem.getChildNodes();
                 StringBuffer buffer = new StringBuffer();
                 for(int n=0; n<contentList.getLength(); n++)
                 {
                     Node c = contentList.item(n);
                     if(c.getNodeType() == Node.TEXT_NODE)
                     {
                         buffer.append(((Text) c).getData());
                     }
                 }
                 setAnnotation(buffer.toString());
             }
             
         }
     }

 }

 /**
  * @return Returns the alias.
  */
 public List getAlias() {
     return alias;
 }

 public void addAlias(String geneName) {
     alias.add(geneName);
 }

 public String getStandardName() {
     return standardName;
 }

 public void setStandardName(String standardName) {
     this.standardName = standardName;
 }

 /**
  * @return Returns the orf.
  */
 public String getOrf() {
     return orf;
 }
 
 public void setOrf(String orf)
 {
     Pattern p = Pattern.compile("(y[a-z]{2}[0-9]{3})([wc])(-[a-z])?", Pattern.CASE_INSENSITIVE);
     Matcher m = p.matcher(orf);
     String orfString;
     if(m.matches())
     {
         orfString = m.group(1).toUpperCase() + m.group(2).toLowerCase() + (m.group(3) == null? "" : m.group(3));
     }
     else
         orfString = orf;
         
     this.orf = orfString;
 }

 public String getAliasString() {
     String ret = "";
        for (Iterator it = alias.iterator(); it.hasNext();)
     {
         String geneName = (String) it.next();
         ret += geneName + "; "; 
     }
     if(ret.length() > 0 ) {
         ret = ret.substring(0, ret.length() - 2);
     } else
         return "";

     return ret;
 }
 
 public String getAnnotation() {
    if( annotation == null)
        return "";
    int endplace = 300;
    if( annotation.length() > endplace ) {
        while( annotation.length() > endplace && annotation.charAt(endplace) != ' ')
            endplace++;
        annotation = annotation.substring(0, endplace);
        annotation += "...";
    }
    return annotation;
 }

    public void setAnnotation(String annot) {
        annotation = annot;
    }
 
 public String getColor() {
     return color;
 }
 
 public void setColor(String color) {
     this.color = color;
 }
 public String isSelectedColor(String selectedColor) {
     if( this.color.equals(selectedColor) )
         return "selected";
     return "";
 }
}

//--------------------------------------
//$Log: YeastGene.java,v $
//Revision 1.9  2004/09/21 06:13:05  leo
//warning fix
//
//Revision 1.8  2004/09/03 07:31:53  leo
//デザインの調整
//standardnameを表示
//
//Revision 1.7  2004/08/26 08:45:52  leo
//Queryの追加。 selectionの修正
//
//Revision 1.6  2004/08/25 09:06:00  leo
//userselectionの追加
//
//Revision 1.5  2004/08/15 02:48:48  sesejun
//standardnameが無い場合に別名が表示されないバグを修正。
//ORFListで、annotationを表示できるように修正。
//
//Revision 1.4  2004/08/14 11:09:08  leo
//Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
//Revision 1.3  2004/08/12 14:49:24  leo
//DBとの接続開始
//
//Revision 1.2 2004/08/09 13:47:03 leo
//写真がないとき、代替画像を表示するようにした
//
//Revision 1.1 2004/07/26 11:19:11 leo
//Yeast Mutants page用のクラスを追加
//
//--------------------------------------
