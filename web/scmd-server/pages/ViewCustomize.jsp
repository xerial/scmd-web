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
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
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

Back to <a href="ViewDataSheet.do">datasheet</a> or <a href="ORFTeardrop.do">ORF Teardrop</a> page.

<table width="500">
<tr><td align="right">
<html:submit value="remove" property="button"/>
<html:submit value="remove all" property="button"/>
</td></tr>
</table>

<% if ( cellParameterList.size() != 0  ) { %>
<table>
<tr><td align="top">
<!-- show selected cell params -->
<p class="title"> Selected DataSheet Parametes </p>
<table class="small" border="0" cellspacing="0" cellpadding="0">
<tr nowrap="nowrap">
<td class="sheetlabel"> </td>
<td class="sheetlabel">Abbreviated Name</td>
<td class="sheetlabel">Name</td></tr>
<logic:iterate id="morphParam" name="cellParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeCellParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td>
${morphParam.shortName}
</td><td>
${morphParam.displayname}
</td></tr>
</logic:iterate>
</table>
<% } %>
</td>
<td align="top">
<% if ( orfParameterList.size() != 0  ) { %>
<!-- show selected cell params -->
<p class="title"> ORF Teardrop Parametes </p>
<table class="small" border="0" cellspacing="0" cellpadding="0">
<tr nowrap="nowrap">
<td class="sheetlabel"> </td>
<td class="sheetlabel">Abbreviated Name</td>
<td class="sheetlabel">Name</td></tr>
<logic:iterate id="morphParam" name="orfParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
<html:multibox name="viewConfigForm" property="removeORFParamList" value="<%= morphParam.getIdStr() %>"/>
</td><td>
${morphParam.shortName}
</td><td>
${morphParam.name}
</td></tr>
</logic:iterate>
</table>
<!-- -->
<% } %>
</td></tr>
</table>


<p class="title"> Add Datasheet Parameters </p>
<p class="small"> Select and add parameters </p>
<table>
<tr><td>Category</td><td></td><td>Parameter Name</td></tr>
<tr><td>
<html:select onchange="submit();" property="cellCategory" size="10">
<logic:iterate id="param" name="viewConfigForm" property="cellCategoryList" type="java.lang.String">
<html:option value="<%= param %>"><%= param %></html:option>
</logic:iterate>
</html:select>
</td><td>
&gt; &gt;
</td><td>
<html:select property="selectedCellParameter" multiple="true" size="10">
<logic:iterate id="param" name="viewConfigForm" property="cellDetailCategoryList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<html:option value="<%= param.getIdStr()%>"><%=param.getDisplayname() %></html:option>
</logic:iterate>
</html:select>
</td><td>
<html:submit value="add" property="button"/>
</td></tr>
</table>

<p class="title"> ORF Unit Parameters </p>
<p class="small"> Select and add parameters </p>
<table>
<tr><td>Category</td><td></td><td>Parameter Name</td></tr>
<tr><td>
<html:select onchange="submit();" property="orfCategory" size="10">
<logic:iterate id="param" name="viewConfigForm" property="ORFCategoryList" type="java.lang.String">
<html:option value="<%= param %>"><%= param %></html:option>
</logic:iterate>
</html:select>
</td><td>
&gt; &gt;
</td><td>
<html:select property="selectedORFParameter" multiple="true" size="10">
<logic:iterate id="param" name="viewConfigForm" property="ORFDetailCategoryList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<html:option value="<%= param.getIdStr()%>"><%=param.getDisplayname() %></html:option>
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
