//--------------------------------------
// SCMDServer
// 
// Width.java 
// Since: 2004/08/07
//
// $URL$ 
// $LastChangedBy$ 
//--------------------------------------

package lab.cb.scmd.web.table;

import lab.cb.scmd.web.table.decollation.IntegerAttributeDecollation;

/**
 * @author leo
 *
 */
public class Width extends IntegerAttributeDecollation
{
    /**
     * @param attribute
     * @param width
     */
    public Width(int width, TableElement element)
    {
        super("width", width, element);
    }
}


//--------------------------------------
// $Log: Width.java,v $
// Revision 1.3  2004/12/10 05:15:49  leo
// move from lab.cb.scmd.web.table to lab.cb.scmd.web.table.decollation
//
// Revision 1.2  2004/08/09 02:10:04  leo
// Decollation, DecollatorÇêÆóù
//
// Revision 1.1  2004/08/07 11:48:43  leo
// WebópÇÃTableÉNÉâÉX
//
//--------------------------------------