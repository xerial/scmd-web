<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="targetCell"  scope="request" class="scmd.web.common.Cell"/>
<jsp:useBean id="cellSheet"  scope="request" class="scmd.web.table.Table"/>

<scmd-base:header title="Cell Info. ${targetCell.photo.orf}" css="/css/tabsheet.css"/>
<body>

<center>
<table>
<tr height="20"> <td> </td> </tr>
<c:set var="width" value="${targetCell.boundingRectangle.x2 - targettCell.boundingRectangle.x1 + 4}"/>
<c:set var="height" value="${targetCell.boundingRectangle.y2 - targetCell.boundingRectangle.y1 + 4}"/>
<c:set var="arg" value="${targetCell.boundingRectangle.cgiArgument}"/>
<tr height="${height}">
<c:forEach var="stainType" begin="0" end="2">
<td bgcolor="black" align="center">
<html:img align="center" page="/DisplayCell.do?orf=${targetCell.photo.orf}&photoNum=${targetCell.photo.photoNum}&photoType=${view.photoType}&stainType=${stainType}&${arg}" border="0"  alt="ID=${targetCell.cellID}"/>
</td>
</c:forEach>
</tr>
</table>

<table>
<tr>
<scmd-base:tablist selected="${view.sheetType}">
<logic:iterate id="tab" name="tabName" indexId="i">
<scmd-base:tab name="${i}"> 
<html:link page="/ViewCellInfo.do?cellID=${targetCell.cellID}&sheetType=${i}"> <span class="small"> ${tab}</span></html:link>
</scmd-base:tab>
</logic:iterate>
</scmd-base:tablist>
</tr>
</table>

<%
	cellSheet.setProperty("class", "small");
	cellSheet.setProperty("width", "150");
	cellSheet.setProperty("cellspacing", "0");	
%>
<scmd-base:table name="cellSheet"/>
<%--
<table class="small" width="150" cellspacing="0">
<tr height="20"> <td> </td> </tr>

<logic:iterate id="label" name="cellSheet" property="colLabelList" type="java.lang.String">
<tr>
<td align="left" class="tablelabel"> ${label} </td>
<td align="right"> <%= cellSheet.getCell((new Integer(targetCell.getCellID())).toString(), label) %></td>
</tr>
</logic:iterate>
</table>
--%>

<input type="button" value="close" onClick="window.close()"/>

</center>
</body>
<scmd-base:footer/>
