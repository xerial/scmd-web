<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="para"  scope="request" class="lab.cb.scmd.web.sessiondata.MorphParameter"/>
<jsp:useBean id="range"  scope="request" class="lab.cb.scmd.web.bean.Range"/>
<jsp:useBean id="pageStatus"  scope="request" class="lab.cb.scmd.db.common.PageStatus"/>
<jsp:useBean id="selection" scope="request" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>

<scmd-base:header title="Parameter Sheet of ${para.shortName}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

<html:form action="ViewSelection.do" method="GET">
<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
</table>




<table>
<tr>
<td width="150"><img src="DrawTeardrop.do?paramID=${para.id}&rangeBegin=${range.min}&rangeEnd=${range.max}"/></td>
<td>
<table>
<tr><td class="small">Parameter Name:</td><td class="genename"><%= para.getName()%></td></tr>
<tr><td class="small">Short Name:</td><td class="small"><%= para.getShortName()%></td></tr>
<tr><td class="small">Stain Type:</td><td class="small"><%= para.getStainType()%></td></tr>
<tr><td class="small">Nucleus Status:</td><td class="small"><%= para.getNucleusStatus() %></td></tr>
<tr><td class="small">Parameter Type:</td><td class="small"><%= para.getParameterType() %></td></tr>
<tr><td class="small">Description:</td><td class="small" width="250"><%= para.getDisplayname() %></td></tr>
</table>
</td>
<td>
<% String link = "paramfig.png?param=" + para.getName(); %>
<img id="param" class="paramview" width="250" height="175" src="<%= link %>">
</td>
</tr>
</table>

<scmd-base:pagemover page="ORFDataSheet.do" target="<%= Integer.toString(para.getId())%>" currentPage="${pageStatus.currentPage}" maxPage="${pageStatus.maxPage}"/>

<table>
<tr>
<logic:iterate id="dataSet" name="orfData" scope="request">
<td valign="top">
<table class="datasheet">
<tr class="sheetlabel">
<td align="center" colspan="2">ORF</td>
<td align="center">Std. name</td>
<td align="center" title="${para.displayname}">${para.shortName}</td>
</tr>
<logic:iterate id="data" name="dataSet" type="lab.cb.scmd.web.container.ORFParamData">
<tr>
<td align="left"><p align="center"><html:multibox name="selection" property="inputList" value="${data.orf}"/></p></td>
<td class="orf"><a href="ViewStats.do?orf=${data.orf}">${data.orf}</a></td>
<td class="genename">${data.standardname}</td>
<td>
${data.data}
</td>
</tr>
</logic:iterate>
</table>
</td>
</logic:iterate>
</tr>
</table>
</html:form>

</center>
</body>
<scmd-base:footer/>
