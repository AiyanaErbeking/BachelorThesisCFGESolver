package folformula.terms;

/**
 * a unary relation; field associatedLetter is necessary for correct naming of this relation!!
 * */
public class LetterAtPos extends Relation{

    private String associatedLetter = null;

    public LetterAtPos(Variable position) {
        super(position);
    }

    public String getAssociatedLetter() {
        return associatedLetter;
    }

    public void setAssociatedLetter(String associatedLetter) {
        this.associatedLetter = associatedLetter;
    }
}
