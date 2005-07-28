/*
 * Created on 2005/02/23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lab.cb.scmd.db.scripts.go;

/**
 * @author sesejun
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Term {
    String id = "";
    String name = "";
    String namespace = "";
    Def def;
    String[] is_a = new String [0];
    String[] subset = new String [0];
    String comment;
    Synonym[] synonym = new Synonym[0];
    Relationship[] relationship = new Relationship[0];
    String is_obsolete;
    
    
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Def getDef() {
        return def;
    }
    public void setDef(Def def) {
        this.def = def;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String[] getIs_a() {
        return is_a;
    }
    public void setIs_a(String[] is_a) {
        this.is_a = is_a;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public Relationship[] getRelationship() {
        return relationship;
    }
    public void setRelationship(Relationship[] relationship) {
        this.relationship = relationship;
    }
    public String[] getSubset() {
        return subset;
    }
    public void setSubset(String[] subset) {
        this.subset = subset;
    }
    public Synonym[] getSynonym() {
        return synonym;
    }
    public void setSynonym(Synonym[] synonym) {
        this.synonym = synonym;
    }
    public String getIs_obsolete() {
        return is_obsolete;
    }
    public void setIs_obsolete(String is_obsolete) {
        this.is_obsolete = is_obsolete;
    }
}
