<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="logic-nonel" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="pageStatus"  scope="request" class="lab.cb.scmd.db.common.PageStatus"/>
<jsp:useBean id="paramIDs" scope="request" type="java.util.List"/>
<jsp:useBean id="sortspec" scope="request" type="java.lang.Integer"/>

<scmd-base:header title="ORF Parameter Sheet" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on" orf="${view.orf}"/>

<p class="title">Sortable ORF Parameter Sheet </p>

<html:form action="ViewSelection.do" method="GET">

<logic-nonel:present name="targetParam" scope="request">
<table>
<tr>
<td width="150">
<logic-nonel:present name="range" scope="request">
<img src="DrawTeardrop.do?paramID=${targetParam.id}&rangeBegin=${range.min}&rangeEnd=${range.max}&plotTargetORF=false"/>
</logic-nonel:present>
<logic-nonel:notPresent name="range" scope="request">
<img src="DrawTeardrop.do?paramID=${targetParam.id}&plotTargetORF=false"/>
</logic-nonel:notPresent>
</td>
<td>
<table>
<tr><td class="small">Parameter Name:</td><td class="genename">${targetParam.name}</td></tr>
<%--<tr><td class="small">Short Name:</td><td class="small">${targetParam.shortName}</td></tr>--%>
<tr><td class="small">Stain Type:</td><td class="small">${targetParam.stainType}</td></tr>
<tr><td class="small">Nucleus Status:</td><td class="small">${targetParam.nucleusStatus} </td></tr>
<tr><td class="small">Parameter Type:</td><td class="small">${targetParam.parameterType}</td></tr>
<tr><td class="small">Description:</td><td class="small" width="250">${targetParam.displayname}</td></tr>
</table>
</td>
<td>
<img id="param" class="paramview" width="250" height="175" src="paramfig.png?param=${targetParam.name}">
</td>
</tr>
</table>
<%--<span class="header"> order by ${targetParam.name} </span>--%>
</logic-nonel:present>
<span class="memo"> click the datasheet labels in order to sort the table</span>

<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
</table>

<span class="small">
<%
	int numParam = paramIDs.size();
	String pid = "";
	for(java.util.Iterator it = paramIDs.iterator(); it.hasNext(); )
		pid += "paramID=" + it.next().toString() + "&";
	pid += "columnType=input";
	pid += "&sortspec=" + sortspec;	
%>
<scmd-base:pagemover page="ViewORFParameter.do" parameter="<%= pid%>" target="${group.groupName}" currentPage="<%= pageStatus.getCurrentPage() %>" maxPage="<%= pageStatus.getMaxPage() %>"/>
</span>
<span class="small">Download data selected in <a href="ViewSelection.do">My Gene List</a> as an
[<a href="ViewORFParameter.do?<%=pid%>&format=xml"> XML </a> ] or 
[<a href="ViewORFParameter.do?<%=pid%>&format=tab"> Tab-separated sheet</a> ]
format. 
</span>

<table class="datasheet" cellpadding="0" cellspacing="1">
<tr>
<td class="sheetlabel" align="center"><a href="ViewORFParameter.do?sortspec=-1">ORF</a></td>
<td class="sheetlabel" align="center">Std. Name</td>
<%-- <td width="150" align="center">Aliases</td> --%>
<td></td>
<logic:iterate id="p" name="paramList" scope="request" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<td class="sheetlabel" align="center" ><a href="ViewORFParameter.do?sortspec=${p.id}">${p.name}</a></td>
</logic:iterate>
</tr>
<logic:iterate id="gene" name="geneList" scope="request" type="lab.cb.scmd.web.bean.YeastGene">
<tr>
<td nowrap="nowrap">
<html:multibox name="selection" property="inputList" value="<%= gene.getOrf().toLowerCase()%>"/>
<span class="orf"><html:link page="/ViewStats.do?orf=${gene.orf}"> ${gene.orf} </html:link> </span>
</td>
<td class="genename" align="center">${gene.standardName}</td>
<td></td>
<%--<td class="small" align="center"> ${gene.aliasString} </td> --%>
<logic:iterate id="p" name="paramList" scope="request" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<logic-nonel:equal name="p" property="id" value="${sortspec}">
<td align="right" bgcolor="#F0D0D0">
</logic-nonel:equal>
<logic-nonel:notEqual name="p" property="id" value="${sortspec}">
<td align="right">
</logic-nonel:notEqual>
<bean:write name="gene" property="parameter(${p.name})"/>
</td>
</logic:iterate>
</tr>
<tr bgcolor="#F0F0E0" height="15">
<td colspan="<%= 4 + numParam %>" width="600" class="annotation">${gene.annotation}</td>
</tr>
</logic:iterate>
</table>

<span class="small">
<scmd-base:pagemover page="ViewORFParameter.do" parameter="<%= pid%>" target="${group.groupName}" currentPage="<%= pageStatus.getCurrentPage() %>" maxPage="<%= pageStatus.getMaxPage() %>"/>
</span>

</html:form>


</center>
<scmd-base:footer/>
