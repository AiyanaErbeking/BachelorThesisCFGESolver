package folformula;

import folformula.operators.*;
import folformula.terms.Variable;
import folformula.tree.Tree;
import folformula.writers.TPTPWriter;

import java.util.ArrayList;

public abstract class FOLFormula extends Tree {

    /**
     * the name of a FOL Formula is only != null if it is a Variable.
     * */
    public String name;

    // for top and bottom:
    public FOLFormula(){}

    // for variables:
    public FOLFormula(String name){
        super();
        this.name = name;
    }

    // for unary operators:
    public FOLFormula(FOLFormula subFormula){
        addChild(subFormula);
        this.name = null;
    }

    public FOLFormula(FOLFormula leftSubFormula, FOLFormula... rightSubformulae){
        addChild(leftSubFormula);
        for (FOLFormula rightSubformula : rightSubformulae){
            addChild(rightSubformula);
        }
        this.name = null;
    }

    public FOLFormula(ArrayList<FOLFormula> subFormulae){
        for (FOLFormula sub : subFormulae){
            addChild(sub);
        }
    }

    public FOLFormula and(FOLFormula... rightSubformulae){ return new Conjunction(this, rightSubformulae); }

    public FOLFormula not(){ return new Negation(this); }

    public FOLFormula or(FOLFormula... rightSubformulae){ return new Disjunction(this, rightSubformulae); }

    public FOLFormula equivalent(FOLFormula rightSubformula){ return new Equivalence(this, rightSubformula); }

    public FOLFormula exists(FOLFormula subformula){
        assert this.getClass() == Variable.class : "trying to quantify something that is not a Variable!";
        return new Exists((Variable) this, subformula);
    }

    public FOLFormula forall(FOLFormula subformula){
        assert this.getClass() == Variable.class : "trying to quantify something that is not a Variable!";
        return new ForAll((Variable) this, subformula);
    }

    public FOLFormula implies(FOLFormula rightSubformula){ return new Implication(this, rightSubformula); }


    public String writeToTPTP(){
        TPTPWriter tptpWriter = new TPTPWriter();

        String tptpString = "fof(cfge, negated_conjecture, ";

        return tptpString + tptpWriter.visitChildrenRecursively(this) + " ).";
    }

}