<%-- 
//--------------------------------------
// SCMDServer
// 
// about.jsp
// Since: 2004/07/14
//
// $Date: 2004/08/14 15:59:33 $ ($Author: leo $)
// $Revision: 1.4 $
//--------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>

<scmd-base:header title="CalMorph" css="/css/toppage.css"/>

<body class="toppage">
<scmd-tags:menu searchframe="off" top="true"/>

<table width="600" class="infoframe">
<tr>
<td class="title">
CalMorph
</td>
</tr>
<tr>
<td>
CalMorph is a program that can obtain a large amount of data on cell cycle phase, cell forms, etc., for individual cells, from a set of pictures of cell walls, cell nuclei, and actins.
</td>
</tr>
</table>

<table width="600" border="0" class="infoframe">
<tr>
<td class="title"> Download </td>
</tr>
<td>
<ul>
  <li><html:link page="/calmorph/CalMorph.jar">Download CalMorph Version 1.1</html:link> 
   <ul>
    <li>CalMorph requres <a href="http://java.sun.com/j2se/1.4.2/index.jsp">Java 1.4.2 or later</a></li>
   </ul>
  </li>
  <li>Sample micrographs: <html:link page="/calmorph/his3.zip">his3.zip</html:link>
  <ul>
    <li>The zip file contains 57 photos
    <ul>
      <li>19 ConA photos (his3-C1.jpg,...,his3-C19.jpg)</li>
      <li>19 DAPI photos (his3-D1.jpg,...,his3-D19.jpg)</li>
	  <li>19 actin photos (his3-A1.jpg,...,his3-A19.jpg)</li>
	</ul>
	</li>
  </ul>
</ul>
</td>
</tr>
</table>

<table width="600" border="0" class="infoframe">
<tr>
<td class="title"> Manual </td>
</tr>
<td>
<ul>
  <li><html:link page="/calmorph/CalMorph-manual.pdf">CalMorph User Manual</html:link> 
  describes basic usage of CalMorph, all the parameters generated by CalMorph and our experimental protocols.</li>
</ul>
</td>
</tr>
</table>

<table width="600" border="0" class="infoframe">
<tr>
<td class="title"> Screenshots </td>
</tr>
</table>
<table>
<tr><td>
<img src="calmorph_win_small.png" />
</td>
<td>
<img src="calmorph_mac_small.png" />
</td></tr>
<tr>
<td align="center" class="small">On Windows</td>
<td align="center" class="small">On Mac OS X</td>
</tr>
</table>

<table width="600" border="0" class="infoframe">
<tr>
<td class="title"> Quick Usage </td>
</tr>
<td>
<ol>
 <li>Set up Java Language if you need (already installed in most computers)</li>
 <li>Download <html:link page="/calmorph/CalMorph.jar">CalMorph.jar</html:link> and <html:link page="/calmorph/his3.zip">his3.zip</html:link> files</li>
 <li>Extract his3.zip which contains folder named his3 and 57 photos</li>
 <li>Double click CalMorph.jar icon (both windows and mac), and you can see the CalMorph window</li> 
 <li>Click "Select" button, and choose your "his3" folder</li>
 <li>Click "Run" button on the bottom of CalMorph</li>
 <li>Wait and watch the status line close to run button</li>
 <li>You will see "Finish" in the status line when CalMorph is finished.</li>
 <li>The data folder in your directory contains the result
  <ul>
   <li>"data/cell": analyzed images and parameters about individual cells</li>
   <li>"data/stage": statistical values about each orf</li>
  </ul>
 </li>
</ol>
</td>
</tr>
</table>

<table width="600" border="0" class="infoframe">
<tr>
<td class="title"> Staffs </td>
</tr>
<td>
<dl>
 <dt>Implementation:</dt>
  <dd>Miwaka Ohtani, Hiroshi Sawai, Taro L. Saito, and Jun Sese</dd>
 <dt>Design:</dt>
  <dd>Miwaka Ohtani, Hiroshi Sawai, Taro L. Saito, Jun Sese, Masashi Yukawa, Fumi Sano, Yoshikazu Ohya, and Shinich Morishita</dd>
 <dt>Documentation:</dt>
  <dd>Hiroshi Sawai, Masashi Yukawa, Fumi Sano, Satoru Nogami, Jun Sese, Yoshikazu Ohya, and Shinichi Morishita</dd>
</dl>
</td>
</tr>
</table>


<p> </p>
</body>
</html>
