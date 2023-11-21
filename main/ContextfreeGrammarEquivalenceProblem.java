public class ContextfreeGrammarEquivalenceProblem {

    public ContextfreeGrammar CFG1;

    public ContextfreeGrammar CFG2;

    public ContextfreeGrammarEquivalenceProblem(ContextfreeGrammar C1, ContextfreeGrammar C2){
        CFG1 = C1;
        CFG2 = C2;
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

    public String reduceToFolSat(){
        return reductionCfgeToFolSat.cfgeToFolSat(CFG1, CFG2);
    }
}
