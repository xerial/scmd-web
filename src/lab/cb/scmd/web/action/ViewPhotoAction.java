//--------------------------------------
// SCMDServer
// 
// ViewPhotoAction.java 
// Since: 2004/07/17
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;


//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
import org.apache.struts.action.Action;
////import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionForm;
//import org.apache.struts.action.ActionForward;
//import org.apache.struts.action.ActionMapping;
//
//import lab.cb.scmd.web.common.PhotoType;
//import lab.cb.scmd.web.common.StainType;

/** @deprecated �����g���܂���
 * @author leo
 *
 */
public class ViewPhotoAction extends Action
{

    /**
     * 
     */
    public ViewPhotoAction()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
    
//    public ActionForward execute(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response) throws Exception
//    {
//        // PropertyUtils.getSimpleProperty(form, "orf");
//        
//    	String action = request.getParameter("action");
//    	if(action == null)
//    	{
//    	    action = "";
//    	}
//    	
//        CurrentView view = (CurrentView) form;
//        
//        /*
//        String orf = (String) request.getAttribute("orf");
//        if(orf != null)
//        {
//            if(!orf.equals(view.getOrf()))
//            {
//                // reset
//                view.setIsLoadedCellList(false);
//            }
//            view.setOrf(orf);
//        }
//        */
//
//    	if(action.equals("viewCell"))
//    	{
//    	    view.setStainType(StainType.STAIN_ConA);
//    	}
//    	else if(action.equals("viewNucleus"))
//    	{
//    	    view.setStainType(StainType.STAIN_DAPI);
//    	}
//    	else if(action.equals("viewActin"))
//    	{
//    	    view.setStainType(StainType.STAIN_RhPh);
//    	}
//    	else if(action.equals("viewOriginal"))
//    	{
//    	    view.setPhotoType(PhotoType.ORIGINAL_PHOTO);
//    	}
//    	else if(action.equals("viewAnalyzed"))
//    	{
//    	    view.setPhotoType(PhotoType.ANALYZED_PHOTO);
//    	}
//    	else if(action.equals("moveToNext"))
//    	{
//    	    view.setCurrentPhotoPage((view.getCurrentPhotoPage() + 1) % view.getMaxPhotoPage());
//    	    view.setIsLoadedCellList(false);
//    	}
//    	else if(action.equals("moveToNext10"))
//    	{
//    	    view.setCurrentPhotoPage((view.getCurrentPhotoPage() + 10) % view.getMaxPhotoPage());
//    	    view.setIsLoadedCellList(false);
//    	}
//    	else if(action.equals("moveToPrev"))
//    	{
//    	    view.setCurrentPhotoPage((view.getCurrentPhotoPage() - 1) % view.getMaxPhotoPage());
//    	    view.setIsLoadedCellList(false);
//    	}
//    	else if(action.equals("moveToPrev10"))
//    	{
//    	    view.setCurrentPhotoPage((view.getCurrentPhotoPage() - 10) % view.getMaxPhotoPage());
//    	    view.setIsLoadedCellList(false);
//    	}
//    	
//    	
//    	// business logic validation
//    	if(view.getCurrentPhotoPage() <= 0)
//    	    view.setCurrentPhotoPage(1);
//    	
//    	if(!view.isLoadedCellList())
//    	{
//    	    // load Clipping Region
//    	    view.loadCellList();
//    	}
//    	if(!view.isLoadedAlias())
//    	{
//    	    view.loadAlias();
//    	}
//    	    
//        return mapping.findForward("success");
//    }

    
}


//--------------------------------------
// $Log: ViewPhotoAction.java,v $
// Revision 1.11  2004/08/14 11:09:08  leo
// Warning�̐����A�����g��Ȃ��Ȃ����N���X��deprecated�}�[�N�����܂���
//
// Revision 1.10  2004/08/14 10:55:44  leo
// photoID��validation����߂�
//
// Revision 1.9  2004/08/01 08:55:51  leo
// Stats�y�[�W���쐬
//
// Revision 1.8  2004/07/26 22:43:32  leo
// PhotoBuffer��p���āADataSheet�̕\����������
//
// Revision 1.7  2004/07/26 19:33:31  leo
// Action�̏C���BDataSheet�y�[�W���H
//
// Revision 1.6  2004/07/26 11:19:11  leo
// Yeast Mutants page�p�̃N���X��ǉ�
//
// Revision 1.5  2004/07/25 14:57:16  leo
// Configuration���`�F�b�N���邽�߂̃y�[�W���쐬
//
// Revision 1.4  2004/07/25 11:26:46  leo
// �f�[�^�x�[�X�ւ̃A�N�Z�X�����𓱓�
//
// Revision 1.3  2004/07/24 14:35:31  leo
// *** empty log message ***
//
// Revision 1.2  2004/07/20 08:44:15  leo
// photo viewer�p�̃R�[�h��ǉ�
//
// Revision 1.1  2004/07/17 08:03:46  leo
// session�Ǘ��ɂ��photoViewer���H
//
//--------------------------------------