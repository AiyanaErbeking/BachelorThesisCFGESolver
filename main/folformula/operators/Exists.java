package folformula.operators;

import folformula.FOLFormula;
import folformula.Variable;

public class Exists extends Quantifier {

    public Exists(Variable boundVariable, FOLFormula quantifiedFormula) {
        super(boundVariable, quantifiedFormula);
    }

}
