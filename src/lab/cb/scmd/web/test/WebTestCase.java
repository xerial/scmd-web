package lab.cb.scmd.web.test;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public abstract class WebTestCase {
	protected String description = "";	//	�e�X�g�ڍׁi�e�X�g��������e�j
	protected String name = "";			//	�e�X�g��
	protected String report = "";		//	���|�[�g
	protected boolean error = false;	//	�G���[�������邩�ǂ���

	/**
	 * �C���X�g���N�^�[���ɖ��O�ƃe�X�g�������Z�b�g����
	 * @param name
	 * @param description
	 */
	public WebTestCase(String name,String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * �e�X�g�I����ɌĂ΂��
	 */
	public void destory() {
		
	}

	/**
	 * �ڍׂ�Ԃ�
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	/**
	 * �G���[���N�����ۂɂ������烁�b�Z�[�W������悤�ɂ���
	 * @return
	 */
	public String getReport() { 
		return this.report;
	}

	/**
	 * �e�X�g�O�ɌĂ΂��
	 */
	public void init() {
		
	}

	/**
	 * �ڍׂȃ��|�[�g����n��
	 * @param report
	 */
	protected void setReport(String report) {
		this.report = report;
	}
	/**
	 * Exception�������̂܂ܓ����
	 * @param e
	 */
	protected void setReport(Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		e.printStackTrace(pw);
		setReport(writer.toString());
		pw.close();
	}	
	/**
	 * �e�X�g�����Đ���ȏꍇ��true
	 * �ُ킪����ꍇ��false��Ԃ�
	 * @return
	 */
	public abstract boolean test();
	
	/**
	 * 
	 * @param error
	 */
	protected boolean setIsError(boolean error) {
		this.error = error;
		return error;
	}
	
	public boolean isError() {
		return error;
	}
}


//	-------------------------
//	$log: $
//	-------------------------