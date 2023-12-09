package folformula.terms;

import folformula.Variable;

/**
 * a unary relation; field associatedLetter is necessary for correct naming of this relation!!
 * */
public class LetterAtPos extends Relation{

    public String associatedLetter;

    public LetterAtPos(Variable position) {
        super(position);
    }

}
