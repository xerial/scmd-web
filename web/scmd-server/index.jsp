
<%--
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<logic:redirect forward="photoviewer"/>

Redirect default requests to Welcome global ActionForward.
By using a redirect, the user-agent will change address to match the path of our Welcome ActionForward. 

--%>

<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-base"  uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base"%>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags"  %>
<scmd-base:header title="Sacharomyces Cerevisiae Morphological Database" css="css/toppage.css"/>
<body class="toppage">
<scmd-tags:menu searchframe="on" top="true"/>
 

<table width="650" border="0">
<tr> 
<td width="34%" bordercolor="#FFFFFF"> 
 <p align="center">
  <html:img src="/image/sample_photo.jpg" width="155" height="158" border="0" 
  alt="sample image">
  </p>
</td>

<td width="66%"> 

<p> <a href="http://scmd.gi.k.u-tokyo.ac.jp"> The <i>Saccharomyces
Cerevisiae</i> Morphological Database(SCMD)</a> is a collection of
micrographs of budding yeast mutants. Micorgraphs of mutants with
altered cell morphology were taken at <a
href="http://www.biol.s.u-tokyo.ac.jp/users/sonoike/top.html">Ohya
Group</a>, <a href="http://www.u-tokyo.ac.jp">University of Tokyo</a>,
from a set of the haploid MATa deleted strains obtained from
EUROSCARF.  From the micrographs, disruptant cells are automatically
extracted by our novel cell-image processing software developed at <a
href="http://www.gi.k.u-tokyo.ac.jp">Morishita Group</a>, <a
href="http://www.u-tokyo.ac.jp">University of Tokyo</a>.</p>
       
<p>We plan to update <a href="http://scmd.gi.k.u-tokyo.ac.jp">the
SCMD </a> weekly and made the data available at <a
href="http://scmd.gi.k.u-tokyo.ac.jp">http://scmd.gi.k.u-tokyo.ac.jp</a>
.</p>

</td>
</tr>
</table>
<table width="400" border="0" class=newsframe>
  <tr> 
    <td colspan=3 align=center>
      <span class=title> Current Status </span>
	</td>
  </tr> 
  <tr> <td colspan=3 align=center class=time> Updated: Tuesday, June 29, 2004 20:55:37 PM JST (GMT +0900) </td>
  </tr>
<tr> 
 <td width="63%" height="22" class=item> Mutants analyzed:</td>
 <td width="37%" height="22" align=right> 4677 </td>
</tr>
<tr> 
 <td width="63%" height="22" nowrap class=item> 
   Micrographs Processed</td>
 <td width="37%" height="22" align=right> 88541 </td>
 <td nowrap> (x 3) </td>
</tr>
<tr> 
 <td width="63%" height="22" class=item> Cells retrieved:</td>
 <td width="37%" height="22" align=right> 1720491 </td>
</tr>
<tr height=10><td></td> </tr>
<tr>
  <td colspan=3 align=center>
   <p><a href="http://www.gi.k.u-tokyo.ac.jp">Morishita Laboratory</a></p>
  </td>
</tr>
</table>
<table width="400" border="0" class=newsframe>
<tr>
 <td class=small align=center> Please enable JavaScript to properly display SCMD. </td> 
</tr>
</table>


</body>	
<scmd-base:footer/>

