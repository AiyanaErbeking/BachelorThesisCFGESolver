package folformula.operators;

import folformula.FOLFormula;

import java.util.ArrayList;

public class Disjunction extends FOLFormula {

    public Disjunction(FOLFormula leftSubFormula, FOLFormula... rightSubformulae) { super(leftSubFormula, rightSubformulae); }

    public Disjunction(ArrayList<FOLFormula> subFormulae){ super(subFormulae); }

}
