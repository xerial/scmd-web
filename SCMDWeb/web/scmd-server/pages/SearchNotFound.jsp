<%--
//--------------------------------------
// SCMDServer
// 
// SearchResult.jsp
// Since  2004/08/25 15:51:33
//
// $Date: 2004/08/31 04:46:21 $ ($Author$)
// $Revision: 1.2 $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>

<%
  String keyword = (String) request.getAttribute("keyword");
%>
<scmd-base:header title="Your Selection" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on"/>

<p class="notice"> Your requested keyword, <span class="notation"> ${keyword} </span> is not found in the database. </p>
<p class="memo"> Try 
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${keyword}" target="_blank">CYGD</a> or 
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${keyword}" target="_blank">SGD</a> .
</p>


</center>
</body>

<scmd-base:footer/>

<%--
//---------------------------------------
// $Log: SearchNotFound.jsp,v $
// Revision 1.2  2004/08/31 04:46:21  leo
// �O���[�v���̃f�[�^�V�[�g�̍쐬�I��
// �����A�f�[�^�V�[�g�̃y�[�W�ړ����I��
//
// Revision 1.1  2004/08/30 10:43:13  leo
// GroupBySheet�̍쐬 page�̈ړ��͂܂�
//
// Revision 1.1  2004/08/27 08:57:43  leo
// �����@�\��ǉ� page�̈ړ��͂܂�
//
// Revision 1.2  2004/08/26 08:48:20  leo
// Query�̒ǉ��B selection�̏C��
//
// Revision 1.1  2004/08/25 09:09:55  leo
// userselection�̒ǉ�
//
//---------------------------------------
--%>


