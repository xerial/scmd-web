//--------------------------------------
//SCMDWeb Project
//
//ViewParamSheetForm.java
//Since: 2005/02/14
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/formbean/ViewCustomizeForm.java $ 
//$Author: sesejun $
//--------------------------------------
package lab.cb.scmd.web.formbean;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author sesejun
 *
 */
public class ViewParamSheetForm extends ActionForm 
{
    String param = "";
    
    public ViewParamSheetForm() {
        super();
    }
        
    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        super.reset(arg0, arg1);
    }
    
    public String getParam() {
        return param;
    }
    public void setParam(String param) {
        this.param = param;
    }
}
