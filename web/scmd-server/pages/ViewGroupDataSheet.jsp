<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="datasheet"  scope="request" class="lab.cb.scmd.web.table.Table"/>
<jsp:useBean id="sheetForm"  scope="request" class="lab.cb.scmd.web.bean.GroupByDatasheetForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="groupNameList"  scope="request" type="java.lang.String[]"/>

<scmd-base:header title="DataSheet ${gene.orf} Grouped by: ${sheetForm.group}" css="/css/tabsheet.css"/>
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
<scmd-tags:menu  toolbar="on" orf="${gene.orf}"/>


<scmd-tags:orfInfo  orf="${gene.orf}" 
	stdname="${gene.standardName}" annot="${gene.annotation}" 
	title="Grouped by ${sheetForm.groupName} (${sheetForm.group})" />

<table><tr><td>
<scmd-base:pagebutton link="ViewGroupDataSheet.do" page="${sheetForm.page}" maxPage="${sheetForm.maxPage}" name="sheetForm" property="argumentMap"/>
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
<html:link page="/ViewGroupDataSheet.do?sheetType=${i}" name="sheetForm" property="argumentMap"> <span class="small"> ${tab}</span>  </html:link> 
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
<scmd-base:pagebutton link="ViewGroupDataSheet.do" page="${sheetForm.page}" maxPage="${sheetForm.maxPage}" name="sheetForm" property="argumentMap" />
</td></tr></table>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 


<table>
<html:form action="ViewGroupDataSheet.do" method="GET">
<html:hidden property="orf"/>
<html:hidden property="page"/>
<html:hidden property="stainType"/>
<html:hidden property="group"/>
<tr><td> <scmd-tags:photoTypeSwitch/> </td></tr>
</html:form> 
</table>
<scmd-tags:searchframe/>

</center>
</body>
<scmd-base:footer/>

