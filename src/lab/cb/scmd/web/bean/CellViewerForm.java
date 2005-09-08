//--------------------------------------
// SCMDServer
// 
// CellViewerForm.java 
// Since: 2004/08/12
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;


import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lab.cb.scmd.db.common.PageStatus;
import lab.cb.scmd.web.common.DataSheetType;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.StainType;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author leo
 *
 */
public class CellViewerForm extends ActionForm implements Serializable
{
    String orf = "";  
    int stainType = StainType.STAIN_ConA;
    int photoType = PhotoType.ANALYZED_PHOTO;
    int magnification = 50;
    int sheetType = DataSheetType.SHEET_SHAPE;
    int orfSheetType = 0;
    
    PageStatus _photoPageStatus = new PageStatus(1, 0);
    boolean _isReadyPhotoPageMax = false;
    
    
    PhotoBuffer photoBuffer[] = new PhotoBuffer[3];
    boolean _isReadyphotoBuffer = false;
    int photoNum = 0;
    
    /**
     * 
     */
    public CellViewerForm()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
    public int getOrfSheetType()
    {
        return orfSheetType;
    }
    public void setOrfSheetType(int orfSheetType)
    {
        this.orfSheetType = orfSheetType;
    }
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		discardPhotoBuffer();		
	}
	
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = super.validate(mapping, request);
        
        // invalidなmagnificationがセットされた場合の処理
        
        if(magnification < 50 || magnification > 125 || (magnification % 25) != 0) 
            magnification = 50;
        
        if(getPhotoPage() <=0)
            setPhotoPage(1);
        
        if(!StainType.isValid(stainType))
        {
            stainType = StainType.STAIN_ConA;
        }
        
        if(!PhotoType.isValid(photoType))
        {
            photoType = PhotoType.ANALYZED_PHOTO;
        }

        if(!DataSheetType.isValidDataSheetType(sheetType))
        {
            sheetType = DataSheetType.SHEET_SHAPE;
        }
        
        if(orf == null || orf.length() <= 0)
            orf = "yor202w";
        
        return errors;
    }
    
    public int getMagnification() {
        return magnification;
    }
    public void setMagnification(int magnification) {
        this.magnification = magnification;
    }
    public String getOrf() {
        final Pattern p = Pattern.compile("(y[a-z]{2}[0-9]{3})([wc])([.]+)?");
        Matcher m = p.matcher(orf);
        if(m.matches())
            return m.group(1).toUpperCase()+m.group(2).toLowerCase() + (m.group(3) != null ? m.group(3) : "");
        else
            return orf.toUpperCase();
    }
    public void setOrf(String orf) {
        String previousORF = this.orf;
        String newORF = orf.toLowerCase();
        if(this.orf == null || !previousORF.equals(newORF))
    	{
    		this.orf = newORF;
    		_photoPageStatus = new PageStatus(1, 1); // reset page
    		_isReadyPhotoPageMax = false;
    	}
    }
    public int getPhotoType() {
        return photoType;
    }
    public void setPhotoType(int photoType) {
        this.photoType = photoType;
    }
    public int getStainType() {
        return stainType;
    }
    public void setStainType(int stainType) {
        this.stainType = stainType;
    }
    
    public int getPhotoPage()
    {
        return _photoPageStatus.getCurrentPage();
    }
    
    public void setPhotoPage(int page)
    {
        _photoPageStatus.setCurrentPage(page);
    }
    
    public int getPhotoPageMax()
    {
        return _photoPageStatus.getMaxPage();
    }
    public void setPhotoPageMax(int page)
    {
        _photoPageStatus.setMaxPage(page);
        _isReadyPhotoPageMax = true;
    }
    
    public Map setupQueryMap(Map map)
    {
        map.put("orf", getOrf());
        map.put("stainType", new Integer(getStainType()));
        map.put("photoType", new Integer(getPhotoType()));
        map.put("photoNum", new Integer(getPhotoNum()));
        map.put("magnification", new Integer(getMagnification()));
        return map;
    }
    
    
    public int getSheetType() {
        return sheetType;
    }
    public void setSheetType(int sheetType) {
        this.sheetType = sheetType;
    }
 
    /**
     * @return Returns the photoBuffer.
     */
    public PhotoBuffer[] getPhotoBuffer() {
    	return photoBuffer;
    }
    
    public void discardPhotoBuffer() {
    	photoBuffer = new PhotoBuffer[3];
    	_isReadyphotoBuffer = false;
    }
    
    public void loadImage()
    {
        for(int i=0; i<photoBuffer.length; i++)
        {
            photoBuffer[i] = new PhotoBuffer(orf, photoNum, i, photoType);
        }
        _isReadyphotoBuffer = true;
    }

	public int getPhotoNum() {
		return photoNum;
	}
	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}
	
	public boolean isReadyPhotoPageMax()
	{
	    return _isReadyPhotoPageMax;
	}
}


//--------------------------------------
// $Log: CellViewerForm.java,v $
// Revision 1.8  2004/08/31 04:46:21  leo
// グループ毎のデータシートの作成終了
// 検索、データシートのページ移動も終了
//
// Revision 1.7  2004/08/14 10:56:39  leo
// CellViewerFormへの対応
//
// Revision 1.6  2004/08/13 19:17:56  leo
// *** empty log message ***
//
// Revision 1.5  2004/08/13 09:35:08  leo
// *** empty log message ***
//
// Revision 1.3  2004/08/12 19:20:48  leo
// 結果のサイズが０の時の対処を追加
//
// Revision 1.2  2004/08/12 17:48:26  leo
// update
//
// Revision 1.1  2004/08/12 14:49:24  leo
// DBとの接続開始
//
//--------------------------------------