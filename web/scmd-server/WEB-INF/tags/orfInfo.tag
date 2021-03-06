<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="headtitle" required="false" %>
<%@ attribute name="orf" required="true" %>
<%@ attribute name="stdname" required="false" %>
<%@ attribute name="annot" required="false" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="cellimage" required="false" %>

<table width="700" border="0">
<tr height="30">



<td align="left" valign="bottom" nowrap="nowrap">
<span class="orf"> ${orf} </span> 
<span class="genename"> ${stdname} </span>
</td>

<td align="left" valign="bottom">
<!-- link for photo and mining pages -->
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<td id="subphoto" onMouseOver="selectMenu('subphotomenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<td class="button">[<html:link page="/ViewPhoto.do">photo</html:link>]</td></tr>
<tr><td>
<table width="200" id="subphotomenu" class="dropdownmenu"> 
<tr>
<td  nowrap="nowrap">
<ul>
<li><html:link page="/ViewPhoto.do?orf=${orf}"> Photo Viewer </html:link></li>
<li><html:link page="/ViewDataSheet.do?orf=${orf}"> Individual Cell Datasheet</html:link> </li>
<li> Cells Grouped by </li>
<ul>
<li> <html:link page="/ViewGroupBySheet.do?stainType=0&orf=${orf}"> bud size </html:link></li>
<li> <html:link page="/ViewGroupBySheet.do?stainType=1&orf=${orf}"> nucleus location </html:link></li>
<li> <html:link page="/ViewGroupBySheet.do?stainType=2&orf=${orf}"> actin distribution </html:link> </li>
</ul>
</ul>
</td>
</tr>
</table>   
</td>

</tr>
</table>
</td>
    
<td id="subdata" onMouseOver="selectMenu('subdataminingmenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr><td class="button">[<html:link page="/ViewStats.do?orf=${orf}">stats & mining</html:link>]</td></tr>
<tr><td>
<table  width="300" class="dropdownmenu" cellpadding="0" id="subdataminingmenu" align="left">
<tr>
<td  nowrap="nowrap">
<ul>
<li> <html:link page="/ORFTeardrop.do?orf=${orf}">Teardrop View of ORF Parameters </html:link></li>
<li> <html:link page="/ViewORFParameter.do">ORF Parameter Datasheet</html:link></li>
<li><html:link page="/View2DPlot.do?orfType=current">2D Plot</html:link></li>
<li> <html:link page="/ViewStats.do?orf=${orf}">Average Shapes</html:link> </li>
<li>Teardrop View of Average Shapes Grouped by</li>
<ul>
<li><html:link page="/ViewGroupByTearDrop.do?stainType=0&orf=${orf}"> bud size </html:link></li>
<li><html:link page="/ViewGroupByTearDrop.do?stainType=1&orf=${orf}"> nucleus location</html:link></li>
<li><html:link page="/ViewGroupByTearDrop.do?stainType=2&orf=${orf}"> actin distribution </html:link></li>
</ul>
</ul>
</td>
</tr>
</table>
</td></tr>
</table>
</td>


</tr>
</table>
<!-- end of links for photo and mining pages -->
</td>

<td class="averageshape" height="10" nowrap="nowrap"><p valign="bottom" class="small"><scmd-tags:selectorf orf="${orf}"/></p></td>

<logic:equal name="cellimage"  value="on">
<!-- cell synthetic image -->
<td align="left" valign="bottom" rowspan="2">
<table><tr><td align="center">
<html:link page="/ViewStats.do?orf=${orf}">
<img src="cellshape.png?orf=${orf}&clip=true" border="0" alt="${orf}" />
</html:link>
</td></tr>
<tr><td align="center">
<span class="annotation">Characteristic Shape</span>
</td>
</tr>
</table>
</td>
</logic:equal>

</tr>
<tr>
<td align="left" valign="top" colspan="3">
<span class="annotation"> ${annot} </span>
<span class="small">
( links to: 
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${orf}" target=_blank>CYGD</a>
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${orf}" target=_blank>SGD</a> )
</span>
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


