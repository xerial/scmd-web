<!-- Copyright (c) 2002 by ObjectLearn. All Rights Reserved. -->

<%@ taglib prefix="scmd-base"  uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base"%>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld"  %>

<scmd-base:header title="Error Page"/>
	<body>
	<center>
<scmd-tags:menu searchframe="on"/>
	
		<blockquote>
		<center>
		<p class="notice">500 Error Page</p>
		</center>
		<p>Internal Server Error
		<p>
		<u><font color="RoyalBlue">${error}</font></u>
		</blockquote>
		<p align="center"> <html:link page="/">SCMD TOP </html:link> </p>
</center>		

</body>
<scmd-base:footer />

