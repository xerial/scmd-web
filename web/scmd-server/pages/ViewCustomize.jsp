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

<scmd-base:header title="Customize View" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>



<html:form action="CustomizeView.do" method="GET">

<p class="title"> Individual Cell Parameters </p>
<table class="small">
<% int col = 0; %>
<logic:iterate id="param" name="viewConfigForm" property="cellParameterList" type="lab.cb.scmd.db.scripts.bean.Parameter">
<% if((col % 10) == 0){%>
<tr>
<%}%>
<td><html:multibox property="selectedCellParameter"><%= param.getId()%></html:multibox> </td>
<td><%= param.getShortname() %></td>
<% if((col % 10) == 9){%>
</tr>
<%}
col++;
%>
</logic:iterate>
</table>

<html:submit value="set" property="button"/>

<p class="title"> ORF Unit Parameters </p>
<table class="small">
<% col = 0; %>
<logic:iterate id="param" name="viewConfigForm" property="ORFParameterList" type="lab.cb.scmd.db.scripts.bean.Parameter">
<% if((col % 10) == 0){%>
<tr>
<%}%>
<td><html:multibox property="selectedCellParameter"><%= param.getId()%></html:multibox> </td>
<td><%= param.getShortname() %></td>
<% if((col % 10) == 9){%>
</tr>
<%}
col++;
%>
</logic:iterate>
</table>

</html:form>


</center>
</body>

<scmd-base:footer/>
