package folformula.terms;

import folformula.FOLFormula;
import folformula.Variable;

public abstract class Relation extends FOLFormula{

    public Relation(Variable var, Variable... vars){
        super(var, vars);
    }
}
