//--------------------------------------
// SCMDWeb Project
//
// DrawTeardropForm.java
// Since: 2005/02/18
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.formbean;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Teardropを描画するためのForm
 * @author leo
 *
 */
public class DrawTeardropForm extends ActionForm
{
    int paramID = 221;
    int groupID = 0;
    double rangeBegin = -1;
    double rangeEnd = -1;
    String orientation = "";
    boolean plotTargetORF = true;
    boolean plotUserORF = true;
    
    /**
     * 
     */
    public DrawTeardropForm()
    {
        super();
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
    {
        if(!(rangeBegin < rangeEnd))
        {
            rangeEnd = rangeBegin;
        }
        return super.validate(arg0, arg1);
    }
    
    public int getParamID()
    {
        return paramID;
    }
    public void setParamID(int paramID)
    {
        this.paramID = paramID;
    }
    
    
    public boolean isPlotUserORF()
    {
        return plotUserORF;
    }
    public void setPlotUserORF(boolean plotUserORF)
    {
        this.plotUserORF = plotUserORF;
    }
    
    public double getRangeBegin()
    {
        return rangeBegin;
    }
    public void setRangeBegin(double rangeBegin)
    {
        this.rangeBegin = rangeBegin;
    }
    public double getRangeEnd()
    {
        return rangeEnd;
    }
    public void setRangeEnd(double rangeEnd)
    {
        this.rangeEnd = rangeEnd;
    }
    
    
    public String getOrientation()
    {
        return orientation;
    }
    public void setOrientation(String orientation)
    {
        this.orientation = orientation;
    }
    
    public boolean isPlotTargetORF()
    {
        return plotTargetORF;
    }
    public void setPlotTargetORF(boolean plotTargetORF)
    {
        this.plotTargetORF = plotTargetORF;
    }
    
    
    public int getGroupID()
    {
        return groupID;
    }
    public void setGroupID(int groupID)
    {
        this.groupID = groupID;
    }
}




