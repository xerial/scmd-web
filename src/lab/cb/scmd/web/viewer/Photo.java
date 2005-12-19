//--------------------------------------
// SCMDServer
// 
// Photo.java 
// Since: 2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.viewer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.web.common.PhotoType;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.common.StainType;

/**
 * �C���[�W�摜����ۑ�
 * 
 * @author leo
 * @version $Revision: $ $LastChangedDate: $ $LastChangedBy$
 */
public class Photo {
	int _photoNum;
	int _stainType;
	int _photoType;
	String _orfName;

	public Photo(String orfName_, int photoNum_, int stainType_, int photoType_)
			throws InvalidParameterException {
		super();
		setPhotoData(orfName_,photoNum_,stainType_,photoType_);
	}

	public Photo(String orfName_, int photoNum_) {
		try{
			setPhotoData(orfName_,photoNum_,StainType.STAIN_ConA,PhotoType.ANALYZED_PHOTO);
		} catch(InvalidParameterException ipe) {
			//	�����ɂ��邱�Ƃ͂Ȃ��̂ŉ������Ȃ�
		}
	}

	/**
	 * Photo�f�[�^���Z�b�g����
	 * @param orfName_
	 * @param photoNum_
	 * @param stainType_
	 * @param photoType_
	 * @throws InvalidParameterException
	 */
	public void setPhotoData(String orfName_, int photoNum_, int stainType_, int photoType_) throws InvalidParameterException {
		_orfName = orfName_.toLowerCase();
		_photoNum = photoNum_;
		_stainType = stainType_;
		_photoType = photoType_;

		//	Strain��Photo�^�C�v�����؂���
		StainType.validateStainType(_stainType);
		PhotoType.validatePhotoType(_photoType);
	}
	
	/**
	 * @param orfName
	 * @param stainType
	 * @param photoNum
	 * @param photoType
	 * @return
	 */
	static String getPhotoFileRelativePath(String orfName, int stainType,
			int photoNum, int photoType) {
		return PhotoType.getPhotoDirectory(photoType) + File.separator
				+ orfName + File.separator + orfName + "-"
				+ StainType.getStainTypeName(stainType) + photoNum
				+ PhotoType.getPhotoSuffix(photoType) + ".jpg";
	}

	/**
	 * �ݒ肵���f�[�^����t�@�C���p�X�𐶐�
	 * @param orfName
	 * @param stainType
	 * @param photoNum
	 * @param photoType
	 * @return
	 */
	static String getPhotoFilePath(String orfName, int stainType, int photoNum,
			int photoType) {
		String file = SCMDConfiguration.getProperty("SCMD_PHOTO_DIR")
				+ File.separator
				+ getPhotoFileRelativePath(orfName, stainType, photoNum,
						photoType);
		return file;
	}

	/**
	 * �ݒ肵���f�[�^����URL�𐶐�����
	 * @param orfName
	 * @param stainType
	 * @param photoNum
	 * @param photoType
	 * @return
	 * @throws MalformedURLException
	 */
	static URL getPhotoURL(String orfName, int stainType, int photoNum,
			int photoType) throws MalformedURLException {
		URL photoDIRURL = new URL(SCMDConfiguration
				.getProperty("SCMD_PHOTO_DIR_URL"));
		String photoPath;
		if (photoType == PhotoType.ORIGINAL_PHOTO) {
			photoPath = PhotoType.getPhotoDirectory(photoType) + "/" + orfName
					+ "/" + orfName + "-"
					+ StainType.getStainTypeName(stainType) + photoNum
					+ PhotoType.getPhotoSuffix(photoType) + ".jpg";
		} else {
			photoPath = "image" + "/" + orfName + "/" + orfName + "-"
					+ StainType.IMAGE_NAME[stainType] + photoNum + ".jpg";
		}
		return new URL(photoDIRURL, photoPath);
	}

	/**
	 * @return
	 */
	public String getPhotoFilePath() {
		return Photo.getPhotoFilePath(_orfName, _stainType, _photoNum,
				_photoType);
	}

	/**
	 * �摜�̃p�X��URL�`���œn��
	 * @return
	 * @throws MalformedURLException
	 */
	public URL getPhotoURL() throws MalformedURLException {
		return getPhotoURL(_orfName, _stainType, _photoNum, _photoType);
	}

	/**
	 * @param stainType
	 * @return
	 * @throws InvalidParameterException
	 */
	public String getPhotoFilePathOfAnotherStainType(int stainType)
			throws InvalidParameterException {
		StainType.validateStainType(stainType);
		return Photo.getPhotoFilePath(_orfName, stainType, _photoNum,
				_photoType);
	}

	/**
	 * @param photoType
	 * @param stainType
	 * @return
	 * @throws InvalidParameterException
	 * @throws MalformedURLException
	 */
	public URL getPhotoURL(int photoType, int stainType)
			throws InvalidParameterException, MalformedURLException {
		StainType.validateStainType(stainType);
		return getPhotoURL(_orfName, stainType, _photoNum, photoType);
	}

	/**
	 * @param photoType
	 * @return
	 */
	public String getPhotoFilePathOfAnotherPhotoType(int photoType) {
		PhotoType.validatePhotoType(photoType);
		return Photo.getPhotoFilePath(_orfName, _stainType, _photoNum,
				photoType);
	}

	/**
	 * @return
	 */
	public String getOrf() {
		return _orfName;
	}

	/**
	 * @param name
	 */
	public void setOrf(String name) {
		_orfName = name;
	}

	/**
	 * @return
	 */
	public int getPhotoNum() {
		return _photoNum;
	}

	/**
	 * @param photoNum
	 */
	public void setPhotoNum(int photoNum) {
		_photoNum = photoNum;
	}

	/**
	 * @return
	 */
	public int getPhotoType() {
		return _photoType;
	}

	/**
	 * @param type
	 */
	public void setPhotoType(int type) {
		_photoType = type;
	}

	/**
	 * @return
	 */
	public int getStainType() {
		return _stainType;
	}

	/**
	 * @param type
	 */
	public void setStainType(int type) {
		_stainType = type;
	}

	/**
	 * @param photoType
	 * @param stainType
	 * @return
	 */
	public String getImageID(int photoType, int stainType) {
		StringBuilder imageID = new StringBuilder();
		imageID.append("P_");
		imageID.append(getOrf().toLowerCase());
		imageID.append("_");
		imageID.append(photoType);
		imageID.append("_");
		imageID.append(stainType);
		imageID.append("_");
		imageID.append(getPhotoNum());
		return imageID.toString();
	}
}

// --------------------------------------
// $Log: Photo.java,v $
// Revision 1.8 2004/08/14 12:34:50 leo
// cell�̕\�����C��
//
// Revision 1.7 2004/08/13 04:53:38 leo
// URL�̕ύX
//
// Revision 1.6 2004/08/12 14:49:24 leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.5 2004/07/25 11:28:06 leo
// constructor��ǉ�
//
// Revision 1.4 2004/07/20 15:45:59 leo
// xerces2.6.2��jar��ǉ�
//
// Revision 1.3 2004/07/17 05:25:56 leo
// InvalidPathException����菜���܂���
//
// Revision 1.2 2004/07/15 02:31:39 leo
// interface�ł͂Ȃ��Ahas-a�̊֌W�Ƃ��Ďg�p����悤�ɕύX
//
// Revision 1.1 2004/07/14 07:05:30 leo
// SCMD_ROOT�̐ݒ��Tomcat�ɔC����
//
// --------------------------------------
