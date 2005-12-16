<%--
//--------------------------------------
// SCMDServer
// 
// GOEnrichedParams.jsp
// Since  2005/12/15 
//
// $Date: $ ($Author: $)
// $Revision: $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<jsp:useBean id="go" scope="request" type="lab.cb.scmd.web.bean.GeneOntology"/>
<jsp:useBean id="enrichList" scope="request" type="java.util.List"/>

<scmd-base:header title="Enriched Parameters in ${go.goid}" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>

<table>
<tr><td colspan="2">
<div class="title" align="left">Enriched Parameters in <html:link page="/Search.do?keyword=${go.goid}">${go.goid}</html:link></div>
<div class="annotation" aling="left">${go.name}</div>
</td></tr>
<tr>
<td width="300"></td>
<td align="left" class="small">
Yellow: Mutants position or ratio associated with ${go.goid}<br/>
Dark Green: Whole mutants in SCMD<br/>
Blue: Low-enriched mutants, Red: High-enriched mutants<br/>
</td>
</tr>
</table>

<table>
<tr><td colspan="2" ></td></tr>
<logic:iterate id="enrich" name="enrichList" type="lab.cb.scmd.web.container.Enrichments">
<tr>
<td class="tablelabel" colspan="2">
<html:link page="/ViewORFParameter.do?columnType=input&paramID=${enrich.param}&sortspec=${enrich.param}"><%=enrich.getMorphParameter().getName()%></html:link>
p-value: <scmd-base:format format="%.2e">${enrich.pvalue}</scmd-base:format>
<div class="annotation"><%= enrich.getMorphParameter().getDisplayname() %></div>
</td>
</tr>
<bean:define id="fwdprop" value="${enrich.fwd}" />
<bean:define id="highprop" value="${enrich.high}" />
<logic:equal name="fwdprop" value="1">
<tr>
<td colspan="2"><img src="enrich.png?goid=${go.goid}&param=${enrich.param}&fwd=${enrich.fwd}&type=${enrich.high}" /><td>
</tr>
<td align="left" class="small">low</td><td align="right" class="small">high</td>
</tr>
</logic:equal>
<logic:notEqual name="fwdprop" value="1">
<logic:equal name="highprop" value="1">
<tr>
<td align="center"><img src="enrich.png?goid=${go.goid}&param=${enrich.param}&fwd=${enrich.fwd}&type=whole" width="75" height="75" /></td>
<td align="center"><img src="enrich.png?goid=${go.goid}&param=${enrich.param}&fwd=${enrich.fwd}&type=high" width="75" height="75" /></td>
</tr>
<tr>
<td align="center" class="small">whole mutants</td>
<td align="center" class="small">enriched mutants</td>
</tr>
</logic:equal>
<logic:notEqual name="highprop" value="1">
<tr>
<td align="center"><img src="enrich.png?goid=${go.goid}&param=${enrich.param}&fwd=${enrich.fwd}&type=whole" width="75" height="75" /></td>
<td align="center"><img src="enrich.png?goid=${go.goid}&param=${enrich.param}&fwd=${enrich.fwd}&type=low" width="75" height="75" /></td>
</tr>
<tr>
<td align="center" class="small">whole mutants</td>
<td align="center" class="small">enriched mutants</td>
</tr>
</logic:notEqual>
</logic:notEqual>
</logic:iterate>
</table>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 
<scmd-tags:searchframe/>

</center>
</body>

<scmd-base:footer/>

<%--
//---------------------------------------
// $Log:  $
//---------------------------------------
--%>


