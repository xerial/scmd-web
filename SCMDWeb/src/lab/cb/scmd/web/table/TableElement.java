//--------------------------------------
// SCMDServer
// 
// TableElement.java 
// Since: 2004/08/02
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import java.util.Properties;

/**
 * @author leo
 *
 */
public interface TableElement
{
    /** v.visit(this)�Ƃ��āATableElement�̔h���N���X��
     * TableVisitor���K�ꂽ�Ƃ��̓����Visitor���ɋL�q���邱�Ƃ��ł���
     * @param v
     */
    public void accept(TableVisitor v);
    
    /** TableElement�̑����l���擾����B HTML�^�O�̑����Ɠ��`
     * @return
     */
    public Properties getProperties();

    /** �������`����
     * @param property
     * @param value
     */
    public void setProperty(String property, String value);

    /**�������Z�b�g����B������Properties�N���X���ė��p�������Ƃ��Ɏg��
     * @param properties
     */
    public void setProperties(Properties properties);
    
    /** ����TableElement���̂��̂̑����l�ł͂Ȃ��A
     * ����TableElement���z�u�����Table����cell�ɂ��Ă̑����l���擾����
     * HTML�ł�TD�^�O�̑����l�Ɠ��`
     * ���̑������Z�b�g����ɂ́ATableElement��Decollation�N���X�ň͂�
     * 
     * ex. 
     * TableElement elem = new Style(new StringElement("hello"), "bold");
     * 
     * elem.getCellProperty() �ŁA�o ("class", "bold") } ��Properties ���Ԃ��Ă���
     * 
     * �����������ݒ肳�ꂽ�ꍇ�́A�O����Decollation���������㏑������
     * @return
     */
    public Properties getCellProperty();
}


//--------------------------------------
// $Log: TableElement.java,v $
// Revision 1.6  2004/08/09 12:26:15  leo
// StringCell -> StringElement�ȂǁATableElement�̗v�f���ۂ����̕ύX
// ColIndex, RowIndex�Ȃǂ�Dynamic Update
//
// Revision 1.5  2004/08/09 03:36:42  leo
// TagDecollator��ǉ�
//
// Revision 1.4  2004/08/09 02:10:04  leo
// Decollation, Decollator�𐮗�
//
// Revision 1.3  2004/08/07 11:48:43  leo
// Web�p��Table�N���X
//
// Revision 1.2  2004/08/06 12:17:22  leo
// Decolator Pattern �ŁArowIndex, colIndex������
// Visitor Pattern�ŁATable���T�����s��
//
// Revision 1.1  2004/08/02 07:56:25  leo
// ������
//
//--------------------------------------