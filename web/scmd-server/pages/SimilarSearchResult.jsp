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
<jsp:useBean id="shape" scope="session" class="lab.cb.scmd.web.bean.SelectedShape"/>

<scmd-base:header title="Morphological Search Result" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>


<html:form action="ViewSelection.do" method="POST">

<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
</table>

<%-- display search phase --%>
<table align=center>
<tr><td colspan="4" class="small">
<logic:iterate id="phase" name="shape" property="selectionPhaseList" indexId="i">
<%
if(!(shape.getAreaRatio() == 0 && ( i.intValue()==3 || i.intValue()==4)))
{
if(phase.equals(shape.getCurrentPhase())) { %>
<font color="#FF3355"> ${phase} </font> <%
}else{ %>
<html:link page="/SelectShape.do?phase=${i}"> <font color="#006699"> ${phase} </font> </html:link>
<%}
if(i.intValue() != shape.SELECT_SEARCH){ %>
&gt;<% }} %>
</logic:iterate>
</tr>
</table>

<%-- display searched shape --%>
<table>
<tr>
<td align="center">
    <html:img page="/cellshape.png" name="shape" property="currentImageArgumentMap" width="128" height="128"/>
</td>
<td>
<table>

<tr>
<td/>
<td class="small" align="right"><font color="#006699">value </font></td>
<td class="small"><font color="#0099ff">(weight)</font></td>
</tr>

<tr>
<td><font color="#006699" class="small"> Bud area raio </font> </td>
<td class="small" align="right"> ${shape.areaRatio} </td>
<td class="small"> <font color="#666666"> (${shape.weightAreaRatio}) </fond> </td>
</tr>

<tr>
<td><font color="#006699" class="small"> Long axis length </font> </td>
<td class="small" align="right"> ${shape.longAxis} </td>
<td class="small"> <font color="#666666"> (${shape.weightLongAxis}) </fond> </td>
</tr>

<tr>
<td><font color="#006699" class="small"> Roundness </font> </td>
<td class="small" align="right"> ${shape.roundness} </td>
<td class="small"> <font color="#666666"> (${shape.weightRoundness}) </fond> </td>
</tr>

<logic:notEqual name="shape" property="areaRatio" value="0.0">
<tr>
<td><font color="#006699" class="small"> Bud neck position </font> </td>
<td class="small" align="right"> ${shape.neckPosition} </td>
<td class="small"> <font color="#666666"> (${shape.weightNeckPosition}) </fond> </td>
</tr>

<tr>
<td><font color="#006699" class="small"> Bud growth direction </font> </td>
<td class="small" align="right"> ${shape.growthDirection} </td>
<td class="small"> <font color="#666666"> (${shape.weightGrowthDirection}) </fond> </td>
</tr>
</logic:notEqual>

</table>
</tr>
</table>

<%-- show search results --%>
<table width="750">
<tr height="20"><td></td> </tr>
<tr><td align="left" class="title"> Search Result </td></tr>
</table>
<scmd-base:pagemover page="SearchSimilarShape.do" name="search" property="argumentMap" currentPage="${pageStatus.currentPage}" maxPage="${pageStatus.maxPage}"/>

<table border="0" cellspacing="0" cellpadding="0">
<tr>
<td colspan="2" class="sheetlabel" width="105"> ORF </td> 
<td class="sheetlabel" witdh="90"> Std. Name </td> 
<td class="sheetlabel" width="150"> Aliases </td> 
<td class="sheetlabel" width="30"> Distance </td>
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
<td class="small" align="center"> <%= yeastGene.getSlimedDistance() %> </td>
<td width="70"/>
<td width="130">
<html:link page="/ViewPhoto.do?orf=${yeastGene.orf}"> viewer </html:link>&nbsp;
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${yeastGene.orf}" target="_blank">CYGD</a>&nbsp;
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${yeastGene.orf}" target="_blank">SGD</a>
</tr>
<tr bgcolor="#F0F0E0" height="15">
<td ></td>
<td colspan="6" width="530" class="annotation"> <%= yeastGene.getAnnotation() %></td> 
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


