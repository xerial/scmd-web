package lab.cb.scmd.web.servlet.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lab.cb.scmd.db.connect.SCMDManager;
import lab.cb.scmd.db.sql.SQLUtil;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.web.bean.UserSelection;
import lab.cb.scmd.web.common.SCMDSessionManager;
import lab.cb.scmd.web.image.TeardropPoint;
import lab.cb.scmd.web.image.teaddrop.Teardrop;
import lab.cb.scmd.web.util.Mutex;

import org.xerial.util.Pair;

/**
 * 
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class TeardropImageServer extends HttpServlet{
	Mutex mutex = new Mutex(20,60*1000);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserSelection userSelection = SCMDSessionManager.getUserSelection(request);
		int paramID = Integer.parseInt(request.getParameter("paramid"));
		String formOrf = request.getParameter("orf");
		String encoding = request.getParameter("encoding");

		mutex.lock();
		if(formOrf == null)
			formOrf = "";
		try{
			HttpSession session = request.getSession();

			//	セッションにデータのキャッシュがある場合はそれを使用する
            Pair<Teardrop, List<TeardropPoint>> pair = (Pair<Teardrop, List<TeardropPoint>>)session.getAttribute("teardrop_paramid="+paramID);
            Teardrop teardrop = null;
            List<TeardropPoint> plotList = null;
            if(pair != null) {
            	teardrop = pair.getFirst();
            	plotList = pair.getSecond();
            } else {
				//	SQLを発行してteardropを取得
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("paramID",String.valueOf(paramID));
				teardrop = SCMDManager.getDBManager().query("ViewORFTeadrop:paramavgsd",map,Teardrop.class);
				teardrop.setGroupID(0);
				teardrop.setParamID(paramID);
				teardrop.setOrientation(Teardrop.Orientation.horizontal);
				
				//	セッションにあるORFリストの取得
				Set<String> orfSet = new TreeSet<String>();
				for(String orf : userSelection.getSelection())
				{
				    orfSet.add(orf.toUpperCase());    
				}
				orfSet.add(formOrf.toUpperCase());
				
				//	ORFリストから点を打つポイントを取得
				map.clear();
				map.put("paramID",String.valueOf(paramID));
				map.put("separatedList",SQLUtil.commaSeparatedList(orfSet, SQLUtil.QuotationType.singleQuote));
				plotList = SCMDManager.getDBManager().queryResults("ViewORFTeadrop:paramstat",map,TeardropPoint.class);
            }
			ImageIO.write(teardrop.drawImage(plotList),encoding,response.getOutputStream());
			return;
		} catch(SQLException e) {
			e.printStackTrace();
	    } catch(SCMDException e) {
			e.printStackTrace();
	    } finally {
	    	mutex.unlock();
	    }
		response.setStatus(404);
	}
}


//	-------------------------
//	$log: $
//	-------------------------