<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ attribute name="orf" required="true" %>

<html:form action="ViewSelection.do" method="POST">
<html:hidden property="inputList" value="${orf}"/>
<html:submit value="select this mutant"/>
</html:form>
