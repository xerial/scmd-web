package lab.cb.scmd.web.servlet;

import java.io.IOException;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cb.scmd.web.log.ErrorLog;
import churchillobjects.rss4j.RssChannel;
import churchillobjects.rss4j.RssChannelItem;
import churchillobjects.rss4j.RssDocument;
import churchillobjects.rss4j.RssDublinCore;
import churchillobjects.rss4j.generator.RssGenerator;

/**
 *
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class RSSErrorServlet extends HttpServlet {

	//	エラーのリストをRSS形式で配信する
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		RssDocument rssdoc=new RssDocument();

		rssdoc.setVersion(rssdoc.VERSION_10);
		RssDublinCore channelDC = new RssDublinCore();

		RssChannel channel = new RssChannel();
		channel.setChannelTitle("SCMD");
		channel.setChannelLink("http://scmd.gi.k.u-tokyo.ac.jp");
		channel.setChannelDescription("SCMD");
		channel.setChannelUri(request.getRequestURL().toString());
		channel.setChannelLanguage("ja");

		TreeMap<Integer,RssChannelItem> items = ErrorLog.getRssChannelItems();
		for(Integer key : items.keySet()) {
			channel.addItem(items.get(key));
		}
		rssdoc.addChannel(channel);

		response.setContentType("text/xml;charset=UTF-8");
		try{
			RssGenerator.generateRss(rssdoc,response.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------