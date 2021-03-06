<%--
//--------------------------------------
// SCMDServer
// 
// ViewCustomize.jsp
// Since  2005/02/08
//
// $URL$ 
// $Author: leo $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<jsp:useBean id="cellParameterList" scope="request" type="java.util.List"/>
<jsp:useBean id="orfParameterList" scope="request" type="java.util.List"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="viewConfigForm"  scope="session" class="lab.cb.scmd.web.formbean.ViewCustomizeForm"/>

<scmd-base:header title="My Parameter List (Customization)" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on" orf="${view.orf}"/>
<html:form action="CustomizeView.do" method="GET">

<bean:define id="orfsize" value="<%= Integer.toString(orfParameterList.size()) %>" />
<bean:define id="cellsize" value="<%= Integer.toString(cellParameterList.size()) %>" />
<table>
<tr>
<td width="15"></td>
<td valign="top">
<!-- show selected cell params -->
<table class="small" valign="top" cellspacing="0"  align="center">
<tr><td colspan="3" class="title" align="center"> Selected ORF Parameters </td></tr>
<tr>
<td class="sheetlabel"> </td>
<td class="sheetlabel">Name</td>
<td class="sheetlabel">Description</td></tr>
<logic:equal name="orfsize" value="0" >
<tr><td colspan="3" align="center">No ORF parameters selected</td></tr>
</logic:equal>
<logic:notEqual name="orfsize" value="0" >
<logic:iterate id="morphParam" name="orfParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeORFParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td>
<html:link page="/ViewORFParameter.do?paramID=${morphParam.id}&sortspec=${morphParam.id}&columnType=input">${morphParam.name}</html:link>
</td><td>
${morphParam.displayname}
</td></tr>
</logic:iterate>
</logic:notEqual>
</table>
<table align="center">
<tr>
<td class="menubutton" align="center">
[<html:link page="/ViewORFParameter.do">Display Datasheet</html:link>]
[<html:link page="/ORFTeardrop.do?orf=${view.orf}&sheetType=3">Teardrop View</html:link>]
</td>
</tr>
<tr>
<td class="menubutton" align="center">
download the ORF parameter datasheet as [<html:link page="/ViewORFParameter.do?format=xml">XML</html:link>] [<html:link page="/ViewORFParameter.do?format=tab">tab-separated sheet</html:link>]
</td>
</tr>
</table>
<!-- end of selected cell params -->
</td>
<td width="15"></td>
<td valign="top" class="small">

<table class="small" valign="top" cellspacing="0" align="center">
<tr><td colspan="3" class="title" align="center">Selected Cell Parametes</td></tr>
<tr>
<td align="center">
<!-- show selected cell params -->
<tr nowrap="nowrap">
<td class="sheetlabel"> </td>
<td class="sheetlabel">Name</td>
<td class="sheetlabel">Description</td></tr>
<logic:equal name="cellsize" value="0" >
<tr><td colspan="3" align="center">No cell parameters selected</td></tr>
</logic:equal>
<logic:notEqual name="cellsize" value="0" >
<logic:iterate id="morphParam" name="cellParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeCellParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td><html:link page="/ViewORFParameter.do?paramID=${morphParam.id}&columnType=input">${morphParam.name}</html:link></td>
<td>${morphParam.displayname}</td>
</tr>
</logic:iterate>
</logic:notEqual>
</table>

<table align="center">
<tr>
<td class="menubutton" align="center">
[<html:link page="/ViewDataSheet.do?sheetType=4">Photo Datasheet</html:link>]
</td>
</tr>
</table>

</td>
</tr>
</table>

<p>
<% if( orfParameterList.size() != 0 || cellParameterList.size() != 0 ) { %>
<table width="500">
<tr><td align="center">
<html:submit value="remove" property="button"/>
<html:submit value="remove all" property="button"/>
</td></tr>
</table>
<% } %>
</p>

<p> <span class="title"> ORF Parameters List </span> <span class="menubutton">[<html:link page="/ParameterHelp.do#orfparam">help</html:link>]</p>
<p class="small"> Select and add parameters below. (press CTRL or SHIFT key to choose multiple items)</p>
<table>
<tr><td>Category</td><td></td><td>Parameter Description</td></tr>
<tr><td>
<html:select onchange="submit();" property="orfCategory" size="10">
<html:options property="ORFCategoryList"/>
</html:select>
</td><td align="center">
&gt; &gt; <br>
<html:submit value="change" property="button"/>
</td><td>
<html:select property="selectedORFParameter" multiple="true" size="10">
<logic:iterate id="p" name="viewConfigForm" property="ORFDetailCategoryList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<html:option value="${p.idStr}">[${p.name}] ${p.displayname}</html:option>
</logic:iterate>
</html:select>
</td><td>
<html:submit value="add" property="button"/>
</td></tr>
</table>

<p> <span class="title"> Cell Parameters List </span> <span class="menubutton">[<html:link page="/ParameterHelp.do#cellparam">help</html:link>]</p>
<p class="small"> Select and add parameters below (press CTRL or SHIFT key to choose multiple items) </p>
<table>
<tr><td>Category</td><td></td><td>Parameter Description</td></tr>
<tr><td>
<html:select onchange="submit();" property="cellCategory" size="10">
<html:options property="cellCategoryList"/>
</html:select>
</td><td align="center">
&gt; &gt; <br>
<html:submit value="change" property="button"/>
</td><td>
<html:select property="selectedCellParameter" multiple="true" size="10">
<logic:iterate id="p" name="viewConfigForm" property="cellDetailCategoryList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<html:option value="${p.idStr}">[${p.name}] ${p.displayname}</html:option>
</logic:iterate>
</html:select>
</td><td>
<html:submit value="add" property="button"/>
</td></tr>
</table>

</html:form>

</center>
</body>

<scmd-base:footer/>
