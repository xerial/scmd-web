<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="plotForm"  scope="request" class="lab.cb.scmd.web.bean.ParamPlotForm"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>

<scmd-base:header title="2D Plot ${view.orf}"/>

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

<body>
<center>
<scmd-tags:menu  toolbar="on"/>

<table width="700">
<tr><td align="left">
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
</td>
<td align="right">
<p align="absbottom">
<scmd-tags:selectorf orf="${gene.orf}"/>
</p>
</td>
</tr>
</table>

<img id="plot" class="plotview" src="Write2DPlot.do?${plotForm.cgiArgument}" 
width="300" height="300" onclick="clk(event)" onmousemove="writeRegion(event)"
/>
<%--<embed src="Write2DPlot.do?${plotForm.cgiArgument}" width="300" height="300" class="plotview"/>--%>
<br/>
<img id="b_n" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_e" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_s" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>
<img id="b_w" style="position:absolute;" src="/image/dot.gif" width="0" height="0"/>

<html:form action="/View2DPlot.do" method="GET">
X-coordinate: 
<html:select name="plotForm" property="param1">
<html:options name="plotForm" property="options"/>
</html:select>
Y-coordinate: 
<html:select name="plotForm" property="param2">
<html:options name="plotForm" property="options"/>
</html:select>

<html:submit value="plot mutants"/>
</html:form>

<scmd-tags:linkMenu orf="${view.orf}" logo="on"/> 

</center>
</body>
<scmd-base:footer/>
