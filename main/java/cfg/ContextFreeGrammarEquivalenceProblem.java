package cfg;

import folformula.FOLFormula;

public class ContextFreeGrammarEquivalenceProblem {

    public ContextFreeGrammar C1;

    public ContextFreeGrammar C2;

    public ContextFreeGrammarEquivalenceProblem(ContextFreeGrammar CFG1, ContextFreeGrammar CFG2){
        C1 = CFG1;
        C2 = CFG2;
    }


    //////////////////////////////////////////////////
    // methods to check if CFG1 is trivially != CFG2
    ////////////////////////////////////////////////

    // one has a start variable, the other doesn't?

    private Boolean grammarsDifferInEpsilonGeneration(){ return (C1.isEpsilonGenerated() ^ C2.isEpsilonGenerated()); }

    public Boolean areGrammarsTriviallyUnequal(){

        if (!C1.isInChomskyNF()) C1.toChomskyNormalForm();
        if (!C2.isInChomskyNF()) C2.toChomskyNormalForm();

        return grammarsDifferInEpsilonGeneration();
    }


    //////////////
    // Reductions
    /////////////

    private ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();

    private FOLFormula reduceToFolSat(){
        return reductionCfgeToFolSat.reduce(C1, C2);
    }

    public String reduceToTPTPFolSat(){

        //if (areGrammarsTriviallyUnequal()) return "Trivially Unequal";

        FOLFormula reduction = reduceToFolSat();
        return reduction.writeToTPTP();
    }

}
