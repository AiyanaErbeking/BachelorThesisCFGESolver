import  org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ReductionCfgeSatTest {

    public ContextfreeGrammar C1 = new ContextfreeGrammar("C1");
    public ContextfreeGrammar C2 = new ContextfreeGrammar("C2");

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();

    @Test
    public void  testEncodingWordStructure(){

        Set<String> alph = new HashSet<>();
        alph.add("a");
        alph.add("b");
        alph.add("c");
        C1.setAlphabet(alph);
        C2.setAlphabet(alph);

        Set<String> rules = new HashSet<>();
        rules.add("Sa");
        rules.add("Xb");
        rules.add("Xc");
        rules.add("AXY");

        C1.setRules(rules);
        C2.setRules(rules);

        Set<String> variables = new HashSet<>();
        variables.add("S");
        variables.add("X");
        variables.add("A");

        C1.setVariables(variables);

        Set<String> startVars = new HashSet<>();
        startVars.add("S1");
        startVars.add("S2");

        C1.setStartVariables(startVars);
        C2.setStartVariables(startVars);

        //System.out.println(reductionCfgeToFolSat.encodingWordStructure(C1, C2));
        //System.out.print(reductionCfgeToFolSat.subwordsLengthOne(C1.getVariables(), C1.getRules(), C1.getName()) + "\n");
        //System.out.println(reductionCfgeToFolSat.encodingGrammarInequivalence(C1, C2));
        System.out.println(reductionCfgeToFolSat.subwordsGreaterOne(C1.variables, C1.rules, C1.name));
    }

}
