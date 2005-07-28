//--------------------------------------
//SCMD Project
//
//SimplifiedSignificanceTable.java 
//Since:  2005/04/05
//
//$Date: $ ($Author: $)
//$Revision: $
//--------------------------------------
package lab.cb.scmd.db.scripts.analysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import lab.cb.scmd.exception.SCMDException;
import lab.cb.scmd.util.table.BasicTable;
import lab.cb.scmd.util.table.FlatTable;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

/**
 * @author sesejun
 *
 * SGDへの情報提供用に、pvalが、10^-4を切るorf , paramの組み合わせを1 
 * (小さいほうにはずれるものは、-1)、残りを0とする booleanのテーブルを生成
 * するため、
 * nakatani/20050322/allparam3/disruptantMutationLevel.xls
 * を加工するプログラム
 * 
 */
public class SimplifiedSignificanceTable {
    //DataMatrix dm = null;
    BasicTable dm = null;
    HashMap<String, String> parameterMap = new HashMap<String, String> ();

    public static void main(String[] args) {
        SimplifiedSignificanceTable sst = new SimplifiedSignificanceTable();
        sst.load(args);
        sst.process();
    }

    /**
     * 
     */
    private void process() {
        getParameterList();

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File("AbnormalMutants.xls"));
            WritableSheet sheet = workbook.createSheet("ORF_Parameter_Abnormality", 0);
            
            sheet.addCell(new Label(0, 0, "ORF"));
            sheet.addCell(new Label(1, 0, "# of abnormal parameters"));
            for(int col = 0; col < validParameterList.length; col++ ) {
                String param = validParameterList[col];
                URL url = new URL("http://scmd.gi.k.u-tokyo.ac.jp/datamine/ViewORFParameter.do?columnType=input&paramID="+ parameterMap.get(param));
                WritableHyperlink link = new WritableHyperlink(col + 2, 0, url);
                link.setDescription(param);
                sheet.addHyperlink(link);
            }
            for(int row = 0; row < dm.getRowSize(); row++ ) {
                String orf = dm.getRowLabel(row);
                URL url = new URL("http://scmd.gi.k.u-tokyo.ac.jp/datamine/ORFTeardrop.do?orf="+ orf);
                WritableHyperlink link = new WritableHyperlink(0, row + 1, url);
                link.setDescription(orf);
                sheet.addHyperlink(link);
                
                int count = 0;
                for(int col = 0; col < validParameterList.length; col++ ) {
                    int val = Integer.parseInt(dm.getCell(row, dm.getColIndex(validParameterList[col])).toString());
                    if( val <= -4 ) {
                        WritableCellFormat cellFormat = new WritableCellFormat();
                        cellFormat.setBackground(Colour.BRIGHT_GREEN);
                        sheet.addCell(new Number(col + 2, row + 1, -1, cellFormat));
                        count++;
                    } else if ( val >= 4 ){
                        WritableCellFormat cellFormat = new WritableCellFormat();
                        cellFormat.setBackground(Colour.RED);
                        sheet.addCell(new Number(col + 2, row + 1, 1, cellFormat));
                        count++;
                    } else 
                        sheet.addCell(new Number(col + 2, row + 1, 0));
                }
                sheet.addCell(new Number(1, row + 1, count));
            }
            
            workbook.write();
            workbook.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void getParameterList() {
        Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
        dataSource.setDataSourceName("SCMD Data Source");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("scmd");
        dataSource.setUser("sesejun");
        dataSource.setPassword("");
        dataSource.setMaxConnections(10);

        QueryRunner queryRunner = new QueryRunner(dataSource);

        // get parameter list
        List<ParameterList> paramList;
        try {
            paramList = (List<ParameterList>) queryRunner.query(
                    "select id as pid, name from parameterlist where scope='orf'", 
                    new BeanListHandler(ParameterList.class));
            for(ParameterList p: paramList) {
                parameterMap.put(p.getName(), p.getPid() + "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    private void load(String[] args) {
        try {
            dm = new FlatTable(args[0], true, true);
        } catch (SCMDException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
    
    private String[] validParameterList = {
            "A101_A1B",
            "A101_C",
            "A102_A1B",
            "A102_C",
            "A103_A1B",
            "A104_C",
            "A106",
            "A107",
            "A107_A1B",
            "A108",
            "A108_C",
            "A109",
            "A109_C",
            "A110",
            "A111",
            "A112",
            "A112_C",
            "A118",
            "A119",
            "A7-1_A1B",
            "A7-1_C",
            "A7-2_A1B",
            "A7-2_C",
            "A9_C",
            "ACV101_C",
            "ACV122_A",
            "ACV122_C",
            "ACV123_A1B",
            "ACV7-1_A1B",
            "ACV7-2_A1B",
            "ACV7-2_C",
            "ACV8-1_A1B",
            "ACV8-2_A1B",
            "ACV8-2_C",
            "ACV9_C",
            "C101_A1B",
            "C101_C",
            "C102_A1B",
            "C102_C",
            "C103_A",
            "C103_A1B",
            "C104_A",
            "C104_C",
            "C105_A1B",
            "C106_A1B",
            "C107_C",
            "C109_C",
            "C11-1_A",
            "C11-1_C",
            "C11-2_C",
            "C110_A1B",
            "C111_C",
            "C114_A1B",
            "C115_A",
            "C115_A1B",
            "C117_A1B",
            "C117_C",
            "C118_A1B",
            "C118_C",
            "C119",
            "C12-1_A",
            "C12-1_C",
            "C12-2_A1B",
            "C12-2_C",
            "C120",
            "C121",
            "C122",
            "C123",
            "C124",
            "C124_A1B",
            "C125",
            "C125_A1B",
            "C127_A1B",
            "C128_C",
            "C13_A1B",
            "C13_C",
            "CCV101_A1B",
            "CCV103_A",
            "CCV103_A1B",
            "CCV104_A1B",
            "CCV104_C",
            "CCV107_A1B",
            "CCV107_C",
            "CCV108_C",
            "CCV109_A1B",
            "CCV109_C",
            "CCV11-1_A",
            "CCV11-2_A1B",
            "CCV110_A1B",
            "CCV112_A1B",
            "CCV113_C",
            "CCV114_A1B",
            "CCV114_C",
            "CCV115_C",
            "CCV116_A1B",
            "CCV12-1_A",
            "CCV12-2_A1B",
            "CCV126_A",
            "CCV126_C",
            "CCV127_A1B",
            "CCV128_C",
            "CCV13_A1B",
            "D102_A",
            "D103_C",
            "D104_A1B",
            "D105_A",
            "D107_A1B",
            "D108_C",
            "D109_C",
            "D110_A1B",
            "D113_C",
            "D114_A1B",
            "D116_C",
            "D117_A",
            "D117_C",
            "D118_A1B",
            "D121_C",
            "D123_C",
            "D125_C",
            "D126_A1B",
            "D127_A",
            "D128_C",
            "D130_C",
            "D131_C",
            "D132_A1B",
            "D134_C",
            "D135_A",
            "D135_C",
            "D137_C",
            "D139_C",
            "D14-1_A",
            "D14-1_C",
            "D14-2_C",
            "D14-3_C",
            "D144_C",
            "D145_A1B",
            "D147_A",
            "D147_A1B",
            "D147_C",
            "D148_A",
            "D152_A1B",
            "D152_C",
            "D153_C",
            "D154_A",
            "D154_A1B",
            "D155_A",
            "D155_A1B",
            "D156_C",
            "D157_C",
            "D16-3_A1B",
            "D162_C",
            "D163_C",
            "D167_C",
            "D169_A1B",
            "D17-3_A1B",
            "D170_A1B",
            "D173_A",
            "D173_C",
            "D174_C",
            "D176_A",
            "D176_C",
            "D177_C",
            "D179_A",
            "D181_A1B",
            "D182_C",
            "D185_C",
            "D189_C",
            "D193_A1B",
            "D195_C",
            "D196_A1B",
            "D197_C",
            "D198_C",
            "D200",
            "D202",
            "D207",
            "D210",
            "D211",
            "D213",
            "D214",
            "D216",
            "DCV103_C",
            "DCV106_C",
            "DCV109_C",
            "DCV112_C",
            "DCV113_C",
            "DCV114_A1B",
            "DCV117_A",
            "DCV119_C",
            "DCV128_C",
            "DCV130_C",
            "DCV131_C",
            "DCV132_A1B",
            "DCV134_C",
            "DCV135_C",
            "DCV136_A1B",
            "DCV137_C",
            "DCV139_C",
            "DCV14-1_C",
            "DCV14-2_C",
            "DCV141_C",
            "DCV142_A1B",
            "DCV143_A1B",
            "DCV144_C",
            "DCV145_A1B",
            "DCV145_C",
            "DCV146_C",
            "DCV147_C",
            "DCV148_A",
            "DCV148_C",
            "DCV149_C",
            "DCV15-1_C",
            "DCV15-2_C",
            "DCV15-3_A1B",
            "DCV15-3_C",
            "DCV150_C",
            "DCV151_C",
            "DCV153_C",
            "DCV154_A",
            "DCV155_A",
            "DCV155_A1B",
            "DCV158_C",
            "DCV159_C",
            "DCV16-2_C",
            "DCV16-3_A1B",
            "DCV161_A1B",
            "DCV165_A1B",
            "DCV166_C",
            "DCV167_C",
            "DCV169_C",
            "DCV17-1_A",
            "DCV17-1_C",
            "DCV17-2_C",
            "DCV170_A1B",
            "DCV172_A1B",
            "DCV173_A",
            "DCV174_C",
            "DCV175_A1B",
            "DCV176_C",
            "DCV177_C",
            "DCV178_A1B",
            "DCV179_C",
            "DCV181_A1B",
            "DCV182_C",
            "DCV184_A1B",
            "DCV188_A",
            "DCV191_C",
            "DCV192_C",
            "DCV193_A1B",
            "DCV193_C",
            "DCV195_C",
            "DCV196_A1B",
            "DCV196_C",
            "DCV197_C",
            "DCV198_C"
    };
}
