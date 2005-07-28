//--------------------------------------
// SCMDServer
// 
// TargetPhoto.java 
// Since: 2004/08/05
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.bean;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.StainType;
import lab.cb.scmd.web.util.CGIUtil;

/**
 * @author leo
 *  
 */
public class TargetPhoto extends ActionForm
{
    String orf = "yor202w";
    int magnification = 50;
    int photoNum      = 1;
    int stainType     = StainType.STAIN_ConA;
    int photoType     = PhotoType.ANALYZED_PHOTO;

    /**
     *  
     */
    public TargetPhoto()
    {
        super();
    }
    
    public TargetPhoto(TargetPhoto photo)
    {
        super();
        setOrf(photo.getOrf());
        setPhotoNum(photo.getPhotoNum());
        setPhotoType(photo.getPhotoType());
        setStainType(photo.getStainType());
        setMagnification(photo.getMagnification());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        // invalidなmagnificationがセットされた場合の処理
        ActionErrors error = super.validate(mapping, request);

        int m = magnification;
        if(m < 50 || m > 125 || (m % 25) != 0) setMagnification(50);

        /*
        if(photoNum <= 0) photoNum = 1;
        if(!StainType.isValid(stainType))
            stainType = StainType.STAIN_ConA;
            
        if(!PhotoType.isValid(photoType))
            photoType = PhotoType.ANALYZED_PHOTO;
            */
            
        return error;
    }
    
    public Map getQueryMap()
    {
        TreeMap map = new TreeMap();
        map.put("orf", orf);
        map.put("magnification", new Integer(magnification));
        map.put("photoNum", new Integer(photoNum));
        map.put("stainType", new Integer(stainType));
        map.put("photoType", new Integer(photoType));
        return map;
    }
    
    public String getQueryString()
    {
        return CGIUtil.getCGIArgument(getQueryMap());
    }
    

    public int getMagnification() {
        return magnification;
    }

    public void setMagnification(int magnification) {
        this.magnification = magnification;
    }

    public int getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(int photoNum) {
        this.photoNum = photoNum;
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
    
    
    public String getOrf() {
        return orf;
    }
    public void setOrf(String orf) {
        this.orf = orf;
    }
}

//--------------------------------------
// $Log: TargetPhoto.java,v $
// Revision 1.4  2004/08/14 10:56:39  leo
// CellViewerFormへの対応
//
// Revision 1.3  2004/08/09 12:26:42  leo
// Commentを追加
//
// Revision 1.2  2004/08/06 14:43:15  leo
// 画像表示もアクションを経由するようにした
//
// Revision 1.1  2004/08/05 14:10:45  leo
// ImageServerをsessionを読むのではなく、GETで取得するように変更
//
//--------------------------------------
