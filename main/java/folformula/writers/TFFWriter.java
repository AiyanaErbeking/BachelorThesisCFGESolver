package folformula.writers;

public class TFFWriter extends TPTPWriter{

    @Override
    protected String getForAll(String X) {
        return "! [" + X + ": $int] : ";
    }

    @Override
    protected String getExists(String X) {
        return "? [" + X + ": $int] : ";
    }

    /*
    @Override
    protected String getLeq(String X, String Y) {
        return "$lesseq(" + X + ", " + Y + ")";
    }
     */
}
