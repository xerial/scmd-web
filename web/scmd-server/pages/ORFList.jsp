<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<scmd-base:header title="Yeast Mutants"/>
<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on"/>

<%@page import="lab.cb.scmd.web.bean.*"%>
<%@page import="java.util.*"%>
<%@page import="lab.cb.scmd.db.common.PageStatus"%>


<jsp:useBean id="groupList" scope="request" type="java.util.List"/>
<jsp:useBean id="listPage" scope="session" class="lab.cb.scmd.web.bean.ORFListViewForm"/>

<html:form action="ViewSelection.do" method="GET">
<table width="750">
<tr><td>
<p align="right"><html:submit value="add selections"/></p>
</td></tr>
</table>

<logic:iterate id="group"  collection="${groupList}" type="lab.cb.scmd.web.bean.ORFGroup">
<center>
<table width="750">
<tr height="20"><td></td> </tr>
<tr><td align="left" class="title"> ${group.groupName}: </td></tr>
</table>
<table>
<tr><td align="center" class="small"> 
<% PageStatus pageStatus = group.getPageStatus(); %>
<scmd-base:pagemover page="ViewORFList.do" target="${group.groupName}" currentPage="<%= pageStatus.getCurrentPage() %>" maxPage="<%= pageStatus.getMaxPage() %>"/>
</td> </tr>
</table>

<table border="0" cellspacing="0" cellpadding="0">
<tr>
<td colspan="2" class="sheetlabel" width="105"> ORF </td> 
<td class="sheetlabel" witdh="90"> Std. Name </td> 
<td class="sheetlabel" width="150"> Aliases </td> 
<td colspan="3" class="sheetlabel"></td>
</tr>
<logic:iterate id="yeastGene" name="group" property="orfList" type="lab.cb.scmd.web.bean.YeastGene">
<tr class="small"> 
<td align="left" width="15"><p align="center"><html:multibox property="inputList" value="<%= yeastGene.getOrf().toLowerCase()%>"/></p></td>
<td align="left" class="orf" width="90"> 
<html:link page="/ViewStats.do?orf=${yeastGene.orf}"> ${yeastGene.orf}  </html:link> 
</td> 
<td class="genename" align="center"> ${yeastGene.standardName} </td> 
<td class="small" align="center"> ${yeastGene.aliasString} </td> 
<td width="40"/>
<td width="240">
<html:link page="/ViewPhoto.do?orf=${yeastGene.orf}"> photo viewer </html:link>&nbsp;
<html:link page="/ORFTeardrop.do?orf=${yeastGene.orf}"> teardrop </html:link>&nbsp;
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${yeastGene.orf}" target="_blank">CYGD</a>&nbsp;
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${yeastGene.orf}" target="_blank">SGD</a>
<%--<td width="40"><p align="center"> <scmd-tags:selectorf orf="${yeastGene.orf}"/></p></td>--%>
</tr>
<tr bgcolor="#F0F0E0" height="15">
<td ></td>
<td colspan="5" width="530" class="annotation"> <%= yeastGene.getAnnotation() %></td> 
</tr>
<tr height="10"><td> </td></tr>

</logic:iterate>

</table>

<table>
<tr><td>
<scmd-base:pagemover page="ViewORFList.do" target="${group.groupName}" currentPage="<%= pageStatus.getCurrentPage() %>" maxPage="<%= pageStatus.getMaxPage() %>"/>
</td></tr>
</center>
</logic:iterate>
</html:form>

 
</body>
<scmd-base:footer/>