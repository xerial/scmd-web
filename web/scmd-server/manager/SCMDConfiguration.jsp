<%@ page language="java" %>
<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<scmd-base:header title="Configuration" css="/css/scmd.css"/>
 

<%@page import="scmd.web.common.SCMDConfiguration"%>

<%
	String action =	request.getParameter("action");
	if(action!=null)
		if(action.equals("reset"))
			SCMDConfiguration.Initialize();
%>

<body>

<center>
<h2 class="title"> SCMD Configuration </h2>
<table border="0" class="datasheet">
<tr> <td class="tablelabel" width="200"> Property </td> <td class="tablelabel"> Value </td> </tr>
<logic:iterate id="prop" collection="<%= SCMDConfiguration.getPropertyKeys() %>" type="java.lang.String">
<tr><td> <b> ${prop} </b> </td> <td> <%= SCMDConfiguration.getProperty(prop) %> </td> </tr>
</logic:iterate>
 
</table>
<form action="SCMDConfiguration.jsp" method="GET">
 <input type="hidden" name="action" value="reset"/>
 <input type="submit" value="reload"/>
</form>

</center>

</body>
<scmd-base:footer/>
