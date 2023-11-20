
public class TPTPWriter {

    protected String and(){ return "& "; }

    protected String or(){ return ""; }

    protected String equivalent(){ return "<=> "; }

    protected String implies(){ return "=> "; }

    protected String not(){ return "~ "; }

    protected String forAll(String X){ return "! [" + X + "] : "; }

    protected String exists(String X){ return "? [" + X + "] : "; }

}
