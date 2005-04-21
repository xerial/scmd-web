<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="headtitle" required="false" %>
<%@ attribute name="orf" required="true" %>
<%@ attribute name="stdname" required="false" %>
<%@ attribute name="annot" required="false" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="cellimage" required="false" %>

<table width="700">
<tr><td align="left" valign="bottom" rowspan="2">
<span class="orf"> ${orf} </span> 
<span class="genename"> ${stdname} </span>
<span class="annotation"> ${annot} </span>
<span class="small">
( links to: 
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${orf}" target=_blank>CYGD</a>
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${orf}" target=_blank>SGD</a> )
</span>
</td>
<logic:equal name="cellimage"  value="on">
<td align="left" valign="bottom">
<img src="cellshape.png?orf=${orf}" alt="${orf}" />
</td>
</logic:equal>
<td align="right" rowspan="2">
<p align="absbottom">
<scmd-tags:selectorf orf="${orf}"/>
</p>
</td>
</tr>
<logic:equal name="cellimage"  value="on">
<tr><td align="center"><span class="small">Average Shape</span></td></tr>
</logic:equal>
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


