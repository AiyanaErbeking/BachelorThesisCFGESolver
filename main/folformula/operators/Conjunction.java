package folformula.operators;

import folformula.FOLFormula;

public class Conjunction extends FOLFormula {

    public Conjunction(FOLFormula leftSubFormula, FOLFormula... rightSubformulae) { super(leftSubFormula, rightSubformulae); }

}
