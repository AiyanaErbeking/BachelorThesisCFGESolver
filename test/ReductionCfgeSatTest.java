import cfg.ContextfreeGrammar;
import cfg.ReductionCfgeToFolSat;
import  org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ReductionCfgeSatTest {

    public ContextfreeGrammar C1 = new ContextfreeGrammar("c_one");
    public ContextfreeGrammar C2 = new ContextfreeGrammar("c_two");

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();

    @Test
    public void  testEncodingWordStructure(){

        Set<String> alph = new HashSet<>();
        alph.add("a");
        alph.add("b");
        alph.add("c");
        C1.setAlphabet(alph);
        C2.setAlphabet(alph);

        Set<String> rulesc1 = new HashSet<>();
        rulesc1.add("Sa");
        rulesc1.add("SSS");
        //rules.add("Xb");
        //rules.add("Xc");
        //rules.add("AXY");

        Set<String> rulesc2 = new HashSet<>();
        //rulesc2.add("Xa");
        rulesc2.add("Sa");

        C1.setRules(rulesc1);
        C2.setRules(rulesc2);

        Set<String> variables = new HashSet<>();
        variables.add("S");
        //variables.add("X");
        //variables.add("A");
        //variables.add("Y");

        C1.setVariables(variables);
        C2.setVariables(variables);

        Set<String> startVars = new HashSet<>();
        startVars.add("S");

        C1.setStartVariables(startVars);
        C2.setStartVariables(startVars);

        //System.out.println(reductionCfgeToFolSat.encodingWordStructure(C1, C2).writeToTPTP());
        //System.out.print(reductionCfgeToFolSat.subwordsLengthOne(C1.getVariables(), C1.getRules(), C1.getName()).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingGrammarInequivalence(C1, C2).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.subwordsGreaterOne(C1.getVariables(), C1.getRules(), C1.getName()).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingCYKTable(C2).writeToTPTP());
        System.out.println(reductionCfgeToFolSat.reduce(C1, C2).writeToTPTP());
    }

}
