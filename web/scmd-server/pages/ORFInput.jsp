<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<scmd-base:header title="Input ORF" css="/css/tabsheet.css"/>

<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on"/>

<html:form action="ViewSelection.do" method="GET">

<html:textarea rows="10" property="orfInput"/>  
</html:form>

</center>
</body>

<scmd-base:footer/>