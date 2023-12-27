import cfg.ContextFreeGrammar;
import cfg.ContextFreeGrammarEquivalenceProblem;
import cfg.ReductionCfgeToFolSat;
import grammarhandling.TxtCFGPairsToVampInput;
import org.junit.Test;

import java.util.*;

public class ReductionCfgeSatTest {

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();



    @Test
    public void  testReduction(){

        // Creating a map of rules
        Map<String, Set<List<String>>> rulesMap = new HashMap<>();

        // Adding rules for variable 'S'
        Set<List<String>> sRules = new HashSet<>();
        sRules.add(Arrays.asList("a", "A", "b"));
        sRules.add(Arrays.asList("c", "S", "a"));
        sRules.add(List.of("A"));
        rulesMap.put("S", sRules);

        // Adding rules for variable 'A'
        Set<List<String>> aRules = new HashSet<>();
        aRules.add(Collections.singletonList("b"));
        aRules.add(Arrays.asList("y", "A", "z"));
        aRules.add(Collections.singletonList("ε"));
        rulesMap.put("A", aRules);

        // Creating an instance of ContextFreeGrammar
        //ContextFreeGrammar grammar = new ContextFreeGrammar("ExampleGrammar", rulesMap);

        String gram = "S → ε | S S | a S a S b | b S a S a | a S b S a";
        ContextFreeGrammar cfg = ContextFreeGrammar.parse("g", gram);

        String gram2 = "S -> A | ?\n" +
                "A -> B C C | C B C | C C B\n" +
                "B -> A B | B A | b\n" +
                "C -> A C | C A | a\n";

        ContextFreeGrammar cfg2 = ContextFreeGrammar.parse("num2", gram2);

        System.out.println( "Start vars: " + cfg.getStartVariables());

        cfg.removeStartVarFromRightSides();
        cfg.removeNonSolitaryTerminals();
        cfg.removeRulesWithRightSideGreaterTwo();
        cfg.removeEpsilonRules();
        cfg.removeUnitRules();

        System.out.println("Rules:");
        for (Map.Entry<String, Set<List<String>>> entry : cfg.getRules().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }







        //cfg2.toChomskyNormalForm();

        System.out.println(" \n Rules:");
        for (Map.Entry<String, Set<List<String>>> entry : cfg.getRules().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("Alphabet: " + cfg.getAlphabet());

        ContextFreeGrammarEquivalenceProblem cfge = new ContextFreeGrammarEquivalenceProblem(cfg, cfg);
        //System.out.println(cfge.reduceToTPTPFolSat());

        //System.out.println(cfge.reduceToTPTPFolSat());




        //System.out.println(reductionCfgeToFolSat.encodingWordStructure(grammar, grammar).writeToTPTP());
        //System.out.print(reductionCfgeToFolSat.subwordsLengthOne(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingGrammarInequivalence(grammar, grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.subwordsGreaterOne(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingCYKTable(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.reduce(cfg, cfg2).writeToTPTP());

        //VampireHandler vampireHandler = new VampireHandler();

        //vampireHandler.runVampire("20", Boolean.TRUE);


        /*String grammarString = "S -> a S b | A | B | ?\n" +
                    "A -> a A a | C\n" +
                    "B -> b B b | C\n" +
                    "C -> b C a | ?";
        */
        //CFGParser.parseGrammarString(grammarString).getAlphabet();

        //SelectingCFGPairs selectingCFGPairs = new SelectingCFGPairs();
        //selectingCFGPairs.readCSVFile();

        TxtCFGPairsToVampInput txtCFGPairsToVampInput = new TxtCFGPairsToVampInput();
        txtCFGPairsToVampInput.parseAndWriteGrammars();
    }

}
