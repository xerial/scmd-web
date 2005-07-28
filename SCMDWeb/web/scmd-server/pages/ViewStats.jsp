<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="statsTable0"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="statsTable1"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="statsTable2"  scope="request" class="lab.cb.scmd.web.table.Table"/>

<scmd-base:header title="Average Shape ${view.orf}" css="/css/tabsheet.css"/>


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
	helpWin = window.open(url, 'parameter');
	helpWin.focus();
}
//-->
</script>

 

<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on" orf="${gene.orf}"/>



<scmd-tags:orfInfo orf="${gene.orf}" annot="${gene.annotation}" stdname="${gene.standardName}"/>

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