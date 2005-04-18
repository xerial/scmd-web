<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="logic-nonel" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>


<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>

<scmd-base:header title="Cell Parameter" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on" orf="${view.orf}"/>

<p class="title">Cell Parameter</p>

<logic-nonel:present name="targetParam" scope="request">
<table>
<tr>
<td>
<table>
<tr><td class="small">Parameter Name:</td><td class="genename">${targetParam.name}</td></tr>
<%--<tr><td class="small">Short Name:</td><td class="small">${targetParam.shortName}</td></tr>--%>
<tr><td class="small">Stain Type:</td><td class="small">${targetParam.stainType}</td></tr>
<tr><td class="small">Nucleus Status:</td><td class="small">${targetParam.nucleusStatus} </td></tr>
<tr><td class="small">Parameter Type:</td><td class="small">${targetParam.parameterType}</td></tr>
<tr><td class="small">Value Type:</td><td class="small">${targetParam.datatype}</td></tr>
<tr><td class="small">Description:</td><td class="small" width="250">${targetParam.displayname}</td></tr>
<tr><td class="small">Definition:</td><td class="small" width="250">${targetParam.htmlDefinition}</td></tr>
</table>
</td>
<td>
<img id="param" class="paramview" width="250" height="175" src="paramfig.png?param=${targetParam.name}">
</td>
</tr>
</table>
<%--<span class="header"> order by ${targetParam.name} </span>--%>
</logic-nonel:present>


</center>
<scmd-base:footer/>
