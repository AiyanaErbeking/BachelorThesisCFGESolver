package folformula.operators;

import folformula.FOLFormula;

import java.util.ArrayList;

public class Conjunction extends FOLFormula {

    public Conjunction(FOLFormula leftSubFormula, FOLFormula... rightSubformulae) { super(leftSubFormula, rightSubformulae); }

    public Conjunction(ArrayList<FOLFormula> subFormulae){ super(subFormulae); }

}
