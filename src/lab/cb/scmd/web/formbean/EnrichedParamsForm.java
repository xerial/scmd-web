package lab.cb.scmd.web.formbean;

import org.apache.struts.action.ActionForm;

public class EnrichedParamsForm extends ActionForm {
	String goid = "";
	
	public EnrichedParamsForm () {
		
	}

	public String getGoid() {
		return goid;
	}

	public void setGoid(String goid) {
		this.goid = goid;
	}

}
