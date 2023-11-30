package FOLFormula;

import java.util.TreeSet;

public class FOLFormula extends TreeSet<FOLFormula> {

    public FOLFormula(FOLFormula subformula){
        super();
    }

    public FOLFormula and(FOLFormula... rightSubformulae){ return Conjunction(this, rightSubformulae); }


}
