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
	protected String description = "";	//	テスト詳細（テストをする内容）
	protected String name = "";			//	テスト名
	protected String report = "";		//	レポート
	protected boolean error = false;	//	エラーががあるかどうか

	/**
	 * インストラクター時に名前とテスト説明をセットする
	 * @param name
	 * @param description
	 */
	public WebTestCase(String name,String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * テスト終了後に呼ばれる
	 */
	public void destory() {
		
	}

	/**
	 * 詳細を返す
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	/**
	 * エラーが起きた際にここからメッセージが取れるようにする
	 * @return
	 */
	public String getReport() { 
		return this.report;
	}

	/**
	 * テスト前に呼ばれる
	 */
	public void init() {
		
	}

	/**
	 * 詳細なレポート情報を渡す
	 * @param report
	 */
	protected void setReport(String report) {
		this.report = report;
	}
	/**
	 * Exception情報をそのまま入れる
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
	 * テストをして正常な場合はtrue
	 * 異常がある場合はfalseを返す
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