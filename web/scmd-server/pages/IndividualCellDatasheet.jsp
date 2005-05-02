<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>

<scmd-base:header title="Individual Cell Datasheet of ${view.orf}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on" orf="${gene.orf}" />

<scmd-tags:orfInfo  orf="${gene.orf}" 
	stdname="${gene.standardName}" annot="${gene.annotation}" 
	title="Individual Cell Datasheet" />

<c:if test="${view.sheetType == 4}">
<table>
<tr>
<td width="500"></td>
<td class="tool" width="150" align="right"> <a href="CustomizeView.do">customize parameters</a></td>
</tr>
</table>
</c:if>

<table>
<tr><td class="menubutton" align="center">[<a href="ViewPhoto.do?orf=${view.orf}">Photo Viewer</a>]</td></tr>
<tr><td>
<scmd-tags:pageMoveButton actionURL="ViewDataSheet.do" currentPage="${view.photoPage}" maxPage="${view.photoPageMax}"/>
</td>
</tr>
</table>


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

<tr>
<td>
<table class="datasheet">

<tr class="sheetlabel">
<logic:iterate id="stainName" collection="<%= lab.cb.scmd.web.common.StainType.TAB_NAME%>">
<td>${stainName}</td>
</logic:iterate>
<logic:iterate id="p" name="paramList" scope="request" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<td title="${p.displayname}">
<html:link page="/ViewORFParameter.do?columnType=input&paramID=${p.id}">${p.name}</html:link>
</td>
</logic:iterate>
</tr>

<logic:iterate id="c" name="cells" scope="request" type="lab.cb.scmd.web.bean.IndividualCell">
<tr>
<logic:iterate id="stain" collection="<%= lab.cb.scmd.web.common.StainType.getStainTypes()%>">
<td class="cellimg">
<html:img page="/scmdimage.png?encoding=jpeg&imageID=${c.imageID[stain]}" border="0" width="${c.width}" height="${c.height}"/> 
</td>
</logic:iterate>

<logic:iterate id="p" indexId="col" name="paramList" scope="request" type="lab.cb.scmd.web.sessiondata.MorphParameter">
<td align="right" 
<c:if test="${col % 3 == 0}">bgcolor="#F8F8F8"</c:if>
<c:if test="${col % 3 == 1}">bgcolor="#E0F0F0"</c:if>
<c:if test="${col % 3 == 2}">bgcolor="#F0F0E0"</c:if>
>
<bean:write name="c" property="value(${p.name})"/>
</td>
</logic:iterate>
</tr>

</logic:iterate>
</table>
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
