//--------------------------------------
// SCMDWeb Project
//
// Observer.java
// Since: 2005/02/08
//
// $URL$ 
// $Author$
//--------------------------------------
package lab.cb.scmd.web.common;

/**
 * SCMDConfigurationが再セットされたときに呼ばれる
 * @author leo
 *
 */
public interface  ConfigObserver
{
    /**
     * SCMDConfigurationがreloadされたときに呼ばれるメソッド
     */
    public void reloaded();
}



