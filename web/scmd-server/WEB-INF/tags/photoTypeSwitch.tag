<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>

<p align="center" class="noret">
<html:radio onchange="submit();" property="photoType" value="0" accesskey="o"/>
<span class="small"> Original Photo </span>
<html:radio onchange="submit();" property="photoType" value="1" accesskey="a"/>
<span class="small"> Analysis Result </span>
<html:submit value="show"/>
</p>
