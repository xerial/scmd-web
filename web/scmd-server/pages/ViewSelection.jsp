<%--
//--------------------------------------
// SCMDServer
// 
// ViewSelection.jsp
// Since  2004/08/25 15:51:33
//
// $Date: 2004/09/01 06:39:59 $ ($Author$)
// $Revision: 1.4 $
//--------------------------------------
--%>

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>

<jsp:useBean id="userSelection"  scope="session" class="lab.cb.scmd.web.bean.UserSelection"/>
<jsp:useBean id="selection"  scope="page" class="lab.cb.scmd.web.bean.ORFSelectionForm"/>
<jsp:useBean id="orfList"  scope="request" type="java.util.List"/>
<jsp:useBean id="gene"  scope="request" class="lab.cb.scmd.web.bean.YeastGene"/>

<scmd-base:header title="Your Selection" css="/css/tabsheet.css"/>
<body>
<center>
<scmd-tags:menu toolbar="on" searchframe="on" orf="${gne.orf}"/>

<scmd-tags:linkMenu orf="${gene.orf}" logo="on"/> 

<html:form action="ViewSelection.do" method="GET">

<table width="500">
<tr><td align="right">
<html:submit value="remove" property="button"/>
<html:submit value="remove all" property="button"/>
</td></tr>
</table>

<table border="0" cellspacing="0" cellpadding="0">
<tr nowrap="nowrap">
<td colspan="2" class="sheetlabel" width="105"> ORF </td> 
<td class="sheetlabel" witdh="90"> Std. Name </td> 
<td class="sheetlabel" width="150"> Aliases </td> 
<td colspan="3" class="sheetlabel"></td>
<td class="sheetlabel" width="50"> Color </td>
</tr>
<logic:iterate id="yeastGene" name="orfList" type="lab.cb.scmd.web.bean.YeastGene">

<tr class="small"> 
<td align="left" width="15"><p align="center">
<html:multibox name="selection" property="inputList" value="${yeastGene.orf}"/></p></td>
<td align="left" class="orf" width="90"> 
<html:link page="/ViewStats.do?orf=${yeastGene.orf}"> ${yeastGene.orf}  </html:link> 
</td> 
<td class="genename" align="center"> ${yeastGene.standardName} </td> 
<td class="small" align="center"> ${yeastGene.aliasString} </td> 
<td width="80"/>
<td width="200">
<html:link page="/ViewPhoto.do?orf=${yeastGene.orf}"> viewer </html:link>&nbsp;
<html:link page="/ORFTeardrop.do?orf=${yeastGene.orf}"> teardrop </html:link>&nbsp;
<a href="http://mips.gsf.de/genre/proj/yeast/searchEntryAction.do?text=${yeastGene.orf}" target="_blank">CYGD</a>&nbsp;
<a href="http://genome-www4.stanford.edu/cgi-bin/SGD/locus.pl?locus=${yeastGene.orf}" target="_blank">SGD</a>
</td>
<td></td>
<td align="center"> 
<html:select name="userSelection" property="colorList">
<logic:iterate id="plotColor" collection="<%= lab.cb.scmd.web.design.PlotColor.getDefaultPlotColorList()%>" type="lab.cb.scmd.web.design.PlotColor">
<html:option style="color:#${plotColor.colorCode}; background:#${plotColor.colorCode};" value="${yeastGene.upperCaseOrf}_${plotColor.colorName}">${plotColor.colorName}</html:option>
</logic:iterate>
</html:select>
</td>
</tr>	
<tr bgcolor="#F0F0E0" height="15">
<td></td>
<td colspan="8" width="600" class="annotation"> <%= yeastGene.getAnnotation() %></td> 
</tr>
<tr height="10"><td> </td></tr>
</logic:iterate>
</table>
<html:submit value="apply color changes" property="button"/>
<p align="center" class="menubutton"> 
[ <html:link page="/View2DPlot.do?orfType=user"> Display 2D plot of these mutants</html:link> ] 
</p>
</html:form>


<html:form action="ViewSelection.do" method="POST" enctype="multipart/form-data">
<table>
<tr>
<td class="small" valign="center"><b>input ORFs manually</b> : <br> (ex. yor202w, yal002w, rad51, ...) </td>
<td><textarea name="orfList" rows="5" cols="15"></textarea> </td>
</td>
</tr>
</table>
<html:submit value="update" property="button"/>

<p class="small">Save the above list of ORFs as an XML file : <html:submit value="save" property="button"/></p>
<p class="small">Load an ORF list from an XML file : <html:file property="file" /> <html:submit value="load" property="button"/></p>
</html:form> 

</center>
</body>

<scmd-base:footer/>

<%--
//---------------------------------------
// $Log: ViewSelection.jsp,v $
// Revision 1.4  2004/09/01 06:39:59  leo
// TearDropViewを追加
//
// Revision 1.3  2004/08/27 08:57:43  leo
// 検索機能を追加 pageの移動はまだ
//
// Revision 1.2  2004/08/26 08:48:20  leo
// Queryの追加。 selectionの修正
//
// Revision 1.1  2004/08/25 09:09:55  leo
// userselectionの追加
//
//---------------------------------------
--%>


