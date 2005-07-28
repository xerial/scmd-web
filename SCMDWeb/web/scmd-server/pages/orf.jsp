<%@ page language="java" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="c" uri="/WEB-INF/c-rt.tld"%>
<jsp:useBean id="currentView" scope="session" class="lab.cb.scmd.web.bean.CurrentView"/>

<span class="orf"> ${currentView.orf} </span> 
<span class="genename"> ${currentView.standardName} </span>
