package folformula.writers;

import folformula.terms.*;
import folformula.tree.Tree;
import folformula.tree.TreeVisitor;
import folformula.operators.*;

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

    protected String getEquals(String X, String Y){ return X + "=" + Y; }

    protected String getLetterAtPos(String letter, String position){ return "letter_is_" + letter + "(" + position + ")";}

    protected String getTableau(String grammarName, String grammarVar, String positionX, String positionY){
        // predicates can't contain capital letters as these are reserved for variables
        String lowercaseVar = grammarVar.toLowerCase();
        return grammarName + "_tableau_" + lowercaseVar + "(" + positionX + ", " + positionY + ")";
    }

    protected String getBottom(){ return "$false"; }


    private String checkOutAnd(Conjunction tree, ArrayList<String> subFormulae){

        String conjunction = "";

        for (int i=0; i < subFormulae.size(); i++){
            if (i != 0) conjunction += getAnd();
            conjunction += subFormulae.get(i);
        }

        return "( " + conjunction + " )";
    }

    private String checkOutOr(Disjunction tree, ArrayList<String> subFormulae){

        String disjunction = "";

        for (int i=0; i < subFormulae.size(); i++){
            if (i != 0) disjunction += getOr();
            disjunction += subFormulae.get(i);
        }

        return "( " + disjunction + " )";
    }

    private String checkOutEquivalence(Equivalence tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("equivalence with number subFormulae != 2");

        return "( " + subFormulae.get(0) + getEquivalence() + subFormulae.get(1) + " )";
    }

    private String checkOutImplication(Implication tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("implication with number subFormulae != 2");

        return "( " + subFormulae.get(0) + getImplication() + subFormulae.get(1) + " )";
    }

    private String checkOutExists(Exists tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("exists with number children != 2");

        return getExists(subFormulae.get(0)) + subFormulae.get(1);
    }

    private String checkOutForAll(ForAll tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("for all with number children != 2");

        return getForAll(subFormulae.get(0)) + subFormulae.get(1);
    }

    private String checkOutNegation(Negation tree, ArrayList<String> subFormula){
        if (subFormula.size() != 1) throw new RuntimeException("negation with number children != 1");

        return getNegation() + subFormula.get(0);
    }

    private String checkOutLeq(LEQ tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("leq with arity != 2");

        return getLeq(subFormulae.get(0), subFormulae.get(1));
    }

    private String checkOutEquals(Equals tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 2) throw new RuntimeException("equals with arity != 2");

        return "( " + getEquals(subFormulae.get(0), subFormulae.get(1)) + " )";
    }

    private String checkOutLetterAtPos(LetterAtPos tree, ArrayList<String> subFormulae){
        if (subFormulae.size() != 1) throw new RuntimeException("LetterAtPos with arity != 1");

        return getLetterAtPos(tree.getAssociatedLetter(), subFormulae.get(0));
    }

    private String checkOutTableau(Tableau tree, ArrayList<String> tuple){
        if (tuple.size() != 2) throw new RuntimeException("Tableau with arity != 2");

        return getTableau(tree.associatedGrammarName, tree.associatedVariable, tuple.get(0), tuple.get(1));
    }

    private String checkOutVariable(Variable tree, ArrayList<String> subFormulae){

        assert subFormulae.isEmpty() : "Variable has non-empty list of subformulae...";

        return tree.getName();
    }

    private String checkOutBottom(Bottom tree, ArrayList<String> subFormulae){
        assert subFormulae.isEmpty() : "Bottom shouldn't have any children";

        return getBottom();
    }

    @Override
    public String checkOut(Tree currentTree, ArrayList<String> subFormulae){
        if (currentTree instanceof Conjunction)
            return checkOutAnd((Conjunction) currentTree, subFormulae);
        if (currentTree instanceof Variable)
            return checkOutVariable((Variable) currentTree, subFormulae);
        if (currentTree instanceof Disjunction)
            return checkOutOr((Disjunction) currentTree, subFormulae);
        if (currentTree instanceof Equivalence)
            return checkOutEquivalence((Equivalence) currentTree, subFormulae);
        if (currentTree instanceof Exists)
            return checkOutExists((Exists) currentTree, subFormulae);
        if (currentTree instanceof ForAll)
            return checkOutForAll((ForAll) currentTree, subFormulae);
        if (currentTree instanceof Implication)
            return checkOutImplication((Implication) currentTree, subFormulae);
        if (currentTree instanceof Negation)
            return checkOutNegation((Negation) currentTree, subFormulae);
        if (currentTree instanceof LEQ)
            return checkOutLeq((LEQ) currentTree, subFormulae);
        if (currentTree instanceof LetterAtPos)
            return checkOutLetterAtPos((LetterAtPos) currentTree, subFormulae);
        if (currentTree instanceof Tableau)
            return checkOutTableau((Tableau) currentTree, subFormulae);
        if (currentTree instanceof Bottom)
            return checkOutBottom((Bottom) currentTree, subFormulae);
        if (currentTree instanceof Equals)
            return checkOutEquals((Equals) currentTree, subFormulae);

        throw new RuntimeException("current formula has no known type");
    }

}
