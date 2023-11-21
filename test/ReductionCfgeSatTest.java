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

        C1.setRules(rules);
        C2.setRules(rules);

        Set<String> variables = new HashSet<>();
        variables.add("S");
        variables.add("X");

        C1.setVariables(variables);

        System.out.println(reductionCfgeToFolSat.encodingWordStructure(C1, C2));
        System.out.print(reductionCfgeToFolSat.subwordsLengthOne(C1.getVariables(), C1.getRules(), C1.getName()));
    }

}
