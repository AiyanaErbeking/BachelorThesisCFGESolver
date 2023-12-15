package cfg;

import cfg.ContextfreeGrammar;
import folformula.FOLFormula;

public class ContextfreeGrammarEquivalenceProblem {

    public ContextfreeGrammar C1;

    public ContextfreeGrammar C2;

    public ContextfreeGrammarEquivalenceProblem(ContextfreeGrammar CFG1, ContextfreeGrammar CFG2){
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


    ///////////////////////////////////////////////
    // Methods for converting to other normal forms
    //////////////////////////////////////////////


    //////////////
    // Reductions
    /////////////

    private ReductionCfgeToFolSat reductionCfgeToFolSat;

    public FOLFormula reduceToFolSat(){
        return reductionCfgeToFolSat.reduce(C1, C2);
    }
}
