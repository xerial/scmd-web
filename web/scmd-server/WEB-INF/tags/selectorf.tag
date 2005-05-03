<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ attribute name="orf" required="true" %>

<html:form action="ViewSelection.do" method="POST">
<html:hidden property="inputList" value="${orf}"/>
<html:submit value="Add to My Gene List"/>
</html:form>
