//--------------------------------------
// SCMD Project
// 
// ImageServer.java 
// Since:  2004/07/13
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.image;

import java.io.*;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import lab.cb.scmd.exception.InvalidParameterException;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.image.ImageConverter;
import lab.cb.scmd.web.bean.TargetPhoto;
import lab.cb.scmd.web.common.SCMDConfiguration;
import lab.cb.scmd.web.viewer.Photo;

/**
 * @author leo
 * @deprecated 
 */
public class ImageServer extends HttpServlet
{

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        String convertProgram = SCMDConfiguration
                .getProperty(SCMDConfiguration.IMAGEMAGICK_CONVERT);
//        _photoDir = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_PHOTO_URL));

        try
        {
            _imageConverter = new ImageConverter(new File(convertProgram));
        }
        catch (FileNotFoundException e)
        {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        PrintStream out = new PrintStream(response.getOutputStream());

        TargetPhoto targetPhoto = (TargetPhoto) request.getAttribute("targetPhoto");
        if(targetPhoto == null)
        {
            displayNotAvailable(request, response);
        }
        String orf = targetPhoto.getOrf();
        int magnification = targetPhoto.getMagnification();
        int photoID = targetPhoto.getPhotoNum();
        int stainType = targetPhoto.getStainType();
        int photoType = targetPhoto.getPhotoType();
        
        Photo photo;
        try
        {
            photo = new Photo(orf, photoID, stainType, photoType);
        }
        catch (InvalidParameterException e)
        {
            throw new ServletException(e);
        }

        try
        {
            //File photoPath = new File(photo.getPhotoFilePath());
            URL photoURL = photo.getPhotoURL();
            try
			{
              	ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
            	_imageConverter.scaleImage(bufferOut, photoURL, magnification);
				response.setContentType("image/jpeg");
				out.write(bufferOut.toByteArray());
                
//				BufferedImage origImage = ImageIO.read(photoURL);
//				Graphics2D g = origImage.createGraphics();
//				/*
//				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//				*/
//				
//		        int width = origImage.getWidth();
//		        int height = origImage.getHeight();
//		        int newWidth = width * magnification / 100;
//		        int newHeight = height * magnification / 100;
//		        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//		        Graphics2D scaledImageGraphics = scaledImage.createGraphics();
//		        scaledImageGraphics.drawImage(origImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
//				
//		        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		        encoder.encode(scaledImage);
			}
            catch(IOException e)
			{
            	log(e.getMessage());
            	displayNotAvailable(request, response);
			}
        }
        catch(SCMDException e)
        {
            e.what();
            displayNotAvailable(request, response);
        }
    }
    
    private void displayNotAvailable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	File photoPath = new File(SCMDConfiguration.getProperty(SCMDConfiguration.SCMD_ROOT), "/image/notavailable.png");
    	response.setContentType("image/png");
    	request.getRequestDispatcher("/png/notavailable.png").forward(request, response);
    }

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException
    {
        doGet(arg0, arg1);
    }

//    File           _photoDir;

    protected ImageConverter _imageConverter;
}

//--------------------------------------
// $Log: ImageServer.java,v $
// Revision 1.21  2004/09/21 06:13:05  leo
// warning fix
//
// Revision 1.20  2004/09/09 02:15:10  leo
// bufferedimage�ŁAclipping������悤�ɂ��Ă݂�
//
// Revision 1.19  2004/08/23 07:09:09  leo
// *** empty log message ***
//
// Revision 1.18  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.17  2004/08/12 17:48:26  leo
// update
//
// Revision 1.16  2004/08/12 14:49:24  leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.15  2004/08/11 07:28:37  leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.14  2004/08/09 13:47:03  leo
// �ʐ^���Ȃ��Ƃ��A��։摜��\������悤�ɂ���
//
// Revision 1.13  2004/08/09 12:26:42  leo
// Comment��ǉ�
//
// Revision 1.12  2004/08/06 14:43:15  leo
// �摜�\�����A�N�V�������o�R����悤�ɂ���
//
// Revision 1.11  2004/08/05 14:10:45  leo
// ImageServer��session��ǂނ̂ł͂Ȃ��AGET�Ŏ擾����悤�ɕύX
//
// Revision 1.10  2004/07/26 19:33:31  leo
// Action�̏C���BDataSheet�y�[�W���H
//
// Revision 1.9  2004/07/20 08:44:15  leo
// photo viewer�p�̃R�[�h��ǉ�
//
// Revision 1.8  2004/07/17 08:03:46  leo
// session�Ǘ��ɂ��photoViewer���H
//
// Revision 1.7  2004/07/17 05:25:56  leo
// InvalidPathException����菜���܂���
//
// Revision 1.6 2004/07/16 16:44:25 leo
// doPost��doGet�ɂ܂킷�悤�ɂ�������
//
// Revision 1.5 2004/07/15 09:21:20 leo
// jsp �̎g�p�J�n
//
// Revision 1.4 2004/07/14 07:17:47 leo
// SCMD_ROOT�̐ݒ���@��ύX
//
// Revision 1.3 2004/07/14 03:18:37 leo
// ImageMagick��A�ʐ^�̓����Ă���t�H���_��configuration�t�@�C����
// �w��ł���悤��SCMDConfiguration�N���X(Singleton)���쐬
//
// Revision 1.2 2004/07/13 16:21:40 leo
// update
//
// Revision 1.1 2004/07/13 15:42:00 leo
// first ship
//
//--------------------------------------
