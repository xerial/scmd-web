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

Back to <a href="ViewDataSheet.do">datasheet</a> page.
<p class="title"> Individual Cell Parameters </p>
<!--
<table class="small">
<% int col = 0; %>
<logic:iterate id="param" name="viewConfigForm" property="cellParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<% if((col % 10) == 0){%>
<tr>
<%}%>
<td><html:multibox property="selectedCellParameter"><%= param.getId()%></html:multibox> </td>
<td><%= param.getShortName() %></td>
<% if((col % 10) == 9){%>
</tr>
<%}
col++;
%>
</logic:iterate>
</table>
-->

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
<html:submit value="set" property="button"/>
</td></tr>
</table>

<p class="title"> ORF Unit Parameters </p>
<!--
<table class="small">
<% col = 0; %>
<logic:iterate id="param" name="viewConfigForm" property="ORFParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<% if((col % 10) == 0){%>
<tr>
<%}%>
<td><html:multibox property="selectedORFParameter"><%= param.getId()%></html:multibox> </td>
<td><%= param.getShortName() %></td>
<% if((col % 10) == 9){%>
</tr>
<%}
col++;
%>
</logic:iterate>
</table>
-->
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
<html:submit value="set" property="button"/>
</td></tr>
</table>

</html:form>

<table>
<tr><td align="top">
<!-- show selected cell params -->
<p class="title"> Selected Cell Detail Parametes </p>
<table class="small">
<tr><td>Abbreviated Name</td><td>Name</td></tr>
<logic:iterate id="morphParam" name="cellParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
${morphParam.shortName}
</td><td>
${morphParam.displayname}
</td></tr>
</logic:iterate>
</table>
</td>
<td align="top">
<!-- show selected cell params -->
<p class="title"> Selected Average/SD Parametes </p>
<table class="small">
<tr><td>Abbreviated Name</td><td>Name</td></tr>
<logic:iterate id="morphParam" name="orfParameterList" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<tr><td>
${morphParam.shortName}
</td><td>
${morphParam.name}
</td></tr>
</logic:iterate>
</table>
<!-- -->
</td></tr>
</table>


</center>
</body>

<scmd-base:footer/>
