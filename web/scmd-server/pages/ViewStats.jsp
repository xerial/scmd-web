<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="statsTable0"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="statsTable1"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="statsTable2"  scope="request" class="lab.cb.scmd.web.table.Table"/>

<scmd-base:header title="Stats ${view.orf}" css="/css/tabsheet.css"/>


<script language="JavaScript">
<!--
function on(idname)
{
   document.getElementById(idname).className="sheetlabel_on";
}
function off(idname)
{
   document.getElementById(idname).className="sheetlabel";
}
function help(url)
{
	helpWin = window.open(url, 'parameter', 'width=400, height=500, status=no, menubar=no, scrollbar=yes');
	helpWin.focus();
}
//-->
</script>

 

<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on" orf="${gene.orf}"/>


<table width="750">
<tr><td align="left">
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
<span class="annotation"> <%= gene.getAnnotation() %></span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${view.orf}"/>
</p>
</td>
</tr>
</table>

<p align="center" class="title"> Average Shapes of ${view.orf} </p>

<scmd-base:table name="statsTable0"/>
<p class="title"> Average Shapes Grouped by Bud Size </p>


<scmd-base:table name="statsTable1"/>
<p class="title"> Average Shapes Grouped by Nucleus Location </p>
<scmd-base:table name="statsTable2"/>
<p class="title"> Average Shapes Grouped by (peculiar) Nucleus Location </p>

<scmd-base:table name="statsTable3"/>
<p class="title"> Average Shapes Grouped by Actin Distribution </p>

<scmd-tags:linkMenu orf="${view.orf}" logo="on"/>

<scmd-base:footer/>