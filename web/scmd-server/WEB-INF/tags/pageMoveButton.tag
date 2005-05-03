<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ attribute name="actionURL" required="true" %>
<%@ attribute name="currentPage" required="true" %>
<%@ attribute name="maxPage" required="true" %>
<span class="button">
[ <html:link page="/${actionURL}?action=prev10"> prev10 </html:link> ]
[ <html:link page="/${actionURL}?action=prev"> prev </html:link> ]
${currentPage} / ${maxPage}
[ <html:link page="/${actionURL}?action=next"> next </html:link> ]
[ <html:link page="/${actionURL}?action=next10"> next10 </html:link> ]
</span>
