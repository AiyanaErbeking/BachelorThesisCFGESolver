package folformula.operators;

import folformula.FOLFormula;
import folformula.terms.Variable;

public class ForAll extends Quantifier{

    public ForAll(Variable boundVariable, FOLFormula quantifiedFormula) {
        super(boundVariable, quantifiedFormula);
    }

}
