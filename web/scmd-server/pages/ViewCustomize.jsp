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
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<jsp:useBean id="cellParameterList" scope="request" type="java.util.List"/>
<jsp:useBean id="orfParameterList" scope="request" type="java.util.List"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>

<scmd-base:header title="Customize View" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 
<html:form action="CustomizeView.do" method="GET">

Back to <a href="ViewDataSheet.do">Photo Datasheet</a>, <a href="ORFTeardrop.do">ORF Teardrop</a> or 
<a href="ViewORFParameter.do">ORF Prarameter Sheet</a> page.


<% if ( cellParameterList.size() != 0  ) { %>
<table>
<tr>
<td valign="top" class="small">

<table class="small" valign="top" cellspacing="0">
<tr><td colspan="3" class="title" align="center">Selected Cell Parametes</td></tr>
<tr>
<td align="center">
<!-- show selected cell params -->
<tr nowrap="nowrap">
<td class="sheetlabel"> </td>
<td class="sheetlabel">Name</td>
<td class="sheetlabel">Description</td></tr>
<logic:iterate id="morphParam" name="cellParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeCellParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td>
<html:link page="/ORFDataSheet.do?paramID=${morphParam.id}">${morphParam.shortName}</html:link>
</td><td>
${morphParam.displayname}
</td></tr>
</logic:iterate>
</table>
<% } %>
</td>


<td valign="top">
<% if ( orfParameterList.size() != 0  ) { %>
<!-- show selected cell params -->
<table class="small" valign="top" cellspacing="0">
<tr><td colspan="3" class="title" align="center"> Selected ORF Parameters </td></tr>
<tr>
<td class="sheetlabel"> </td>
<td class="sheetlabel">Name</td>
<td class="sheetlabel">Description</td></tr>
<logic:iterate id="morphParam" name="orfParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeORFParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td>
<html:link page="/ORFDataSheet.do?paramID=${morphParam.id}">${morphParam.shortName}</html:link>
</td><td>
${morphParam.name}
</td></tr>
</logic:iterate>
</table>
<!-- -->
<% } %>
</td>
</tr>
</table>

<table width="500">
<tr><td align="center">
<html:submit value="remove" property="button"/>
<html:submit value="remove all" property="button"/>
</td></tr>
</table>


<p class="title"> Cell Parameters </p>
<p class="small"> Select and add parameters below</p>
<table>
<tr><td>Category</td><td></td><td>Parameter Description</td></tr>
<tr><td>
<html:select onchange="submit();" property="cellCategory" size="10">
<logic:iterate id="param" name="viewConfigForm" property="cellCategoryList" type="java.lang.String">
<html:option value="<%= param %>"><%= param %></html:option>
</logic:iterate>
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

<p class="title"> ORF Parameters </p>
<p class="small"> Select and add parameters below</p>
<table>
<tr><td>Category</td><td></td><td>Parameter Description</td></tr>
<tr><td>
<html:select onchange="submit();" property="orfCategory" size="10">
<logic:iterate id="param" name="viewConfigForm" property="ORFCategoryList" type="java.lang.String">
<html:option value="<%= param %>"><%= param %></html:option>
</logic:iterate>
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

</html:form>

</center>
</body>

<scmd-base:footer/>
