<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%--
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
--%>

<jsp:useBean id="photoTable"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="groupBySheet"  scope="request" class="lab.cb.scmd.web.bean.GroupViewForm"/>

<scmd-base:header title="Group By DataSheet ${gene.orf}" css="/css/tabsheet.css"/>
<script language="JavaScript">
<!--
function help(url)
{
	helpWin = window.open(url, 'parameter', 'width=400, height=500, status=no, menubar=no, scrollbar=yes');
	helpWin.focus();
}
//-->
</script>

<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

<table width="700">
<tr><td align="left">
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
<span class="annotation"> <%= gene.getAnnotation() %></span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${gene.orf}"/>
</p></td></tr>
<tr><td colspan=2>
<p class="header"> Cells Grouped by <%= lab.cb.scmd.web.common.GroupType.STAIN_GROUP[groupBySheet.getStainType()] %> </p>
</td>
</tr>
</table>

<scmd-base:table name="photoTable"/>

<scmd-base:pagebutton link="ViewGroupBySheet.do" page="${groupBySheet.page}" maxPage="${groupBySheet.maxPage}" name="groupBySheet" property="argumentMap" />


<html:form action="ViewGroupBySheet.do" method="POST">
<html:hidden property="orf"/>
<html:hidden property="stainType"/>
<html:hidden property="page"/>
<scmd-tags:photoTypeSwitch/>
</html:form>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/>

</center>
</body>
<scmd-base:footer/>