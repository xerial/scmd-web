<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="toolbar" required="false" %>
<%@ attribute name="searchframe" required="false" %>
<%@ attribute name="top" required="false"%>

<script language="javascript" src="js/styleutil.js"></script>

<table width="700" border="0" cellspacing=0 cellpadding=0>
<tr>
<logic:equal name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo.png" alt="SCMD" border="0"/></html:link></td>
</logic:equal>
<logic:notEqual name="top" value="true"> 
<td width="110"><html:link page="/"><html:img page="/image/scmd_logo_grad.png" alt="SCMD" border="0"/></html:link></td>
</logic:notEqual>
<td width="540" valign="bottom" align="center"> 
 <html:link page="/">
   <html:img page="/image/scmd_title.png" align="absbottom" width="467" height="22" 
   alt="Saccharomyces Cerevisiae Morphological Database" border="0"/>
 </html:link>
</td>
</tr>
</table>
<table width="700" border="0" bgcolor="#70b0b0" class="menubar" cellpadding="0">
  <tr> 
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="ViewORFList.do">Yeast Mutant</a></td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="SelectShape.do">Morphology Search</a> </td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="ViewPhoto.do">Cell Viewer</a> </td>
    <td align="center" width="100" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="ViewStats.do"> Stats </a> </td>     
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="publication.jsp">Publications</a> </td>
    <td align="center" onMouseOver="switchStyle(this,'menubar_hover')" onMouseOut="switchStyle(this, 'menu_bar');"> <a href="about.jsp">About SCMD</a> </td>
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



