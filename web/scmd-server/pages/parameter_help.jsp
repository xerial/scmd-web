<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>

<scmd-base:header title="Parameter Help"/>
<body>
<center>
<scmd-tags:menu searchframe="on"/>

<p align=center class=header> Parameter Help </p>

  <table width=500 border=0>
    <tr>
      <td colspan=3 class=tablelabel> Cell Shape Parameters </td>
    </tr>
    <tr>
      <td> <img src="/help/paramimage/longaxis.gif" width="74" height="53"></td>
      <td colspan=2 class=notation> Long axis length </td>
    </tr>
    <tr>
      <td> <img src="/help/paramimage/roundness.gif" width="74" height="53"></td>
      <td colspan=2 class=notation> Roundness <span class=memo> := long axis length / short axis length</span> </td>
    </tr>
    <tr>
      <td> <img src="/help/paramimage/budneck.gif" width="74" height="53"></td>
      <td colspan=2 class=notation> Bud neck position angle (0-90) </td>
    </tr>
    <tr>
      <td> <img src="/help/paramimage/budgrowthdir.gif" width="70" height="53"></td>
      <td colspan=2 class=notation> Bud growth direction angle (0-90) </td>
    </tr>
	<tr> <td class=notation colspan=3> In SCMD, the unit of displayed axis lengths is a <i> pixel </i> (= 0.129512 micrometer) on the images, and the unit of areas of cells is its square, pixel x pixel (= 0.016773358144). </td> </tr>
 <tr> <td height=30> </td></tr>
     <tr>
	      <td colspan=3 class=tablelabel> Other Parameters </td>
    </tr>
	<tr>
      <td class=tablelabel> Fitness : </td> 
      <td class=notation colspan=2> This value shows how well does an ellipse fit to the outline of the cell. The smaller the better. <br>
	  Cell walls of some disruptants are more rectangular than elliptical. The <i class=label>fitness</i> is useful to distinguish such shapes.  <br>
	  <span class=memo> (note:  7.71e-05 := 7.71 / 100000 ) </span>   </td>
    </tr>

 <tr> <td height=30> </td></tr>



   <tr>
   <td colspan=3 class=tablelabel> Nucleus Location Groups </td>
   </tr>

<tr> 
  <td bgcolor=black align=center><img src="/help/nucleus-A.jpg"></td> 
  <td class=label width=50>A</td> 
  <td class=notation> A single nucleus <span class=memo>(unbudded cell)</span> </td> 
</tr>

<tr> 
  <td bgcolor=black align=center><img src="/help/nucleus-A1.jpg"></td> 
  <td class=label>A1</td> 
  <td class=notation> A single nucleus in mother cell <span class=memo>(budded cell)</span></td>
</tr>
<tr> 
 <td bgcolor=black align=center><img src="/help/nucleus-B.jpg"></td>  
 <td class=label>B</td>  
 <td class=notation> A single nucleus at bud neck <span class=memo>(budded cell)</span>.  The yellow point shows the center of gravity of the elongated nucleus. </td>
</tr>
<tr> 
  <td bgcolor=black align=center><img src="/help/nucleus-C.jpg"></td>
  <td class=label>C</td> 
  <td class=notation> Nuclei in both mother and daughter cell <span class=memo>(budded cell)</span></td> 
</tr>
<tr> 
  <td bgcolor=black align=center><img src="/help/nucleus-D.jpg"></td>  
  <td class=label>D</td> 
  <td class=notation> Multi-nucleated mother cell </td> 
</tr>
<tr> 
 <td bgcolor=black align=center><img src="/help/nucleus-E.jpg"></td> 
 <td class=label>E</td> 
 <td class=notation> No nucleus is detected </td> 
</tr>
<tr> 
  <td bgcolor=black align=center><img src="/help/nucleus-F.jpg"></td> 
  <td class=label>F</td> 
  <td class=notation> A single nucleus in daughter cell <span class=memo>(budded cell)</span> </td> 
</tr>

<tr> <td height=30> </td></tr>


   <tr>
   <td colspan=3 class=tablelabel> Actin Localization Groups </td>
   </tr>

<tr> 
  <td bgcolor=black align=center><img src="/help/actin-A.jpg"></td> 
  <td class=label width=50>A</td> 
  <td class=notation> Delocalized <span class=memo>(unbudded cell) </span> </td> 
</tr>

<tr> 
  <td bgcolor=black align=center><img src="/help/actin-B.jpg"></td> 
  <td class=label>B</td> 
  <td class=notation> Localized at bud size <span class=memo>(unbudded cell)</span></td>
</tr>
<tr> 
 <td bgcolor=black align=center><img src="/help/actin-api.jpg"></td>  
 <td class=label>api</td>  
 <td class=notation> Hyperlocalized at bud tip <span class=memo>(budded cell)</span> </td>
</tr>
<tr> 
  <td bgcolor=black align=center><img src="/help/actin-iso.jpg"></td>
  <td class=label>iso</td> 
  <td class=notation> Localized at bud <span class=memo>(budded cell)</span></td> 
</tr>
<tr> 
  <td bgcolor=black align=center><img src="/help/actin-E.jpg"></td>  
  <td class=label>E</td> 
  <td class=notation> Delocalized <span class=memo>(budded cell)</span> </td> 
</tr>
<tr> 
 <td bgcolor=black align=center><img src="/help/actin-F.jpg"></td> 
 <td class=label>F</td> 
 <td class=notation> Localized at bud neck <span class=memo>(budded cell)</span></td> 
</tr>

</table>


</center>
</body>
</html>