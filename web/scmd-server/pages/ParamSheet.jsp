<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="param"  scope="request" class="lab.cb.scmd.web.sessiondata.MorphParameter"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="userSelection"  scope="session" class="lab.cb.scmd.web.bean.UserSelection"/>

<scmd-base:header title="<%= param.getName() %>"/>

<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

<table>
<tr><td class="small">Parameter Name:</td><td class="genename"><%= param.getName()%></td></tr>
<tr><td class="small">Short Name:</td><td class="small"><%= param.getShortName()%></td></tr>
<tr><td class="small">Stain Type:</td><td class="small"><%= param.getStainType()%></td></tr>
<tr><td class="small">Nucleus Status:</td><td class="small"><%= param.getNucleusStatus() %></td></tr>
<tr><td class="small">Parameter Type:</td><td class="small"><%= param.getParameterType() %></td></tr>
<tr><td class="small">Description:</td><td class="small"><%= param.getDisplayname() %></td></tr>
</table>
<% String link = "paramfig.png?param=" + param.getName(); %>
<img id="param" class="paramview" width="250" height="175" src="<%= link %>">

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

</center>
</body>
<scmd-base:footer/>
