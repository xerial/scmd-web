<%--
//--------------------------------------
// SCMDServer
// 
// ViewCustomize.jsp
// Since  2005/02/08
//
// $URL$ 
// $Author: leo $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>



<scmd-base:header title="Customize View" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>

<html:form action="CutomizeView.do" method="GET">

<html:submit value="set" property="button"/>

</html:form>


</center>
</body>

<scmd-base:footer/>
