<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="headtitle" required="false" %>
<%@ attribute name="orf" required="true" %>
<%@ attribute name="stdname" required="false" %>
<%@ attribute name="annot" required="false" %>
<%@ attribute name="title" required="false" %>

<table width="700">
<tr><td align="left">
<span class="orf"> ${orf} </span> 
<span class="genename"> ${stdname} </span>
<span class="annotation"> ${annot} </span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${orf}"/>
</p>
</td>
</tr>
</table>

<logic:present name="title">
<table align="center">
<tr>
<td colspan="4" align="center"> 
<span class="title"> ${title} </span> 
<span class="header"> ${headtitle} </span>
</td>
</tr>
</table>
</logic:present>


