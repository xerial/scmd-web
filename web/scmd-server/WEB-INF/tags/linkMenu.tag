<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ attribute name="orf" required="false" %>
<%@ attribute name="logo" required="false" %>
<%@ attribute name="width" required="false" %>
<logic:equal name="orf" value="">
<c:set var="orf" value="yor202w"/>
</logic:equal>
<logic:equal name="logo" value="on">
<logic:notEqual name="width" value="">
<table width="${width}">
</logic:notEqual>
<logic:equal name="width" value="">
<table width="730">
</logic:equal>
<tr><td>
</logic:equal>
<span class="menubutton">
<%--
[ <html:link page="/ViewPhoto.do?orf=${orf}"> photo viewer </html:link> ] 
[ <html:link page="/ViewDataSheet.do?orf=${orf}"> photo datasheet </html:link> ] 
[<span class="small"> Group by: </span> 
 <html:link page="/ViewGroupBySheet.do?orf=${orf}&stainType=0"> bud size </html:link>
 <html:link page="/ViewGroupBySheet.do?orf=${orf}&stainType=1"> nucleus </html:link>
 <html:link page="/ViewGroupBySheet.do?orf=${orf}&stainType=2"> actin </html:link> ] 
[<span class="small"> Teardrop Stats: </span> 
 <html:link page="/ViewGroupByTearDrop.do?orf=${orf}&stainType=0"> bud size </html:link>
 <html:link page="/ViewGroupByTearDrop.do?orf=${orf}&stainType=1"> nucleus </html:link>
 <html:link page="/ViewGroupByTearDrop.do?orf=${orf}&stainType=2"> actin </html:link> ] 
[ <html:link page="/ORFTeardrop.do?orf=${orf}"> ORF Teardrop </html:link> ] 
[ <html:link page="/ViewStats.do?orf=${orf}"> average shape </html:link> ] 
[ <html:link page="/View2DPlot.do?orf=${orf}"> 2D plot </html:link> (<html:link page="/View2DPlot.do?param1=-1&param2=-1"> lucky! </html:link>) ] 
[ <html:link page="/ViewORFParameter.do"> ORF parameter sheet </html:link> ] 
--%>
[ <html:link page="/ViewORFList.do"> yeast mutants </html:link> ] 
[ <html:link page="/ParameterHelp.do"> parameter help </html:link> ] 
[ <html:link page="/sitemap.jsp"> site map </html:link> ] 
[ <html:link page="/"> top </html:link> ] 
</span>
<span class=small>
( links to: 
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${orf}" target=_blank>CYGD</a>
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${orf}" target=_blank>SGD</a> )
</span>
<logic:equal name="logo" value="on">
</td>
<td><html:link page="/"><html:img page="/image/scmd_logo_grad.png" border="0"/></html:link></td>
</tr>
</table>
</logic:equal>

