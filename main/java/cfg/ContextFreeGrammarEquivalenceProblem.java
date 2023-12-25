package cfg;

import folformula.FOLFormula;

public class ContextFreeGrammarEquivalenceProblem {

    public ContextFreeGrammar C1;

    public ContextFreeGrammar C2;

    public ContextFreeGrammarEquivalenceProblem(ContextFreeGrammar CFG1, ContextFreeGrammar CFG2){
        C1 = CFG1;
        C2 = CFG2;
    }

    //////////////////////////////
    // methods to check if CFG1 is trivially != CFG2
    /////////////////////////////

    // one has a start variable, the other doesn't?

    // alphabets unequal?

    // one generates epsilon, the other doesn't (in CNF, there must exist a start V that generates epsilon)

    // one method which checks all the above cases



    //////////////
    // Reductions
    /////////////

    private ReductionCfgeToFolSat reductionCfgeToFolSat;

    public FOLFormula reduceToFolSat(){
        return reductionCfgeToFolSat.reduce(C1, C2);
    }

    public String reduceToTPTPFolSat(){ return reduceToFolSat().writeToTPTP(); }
}
