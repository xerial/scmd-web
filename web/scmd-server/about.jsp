<%-- 
//--------------------------------------
// SCMDServer
// 
// about.jsp
// Since: 2004/07/14
//
// $Date: 2004/08/14 15:59:33 $ ($Author$)
// $Revision: 1.4 $
//--------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>

<scmd-base:header title="About SCMD" css="/css/toppage.css"/>

<body class="toppage">
<scmd-tags:menu searchframe="on" top="true"/>

<table width="600" border="0" class="infoframe">
  <tr>
    <td>
      <table width="100%" border="0">
        <tr> 
          <td colspan="3" height="50" align=left class=title> Materials </td>
        </tr>
        <tr> 
          <td colspan="3"> Mutants with altered cell morphology 
            were screened from a set of the haploid MATa deleted strains obtained 
            from EUROSCARF. Each strain was grown in YPD, harvested, stained and 
            used for computational morphological analysis. For obtaining fluoresent 
            images of the cell surface mannoprotein, actin cytoskeleton and nuclear 
            DNA, cells were triply stained with FITC-ConA, rhodamine-phalloidin 
            and DAPI. </td>
        </tr>
        <tr> 
          <td colspan="3" height="50" align=left class=title> Core Members </td>
        </tr>
        <tr> 
          <td width="48%" height="38" class=notation> Ohya Laboratory
		   (<a href="http://www.biol.s.u-tokyo.ac.jp/users/sonoike/top.html">homepage</a>)</td>
          <td width="4%" height="38"></td>
          <td width="48%" height="38" class=notation>Morishita Laboratory
		  (<a href="http://www.gi.k.u-tokyo.ac.jp">homepage</a>)</td>
        </tr>
        <tr> 
          <td width="48%" class="arialsmall"><a href="http://www.k.u-tokyo.ac.jp/integ/index.html">Department 
            of Integrated Biosciences, Graduate School of Frontier Sciences</a>, 
            University of Tokyo.</td>
          <td width="4%"> </td>
          <td width="48%"> 
            <p class="arialsmall"><span class="arialsmall"><a href="http://www.k.u-tokyo.ac.jp/bi/index.html">
			Department of Computational Biology, Graduate School of Frontier Science</a>, University of Tokyo.</span></p>
          </td>
        </tr>
        <tr> 
          <td width="48%" nowrap> </td>
          <td width="4%" nowrap> </td>
          <td width="48%" nowrap> </td>
        </tr>
        <tr> 
          <td width="48%" nowrap>
            <ul>
              <li><span class=notation> Professor </span>
                <ul>
                  <li><a href="mailto:ohya@k.u-tokyo.ac.jp"> Yoshikazu Ohya </a> </li>
                </ul>
              </li>
			</ul>
	      </td>
          <td width="4%"> </td>
          <td width="48%" align=left> 
              <ul>
                <li><span class=notation>Professor</span>
                  <ul>
                    <li>
					  <a href="http://www.gi.k.u-tokyo.ac.jp/~moris"> Shinichi Morishita </a>
					</li>
                  </ul>
                </li>
			  </ul>
          </td>
		</tr>
		  <tr>
          <td width="48%" nowrap valign=top>
		     <ul>
			  <br>
              <li><span class=notation> Postdoctroal Fellows </span> 
                <ul>
                  <li><a href="mailto:kumagai@k.u-tokyo.ac.jp"> Fumi Sano</a> </li>
                  <li><a href="mailto:kmyukawa@mail.ecc.u-tokyo.ac.jp"> Masashi Yukawa </a> </li>
                </ul>
              </li>
			  <br>
              <li><span class="notation">Graduate Students </span> 
                <ul>
                  <li><a href="mailto:kk27517@mail.ecc.u-tokyo.ac.jp"> Genjiro Suzuki</a> </li>
                  <li>Keiko Kono </li>
                  <li>Mieko Higuchi</li>
				  <li>Satoshi Ishihara</li>
				  <li>Aiko Hirata</li>
                </ul>
              </li>
            </ul>
          </td>

          <td width="4%"> </td>
          <td width="48%" valign=top> 
              <ul>
				<br>

                <li><span class=notation>Research Associate</span>
				<ul> 
				  <li>Jun Sese </li>
					<ul>
					  <li><span class="arialsmall">image processing & data mining</span></li>
					</ul>
				</ul>
                <li><span class=notation>Graduate Students</span>
                <ul>
                  <li><a href="http://www.gi.k.u-tokyo.ac.jp/%7Eleo">Taro L. Saito</a>
                    <ul>
					  <li><span class="arialsmall">database program & web site management</span></li>
                    </ul>
                  </li>
				  <li> Yoichiro Nakatani </li>
				  <ul><li> statistical analysis & data mining</li></ul>
                </ul>
				<br>
				<li><span class=notation>Secretary</span>
				<ul>
				  <li>Tsurutsuki Iijima</li>
				</ul>
              </ul>
          </td>
        </tr>
        <tr> 
          <td width="48%"> </td>
          <td width="4%"> </td>
          <td width="48%"> </td>
        </tr>
        <tr> 
          <td colspan="3" height="50"> 
            <div align="left" class="arial"><b><font color="#006699"> Supporting 
              Members </font></b></div>
          </td>
        </tr>
        <tr> 
          <td colspan="3" class="arial"> 
Emi Shimoi, Tamao Goto, Yuka Kitamura,
Shin Sasaki, Naoko Bando, Yukinobu Nagayasu, Eri Nakamura, Ahsan Budrul, Wei Qu
		  </td>
        </tr>
        <tr>           <td colspan="3" height="50"> 
            <div align="left" class="arial"><b><font color="#006699"> Ex-members </font></b></div>
          </td>
        </tr>
        <tr>
          <td colspan="3" class="arial"> 
Miwaka Ohtani, Hiroshi Sawai, Ayaka Saka, Daisuke Watanabe,
Mitsuhiro Abe, Yuri Nagai, Kouichiro Doi, Tomoyuki Yamada, Masahiro Kasahara
       </td></tr>
        <tr> 

          <td colspan="3" height="50"> 
            <div align="left" class="arial"><b><font color="#006699"> Acknowledgments 
              </font></b></div>
          </td>
        </tr>
        <tr> 
          <td colspan="3" class="arial"> This research has been partly supported 
            by the Institute for Bioinformatics and Research and Development (<a href="http://bird.jst.go.jp/index_e.html">BIRD</a>) 
            of the Japan Science and Technology Corporation (JST). </td>
        </tr>
      </table>
    </td>
  </tr>
</table>


<p> </p>
</body>
</html>
