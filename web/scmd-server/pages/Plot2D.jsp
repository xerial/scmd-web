<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>
<jsp:useBean id="userSelection"  scope="session" class="lab.cb.scmd.web.bean.UserSelection"/>

<scmd-base:header title="2D Plot ${view.orf}"/>

<%--
<script language="javascript">
<!--
var x1 = 0;
var y1 = 0;
var x2 = 0;
var y2 = 0;
var phase = 0;
function clk(e)
{
   if(phase==0)
   {
      x1 = e.offsetX;
      y1 = e.offsetY;
      phase = 1;
   }
   else
   {
      x2 = e.offsetX;
      y2 = e.offsetY;
      phase =0;
   }
}
function writeRegion(e)
{
  if(phase==1)
  {
      x2 = e.offsetX;
      y2 = e.offsetY;
      document.getElementById("b_n").style.posTop = e.clientY - y2 + y1;
      document.getElementById("b_n").style.posLeft = e.clientX - x2 + x1;
      document.getElementById("b_n").style.width = x2 - x1;
      document.getElementById("b_n").style.height = 2;

      document.getElementById("b_e").style.posTop = e.clientY - y2 + y1;
      document.getElementById("b_e").style.posLeft = e.clientX;
      document.getElementById("b_e").style.width = 2;
      document.getElementById("b_e").style.height = y2 - y1 + 2;

      document.getElementById("b_w").style.posTop = e.clientY - y2 + y1;
      document.getElementById("b_w").style.posLeft = e.clientX - x2 + x1;
      document.getElementById("b_w").style.width = 2;
      document.getElementById("b_w").style.height = y2 - y1;

      document.getElementById("b_s").style.posTop = e.clientY;
      document.getElementById("b_s").style.posLeft = e.clientX - x2 + x1;
      document.getElementById("b_s").style.width = x2 - x1 + 2;
      document.getElementById("b_s").style.height = 2;
  }
}
//-->
</script>
--%>

<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>
<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

<c:if test="${addViewORF}">
<scmd-tags:orfInfo  orf="${gene.orf}" 
	stdname="${gene.standardName}" annot="${gene.annotation}" 
	title="2D Plot"  />
</c:if>
<c:if test="${!addViewORF}">
<p class="title">2D Plot</p>
</c:if>

<table>
<tr>
<td>
<table>
<logic:iterate id="orf" name="userSelection" property="selection" type="java.lang.String">
<tr>
<td bgcolor="<%= userSelection.getColor(orf) %>" width="25"></td>
<td width="5"></td>
<td class="orf" width="100" align="left">
<a href="ViewStats.do?orf=${orf}"><%= lab.cb.scmd.web.bean.YeastGene.formatedOrf(orf) %></a> 
</td>
</tr>
</logic:iterate>
</table>
</td>
<td align="center">
<c:if test="${plotForm.param2 != -1}">
<a href="ORFDataSheet.do?paramID=${plotForm.param2}"><img src="DrawTeardrop.do?paramID=${plotForm.param2}&orientation=vertical&plotTargetORF=${addViewORF}" border="0"/></a>
</c:if>
</td>
<td>
<img id="plot" class="plotview" src="Write2DPlot.do?${plotForm.cgiArgument}" width="300" height="300"/>
</td>
</tr>
<tr>
<td/>
<td/>
<td align="center">
<c:if test="${plotForm.param1 != -1}">
<a href="ORFDataSheet.do?paramID=${plotForm.param1}"><img src="DrawTeardrop.do?paramID=${plotForm.param1}&plotTargetORF=${addViewORF}" border="0"/></a>
</c:if>
</td>
</tr>
</table>

<%--<embed src="Write2DPlot.do?${plotForm.cgiArgument}" width="300" height="300" class="plotview"/>--%>
<br/>
<%--
<img id="b_n" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_e" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_s" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_w" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
--%>

<html:form action="/View2DPlot.do" method="GET">
<table>
<tr>
<td>X-coordinate: </td>
<td align="left">
<html:select name="plotForm" property="param1">
<html:options name="plotForm" property="optionIDs" labelProperty="optionLabels"/>
</html:select>
</td>
</tr>
<tr>
<td>Y-coordinate: </td>
<td align="left">
<html:select name="plotForm" property="param2">
<html:options name="plotForm" property="optionIDs" labelProperty="optionLabels"/>
</html:select>
</td>
</tr>
</table>

<table>
<tr>
<td>
<html:submit value="plot mutants"/>
</html:form>
</td>
<td>
<html:form action="/View2DPlot.do" method="GET">
<input type="hidden" name="param1" value="-1">
<input type="hidden" name="param2" value="-1">
<html:submit value="lucky coordinate!"/>
</html:form>
</td>
</tr>
</table>


<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

</center>
</body>
<scmd-base:footer/>
