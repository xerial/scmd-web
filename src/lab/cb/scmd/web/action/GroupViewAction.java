//--------------------------------------
// SCMDServer
// 
// GroupViewAction.java 
// Since: 2004/08/11
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.util.image.BoundingRectangle;
import lab.cb.scmd.util.xml.XMLUtil;
import lab.cb.scmd.web.action.logic.DBUtil;
import lab.cb.scmd.web.bean.CellViewerForm;
import lab.cb.scmd.web.bean.GroupViewForm;
import lab.cb.scmd.web.common.Cell;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.SCMDThreadManager;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.image.ImageCache;
import lab.cb.scmd.web.table.ColLabelIndex;
import lab.cb.scmd.web.table.ImageElement;
import lab.cb.scmd.web.table.Link;
import lab.cb.scmd.web.table.RowLabelIndex;
import lab.cb.scmd.web.table.StringElement;
import lab.cb.scmd.web.table.Table;
import lab.cb.scmd.web.table.TableElement;
import lab.cb.scmd.web.table.TableElementList;
import lab.cb.scmd.web.table.decollation.AttributeDecollation;
import lab.cb.scmd.web.table.decollation.AttributeDecollator;
import lab.cb.scmd.web.table.decollation.StyleDecollator;
import lab.cb.scmd.web.viewer.Photo;

/** @deprecated
 * @author leo
 *
 */
public class GroupViewAction extends Action
{
    static final String[][] _groupName =
    {
            { "no", "small", "medium", "large"},
            { "A", "A1", "B", "C", "D", "E", "F"},
            {"A", "B", "api", "iso", "E", "F"}
    };
     
    /**
     * 
     */
    public GroupViewAction()
    {
        super();
    }
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        GroupViewForm sheetForm = (GroupViewForm) form;
        
        HttpSession session = request.getSession(true);
        CellViewerForm view = (CellViewerForm) session.getAttribute("view");
        if(view == null)
            view = new CellViewerForm();        
        if(request.getParameter("photoType") != null)
            view.setPhotoType(Integer.parseInt(request.getParameter("photoType")));


        TableQuery query = SCMDConfiguration.getTableQueryInstance();
        Table countTable = query.getGroupCount(sheetForm.getOrf(), sheetForm.getStainType());
        if(countTable==null)
            return mapping.findForward("failure");
        RowLabelIndex rowLabelIndex = new RowLabelIndex(countTable);
        ColLabelIndex colLabelIndex = new ColLabelIndex(countTable);
        
        // calc percentage 
        int numCellsInARow = 10;
        int sum = 0;
        int maxPage = 0;
        for(int i=1; i<countTable.getRowSize(); i++)
        {
            try
            {
                int numCell = Integer.parseInt(colLabelIndex.get(i, "count").toString()); 
                sum += numCell;
                int numPage = numCell / numCellsInARow; 
                if(numCell % numCellsInARow != 0)
                    numPage++;
                if(numPage > maxPage)
                    maxPage = numPage;
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // no data
            }
        }
        if(sum == 0)
            sum = 1;
        
        sheetForm.setMaxPage(maxPage);
        
        Table table = new Table();
        
        int stainType = sheetForm.getStainType();
        int width[] = new int[numCellsInARow];
        int height[] = new int[_groupName[stainType].length];
        for(int i=0; i<numCellsInARow; i++)
            width[i] = 0;
        for (int i = 0; i < _groupName[stainType].length; i++)
    		height[i] = 0;
        
        TreeMap argMap = new TreeMap();
        argMap.put("orf", sheetForm.getOrf());
        argMap.put("stainType", Integer.toString(sheetForm.getStainType()));
        argMap.put("page", Integer.toString(sheetForm.getPage()));
        
        LinkedList<Cell> cellsInThePage = new LinkedList<Cell>();
        ImageCache imageCache = ImageCache.getImageCache(request);
        for(int g=0; g<_groupName[stainType].length; g++)
        {
            LinkedList row = new LinkedList();
            String groupName = _groupName[stainType][g];
            TableElement elem = new StringElement(groupName);
            switch(stainType)
            {
            case StainType.STAIN_DAPI:
                elem = new Link(XMLUtil.createCDATA("javascript:help('help/nucleus_param.html');"), elem);
                break;
            case StainType.STAIN_RhPh:
                elem = new Link(XMLUtil.createCDATA("javascript:help('help/actin_param.html');"), elem);
            	break;
            case StainType.STAIN_ConA:
            default:
            }
            row.add(elem);
            
            int count = 0;
            try
            {
                count = Integer.parseInt(rowLabelIndex.get(groupName, colLabelIndex.getColIndex("count")).toString());
            }
            catch(ArrayIndexOutOfBoundsException e)
            {
                // no data was found (skip)
            }
            
            row.add("(" + count + ")");
            NumberFormat format = NumberFormat.getInstance();
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            row.add((format.format((double) count / sum * 100)) + "%"); 
            
            TreeMap map = (TreeMap) argMap.clone();
            map.put("group", groupName);
            TableElementList datasheetCell = new TableElementList();
            datasheetCell.add("[");
            datasheetCell.add(new Link("ViewGroupDataSheet.do", map, "datasheet"));
            datasheetCell.add("]");
            row.add(datasheetCell);
            
            // cell photo array

            Table cellInfo = query.getGroupByDatasheet(sheetForm.getOrf(), DataSheetType.SHEET_PHOTO_INFO_ONLY,
                                                       stainType, groupName,sheetForm.getPage(), numCellsInARow);
            ColLabelIndex colIndex = new ColLabelIndex(cellInfo); 
            
            for(int i=1; i<cellInfo.getRowSize(); i++)
            {
                TreeMap imageMap = new TreeMap();
                //imageMap.put("photoType", Integer.toString(view.getPhotoType()));
                //imageMap.put("photoNum", colIndex.get(i, "image_number"));
                int x1 = Integer.parseInt(colIndex.get(i, "x1").toString());
                int x2 = Integer.parseInt(colIndex.get(i, "x2").toString());
                int y1 = Integer.parseInt(colIndex.get(i, "y1").toString());
                int y2 = Integer.parseInt(colIndex.get(i, "y2").toString());
                int w = x2 - x1 + 4;
                int h = y2 - y1 + 4;
                if(w > width[i-1])
                    width[i-1] = w;
                if(h > height[g])
                    height[g] = h;
                
                //imageMap.put("x1", colIndex.get(i, "x1"));
                //imageMap.put("x2", colIndex.get(i, "x2"));
                //imageMap.put("y1", colIndex.get(i, "y1"));
                //imageMap.put("y2", colIndex.get(i, "y2"));
                
                Photo photo = new Photo(sheetForm.getOrf(), Integer.parseInt(colIndex.get(i, "image_number").toString()), 
                        sheetForm.getStainType(), view.getPhotoType());
                Cell cell = new Cell(photo, Integer.parseInt(colIndex.get(i, "cell_local_id").toString()), new BoundingRectangle(x1, x2, y1, y2));
                cellsInThePage.add(cell);
                String imageID = cell.getImageID(view.getPhotoType(), sheetForm.getStainType());
                imageCache.registerImage(imageID);
                imageMap.put("imageID", imageID);
                imageMap.put("encoding", "jpeg");
                ImageElement img = new ImageElement("scmdimage.img", imageMap);
                img.setProperty("width", Integer.toString(w));
                img.setProperty("height", Integer.toString(h));
                img.setProperty("alt", "cell ID=" + colIndex.get(i, "cell_local_id"));                
                row.add(new AttributeDecollation(new AttributeDecollation(img, "align", "center"), "bgcolor", "black"));
            }   
            table.addRow(row);
        }
        SCMDThreadManager.addTask(new PhotoClippingProcess(imageCache, cellsInThePage, view.getPhotoType(), new int[] { sheetForm.getStainType() } ));
        SCMDThreadManager.addTask(new ImageCache.ImageRecallProcess(imageCache, 20));

        table.decollateCol(0, new StyleDecollator("title"));
        table.decollateCol(0, new AttributeDecollator("width", "40"));
        table.decollateCol(1, new StyleDecollator("small"));
        table.decollateCol(1, new AttributeDecollator("align", "center"));
        table.decollateCol(2, new AttributeDecollator("align", "right"));
        table.decollateCol(2, new StyleDecollator("small"));
        table.decollateCol(3, new StyleDecollator("button"));
        
//        // set width & height
//        for(int i=0; i<numCellsInARow; i++)
//        {
//            table.decollateCol(i+4, new AttributeDecollator("width", Integer.toString(width[i])));
//        }
//        for(int i=0; i<_groupName[stainType].length; i++)
//        {
//            table.decollateRow(i, new AttributeDecollator("height", Integer.toString(height[i])));
//        }
        
        request.setAttribute("photoTable", table);
        request.setAttribute("gene", DBUtil.getGeneInfo(sheetForm.getOrf()));
        return mapping.findForward("success");
    }


//    protected void loadData(GroupViewForm form, HashMap groupToCellList, HashMap groupToCellNum)
//    {
//        Document document;
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setValidating(false);
//        DocumentBuilder builder;
//        try
//        {
//            builder = factory.newDocumentBuilder();
//            ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
//            XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
//            query.getGroupBySheet(bufferOut, form.getOrf(), form.getStainType(), form.getCurrentPage()); 
//            
//            document = builder.parse(new InputSource(new StringReader(bufferOut.toString())));;
//            
//            // read data
//            NodeList resultNodeList = document.getElementsByTagName("lab.cb.scmdresult");
//            if(resultNodeList.getLength() <= 0)
//                return;
//
//            Element resultNode = (Element) resultNodeList.item(0);
//            NodeList categoryList = resultNode.getElementsByTagName("category");
//            Element category = (Element) categoryList.item(0);
//            if(category != null)
//            {
//                String categoryName = category.getAttribute("name");
//                Integer stainType = (Integer) _categoryToStainTypeMap.get(categoryName);
//                if(stainType != null)
//                {
//                    form.setStainType(stainType.intValue());
//                }
//            }
//            
//            NodeList groupList = resultNode.getElementsByTagName("group");
//            for(int i=0; i<groupList.getLength(); i++)
//            {
//                Vector cells = new Vector();
//                
//                Element group = (Element) groupList.item(i);
//                String groupName = group.getAttribute("name");
//                int numCellsInGroup = Integer.parseInt(group.getAttribute("num"));
//                
//                groupToCellNum.put(groupName, new Integer(numCellsInGroup));
//                
//                NodeList orfList = group.getElementsByTagName("orf");
//                for(int o=0; o<orfList.getLength(); o++)
//                {
//                    Element orf = (Element) orfList.item(o);
//                    String orfName = orf.getAttribute("orfname").toString();
//                    NodeList photoList = orf.getElementsByTagName("photo");
//                    for(int p=0; p<photoList.getLength(); p++)
//                    {
//                        Element photo = (Element) photoList.item(p);
//                        int photoID = Integer.parseInt(photo.getAttribute("id"));
//                        NodeList cellList = photo.getElementsByTagName("cell");
//                        for(int c=0; c<cellList.getLength(); c++)
//                        {
//                            Element cell = (Element) cellList.item(c);
//                            int cellID = Integer.parseInt(cell.getAttribute("id"));
//                            int x1 = Integer.parseInt(cell.getAttribute("x1"));
//                            int x2 = Integer.parseInt(cell.getAttribute("x2"));
//                            int y1 = Integer.parseInt(cell.getAttribute("y1"));
//                            int y2 = Integer.parseInt(cell.getAttribute("y2"));
//                            
//                            try
//                            {
//                                cells.add(new Cell(new Photo(orfName, photoID), cellID, new BoundingRectangle(x1, x2, y1, y2)));
//                            }
//                            catch(lab.cb.scmd.exception.InvalidParameterException e)
//                            {
//                                System.err.println(e);
//                            }
//                        }
//                    }
//                }
//                groupToCellList.put(groupName, cells);
//            }
//            
//            
//        }
//        catch (SAXException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        catch (IOException e1)
//        {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        catch (ParserConfigurationException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        
//}
    
}


//--------------------------------------
// $Log: GroupViewAction.java,v $
// Revision 1.6  2004/12/10 08:57:00  leo
// ファイルの移動。追加
//
// Revision 1.5  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.4  2004/09/03 08:14:46  leo
// デザイン調整
//
// Revision 1.3  2004/09/03 07:31:53  leo
// デザインの調整
// standardnameを表示
//
// Revision 1.2  2004/09/01 06:39:59  leo
// TearDropViewを追加
//
// Revision 1.1  2004/08/31 08:44:01  leo
// Group By Sheet完成
//
// Revision 1.5  2004/08/30 10:43:13  leo
// GroupBySheetの作成 pageの移動はまだ
//
// Revision 1.4  2004/08/15 07:14:42  sesejun
// GroupByのDB接続書き始め(未完成)
//
// Revision 1.3  2004/08/14 11:09:08  leo
// Warningの整理、もう使わなくなったクラスにdeprecatedマークを入れました
//
// Revision 1.2  2004/08/12 14:49:24  leo
// DBとの接続開始
//
// Revision 1.1  2004/08/11 14:02:32  leo
// Group by のシート作成
//
//--------------------------------------