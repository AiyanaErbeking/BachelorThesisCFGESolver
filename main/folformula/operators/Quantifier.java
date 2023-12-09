package folformula.operators;

import folformula.FOLFormula;
import folformula.Variable;

public abstract class Quantifier extends FOLFormula {

    public Quantifier(Variable boundVariable, FOLFormula quantifiedFormula){
        super(boundVariable, quantifiedFormula);
    }

}
