//--------------------------------------
// SCMDWeb Project
//
// ViewORFDataSheetForm.java
// Since: 2005/02/15
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.formbean;

import org.apache.struts.action.ActionForm;

/**
 * ORFごとのparameterのデータシート表示用の、入力を受け取るクラス
 * @author leo
 *
 */
public class ViewORFDataSheetForm extends ActionForm
{
    static public enum Order { asc, desc } 
    int paramID = 221;
    int page = 1;    
    Order order = Order.asc;
    

    /**
     * 
     */
    public ViewORFDataSheetForm()
    {
    }
    public int getPage()
    {
        return page;
    }
    public void setPage(int page)
    {
        this.page = page;
    }
    public int getParamID()
    {
        return paramID;
    }
    public void setParamID(int paramID)
    {
        this.paramID = paramID;
    }
    
    public void setTarget(int id)
    {
        this.paramID = id;
    }
    
    public Order getOrder()
    {
        return order;
    }
    public void setOrder(String order)
    {
        if(order.equals("descending"))
            this.order = Order.desc;
        else
            this.order = Order.asc;            
    }
}




