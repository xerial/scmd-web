//--------------------------------------
// SCMDServer
// 
// CurrentView.java 
// Since: 2004/07/17
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionMapping;
//
//import lab.cb.scmd.db.common.XMLQuery;
//import lab.cb.scmd.exception.SCMDException;
//import lab.cb.scmd.web.common.SCMDConfiguration;
//import lab.cb.scmd.web.xml.CellCoordinatesSAXReader;
//import lab.cb.scmd.web.xml.ORFListReaderSAXHandler;
//import lab.cb.scmd.web.xml.SCMDSAXParser;
//import lab.cb.scmd.web.xml.XMLReaderThread;

/** @deprecated ������Ə璷�ɂȂ肷�����̂ŁA�̂Ă�R�[�h�Ƃ��܂�
 * @author leo
 *  
 */
//public class CurrentView extends ActionForm
//{
//    String     orf               = "yor202w";
//    String 	   standardName	     = "";
//    String     alias             = "";
//    boolean _isLoadedAlias    = false;
//    Collection cellList          = new LinkedList();
//    boolean    _isLoadedCellList = false;
//    int        photoID           = 1;
//    int 	   photoNum 		  = 1;
//    int        stainType         = 0;
//    int        photoType         = 1;
//    static final String[]   tabName = { "Cell", "Nucleus", "Actin"};
//    static final String[]   datasheetTabName = { "Shape", "Cell", "Nucleus", "Actin", "Custom"};
//    int 	    sheetType		  = 0;
//    
//    int        magnification     = 50;
//
//    int        currentPhotoPage  = 1;
//    int        maxPhotoPage      = 1;
//    PhotoBuffer photoBuffer[] = new PhotoBuffer[3];
//    boolean	isLoadedImages = false;
//
//    /**
//     *  
//     */
//    public CurrentView()
//    {
//        super();
//        //loadCellList();
//    }
//
//    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
//        ActionErrors errors = super.validate(mapping, request);
//
//        // invalid��magnification���Z�b�g���ꂽ�ꍇ�̏���
//        int m = getMagnification();
//        if(m < 50 || m > 125 || (m % 25) != 0) setMagnification(50);
//        
//        if(currentPhotoPage <=0)
//            currentPhotoPage = 1;
//        if(sheetType < 0 || sheetType >= datasheetTabName.length)
//        {
//            sheetType = 0;
//        }
//        
//        return errors;
//    }
//
//    public void reset(ActionMapping mapping, HttpServletRequest request) {
//        super.reset(mapping, request);
//    }
//
//    public Map getPhotoArgumentMap()
//    {
//        TreeMap map = new TreeMap();
//        map.put("orf", orf);
//        map.put("photoNum", new Integer(photoID));
//        map.put("magnification", new Integer(magnification));
//        map.put("photoType", new Integer(photoType));
//        map.put("stainType", new Integer(stainType));
//        return map;
//    }
//    
//    public String getSheetName()
//    {
//        return datasheetTabName[sheetType];
//    }
//    
//    public void loadCellList() {
//        try
//        {
//            CellCoordinatesSAXReader coordinatesReader = new CellCoordinatesSAXReader();
//            XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
//            XMLReaderThread readerThread = new XMLReaderThread(new SCMDSAXParser(coordinatesReader));
//            readerThread.start();
//            query.getCellCoordinates(readerThread.getOutputStream(), orf, photoID);
//            readerThread.join();
//
//            List cellList = coordinatesReader.getCellList();
//            setCellList(cellList);
//
//            setIsLoadedCellList(true);
//        }
//        catch (SCMDException e)
//        {
//            e.printStackTrace();
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void loadAlias() {
//        try
//        {
//            ORFListReaderSAXHandler reader = new ORFListReaderSAXHandler();
//            XMLQuery query = SCMDConfiguration.getXMLQueryInstance();
//            XMLReaderThread readerThread = new XMLReaderThread(new SCMDSAXParser(reader));
////            RecordThread recorder = new RecordThread();
////            readerThread.addPipe(recorder);
//            readerThread.start();
//            query.getORFInfo(readerThread.getOutputStream(), orf, XMLQuery.ORFINFO_GENENAME);
//            readerThread.join();
//
//            List cellList = reader.getYeastGeneList("global");
//            if(cellList.size() > 0)
//            {
//                YeastGene gene = (YeastGene) cellList.get(0);
//                setStandardName(gene.getStandardName());
////                setAlias(gene.getGeneNameString());
//            }            
//            _isLoadedAlias = true;
//        }
//        catch (SCMDException e)
//        {
//            e.printStackTrace();
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
//    public String getStandardName() {
//        return standardName;
//    }
//    public void setStandardName(String standardName) {
//        this.standardName = standardName;
//    }
//    /**
//     * @return Returns the photoBuffer.
//     */
//    public PhotoBuffer[] getPhotoBuffer() {
//        return photoBuffer;
//    }
//    public void discardPhotoBuffer() {
//        photoBuffer = new PhotoBuffer[3];
//        setIsLoadedImages(false);
//    }
//    
//    
//    public void loadImage()
//    {
//        for(int i=0; i<photoBuffer.length; i++)
//        {
//            photoBuffer[i] = new PhotoBuffer(orf, photoID, i, photoType);
//            isLoadedImages = true;
//        }
//    }
//    public boolean isLoadedImages()
//    {
//        return isLoadedImages;
//    }
//    
//    public void setIsLoadedImages(boolean flag)
//    {
//        isLoadedImages = flag;
//    }
//    
//    public boolean isLoadedCellList() {
//        return _isLoadedCellList;
//    }
//    
//    public boolean isLoadedAlias()
//    {
//        return _isLoadedAlias;
//    }
//
//    public void setIsLoadedCellList(boolean flag) {
//        _isLoadedCellList = flag;
//    }
//    public void setIsLoadedAlias(boolean flag)
//    {
//        _isLoadedAlias = flag;
//    }
//
//    public Collection getCellList() {
//        return cellList;
//    }
//
//    public void setCellList(Collection cellList) {
//        this.cellList = cellList;
//    }
//
//    public String getOrf() {
//        return orf;
//    }
//
//    public void setOrf(String orf) {
//        this.orf = orf;
//        loadAlias();
//    }
//
//    public String getAlias() {
//        return alias;
//    }
//
//    public void setAlias(String alias) {
//        this.alias = alias;
//    }
//
//    public int getPhotoID() {
//        return photoID;
//    }
//
//    public void setPhotoID(int photoID) {
//        this.photoID = photoID;
//    }
//
//    public int getPhotoType() {
//        isLoadedImages = false;
//        return photoType;
//    }
//
//    public void setPhotoType(int photoType) {
//        this.photoType = photoType;
//    }
//
//    public int getStainType() {
//        return stainType;
//    }
//
//    public void setStainType(int stainType) {
//        this.stainType = stainType;
//    }
//
//    public String[] getTabName() {
//        return tabName;
//    }
//
//    public int getMagnification() {
//        return magnification;
//    }
//
//    public void setMagnification(int magnification) {
//        this.magnification = magnification;
//    }
//
//    public int getCurrentPhotoPage() {
//        return currentPhotoPage;
//    }
//
//    public void setCurrentPhotoPage(int currentPhotoPage) {
//        this.currentPhotoPage = currentPhotoPage;
//    }
//
//    public int getMaxPhotoPage() {
//        return maxPhotoPage;
//    }
//
//    public void setMaxPhotoPage(int maxPhotoPage) {
//        this.maxPhotoPage = maxPhotoPage;
//    }
//    public int getSheetType() {
//        return sheetType;
//    }
//    public void setSheetType(int sheetType) {
//        this.sheetType = sheetType;
//    }
//    public String[] getDatasheetTabName() {
//        return datasheetTabName;
//    }
//
//}
//
//--------------------------------------
// $Log: CurrentView.java,v $
// Revision 1.26  2004/08/26 08:45:52  leo
// Query�̒ǉ��B selection�̏C��
//
// Revision 1.25  2004/08/25 09:06:00  leo
// userselection�̒ǉ�
//
// Revision 1.24  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.23  2004/08/12 17:48:26  leo
// update
//
// Revision 1.22  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.21  2004/08/11 07:28:37  leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.20  2004/08/11 05:47:13  leo
// XMLOutputter�ɃN�G���̌��ʂ��o�͂���̂ł͂Ȃ��A������OutputStream�ɂ��܂���
//
// Revision 1.19  2004/08/09 13:47:03  leo
// �ʐ^���Ȃ��Ƃ��A��։摜��\������悤�ɂ���
//
// Revision 1.18  2004/08/09 12:26:42  leo
// Comment��ǉ�
//
// Revision 1.17  2004/08/09 03:39:54  sesejun
// SCMDXMLQuery���Ăяo���Ă���̂��AMock�ւ̕ύX
//
// Revision 1.16  2004/08/09 03:39:23  sesejun
// ORFList��DB���琶��
//
// Revision 1.15  2004/08/06 14:43:15  leo
// �摜�\�����A�N�V�������o�R����悤�ɂ���
//
// Revision 1.14  2004/08/03 06:19:04  leo
// �X�^�C���̔�����
//
// Revision 1.13  2004/08/02 07:57:16  leo
// ������
//
// Revision 1.12  2004/08/01 08:55:51  leo
// Stats�y�[�W���쐬
//
// Revision 1.11  2004/07/31 05:11:36  leo
// *** empty log message ***
//
// Revision 1.10  2004/07/27 05:17:50  leo
// Datasheet�̃T���v���\��
//
// Revision 1.9  2004/07/26 22:52:13  leo
// �������Ǘ��ɂ��čl�@���c
//
// Revision 1.8  2004/07/26 22:43:32  leo
// PhotoBuffer��p���āADataSheet�̕\����������
//
// Revision 1.7  2004/07/26 19:33:31  leo
// Action�̏C���BDataSheet�y�[�W���H
//
// Revision 1.6  2004/07/26 14:20:25  leo
// *** empty log message ***
//
// Revision 1.5  2004/07/25 11:26:57  leo
// �f�[�^�x�[�X�ւ̃A�N�Z�X�����𓱓�
//
// Revision 1.4 2004/07/24 14:35:31 leo
// *** empty log message ***
//
// Revision 1.3 2004/07/22 14:42:39 leo
// test�̎d�����኱�ύX
//
// Revision 1.2 2004/07/20 08:44:15 leo
// photo viewer�p�̃R�[�h��ǉ�
//
// Revision 1.1 2004/07/17 08:03:46 leo
// session�Ǘ��ɂ��photoViewer���H
//
//--------------------------------------
