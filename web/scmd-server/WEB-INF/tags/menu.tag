<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib uri="/WEB-INF/c-rt.tld" prefix="c" %> 
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="toolbar" required="false" %>
<%@ attribute name="searchframe" required="false" %>
<%@ attribute name="top" required="false"%>
<%@ attribute name="orf" required="false"%>

<logic:equal name="orf" value="">
<logic:equal name="view" value="">
<c:set var="orf" value="yor202w"/>
</logic:equal>
<logic:notEqual name="view" value="">
<c:set var="orf" value="${view.orf}"/>
</logic:notEqual>
</logic:equal>

<script type="text/javascript" language="javascript" src="js/styleutil.js"></script>


<table width="700" border="0" cellspacing=0 cellpadding=0>
<tr>
<logic:equal name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo.png" alt="SCMD" border="0"/></html:link></td>
</logic:equal>
<logic:notEqual name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo_grad.png" alt="SCMD" border="0"/></html:link></td>
</logic:notEqual>
<td width="400" valign="bottom" align="left"> 
 <html:link page="/">
   <html:img page="/image/scmd_title.png" align="bottom" width="467" height="22" 
   alt="Saccharomyces Cerevisiae Morphological Database" border="0"/>
 </html:link>
</td>
</tr>
</table>

<table width="700" border="0" cellpadding="0" cellspacing="0">
<tr class="menubar">
<td width="5"></td>
<td align="center" id="yeast" onMouseOver="closeAllMenu();"><a href="ViewORFList.do">Mutant List</a></td>
<td align="center" id="parameter" onMouseOver="closeAllMenu();"><a href="ParameterHelp.do">Parameter List</a></td>

<td align="center" id="photo" onMouseOver="selectMenu('photomenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr class="menubar"><td><a href="ViewPhoto.do">Photo</a></td></tr>
<tr><td>
<table width="200" id="photomenu" class="dropdownmenu"> 
<tr>
<td  nowrap="nowrap">
<ul>
<li><a href="ViewPhoto.do?orf=${orf}"> Photo Viewer </a></li>
<li><a href="ViewDataSheet.do?orf=${orf}"> Individual Cell Datasheet</a> </li>
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

<td align="center" id="data" onMouseOver="selectMenu('dataminingmenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr class="menubar"><td><html:link page="/ViewStats.do?orf=${orf}">Data Mining</html:link></td></tr>
<tr><td>
<table  width="300" class="dropdownmenu" cellpadding="0" id="dataminingmenu" align="left">
<tr>
<td  nowrap="nowrap">
<ul>
<li> <a href="SelectShape.do">Morphology Search </a></li>
<li> <a href="ORFTeardrop.do?orf=${orf}">Teardrop View of ORF Parameters </a></li>
<li> <a href="ViewORFParameter.do">ORF Parameter Datasheet</a></li>
<li><a href="View2DPlot.do?orfType=current">2D Plot</a></li>
<li> <a href="ViewStats.do?orf=${orf}">Average Shapes</a> </li>
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




<td align="center" id="customize" onMouseOver="selectMenu('customizemenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr class="menubar"><td><html:link page="/CustomizeView.do">Customization</html:link></td></tr>
<tr><td>
<table border="0" width="150" class="dropdownmenu" id="customizemenu">
<tr><td>
<ul>
<li> <html:link page="/ViewSelection.do"> My Gene List</html:link> </li>
<li> <html:link page="/CustomizeView.do"> My Parameter List </html:link></li>
</ul>
</td></tr>
</table>
</td></tr>
</table>
</td>

<td align="center" id="help" onMouseOver="selectMenu('hmenu');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr class="menubar"><td><a href="."> Help </a></td></tr>
<tr><td>
<table width="150" border="0" class="dropdownmenu" cellpadding="0" id="hmenu" align="left">
<tr><td nowrap="nowrap"> 
<ul>
<li><a href="ParameterHelp.do">Parameter Help</a> </li>
<li> <a href="sitemap.jsp">Site Map </a></li>
</ul>
</td></tr>
</table>
</td></tr>
</table>    

</td>     


<td align="center" id="about" onMouseOver="selectMenu('aboutscmd');" onMouseOut="detectMouseOut(event,this);">
<table cellpadding="0" cellspacing="0">
<tr class="menubar"><td><a href="."> About SCMD</a></td></tr>
<tr><td>
<table width="120" border="0" class="dropdownmenu" cellpadding="0" id="aboutscmd" align="left">
<tr>
<td  nowrap="nowrap">
<ul>
<li> <a href="publication.jsp"> Publications</a> </li>
<li> <a href="about.jsp"> Info. & Staffs </a> </li>
</ul>
</td>
</tr>
</table>
</td></tr>
</table>    
</td></tr>
</table>   
</td>

</tr>
</table>

<logic:equal name="toolbar"  value="on">
<table width="700" border="0" class="small" cellpadding="0">
  <tr> 
 	<td> </td>
   <td align="right">
    <table>
     <tr align="top">
	    <td class="tool" width="150"> <a href="ViewSelection.do">My Gene List</a> </td>
    	<td class="tool" width="150"> <a href="CustomizeView.do">My Parameter List</a></td>
     </tr>
	</table>    
   </td>
  </tr>
</table>
</logic:equal>
<logic:equal name="searchframe" value="on">
<table width="700" border="0"><tr><td><scmd-tags:searchframe/></td></tr></table>
</logic:equal>



