<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<logic:equal name="view" value="">
<c:set var="orf" value="yor202w"/>
</logic:equal>
<logic:notEqual name="view" value="">
<c:set var="orf" value="${view.orf}"/>
</logic:notEqual>

<scmd-base:header title="Site Map" css="/css/scmd.css"/>
<body>
<center>
<scmd-tags:menu  toolbar="on" searchframe="on"/>

<p class="title"> SCMD Site Map </p>

<p class="menubutton">
[<a href="#yeastmutant">Yeast Mutant</a>]
[<a href="#pmenu">Photo Menu</a>]
[<a href="#datamining">Data Mining Menu</a>]
[<a href="#custom">Customization Menu</a>]
[<a href="#other">Other Menus</a>]
</p>

<table width="500">
<tr>
<td class="small">
You can select all menus described below from the above drop-down menu bar.
</td>
<td>
<img src="image/dropdownmenu.png"/>
</td>
</table>

  <table width="650" border="0" class="helpmenu">
    <tr> 

      <td colspan="2" class="menubar"><a name="yeastmutant"/>Yeast Mutant</td>
    </tr>
    <tr> 
      <td> 
        <ul>
	      <li><a href="ViewORFList.do">Yeast Mutant</a>
		  <ul>Display a list of yeast mutants available in SCMD. </ul>
		  </li>
        </ul>
        
      </td>
      <td><img src="image/yeastmutant.png" align="middle"/></td>
    </tr>
    <tr>    
      <td colspan="2"  class="menubar"><a name="pmenu"/>Photo Menu </td>
    </tr>
    <tr>
      <td valign="top"> 
        <ul>
          <li><a href="ViewPhoto.do?orf=${orf}"> Photo Viewer </a>
          <ul> Display micrographs of yeast mutants stained with ConA, DAPI and Rh-Ph.
		 <p align="center">      <img src="image/photoviewer.png" align="center"/>  </p>   
          </ul>
		  </li>          
                   
                              
        </ul>
        </td>
      <td valign="top"> 
        <ul>
                  <li><a href="ViewDataSheet.do?orf=${orf}"> Individual Cell Datasheet</a> 
              <ul> Show individual cells and their morphological prameters (cell 
                parameters) in the micrographs.
  <img src="image/indivcell.png" align="center"/> 
                </ul>
                </li>
         </ul>
       </td>
       </tr>
       <tr><td colspan="2" align="center">
		 <ul>       
                  <li> Cells Grouped by <html:link page="/ViewGroupBySheet.do?stainType=0&orf=${orf}">
                           bud size </html:link>, <html:link page="/ViewGroupBySheet.do?stainType=1&orf=${orf}"> 
            nucleus location </html:link>, <html:link page="/ViewGroupBySheet.do?stainType=2&orf=${orf}"> 
            actin distribution</html:link> 
              <ul> Show cells grouped by morphological conditions such as bud size, 
                nucleus location and action distributions. <img src="image/groupcell.png"  /> </ul>
                </li>
        </ul>
      </td>
    </tr>
    <tr> 
      <td colspan="2" class="menubar"><a name="datamining"/>
      Data Mining Menu</td>
    </tr>
    <tr> 
      <td valign="top"> 
        <ul>
          <li><a href="SelectShape.do">Morphology Search </a> 
             <ul>
              By inputting or selecting cell chapes displayed in dialogue, 
               search yeast mutants that are similar in shapes. 
               <img src="image/morphologysearch.png"/>               
             </ul>
              </li>
          <li><a href="ViewORFParameter.do">ORF Parameter Datasheet</a> 
            <ul>
              Show descriptions of ORF parameters and their datasheet sorted by values. 
              <img src="image/orfdatasheet.png" width="385" height="233"> 
            </ul>
          </li>
          <li><a href="ViewStats.do?orf=${orf}">Average Shapes</a> 
            <ul>
              Display average shapes of mutants grouped by morphological conditions 
              <img src="image/averageshape.png" width="344" height="159"> 
            </ul>
          </li>
        </ul>
      </td>
      <td  valign="top"> 
        <ul>
          <li><a href="ORFTeardrop.do?orf=${orf}">Teardrop View of ORF Parameters 
            </a> 
            <ul>
            Display values and statistical information (average, distribution, 
            etc.) of morphological parameters of an ORF at a glance. By clicking the teardrop images, you can see their <a href="ViewORFParameter.do">ORF Parameter Datasheet</a>.
              <p align="center">
                    <img src="image/orfterdrop.png" align="center"/> </p>
	                    
            </ul>
            </li>
          </ul>

        <ul>
          <li><a href="View2DPlot.do?orfType=current">2D Plot</a>
          <ul>
              Plot pairs of two morphological parameters of all yeast mutants 
                on a two dimensional plane. 
                <p align="center"><img src="image/2dplot.png" align="center" width="184" height="189"/> </p>  
                
                
	      </ul>
	      </li>
        </ul>
        <ul>
        <li>Teardrop View of Average Shapes Grouped by 
              <html:link page="/ViewGroupByTearDrop.do?stainType=0&orf=${orf}"> bud size </html:link>, 
			<html:link page="/ViewGroupByTearDrop.do?stainType=1&orf=${orf}">nucleus location</html:link> and
             <html:link page="/ViewGroupByTearDrop.do?stainType=2&orf=${orf}"> 
                actin distribution </html:link>
          <ul>
                      Show average shapes and teardrop views of mutants grouped by morphological 
              conditions 
              <p align="center">
                      <img src="image/teardropgroup.png" width="334" height="302" align="center"/></p>  </ul>  
          </li>
		</ul>
	        
        
      </td>
    </tr>
    
    <tr> 
      <td colspan="2"  class="menubar"><a name="custom"/>Customization Menu </td>
    </tr>
    <tr> 
      <td valign="top"> 
        <ul>
          <li><a href="ViewSelection.do"> My Gene List</a> 
            <ul>
              By selecting genes in this page, positions of these genes are shown 
              in the teardrop view and the 2D plot display. Inputted data can 
              be saved and loaded with an XML format. <img src="image/mygene.png"/> 
            </ul>
          </li>
        </ul>
      </td>
      <td valign="top"> 
        <ul>
          <li><a href="CustomizeView.do"> My Parameter List</a>
            <ul>
            By selecting several morphological parameters related to genes(ORFs) or indivial cells, you can customize displayed parameters in the <a href="ViewORFParameter.do">ORF Parameter Datasheet</a>, <a href="ORFTeardrop.do">Teardrop View</a> and <a href="ViewDataSheet.do">Indivisual Cell Parameter Datasheet</a>.
           <img src="image/myparm.png" align="center"/> 
           </ul>
	     </li>
        </ul>

      </td>
    </tr>
    <tr><td colspan="2"  class="menubar"><a name="other"/>Other Menus</td></tr>
    <tr> 
      <td colspan="2">
        <ul>
          <li>Help Menu 
            <ul>
              <li><a href="ParameterHelp.do">Parameter Help</a> </li>
              <li><a href="sitemap.jsp">Site Map</a></li>
            </ul>
          </li>
        </ul>
      </td>
    </tr>
    <tr> 
      <td colspan="2"> 
        <ul>
          <li><a href="publication.jsp">Publications</a> </li>
          <li><a href="about.jsp">About SCMD</a> </li>
        </ul>
      </td>
    </tr>
  </table>
</center>
<scmd-base:footer/>
