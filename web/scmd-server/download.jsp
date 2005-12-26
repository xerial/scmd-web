<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="scmd-base" uri="http://scmd.gi.k.u-tokyo.ac.jp/taglib/scmd-base" %>
<%@ taglib prefix="scmd-tags" tagdir="/WEB-INF/tags" %>

<scmd-base:header title="Download" css="/css/toppage.css"/>

<body class="toppage">
<scmd-tags:menu searchframe="off" top="true"/>

<table width="600" class="infoframe">
<tr>
<td>

<p class="title">Data Release Policy </p>
<div class="note">
<p>We (the Saccharomyces Cerevisiae Morphological Database project) herein make a collection of micrographs, processed images, and morphological parameter values of yeast mutants publicly available by providing image browsing and data mining functions before scientific publication. We reserve the exclusive right to publish, in a timely manner, the comprehensive analysis of all the data in the SCMD. Reserved analyses include the overall statistics of morphological parameters, and the comprehensive analysis of correlations between morphological parameters and other biological data such as gene ontology categories, gene expressions, gene localizations, and gene interactions. All users may search and use the SCMD database freely under the restrictions of the previous paragraph. Since the current version is still a preliminary one and may contain mistakes, users should use the data at their own risk and are not allowed to redistribute or repackage the data.</p>
<p>When users publish the analysis of individual genes using the data of this site, they should include the acknowledgement 
<font color="#C03333">"The SCMD database has been provided freely by the University of Tokyo for use in this publication/correspondence only."</font></p>
<p>Finally, we are continuing to improve the SCMD database; therefore, any feedback information from the users should be highly welcomed.</p>
</div>

<p class="title">Download</p>

<p class="subtitle">Tab-separated ORF Parmeter Data Sheets </p> 
 <div class="note">You also can see partial previews of the data sheets below from the <html:link page="/ViewORFParameter.do">ORF Parmeter Sheet</html:link> page. 
 </div>
<ul>
<li><html:link href="pnas/mutant_analysis.tab">Mutant Data (501 parameters x 4718 mutants) (43.3MBytes) </html:link> </li>
<li><html:link href="pnas/wildtype_analysis.tab">Wild type Data (501 parameters x 126 mutants) (1.2MBytes)  </html:link> </li>
</ul>
<p class="notice">Notice!</p> 
<div class="note">
These files are too large to open with Microsoft Excel (version 2003 or prev.), OpenOffice2.0, etc. Thus, 
do not try to open these links above directly from your browser, but download them as files (e.g. right click the links, and choose save as ...).  
<br/>
To handle such large files, we recommend you to use scripting languages such as 
<a href="http://perl.com">Perl</a> or <a href="http://www.ruby-lang.org">Ruby</a>, etc. These programming languages
are designed to be easy to learn, compared to the other traditional programming languages (e.g. C/C++, Java, etc...) 
<br/>
<br/>
Here is a sample scirpt written in Ruby that picks some specific columns from the tab-separated datasheet.
</div>
<pre>

# colpicker.rb
# create a hash that maps a ORF parameter name to its col number
col_name = gets.chomp.split(/\t/)
col_map = {}
for i in 0..col_name.length
col_map[col_name[i]] = i
end

# pick up col data in name, A119 and DCV198_C.
output_param = [ "name", "A119", "DCV198_C" ]

# output col names
puts output_param.join(", ") 
# pick up the specific columns
while gets
cols = chomp.split(/\t/)
line = []
output_param.each do |param|
line << cols[col_map[param]] if defined? col_map[param]
end
puts line.join(", ")
end
</pre>

<div class="note"> A usage example : </div>
<pre>
> ruby colpicker.rb < wildtype_analysis.tab
name, A119, DCV198_C
HIS3-001, 0.16753926701570682, 0.32862727076517567
HIS3-010, 0.14772727272727273, 0.32150172876549693
HIS3-100, 0.14912280701754385, 0.41356787768258824
HIS3-011, 0.17721518987341772, 0.32968679784387406
HIS3-012, 0.1487603305785124, 0.34199674105762645
HIS3-013, 0.13725490196078433, 0.34273099314712885
HIS3-014, 0.09774436090225563, 0.2997897598898407
HIS3-015, 0.22727272727272727, 0.3182409384459814
HIS3-016, 0.1610738255033557, 0.34367878038965255
HIS3-017, 0.16666666666666666, 0.3907860916398882
HIS3-018, 0.15966386554621848, 0.27215895624014746
HIS3-019, 0.11538461538461539, 0.37650734396716856
...
</pre>

</td>
</tr>
</table>


<scmd-base:footer/>

