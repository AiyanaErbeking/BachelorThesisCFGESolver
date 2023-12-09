package folformula.writers;

import folformula.Tree;
import folformula.TreeVisitor;
import folformula.Variable;
import folformula.operators.*;
import folformula.terms.LEQ;
import folformula.terms.LetterAtPos;

import java.util.ArrayList;

public class TPTPWriter extends TreeVisitor {


    protected String getAnd(){ return " & "; }

    protected String getOr(){ return " | "; }

    protected String getEquivalence(){ return " <=> "; }

    protected String getImplication(){ return " => "; }

    protected String getNegation(){ return " ~ "; }

    protected String getForAll(String X){ return "! [" + X + "] : "; }

    protected String getExists(String X){ return "? [" + X + "] : "; }

    protected String getLeq(String X, String Y){ return " leq(" + X + ", " + Y + ")"; }

    protected String getEquals(String X, String Y){ return getLeq(X, Y) + getAnd() + getLeq(Y, X); }

    protected String getNotEquals(String X, String Y){ return getNegation() + "( " + getEquals(X, Y) + " )"; }

    protected String getLessThan(String X, String Y){ return getLeq(X, Y) + getAnd() + getNegation() + "( " + getLeq(Y, X) + " )"; }

    protected String getLetterAtPos(String letter, String position){ return "letter_is_" + letter + "(" + position + ")";}

    protected String getTableau(String name, String var, String positionX, String positionY){
        // predicates can't contain capital letters as these are reserved for variables
        String lowercaseVar = var.toLowerCase();
        return name + "_tableau_" + lowercaseVar + "(" + positionX + ", " + positionY + ")";
    }

    protected String positionPlusOne(String position){ return getExists(position + "PlusOne") + "( " + getLessThan(position, position + "PlusOne") + getAnd() + getNegation() + getExists("M") + "( " + getLessThan("M", position + "PlusOne") + getAnd() + getNotEquals("M", position) + getAnd() + getNegation() + "(" + getLessThan("M", position) + ") )"; }



    private String inspectAnd(Conjunction tree, ArrayList<String> subFormulae){

        String conjunction = "";

        for (int i=0; i < subFormulae.size(); i++){
            if (i != 0) conjunction += getAnd();
            conjunction += subFormulae.get(i);
        }

        return "( " + conjunction + " )";
    }

    private String inspectOr(Disjunction tree, ArrayList<String> subFormulae){

        String disjunction = "";

        for (int i=0; i < subFormulae.size(); i++){
            if (i != 0) disjunction += getOr();
            disjunction += subFormulae.get(i);
        }

        return "( " + disjunction + " )";
    }

    private String inspectEquivalence(Equivalence tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("equivalence with number subFormulae != 2");

        return "( " + subFormulae.get(0) + getEquivalence() + subFormulae.get(1) + " )";
    }

    private String inspectImplication(Implication tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("implication with number subFormulae != 2");

        return "( " + subFormulae.get(0) + getImplication() + subFormulae.get(1) + " )";
    }

    private String inspectExists(Exists tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("exists with number children != 2");

        return "( " + getExists(subFormulae.get(0)) + "( " + subFormulae.get(1) + " ) )";
    }

    private String inspectForAll(ForAll tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("for all with number children != 2");

        return "( " + getForAll(subFormulae.get(0)) + "( " + subFormulae.get(1) + " ) )";
    }

    private String inspectNegation(Negation tree, ArrayList<String> subFormula){
        if (subFormula.size() != 1) throw new RuntimeException("negation with number children != 1");

        return "( " + getNegation() + subFormula.get(0) + " )";
    }

    private String inspectLeq(LEQ tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("leq with arity != 2");

        return getLeq(subFormulae.get(0), subFormulae.get(1));
    }

    private String inspectLetterAtPos(LetterAtPos tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("LetterAtPos with arity != 2");

        return getLetterAtPos(subFormulae.get(0), subFormulae.get(1));
    }

    private String inspectVariable(Variable tree, ArrayList<String> subFormulae){

        assert subFormulae.isEmpty() : "Variable has non-empty list of subformulae...";

        return tree.name;
    }

    @Override
    public String inspect(Tree currentTree, ArrayList<String> subFormulae){
        if (currentTree instanceof Conjunction)
            return inspectAnd((Conjunction) currentTree, subFormulae);
        else if (currentTree instanceof Variable) {
            return inspectVariable((Variable) currentTree, subFormulae);
        }
        return null;
    }

}
