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

}
