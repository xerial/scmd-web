//--------------------------------------
// SCMD Project
// 
// SCMDConfiguration.java 
// Since:  2004/07/14
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.common;

import java.io.*;

import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lab.cb.scmd.db.common.TableQuery;
import lab.cb.scmd.db.common.ValueQuery;
import lab.cb.scmd.db.common.XMLQuery;
import lab.cb.scmd.db.mock.MockTableQuery;
import lab.cb.scmd.db.mock.MockValueQuery;
import lab.cb.scmd.db.mock.MockXMLQuery;
import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.io.FileUtil;

/**
 * Singleton�N���X SCMDServer�̃��[�g�̈ʒu�Ȃǂ̏���ێ����� �g�p�O�ɂ́Ainitialize�ŏ��������Ȃ���΂Ȃ�Ȃ�
 * 
 * @author leo
 *  
 */
public class SCMDConfiguration
{
    public static final String SCMD_ROOT            = "SCMD_ROOT";
    public static final String SCMD_PHOTO_URL       = "SCMD_PHOTO_DIR_URL";
    public static final String IMAGEMAGICK_CONVERT  = "IMAGEMAGICK_CONVERT";
    public static final String XMLQUERY             = "XMLQUERY";
    public static final String TABLEQUERY           = "TABLEQUERY";
    public static final String VALUEQUERY           = "VALUEQUERY";
    public static final String TEARDROP_URI			= "TEARDROP_URI";

    String[]                   _requiredConfigParam = new String[] { SCMD_ROOT, SCMD_PHOTO_URL, IMAGEMAGICK_CONVERT,
            XMLQUERY, TABLEQUERY, VALUEQUERY, TEARDROP_URI };

    public static void Initialize() throws SCMDException {
        _instance = new SCMDConfiguration();
    }

    public static String getProperty(String property) {
        return _instance._properties.getProperty(property);    
    }

    protected static Object getInstance(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        return Class.forName(className).newInstance();
    }

    public static XMLQuery getXMLQueryInstance() {
        String queryClassName = _instance._properties.getProperty(XMLQUERY, "lab.cb.scmd.db.mock.MockXMLQuery");
        try
        {
            return (XMLQuery) getInstance(queryClassName);
        }
        catch (Exception e)
        {
            System.err.println(e);
            return new MockXMLQuery();
        }
    }

    public static TableQuery getTableQueryInstance() {
        String tableQueryClassName = _instance._properties.getProperty(TABLEQUERY, "lab.cb.scmd.db.mock.MockTableQuery");
        try
        {
            return (TableQuery) getInstance(tableQueryClassName);
        }
        catch (Exception e)
        {
            System.err.println(e);
            return new MockTableQuery();
        }
    }

    public static ValueQuery getValueQueryInstance() {
        String valueQueryClassName = _instance._properties.getProperty(VALUEQUERY, "lab.cb.scmd.db.mock.MockValueQuery");
        try
        {
            return (ValueQuery) getInstance(valueQueryClassName);
        }
        catch (Exception e)
        {
            System.err.println(e);
            return new MockValueQuery();
        }
    }

    public static Set getPropertyKeys() {
        SortedSet set = new TreeSet(_instance._properties.keySet());
        return set;
    }

    protected SCMDConfiguration() throws SCMDException
    {
        // default value (�󔒒l) ���Z�b�g
        for (int i = 0; i < _requiredConfigParam.length; i++)
        {
            _properties.setProperty(_requiredConfigParam[i], "");
        }

        try
        {
            // TOMCAT ��JNDI ������ϐ���ǂݍ���
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            _scmdRootDir = (String) envContext.lookup(SCMD_ROOT);
            _properties.setProperty(SCMD_ROOT, _scmdRootDir);
        }
        catch (NamingException e)
        {
            System.err.println("error while reading SCMD_ROOT environment context: " + e.getMessage());
            return;
        }

        File configurationFile = new File(_scmdRootDir + "config/SCMDServer.config");
        try
        {
            // configuration file��ǂݍ���
            _properties.load(new FileInputStream(configurationFile));

            // translate context paths to real ones in the server
            //String convertExec = _properties.getProperty(IMAGEMAGICK_CONVERT);
            //FileUtil.testExistence(new File(convertExec));
        }
        catch (FileNotFoundException e)
        {
            System.err.println(configurationFile + "is not found");
        }
        catch (IOException e)
        {
            throw new SCMDException(e);
        }

    }

    static SCMDConfiguration _instance;

    String                   _scmdRootDir;
    Properties               _properties = new Properties();
}

//--------------------------------------
// $Log: SCMDConfiguration.java,v $
// Revision 1.14  2004/09/02 06:37:08  sesejun
// �_���Ԃ���ꍇ�ɏc�����ɉ��
// �_���͈͊O�ɑł����ۂɁA�[�ɓ_��ł悤�ɕύX
//
// Revision 1.13  2004/08/23 03:47:05  leo
// �f�t�H���g�l���ݒ肳���悤�ɕύX
//
// Revision 1.12 2004/08/12 14:49:24 leo
// DB�Ƃ̐ڑ��J�n
//
// Revision 1.11 2004/08/11 14:02:32 leo
// Group by �̃V�[�g�쐬
//
// Revision 1.10 2004/08/11 07:28:37 leo
// �ݒ�t�@�C���ŁAXMLQuery��instance��ύX�ł���悤�ɂ���
//
// Revision 1.9 2004/07/25 14:57:16 leo
// Configuration���`�F�b�N���邽�߂̃y�[�W���쐬
//
// Revision 1.8 2004/07/25 11:28:21 leo
// SCMD_ROOT��property�ɒǉ����Y��Ă��܂���
//
// Revision 1.7 2004/07/17 05:25:56 leo
// InvalidPathException����菜���܂���
//
// Revision 1.6 2004/07/16 16:35:51 leo
// web module���Aserv����Alab.cb.scmd-server�ɕύX���܂���
//
// Revision 1.5 2004/07/15 09:21:20 leo
// jsp �̎g�p�J�n
//
// Revision 1.4 2004/07/14 07:17:47 leo
// SCMD_ROOT�̐ݒ���@��ύX
//
// Revision 1.3 2004/07/14 07:05:30 leo
// SCMD_ROOT�̐ݒ��Tomcat�ɔC����
//
// Revision 1.2 2004/07/14 03:18:37 leo
// ImageMagick��A�ʐ^�̓����Ă���t�H���_��configuration�t�@�C����
// �w��ł���悤��SCMDConfiguration�N���X(Singleton)���쐬
//
// Revision 1.1 2004/07/13 16:21:11 leo
// temporal commit
//
//--------------------------------------
