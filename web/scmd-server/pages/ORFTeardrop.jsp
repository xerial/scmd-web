<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>

<scmd-base:header title="Teardrop View of ${view.orf}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on"/>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

<table width="700">
<tr><td align="left">
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${gene.orf}"/>
</p>
</td>
</tr>
</table>


<table><tr><td>
<scmd-tags:pageMoveButton actionURL="ViewDataSheet.do" currentPage="${view.photoPage}" maxPage="${view.photoPageMax}"/>
</tr></td></table>

<%-- データシート切り替えTab --%>
<table border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="left">
<table border="0">
<tr>
<scmd-base:tablist selected="${view.sheetType}">
<logic:iterate id="tab" name="tabName" indexId="i">
<scmd-base:tab name="${i}" width="80"> 
<html:link page="/ViewDataSheet.do?sheetType=${i}"> <span class="small"> ${tab}</span>  </html:link> 
</scmd-base:tab>
</logic:iterate>
</scmd-base:tablist>
</tr>
</table></td></tr>

<tr><td>
<scmd-base:table name="datasheet"/>
</td>
</tr>
</table>

<table><tr><td>
<scmd-tags:pageMoveButton actionURL="ViewDataSheet.do" currentPage="${view.photoPage}" maxPage="${view.photoPageMax}"/>
</tr></td></table>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 


<table>
<html:form action="ViewDataSheet.do" method="POST">
<tr><td> <scmd-tags:photoTypeSwitch/> </td></tr>
</html:form> 
</table>
<scmd-tags:searchframe/>

</center>
</body>
<scmd-base:footer/>
