package lab.cb.scmd.db.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.dbutils.QueryRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * JDK 1.5.0
 * 
 * GoF Design Pattern Type **** 
 *
 * XMLからSQL発行文章を読み出して
 *
 * @author mattun
 * @version 
 * $Revision$ <br>
 * $LastChangedDate$<br>
 * $LastChangedBy$<br>
 */
public class SqlManager {
	private Document doc;
	private DocumentBuilder builder;
	HashMap<String,SqlQuery> sqlquerys = null;
	HashMap<String,String> global;
	QueryRunner queryrunner = new QueryRunner();

	/**
	 * SQLが記述してあるXMLとスキーマのパスを渡す
	 * Connectionはすでに接続してあるコネクションを渡す 
	 * 
	 * @param xmlpath
	 * @param schemepath
	 * @param conn
	 * @throws Exception
	 */
	public SqlManager(String xmlpath,String schemepath) throws ParserConfigurationException,IOException,SAXException {
		sqlquerys = new HashMap<String,SqlQuery>();
		global = new HashMap<String,String>();
		String args[] = {xmlpath,schemepath};
//		if(Driver.run(args)==1) {
			XMLLoad(xmlpath,schemepath);
//		}
	}
	
	public SqlQuery getSqlQuery(String name,Connection conn) {
		SqlQuery query = sqlquerys.get(name);
		query.setConnection(conn);
		return query;
	}

	/**
	 * PrepareStatementのように?を使うことができる
	 * @param conn
	 * @param sql
	 * @param obj String,Integer,ByteなどSQLの型マッピングできるデータ
	 * @return　変更された行数
	 * @throws SQLException
	 */
	public int executeUpdate(Connection conn,String sql,Object ...obj) throws SQLException{
		return queryrunner.update(conn,sql,obj);
	}

	/**
	 * XMLを取得してSqlQueryを生成する
	 * 
	 * @param filepath
	 * @param object
	 */
	protected void XMLLoad(String filepath,String schmepath) throws ParserConfigurationException,IOException,SAXException {
		global.clear();
		//	最初にRelaxNGの検証
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		doc = builder.parse(new File(filepath));
		Element root = doc.getDocumentElement();
		
		NodeList querys = root.getChildNodes();

		for(int i=0;i<querys.getLength();i++) {
			Node nodeQuery = querys.item(i);
			//	globalデータを取り出す
			if(nodeQuery.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeQuery.getNodeName().equals("global")) {
				NodeList params = nodeQuery.getChildNodes();
				for(int j=0;j<params.getLength();j++) {
					Node nodeParam = params.item(j);
					if(nodeParam.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeParam.getNodeName().equals("param")) {
						//	要素name,sqlを取り出す <param name="">
						NamedNodeMap attrMap = nodeParam.getAttributes();
						Node paramName = attrMap.getNamedItem("name");
						//	パラメータ部分を入れる
						global.put(paramName.getNodeValue(),nodeParam.getTextContent());
					}
				}
			}
			
			//	queryを取り出す
			if(nodeQuery.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeQuery.getNodeName().equals("query")) {
				SqlQuery query = new SqlQuery(global);
				NamedNodeMap attrMap = nodeQuery.getAttributes();

				//	要素name,sqlを取り出す <query name="" sql="">
				Node queryName = attrMap.getNamedItem("name");
				Node querySql = attrMap.getNamedItem("sql");

				//	SqlQueryにSQL文章をセットする
				query.setSql(querySql.getNodeValue());

				//	すでにnameが存在しているかを見る
				if(!sqlquerys.containsKey(queryName.getNodeValue()) ) {
					NodeList params = nodeQuery.getChildNodes();
					for(int j=0;j<params.getLength();j++) {
						Node nodeParam = params.item(j);
						if(nodeParam.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeParam.getNodeName().equals("param")) {
							//	要素name,sqlを取り出す <param name="">
							attrMap = nodeParam.getAttributes();
							Node paramName = attrMap.getNamedItem("name");
							//	パラメータ部分を入れる
							query.putParams(paramName.getNodeValue(),nodeParam.getTextContent());

							//System.out.println(paramName.getNodeValue()+"/"+nodeParam.getTextContent());
						}
					}
					query.name = queryName.getNodeValue();
					sqlquerys.put(queryName.getNodeValue(),query);								
				} else {
					//	TODO もしnameがほかと被っていたらエラーを出す
					System.out.println("すでに"+queryName.getNodeValue()+"は存在します");
				}
			}
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------

