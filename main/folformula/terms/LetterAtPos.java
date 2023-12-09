package folformula.terms;

import folformula.Variable;

/**
 * a unary relation; but implemented here as a binary relation where the first variable is actually part of the name of the relation
 * */
public class LetterAtPos extends Relation{

    public LetterAtPos(Variable letter, Variable var) {
        super(letter, var);
    }

}
