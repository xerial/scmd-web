<%@ page language="java" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<scmd-base:header title="Yeast Mutants"/>
<body>
<center>
<scmd-tags:menu searchframe="on" toolbar="on"/>

<%@page import="lab.cb.scmd.web.bean.*"%>
<%@page import="java.util.*"%>
<%@page import="lab.cb.scmd.db.common.PageStatus"%>

<jsp:useBean id="selection" scope="request" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>

<p class="notice"> No ORF parameter is selected.
<br> Please select some ORF parameters in the
<a href="CustomizeView.do">My Parameter List</a> page.
</p>


</center>
<scmd-base:footer/>
