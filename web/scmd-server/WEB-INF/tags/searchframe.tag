<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<form action="Search.do" method="GET" class="searchframe"><span class="small"> Keywords: </span>
<input type="text" name="keyword" size="25" value=""/>
<input type="submit" value="search"/>
<span class="small">(ex. 
<html:link href="Search.do?keyword=rad52">rad52</html:link>, 
<html:link href="Search.do?keyword=polarisome">polarisome</html:link>, 
<html:link href="Search.do?keyword=GO:0005678">GO:0005678</html:link>)
</span>
</form> 
