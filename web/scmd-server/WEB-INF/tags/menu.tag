<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="toolbar" required="false" %>
<%@ attribute name="searchframe" required="false" %>
<%@ attribute name="top" required="false"%>

<script language="javascript" src="js/styleutil.js"></script>

<table width="750" border="0" cellspacing=0 cellpadding=0>
<tr>
<logic:equal name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo.png" alt="SCMD" border="0"/></html:link></td>
</logic:equal>
<logic:notEqual name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo_grad.png" alt="SCMD" border="0"/></html:link></td>
</logic:notEqual>
<td width="450" valign="bottom" align="left"> 
 <html:link page="/">
   <html:img page="/image/scmd_title.png" align="absbottom" width="467" height="22" 
   alt="Saccharomyces Cerevisiae Morphological Database" border="0"/>
 </html:link>
</td>
</tr>
</table>
<table width="750" border="0" bgcolor="#70b0b0" class="menubar" cellpadding="0">
  <tr> 
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover'); selectMenu('yeastmenu');" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="ViewORFList.do">Yeast Mutant</a></td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover'); selectMenu('photomenu');" onMouseOut="switchStyle(this, 'menu_bar'); "> <a href="ViewPhoto.do">Photo</a> </td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover'); selectMenu('teardropmenu');" onMouseOut="switchStyle(this, 'menu_bar');"> Data Mining </td>
<%--    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="ViewStats.do"> Average Shape </a> </td>     --%>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> Customization </td>     
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="sitemap.jsp">Site Map</a> </td>     
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="publication.jsp">Publications</a> </td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="about.jsp">About SCMD</a> </td>
</tr>
<tr>
<td>
<table border="0" width="100" bgcolor="#70b0b0" class="dropdownmenu" cellpadding="0" id="yeastmenu">
<tr><td> Wildtype </td></tr>
<tr><td> Mutant </td></tr>
</table>
</td>

<td>
<table border="0" width="150" bgcolor="#70b0b0" class="dropdownmenu" cellpadding="0" id="photomenu">
<tr><td><a href="ViewPhoto.do"> Photo </a></td></tr>
<tr><td><a href="ViewDataSheet.do"> Individual Cell Datasheet</a> </td></tr>
</table>
</td>

<td>
<table border="0" width="180" bgcolor="#70b0b0" class="dropdownmenu" cellpadding="0" id="dataminingmenu">
<tr><td> Teardrop View of ORF Parameters </td></tr>
<tr><td> Average Shape </td></tr>
<tr><td> 2D plot </td></tr>
<tr><td> 2D plot </td></tr>
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
	    <td class="tool" width="150" onMouseOver="switchStyle(this,'tool_on')" onMouseOut="switchStyle(this,'tool')"> <a href="ViewSelection.do">view your selection</a> </td>
    	<td class="tool" width="150" onMouseOver="switchStyle(this,'tool_on')" onMouseOut="switchStyle(this,'tool')"> <a href="CustomizeView.do">customize view</a></td>
     </tr>
	</table>    
   </td>
  </tr>
</table>
</logic:equal>
<logic:equal name="searchframe" value="on">
<table width="700" border="0"><tr><td><scmd-tags:searchframe/></td></tr></table>
</logic:equal>



