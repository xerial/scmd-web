<%-- 
//--------------------------------------
// SCMDServer
// 
// Viewer.jsp 
// Since: 2004/07/14
//
// $Date: 2004/09/03 08:14:46 $ ($Author: leo $)
// $Revision: 1.25 $
//--------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<jsp:useBean id="view"  scope="session" class="scmd.web.bean.CellViewerForm"/>
<jsp:useBean id="cellList"  scope="request" type="java.util.List"/>
<jsp:useBean id="gene"  scope="request" class="scmd.web.bean.YeastGene"/>
<jsp:useBean id="photoPropertyMap"  scope="request" type="java.util.Map"/>
<jsp:useBean id="pageStatus"  scope="request" class="scmd.db.common.PageStatus"/>

<scmd-base:header title="Cell Viewer ${gene.orf}" css="/css/tabsheet.css"/>

<script language="JavaScript">
<!--
function setStyle(idname, c)
{
   document.getElementById(idname).className=c;
}
//-->
</script>

<body>
<center> 
<scmd-tags:menu toolbar="on"/>
 
<%
// 画像サイズの計算
int imageHeight = 540 * view.getMagnification() / 100;
int imageWidth = 696 * view.getMagnification() / 100 + 4;
int blankTabSize = imageWidth - 70 * 3;
%>


<table width="<%=imageWidth + 10%>"> 

<tr>
<td>
<p align="right">
<scmd-tags:selectorf orf="${gene.orf}"/>
</p>
</td>
</tr>
</table>

<table width="<%=imageWidth + 10%>"> 
<scmd-base:tablist selected="${view.stainType}">
<c:forEach var="i" begin="0" end="2">
<scmd-base:tab name="${i}" width="70">
<html:link page="/ViewPhoto.do?stainType=${i}"> ${tabName[i]} </html:link>  
</scmd-base:tab>
</c:forEach>
</scmd-base:tablist>
<td width="<%= blankTabSize%>" align="right">
<span class="orf"> ${gene.orf} </span> 
<span class="genename"> ${gene.standardName} </span>
</td>
</tr>
<tr><td colspan="4" bgcolor="black" align="center" width="<%= imageWidth %>" height="<%= imageHeight%>">
<html:img page="/DisplayPhoto.do" name="photoPropertyMap" usemap="#cellmap" border="0" alt="${gene.orf}"/>
</td></tr>

<tr>
<td colspan="4" align="center">
<scmd-tags:pageMoveButton actionURL="ViewPhoto.do" currentPage="${view.photoPage}" maxPage="${view.photoPageMax}"/>
</td>
</tr>
</table>

<% int menuWidth = imageWidth < 470 ? 470 : imageWidth; %>
<scmd-tags:linkMenu orf="${gene.orf}" logo="on" width="<%=Integer.toString(menuWidth)%>"/>

<table width="<%=imageWidth%>">
<html:form action="ViewPhoto.do" method="POST">
<tr>
<td>
Magnification:
<html:select onchange="submit();" property="magnification">
<c:forEach var="m" items="125,100,75,50"> <html:option value="${m}"> ${m}% </html:option> </c:forEach>
</html:select>
</td>
</tr>
<tr><td align="center"> <scmd-tags:photoTypeSwitch/> </td></tr>
<tr><td align="center"><scmd-tags:searchframe/></td></tr>
</html:form> 
</table>
 
</center>

<script language="javascript">
<!--
function cellinfo(cellID, width){
    target_url = "ViewCellInfo.do?"
     + "cellID=" + cellID;
     widthArg = "width=" + (width * 3 + 100);
     infowin=window.open
	 (target_url, 'status', 
	  widthArg + ', height=430, toolbar=no, location=no, status=no, menuber=no, scrollbar=no'); 
    infowin.focus();
}

//-->
</script>

<map name="cellmap">

<logic:iterate id="cell" name="cellList" type="scmd.web.common.Cell">
<area shapes="rect" coords="<%= cell.getBoundingRectangle().getAreaCoordinates(view.getMagnification()) %>"
href="javascript: cellinfo(${cell.cellID}, ${cell.boundingRectangle.x2 - cell.boundingRectangle.x1})" 
alt="cell ID=${cell.cellID}"/>
</logic:iterate> 
</map>
</body>
<scmd:footer/> 


<%--
//---------------------------------------
// $Log: PhotoViewer.jsp,v $
// Revision 1.25  2004/09/03 08:14:46  leo
// デザイン調整
//
// Revision 1.24  2004/09/03 07:50:15  leo
// linkMenuタグの更新
//
// Revision 1.23  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.22  2004/08/26 01:02:14  leo
// userselectionの追加
//
// Revision 1.21  2004/08/13 18:58:00  leo
// デザインの微調整
//
// Revision 1.20  2004/08/13 13:57:12  leo
// Statページの更新
//
// Revision 1.19  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.18  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.17  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
// Revision 1.16  2004/08/03 06:19:04  leo
// スタイルの微調整
//
// Revision 1.15  2004/08/03 03:32:57  leo
// デザインを更新
//
// Revision 1.14  2004/07/30 07:51:18  leo
// TabSheet用のタグ<scmd-base:tablist> <scmd-base:tab>を追加
//
// Revision 1.13  2004/07/27 07:41:23  leo
// searchframeを付けたしました
//
// Revision 1.12  2004/07/27 06:50:25  leo
// CellInfoページを追加
//
// Revision 1.11  2004/07/26 22:43:32  leo
// PhotoBufferを用いて、DataSheetの表示を高速化
//
// Revision 1.10  2004/07/26 19:57:40  leo
// sessionではなく引数で読み込むように変更
//
// Revision 1.9  2004/07/26 19:33:31  leo
// Actionの修正。DataSheetページ着工
//
// Revision 1.8  2004/07/26 14:20:25  leo
// *** empty log message ***
//
// Revision 1.7  2004/07/25 11:29:20  leo
// clickable mapを導入
//
// Revision 1.6  2004/07/22 14:42:39  leo
// testの仕方を若干変更
//
// Revision 1.5  2004/07/21 14:51:42  leo
// cssのパスの自動挿入を取りやめ （絶対パスで指定したほうがわかりやすかった）
//
// Revision 1.4  2004/07/21 05:47:16  leo
// XML関係のモジュールを追加
//
// Revision 1.3  2004/07/20 09:01:08  leo
// 微調整
//
// Revision 1.2  2004/07/20 08:44:15  leo
// photo viewer用のコードを追加
//
// Revision 1.1  2004/07/17 08:03:46  leo
// session管理によるphotoViewer着工
//
// Revision 1.2  2004/07/16 16:38:34  leo
// compileができるように調整
//
// Revision 1.1  2004/07/16 16:35:50  leo
// web moduleを、servから、scmd-serverに変更しました
//
// Revision 1.1  2004/07/15 09:21:20  leo
// jsp の使用開始
//
// Revision 1.1  2004/07/14 08:13:47  leo
// first ship
//
//---------------------------------------
--%>