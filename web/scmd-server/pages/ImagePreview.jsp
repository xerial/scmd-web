<%-- 
//--------------------------------------
// SCMDServer
// 
// ImagePreview.jsp
// Since: 2004/07/14
//
// $Date: 2004/08/30 06:06:25 $ ($Author: leo $)
// $Revision: 1.2 $
//--------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>

<%@page import="scmd.web.bean.*"%>

<jsp:useBean id="shape" scope="request" class="scmd.web.bean.SelectedShape"/>
<scmd-base:header title="Preview Window"/>

<body>

<table align="center">
<tr height="128">
<td align="center" colspan="2">
  <img src="/scmd-server/cellshape.png?${shape.imageArgument}" alt="preview image"/>
</td>
</tr>
<tr>
  <td align="left" class="small">
  <font color="#006699"> Bud area ratio  </font> </td> 
  <td align=right class="small"> ${shape.areaRatio} </td></tr>
<tr>
  <td align="left" class="small">
  <font color="#006699"> Long axis length  </font> </td> 
  <td align=right class="small"> ${shape.longAxis}</td></tr>
<tr>
  <td align="left" class="small">
  <font color="#006699"> Short axis length </font> </td> 
  <td align=right class="small"> ${shape.shortAxis} </td></tr>
<tr>
  <td align="left" class="small">
  <font color="#006699"> Roundness  </font> </td> 
  <td align=right class="small"> ${shape.roundness} </td></tr>

<logic:notEqual name="shape" property="areaRatio" value="0.0">

<tr>
  <td align="left" class="small">
  <font color="#006699"> Neck position angle  </font> </td> 
  <td align=right class="small"> ${shape.neckPosition} </td></tr>
<tr>
  <td align="left" class="small">
  <font color="#006699"> Bud growth direction  </font> </td> 
  <td align=right class="small"> ${shape.growthDirection}</td></tr>
</logic:notEqual>  
  
</table>
<p align=center>
 <input type="button" value="close" onClick="window.close()">
</p>
</body>

<scmd-base:footer/>

