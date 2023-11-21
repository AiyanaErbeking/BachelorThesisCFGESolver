import  org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ReductionCfgeSatTest {

    public ContextfreeGrammar C1 = new ContextfreeGrammar();
    public ContextfreeGrammar C2 = new ContextfreeGrammar();

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
        rules.add("S, a");
        rules.add("X, a");

        C1.setRules(rules);
        C2.setRules(rules);

        System.out.println(reductionCfgeToFolSat.encodingWordStructure(C1.getAlphabet(), C2.getAlphabet()));
    }

}
