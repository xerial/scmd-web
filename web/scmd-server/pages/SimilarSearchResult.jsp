<%--
//--------------------------------------
// SCMDServer
// 
// SearchResult.jsp
// Since  2004/08/25 15:51:33
//
// $Date: 2004/08/31 04:46:21 $ ($Author: leo $)
// $Revision: 1.2 $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>

<jsp:useBean id="userSelection"  scope="session" class="lab.cb.scmd.web.bean.UserSelection"/>
<jsp:useBean id="selection"  scope="page" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>
<jsp:useBean id="orfList"  scope="request" type="java.util.List"/>
<jsp:useBean id="pageStatus" scope="request" class="lab.cb.scmd.db.common.PageStatus"/>

<scmd-base:header title="Your Selection" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>


<html:form action="ViewSelection.do" method="POST">

<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
</table>



<scmd-base:pagemover page="SearchSimilarShape.do" name="search" property="argumentMap" currentPage="${pageStatus.currentPage}" maxPage="${pageStatus.maxPage}"/>

<table border="0" cellspacing="0" cellpadding="0">
<tr>
<td colspan="2" class="sheetlabel" width="105"> ORF </td> 
<td class="sheetlabel" witdh="90"> Std. Name </td> 
<td class="sheetlabel" width="150"> Aliases </td> 
<td colspan="3" class="sheetlabel"></td>
</tr>
<logic:iterate id="yeastGene" name="orfList" type="lab.cb.scmd.web.bean.YeastGeneWithDistance">

<tr class="small"> 
<td align="left" width="15"><p align="center"><html:multibox name="selection" property="inputList" value="<%= yeastGene.getOrf().toLowerCase()%>"/></p></td>
<td align="left" class="orf" width="90"> 
<html:link page="/ViewStats.do?orf=${yeastGene.orf}"> ${yeastGene.orf}  </html:link> 
</td> 
<td class="genename" align="center"> ${yeastGene.standardName} </td> 
<td class="small" align="center"> ${yeastGene.aliasString} </td> 
<td width="100"/>
<td width="130">
<html:link page="/ViewPhoto.do?orf=${yeastGene.orf}"> viewer </html:link>&nbsp;
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

</html:form>

</center>
</body>

<scmd-base:footer/>

<%--
//---------------------------------------
// $Log:  $
//---------------------------------------
--%>


