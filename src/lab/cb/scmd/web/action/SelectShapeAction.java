//--------------------------------------
// SCMDServer
// 
// SelectShapeAction.java 
// Since: 2004/07/29
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import lab.cb.scmd.web.bean.SelectedShape;

/**
 * @author leo
 *
 */
public class SelectShapeAction extends Action
{

    /**
     * 
     */
    public SelectShapeAction()
    {
        super();
    }
    
    

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        SelectedShape shape = (SelectedShape) form;

        String action = request.getParameter("action");
        if(action != null)
        {
            if(action.equals("setParameter"))
            {
                shape.setParameter();
                int phase = shape.getCurrentPhaseID();
                if(shape.getAreaRatio() == 0 && phase == 2)
                    shape.setPhase(5);  // no bud �̂Ƃ��́Abud �p�����[�^�̑I�����X�L�b�v
                else
                    shape.setPhase(phase + 1);
            }
        }
        
        shape.setInputValue(shape.getParameterValue());
        
        return mapping.findForward("success");
    }
}


//--------------------------------------
// $Log: SelectShapeAction.java,v $
// Revision 1.4  2004/08/29 13:15:11  leo
// morphology search�Bbud size -> bud area ratio�ɕύX
//
// Revision 1.3  2004/08/14 14:32:25  leo
// MorphologySearch�̃C���[�W���\�������悤�ɒ���
// bud size�ɂ��I�𕔕����A���l�Œ���������悤�ɕύX����K�v����
//
// Revision 1.2  2004/07/30 06:40:29  leo
// Morphology Search�̃C���^�[�t�F�[�X�A����
//
// Revision 1.1  2004/07/29 07:50:45  leo
// MorphologySearch�̃p�[�c�쐬��
//
//--------------------------------------