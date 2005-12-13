package lab.cb.scmd.db.common;

public class GOQuery {
	static public String searchBoxWhereClause(String keyword) {
        String curkey = keyword;
        if( curkey.matches("^GO:[0-9]+") ) {
            while(curkey.length() < 10) {
                curkey = "GO:0" + curkey.substring(3);
            }
        }
		String exp_annot = "'%" + keyword + "%'";

		String where = "systematicname in (";
    	where += "SELECT DISTINCT strainname AS systematicname " +
    		"FROM goassociation WHERE " ;
		// curkey‚ª go_id‚Ìê‡‚ÍA‚±‚Ìwhere‹å‚Åˆø‚Á‚©‚©‚éB
		where += "( goid IN ( SELECT cid AS goid FROM term_graph " +
        	"WHERE pid = '" + curkey + "') )";
        // curkey ‚ª go name ‚Ìê‡‚É‚ÍA‚±‚Ìwhere ‹å‚Å‚Ð‚Á‚©‚©‚é        
        where += " OR "; 
        where += "( goid IN ( SELECT goid FROM term "
        	+ "WHERE name ILIKE " + exp_annot + ") )";
        where += ")";
		return where;
	}

}
