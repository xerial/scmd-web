<%-- 
//--------------------------------------
// SCMDServer
// 
// MorphologySearch.jsp
// Since: 2004/07/14
//
// $Date: 2004/08/30 06:06:25 $ ($Author$)
// $Revision: 1.8 $
//--------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="html-el" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="logic-el" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import="lab.cb.scmd.web.bean.*"%>

<jsp:useBean id="shape" scope="session" class="lab.cb.scmd.web.bean.SelectedShape"/>
<scmd-base:header title="Morphology Search"/>
<body>

<center>
<scmd-tags:menu  toolbar="on"/>
<table align=center>
<tr>
<td class=arial colspan=4><font color="#608080">
<b> Morphology Search </b>
</font>

<script language="javascript">
<!--
function help(){
    helpWin = window.open
	('/help/morphParamHelp.html', 'help',
	 'width=330, height=380, status=no, menuber=no, scrollbar=yes'); 
    helpWin.focus();
}
//-->
</script>



<input type=button value=help onClick="help();">
</td>
</tr>
<tr><td colspan="4" class="small"><font color="#444444"> Please select or input some parameter values below to find yeast mutants which are similar in shape. </font></td> </tr>
<tr><td colspan="4" class="small">

<%-- display search phase --%>
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

<logic:notEqual name="shape" property="currentPhase" value="Search">

<tr>
    <td colspan=4 class=arial>
    <font color=#006699>
    <b>Select </b> 
<b> 
  <a href="javascript: help()"> <font color="#ffaa66"> ${shape.selectionMessage} </font></a> 
  ${shape.selectionMessage2} : 
</b> 
</font> 
</td>
</tr>
<tr>
<td height="20" colspan="4" class="small"> <font color="#555555"> 
${shape.selectionSubMessage}
</font>
</td>
</tr>


<script language="javascript">

<!--
function preview(){
    value = document.shape.inputValue.value;
    url = "/scmd-server/ImagePreview.do?" + "<%= shape.getImageArgumentWithout(shape.getParameterName()) %>"  +  "&${shape.parameterName}=" + value;
    previewWin = window.open
	(url, 'preview', 
	 'width=200, height=360, status=no, menuber=no, scrollbar=no'); 
    previewWin.focus();
}
//-->

</script>

<html:form action="/SelectShape.do" method="GET" >
<html:hidden property="action" value="setParameter"/>
<html:hidden property="targetPhase" value="${shape.currentPhaseID}"/>
<tr>
<% for(int i=0; i<4; i++) {%>
<td width="128" height="128">
<img src="/scmd-server/cellshape.png?<%= shape.getImageArgument(i)%>"/>
</td>
<% } %>
</tr>

<tr>
<% for(int i=0; i<4; i++) {%>
<td>
 <html:radio property="selectedValue" value="<%= shape.getSelectionValue(i)%>"/> 
<font color="#006699"> <%= shape.getSelectionValue(i) %> </font>
</td>
<% } %>
</tr>

<logic:notEqual name="shape" property="parameterName" value="budSize">
<tr>
<td colspan=4 align=left class=arial> 
<input type="radio" name="selectedValue" value="manual" checked="checked"/>
<font color="#006699"> value: </font>
<input type="text" name="inputValue" value="${shape.parameterValue}" size="5"/>
<font class="small">(${shape.rangeMin} - ${shape.rangeMax})</font>
<input type="button" value="preview" onClick='preview()'/>


<%--
if(shape.getPhase() != shape.SELECT_BUDSIZE){%>
<font class="small" color="#666666">
<b> (ex. the average of his3 = </b>
<font color="#006666">
${shape.defaultValue}
</font><b> )</b></font>
<%}
--%>

</td>
</tr> 

<tr align="center">
<td colspan=4><b><font color="#006699"> Weight of this parameter: </font> </b> 
<font color="#336699"> low </font>
<c:forEach var="i" begin="0" end="5">
<html:radio property="${shape.weightParameter}" value="${i}"/> 
<font class="small">${i}</font>
</c:forEach>
<font color="336699">high</font>
</td></tr>

<tr align=center>
<td colspan=4 class="small">
If this parameter does not matter to what you wish, please select 0:
</td>
</tr>

</tr>

</logic:notEqual>
<tr align =center>
<td colspan=4><html:submit value="next"/></td>
</tr>



</html:form>

</logic:notEqual>

</table>

<logic:equal name="shape" property="currentPhase" value="Search">
<table>
<tr>
<td align="center">
    <html:img page="/cellshape.png?${shape.imageArgument}" width="128" height="128"/>
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


<tr>
<td colspan="4" align="center" class="menubutton">
[<html:link page="/SearchSimilarShape.do"> search </html:link>]
</td>
</tr>
</table>
</tr>
</table>
</logic:equal>


</table>


</center>
</body>
<scmd-base:footer/>
