<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="tableList"  scope="request" type="java.util.Collection"/>
<jsp:useBean id="sheetForm"  scope="request" class="lab.cb.scmd.web.bean.GroupByDatasheetForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="groupNameList"  scope="request" type="java.lang.String[]"/>

<%
 String groupTitle = lab.cb.scmd.web.common.GroupType.STAIN_GROUP[sheetForm.getStainType()];
%>

<scmd-base:header title="Teardrop View of ${gene.orf}" css="/css/tabsheet.css"/>

<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 


<table width="700">
<tr>
<td>
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${gene.orf}"/>
</p>
</td>
</tr>
<tr> <td colspan="2" align="center"> <span class="title">Averages of Cell Shape Parameters</span> <span class="header"> Grouped by <%= groupTitle%> </span> </td></tr>
</table>

<logic:iterate id="datasheet" name="tableList" type="lab.cb.scmd.web.table.Table">
<scmd-base:table name="datasheet"/>
</logic:iterate>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

</center>
</body>
<scmd-base:footer/>
