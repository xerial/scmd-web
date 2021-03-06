<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>

<scmd-base:header title="Teardrop View of ${view.orf}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on" orf="${view.orf}"/>


<scmd-tags:orfInfo  orf="${gene.orf}" 
	stdname="${gene.standardName}" annot="${gene.annotation}" 
	cellimage="on"
	title="Teardrop View of ORF Parameters" />

<table>
<tr><td><scmd-base:table name="shadingTable"/></td></tr>
<tr><td>
<table class="small">
<tr>
<td width="40" align="center" valign="top">-4SD</td>
<td width="40" align="center" valign="top">-3SD</td>
<td width="40" align="center" valign="top">-2SD</td>
<td width="40" align="center" valign="top">-1SD</td>
<td width="40" align="center" valign="top"> 
avg.
</td>
<td width="40" align="center" valign="top">+1SD</td>
<td width="40" align="center" valign="top">+2SD</td>
<td width="40" align="center" valign="top">+3SD</td>
<td width="40" align="center" valign="top">+4SD</td>
</tr>
<tr><td align="center" colspan="9">(wildtype)</td></tr>
</table>
</td></tr>
</table>

<%-- データシート切り替えTab --%>
<table border="0">
<tr>
<td align="left">
<table border="0">
<tr>
<scmd-base:tablist selected="${view.sheetType}">
<logic:iterate id="tab" name="orfTeardropForm" property="sheetTypeList" indexId="i">
<scmd-base:tab name="${i}" width="80"> 
<html:link page="/ORFTeardrop.do?orf=${view.orf}&sheetType=${i}"> <span class="small"> ${tab}</span>  </html:link> 
</scmd-base:tab>
</logic:iterate>
</scmd-base:tablist>
<td width="300" class="small" align="right">
(click the teardrops below to see detail data)
</td>
</tr>
</table>
</td>
</tr>
<tr>
<td>
<scmd-base:table name="teardropSheet"/> 
</td>
</tr>
</table>



<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

<scmd-tags:searchframe/>

</center>
</body>
<scmd-base:footer/>
