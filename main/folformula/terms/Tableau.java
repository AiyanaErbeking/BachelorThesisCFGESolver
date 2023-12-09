package folformula.terms;

import folformula.Variable;

/**
 * a binary relation.
 * */
public class Tableau extends Relation{

    public String associatedGrammarName;

    public String associatedVariable;

    public Tableau(Variable var1, Variable var2) {
        super(var1, var2);
    }

}
