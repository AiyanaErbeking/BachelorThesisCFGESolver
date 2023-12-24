package folformula.terms;

import folformula.FOLFormula;

public abstract class Relation extends FOLFormula{

    public Relation(Variable var, Variable... vars){
        super(var, vars);
    }
}
