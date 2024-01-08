package folformula.writers;

public class LaTeXWriter extends TPTPWriter{

    @Override
    protected String getAnd() {
        return "\\wedge ";
    }

    @Override
    protected String getOr() {
        return "\\vee ";
    }

    @Override
    protected String getEquivalence() {
        return "\\leftrightarrow ";
    }

    @Override
    protected String getImplication() {
        return "\\rightarrow ";
    }

    @Override
    protected String getNegation() {
        return "\\neg ";
    }

    @Override
    protected String getForAll(String X) {
        return "\\forall " + X;
    }

    @Override
    protected String getExists(String X) {
        return "\\exists " + X;
    }

    @Override
    protected String getLeq(String X, String Y) {
        return X + " \\leq " + Y;
    }

    @Override
    protected String getEquals(String X, String Y) {
        return X + " = " + Y;
    }

    @Override
    protected String getLetterAtPos(String letter, String position) {
        return "Q_{" + letter + "} " + "(" + position + ") ";
    }

    @Override
    protected String getTableau(String grammarName, String grammarVar, String positionX, String positionY) {
        return "T_{" + grammarVar + "}^{" + grammarName + "}(" + positionX + ", " + positionY + ") ";
    }

    @Override
    protected String getBottom() {
        return "\\bot";
    }
}
