package lab.cb.scmd.web.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.TreeMap;

import lab.cb.scmd.web.common.SCMDConfiguration;
import churchillobjects.rss4j.RssChannelItem;

import com.sun.jmx.snmp.Timestamp;

/**
 * エラーがでた時にRSSで見れるようにデータを保存する
 * @author mattun
 * @version $Revision: $ $LastChangedDate: $
 * $LastChangedBy: $
 */
public class ErrorLog {
	
	public static void insert(String title,String description,Exception e) {
		StringWriter w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		e.printStackTrace(pw);
		pw.close();
		insert(title,description,w.toString());
	}

	public static void insert(String title,String description,String content) {
		File logdir = new File(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("LOG_FILEPATH")+"rss/");
		File[] files = logdir.listFiles();
		long num = 0;
		if(files.length >= 1) {
			String numbername = files[files.length-1].getName();
			num = Long.parseLong(numbername);
			num++;
		}

		Properties props = new Properties();
		props.setProperty("title",title);
		props.setProperty("description",description);
		props.setProperty("content",content);
		try{
			FileOutputStream file = new FileOutputStream(logdir.getAbsolutePath()+"/"+new Timestamp().getDateTime());
			props.storeToXML(file,"UTF-8");
			file.close();
		} catch(IOException e) {
			
		}
	}
	
	public static TreeMap<Long,RssChannelItem> getRssChannelItems() {
		TreeMap<Long,RssChannelItem> map = new TreeMap<Long,RssChannelItem>();
		File logdir = new File(SCMDConfiguration.getProperty("SCMD_ROOT")+SCMDConfiguration.getProperty("LOG_FILEPATH")+"rss/");
		File[] files = logdir.listFiles();
		try{
			for(int i=0;i<files.length;i++) {
				Properties props = new Properties();
				FileInputStream fis = new FileInputStream(files[i].getAbsoluteFile());
				props.loadFromXML(fis);
				fis.close();

				String numbername = files[i].getName();
				RssChannelItem item = new RssChannelItem();
				item.setItemTitle(props.getProperty("title"));
				item.setItemDescription(props.getProperty("description"));
				item.setItemLink("http://scmd.gi.k.u-tokyo.ac.jp/rss?content="+numbername);
				map.put(Long.parseLong(numbername),item);
			}
		} catch(Exception e) {
			
		}
		return map;
	}
}


//	-------------------------
//	$log: $
//	-------------------------