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
 * XML����SQL���s���͂�ǂݏo����
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
	 * SQL���L�q���Ă���XML�ƃX�L�[�}�̃p�X��n��
	 * Connection�͂��łɐڑ����Ă���R�l�N�V������n�� 
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
	 * PrepareStatement�̂悤��?���g�����Ƃ��ł���
	 * @param conn
	 * @param sql
	 * @param obj String,Integer,Byte�Ȃ�SQL�̌^�}�b�s���O�ł���f�[�^
	 * @return�@�ύX���ꂽ�s��
	 * @throws SQLException
	 */
	public int executeUpdate(Connection conn,String sql,Object ...obj) throws SQLException{
		return queryrunner.update(conn,sql,obj);
	}

	/**
	 * XML���擾����SqlQuery�𐶐�����
	 * 
	 * @param filepath
	 * @param object
	 */
	protected void XMLLoad(String filepath,String schmepath) throws ParserConfigurationException,IOException,SAXException {
		global.clear();
		//	�ŏ���RelaxNG�̌���
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		doc = builder.parse(new File(filepath));
		Element root = doc.getDocumentElement();
		
		NodeList querys = root.getChildNodes();

		for(int i=0;i<querys.getLength();i++) {
			Node nodeQuery = querys.item(i);
			//	global�f�[�^�����o��
			if(nodeQuery.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeQuery.getNodeName().equals("global")) {
				NodeList params = nodeQuery.getChildNodes();
				for(int j=0;j<params.getLength();j++) {
					Node nodeParam = params.item(j);
					if(nodeParam.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeParam.getNodeName().equals("param")) {
						//	�v�fname,sql�����o�� <param name="">
						NamedNodeMap attrMap = nodeParam.getAttributes();
						Node paramName = attrMap.getNamedItem("name");
						//	�p�����[�^����������
						global.put(paramName.getNodeValue(),nodeParam.getTextContent());
					}
				}
			}
			
			//	query�����o��
			if(nodeQuery.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeQuery.getNodeName().equals("query")) {
				SqlQuery query = new SqlQuery(global);
				NamedNodeMap attrMap = nodeQuery.getAttributes();

				//	�v�fname,sql�����o�� <query name="" sql="">
				Node queryName = attrMap.getNamedItem("name");
				Node querySql = attrMap.getNamedItem("sql");

				//	SqlQuery��SQL���͂��Z�b�g����
				query.setSql(querySql.getNodeValue());

				//	���ł�name�����݂��Ă��邩������
				if(!sqlquerys.containsKey(queryName.getNodeValue()) ) {
					NodeList params = nodeQuery.getChildNodes();
					for(int j=0;j<params.getLength();j++) {
						Node nodeParam = params.item(j);
						if(nodeParam.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE && nodeParam.getNodeName().equals("param")) {
							//	�v�fname,sql�����o�� <param name="">
							attrMap = nodeParam.getAttributes();
							Node paramName = attrMap.getNamedItem("name");
							//	�p�����[�^����������
							query.putParams(paramName.getNodeValue(),nodeParam.getTextContent());

							//System.out.println(paramName.getNodeValue()+"/"+nodeParam.getTextContent());
						}
					}
					query.name = queryName.getNodeValue();
					sqlquerys.put(queryName.getNodeValue(),query);								
				} else {
					//	TODO ����name���ق��Ɣ���Ă�����G���[���o��
					System.out.println("���ł�"+queryName.getNodeValue()+"�͑��݂��܂�");
				}
			}
		}
	}
}


//	-------------------------
//	$log: $
//	-------------------------

