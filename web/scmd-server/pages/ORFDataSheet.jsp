<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="param"  scope="request" class="lab.cb.scmd.web.sessiondata.MorphParameter"/>
<jsp:useBean id="range"  scope="request" class="lab.cb.scmd.web.bean.Range"/>

<scmd-base:header title="Teardrop View of ${view.orf}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

<scmd-base:pagemover page="ORFDataSheet.do" target="<%= Integer.toString(param.getId())%>" currentPage="${page.currentPage}" maxPage="${page.maxPage}"/>

<table>
<tr>
<td><img src="DrawTeardrop.do?paramID=<%= param.getId()%>&rangeBegin=${range.min}&rangeEnd=${range.max}"/></td>
</tr>
</table>

<table class="datasheet">
<tr class="sheetlabel">
<td align="center">ORF</td>
<td align="center" title="<bean:write name="param" property="displayname"/>">
<bean:write name="param" property="shortName"/>
</td>
</tr>
<logic:iterate id="data" name="orfData" scope="request" type="lab.cb.scmd.web.container.ORFParamData">
<tr>
<td class="orf"><a href="ViewStats.do?orf=${data.orf}">${data.orf}</a></td>
<td>${data.data}</td>
</tr>
</logic:iterate>
</table>

</center>
</body>
<scmd-base:footer/>
