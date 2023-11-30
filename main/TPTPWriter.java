
public class TPTPWriter {

    protected String and(){ return " & "; }

    protected String or(){ return " | "; }

    protected String equivalent(){ return " <=> "; }

    protected String implies(){ return " => "; }

    protected String not(){ return " ~ "; }

    protected String forAll(String X){ return "! [" + X + "] : "; }

    protected String exists(String X){ return "? [" + X + "] : "; }

    protected String leq(String X, String Y){ return " leq(" + X + ", " + Y + ")"; }

    protected String eq(String X, String Y){ return leq(X, Y) + and() + leq(Y, X); }

    protected String neq(String X, String Y){ return not() + "( " + eq(X, Y) + " )"; }

    protected String lessthan(String X, String Y){ return leq(X, Y) + and() + not() + "( " + leq(Y, X) + " )"; }

    protected String letter_is(String letter, String position){ return "letter_is_" + letter + "(" + position + ")";}

    protected String tableau(String name, String var, String positionX, String positionY){
        // predicates can't contain capital letters as these are reserved for variables
        String lowercaseVar = var.toLowerCase();
        return name + "_tableau_" + lowercaseVar + "(" + positionX + ", " + positionY + ")";
    }

    protected String positionPlusOne(String position){ return exists(position + "PlusOne") + "( " + lessthan(position, position + "PlusOne") + and() + not() + exists("M") + "( " + lessthan("M", position + "PlusOne") + and() + neq("M", position) + and() + not() + "(" + lessthan("M", position) + ") )"; }

}
