<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ attribute name="headtitle" required="false" %>
<%@ attribute name="orf" required="true" %>
<%@ attribute name="stdname" required="false" %>
<%@ attribute name="annot" required="false" %>
<%@ attribute name="title" required="true" %>

<table width="700">
<tr>
<td valign="top">
<span class="orf"> ${orf} </span> 
</td>
<td valign="top" rowspan="2">
<span class="annotation"> ${annot} </span>
</td>
<td align="right" rowspan="2">
<p align="absbottom">
<html:form action="ViewSelection.do" method="POST">
<html:hidden property="input" value="${orf}"/>
<html:submit value="select this mutant"/>
</html:form>
</p>
</td>
</tr>
<tr>
<td valign="top">
<span class="genename"> ${stdname} </span>
</td>
</tr>
 <tr> <td colspan="4" align="center"> <span class="title"> ${title} </span> 
<span class="header"> ${headtitle} </span>
</td></tr>
</table>

