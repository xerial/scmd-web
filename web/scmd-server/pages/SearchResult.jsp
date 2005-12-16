<%--
//--------------------------------------
// SCMDServer
// 
// SearchResult.jsp
// Since  2004/08/25 15:51:33
//
// $Date: 2004/08/31 04:46:21 $ ($Author$)
// $Revision: 1.2 $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<jsp:useBean id="userSelection"  scope="session" class="lab.cb.scmd.web.bean.UserSelection"/>
<jsp:useBean id="selection"  scope="page" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>
<jsp:useBean id="orfList"  scope="request" type="java.util.List"/>
<jsp:useBean id="goList" scope="request" type="java.util.List"/>
<jsp:useBean id="pageStatus" scope="request" class="lab.cb.scmd.db.common.PageStatus"/>
<jsp:useBean id="keywordList" scope="request" type="java.util.List"/>

<bean:define id="gosize" value="<%= Integer.toString(goList.size()) %>" />
<bean:define id="orfsize" value="<%= Integer.toString(orfList.size()) %>" />

<scmd-base:header title="Your Selection" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>

<html:form action="ViewSelection.do" method="POST">
<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
<tr><td align="left" class="annotation">
Results for 
<logic:iterate id="key" name="keywordList" type="java.lang.String">
<b><html:link page="/Search.do?keyword=${key}"><%=key%></html:link></b>
</logic:iterate>
</td></tr>
</table>

<!-- Search Results for Gene Ontology ID and Terms -->
<logic:notEqual name="gosize" value="0" >
<table border="0" cellspacing="0" cellpadding="0">
<tr><td colspan="4" class="tablename">Gene Ontology Results</td></tr>
<tr>
<td colspan="2" class="sheetlabel" width="150"> GO ID </td> 
<td class="sheetlabel" width="150"> Component </td> 
<td class="sheetlabel" width="50"> </td>
<td class="sheetlabel"> </td> 
</tr>
<logic:iterate id="goElement" name="goList" type="lab.cb.scmd.web.bean.GeneOntology">
<tr class="small">
<td align="left" width="20"></td>
<td align="left" class="orf" width="135">
<html:link page="/Search.do?keyword=${goElement.goid}"> ${goElement.goid} </html:link> 
</td>
<td align="center" class="small"> ${goElement.namespace}</td>
<td />
<bean:define id="paramsize" value="<%= Integer.toString(goElement.getFwdRev().size()) %>" />
<td width="240" align="left">
<logic:equal name="paramsize" value="0">
No enriched parameters
</logic:equal>
<logic:notEqual name="paramsize" value="0">
<html:link page="/GOEnrichedGraphs.do?goid=${goElement.goid}">Enriched in ${paramsize} params</html:link> 
</logic:notEqual>
</td>
</tr>
<tr bgcolor="#F0F0E0" height="15">
<td></td>
<td colspan="4" width="530" class="annotation"> ${goElement.name}</td> 
</tr>
<tr height="7"><td> </td></tr>
</logic:iterate>
</logic:notEqual>

<!-- Search Results for ORF Name, Gene Name and Gene Annotation -->
<logic:notEqual name="orfsize" value="0" >
<logic:notEqual name="gosize" value="0">
<table border="0" cellspacing="0" cellpadding="0">
<tr><td height="10"></td></tr>
<tr><td width="593" class="tablename">Gene Results</td></tr>
</table>
</logic:notEqual>
<scmd-base:pagemover page="Search.do" name="search" property="argumentMap" currentPage="${pageStatus.currentPage}" maxPage="${pageStatus.maxPage}"/>

<table border="0" cellspacing="0" cellpadding="0">
<tr>
<td colspan="2" class="sheetlabel" width="105"> ORF </td> 
<td class="sheetlabel" witdh="90"> Std. Name </td> 
<td class="sheetlabel" width="150"> Aliases </td> 
<td colspan="3" class="sheetlabel"></td>
</tr>
<logic:iterate id="yeastGene" name="orfList" type="lab.cb.scmd.web.bean.YeastGene">

<tr class="small"> 
<td align="left" width="15"><p align="center"><html:multibox name="selection" property="inputList" value="<%= yeastGene.getOrf().toLowerCase()%>"/></p></td>
<td align="left" class="orf" width="90"> 
<html:link page="/ViewStats.do?orf=${yeastGene.orf}"> ${yeastGene.orf}  </html:link> 
</td> 
<td class="genename" align="center"> ${yeastGene.standardName} </td> 
<td class="small" align="center"> ${yeastGene.aliasString} </td> 
<td width="40"/>
<td width="240">
<html:link page="/ViewPhoto.do?orf=${yeastGene.orf}"> photo viewer </html:link>&nbsp;
<html:link page="/ORFTeardrop.do?orf=${yeastGene.orf}"> teardrop </html:link>&nbsp;
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${yeastGene.orf}" target="_blank">CYGD</a>&nbsp;
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${yeastGene.orf}" target="_blank">SGD</a>
</tr>
<tr bgcolor="#F0F0E0" height="15">
<td ></td>
<td colspan="5" width="530" class="annotation"> <%= yeastGene.getAnnotation() %></td> 
</tr>
<tr height="10"><td> </td></tr>
</logic:iterate>
</table>
</logic:notEqual>

</html:form>

</center>
</body>

<scmd-base:footer/>

<%--
//---------------------------------------
// $Log: SearchResult.jsp,v $
// Revision 1.2  2004/08/31 04:46:21  leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
// Revision 1.1  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.2  2004/08/26 08:48:20  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:09:55  leo
// userselectionの追加
//
//---------------------------------------
--%>


