<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic-el.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="view"  scope="session" class="lab.cb.scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="para"  scope="request" class="lab.cb.scmd.web.sessiondata.MorphParameter"/>
<jsp:useBean id="range"  scope="request" class="lab.cb.scmd.web.bean.Range"/>
<jsp:useBean id="pageStatus"  scope="request" class="lab.cb.scmd.db.common.PageStatus"/>
<jsp:useBean id="selection" scope="request" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>

<scmd-base:header title="Site Map" css="/css/scmd.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on"/>

<p class="title"> SCMD Site Map </p>
<p class="notice">  </p>

</center>
<scmd-base:footer/>
