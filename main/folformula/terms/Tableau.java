package folformula.terms;

/**
 * a binary relation. its two fields are necessary for this relation to be adequately named!!!
 * */
public class Tableau extends Relation{

    public String associatedGrammarName;

    public String associatedVariable;

    public Tableau(Variable var1, Variable var2) {
        super(var1, var2);
    }


    public String getAssociatedGrammarName() {
        return associatedGrammarName;
    }

    public void setAssociatedGrammarName(String associatedGrammarName) {
        this.associatedGrammarName = associatedGrammarName;
    }

    public String getAssociatedVariable() {
        return associatedVariable;
    }

    public void setAssociatedVariable(String associatedVariable) {
        this.associatedVariable = associatedVariable;
    }
}
