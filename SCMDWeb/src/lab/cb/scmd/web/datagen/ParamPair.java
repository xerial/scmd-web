//--------------------------------------
//SCMDServer
//
//ParamSet.java 
//Since: 2005/02/05
//
//$URL: http://scmd.gi.k.u-tokyo.ac.jp/devel/svn/phenome/trunk/SCMDWeb/src/lab/cb/scmd/web/datagen/Normalizer.java $ 
//$LastChangedBy: leo $ 
//--------------------------------------
package lab.cb.scmd.web.datagen;

/**
 * @author sesejun
 *
 * paramid ��groupid�̃y�A��ۑ����Ă����N���X
 * paramid �������� groupid���w�肵�Ȃ��ꍇ�ɂ́A-1�����Ă���
 * MorphologicalSearch�Ŏg�p
 */
public class ParamPair {
    int paramid = -1;
    int groupid = -1;
    double value = 0.0;
    double weight = 3;
    
    public int getGroupid() {
        return groupid;
    }
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
    public int getParamid() {
        return paramid;
    }
    public void setParamid(int paramid) {
        this.paramid = paramid;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
