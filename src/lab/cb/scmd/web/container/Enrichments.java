package lab.cb.scmd.web.container;

import lab.cb.scmd.web.sessiondata.MorphParameter;

/*
 * Enrichment‚ð•Û‘¶‚·‚éƒNƒ‰ƒXB
 * 
 */
public class Enrichments {
	String param = "";
	MorphParameter morphParam = null;
	int fwd = 0; // 1 -> forward, 0 -> reverse
	int high = 0; // 1 -> high, 0-> low
	int ingo = 0;
	int inabnorm = 0;
	int intarget = 0;
	double pvalue = 0.0;
	double ratio = 0.0;
	
	public int getFwd() {
		return fwd;
	}
	public void setFwd(int fwd) {
		this.fwd = fwd;
	}
	public int getHigh() {
		return high;
	}
	public void setHigh(int high) {
		this.high = high;
	}
	public int getInabnorm() {
		return inabnorm;
	}
	public void setInabnorm(int inabnorm) {
		this.inabnorm = inabnorm;
	}
	public int getIngo() {
		return ingo;
	}
	public void setIngo(int ingo) {
		this.ingo = ingo;
	}
	public int getIntarget() {
		return intarget;
	}
	public void setIntarget(int intarget) {
		this.intarget = intarget;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public double getPvalue() {
		return pvalue;
	}
	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public void setMorphParameter(MorphParameter morphParam) {
		this.morphParam = morphParam;
	}
	public MorphParameter getMorphParameter() {
		return morphParam;
	}
	
}
