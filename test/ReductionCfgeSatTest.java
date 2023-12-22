import cfg.ContextfreeGrammar;
import cfg.ReductionCfgeToFolSat;
import  org.junit.Test;
import vampirehandling.VampireHandler;

import java.util.HashSet;
import java.util.Set;

public class ReductionCfgeSatTest {

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();

    @Test
    public void  testReduction(){

        Set<String> alphabet = new HashSet<>();
        alphabet.add("a");
        alphabet.add("b");

        // Grammar C1
        Set<String> c1vars = new HashSet<>();
        c1vars.add("S");
        c1vars.add("X");
        c1vars.add("Y");
        c1vars.add("Z");

        Set<String> c1rules = new HashSet<>();
        c1rules.add("SXY");
        c1rules.add("Xa");
        c1rules.add("Yb");
        c1rules.add("XZZ");
        c1rules.add("Za");
        c1rules.add("Zb");

        Set<String> c1start = new HashSet<>();
        c1start.add("S");
        c1start.add("X");

        Set<String> c2vars = new HashSet<>();
        c2vars.add("S");
        c2vars.add("X");
        c2vars.add("Y");
        c2vars.add("Z");

        Set<String> c2rules = new HashSet<>();
        c2rules.add("SXZ");
        c2rules.add("Xa");
        c2rules.add("Yb");
        c2rules.add("XZZ");
        c2rules.add("ZA");
        c2rules.add("ZB");

        Set<String> c2start = new HashSet<>();
        c2start.add("S");
        c2start.add("Y");


        ContextfreeGrammar C1 = new ContextfreeGrammar("c_one", c1vars, alphabet, c1rules, c1start);
        ContextfreeGrammar C2 = new ContextfreeGrammar("c_two", c1vars, alphabet, c1rules, c1start);



        //System.out.println(reductionCfgeToFolSat.encodingWordStructure(C1, C2).writeToTPTP());
        //System.out.print(reductionCfgeToFolSat.subwordsLengthOne(C1.getVariables(), C1.getRules(), C1.getName()).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingGrammarInequivalence(C1, C2).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.subwordsGreaterOne(C1.getVariables(), C1.getRules(), C1.getName()).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingCYKTable(C2).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.reduce(C1, C2).writeToTPTP());

        VampireHandler vampireHandler = new VampireHandler();

        vampireHandler.runVampire("20", Boolean.TRUE);
    }

}
