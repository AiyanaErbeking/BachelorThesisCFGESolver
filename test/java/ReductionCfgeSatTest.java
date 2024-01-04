import cfg.ReductionCfgeToFolSat;
import grammarhandling.FileFilter;
import grammarhandling.SelectingCFGPairs;
import grammarhandling.TxtCFGPairsToVampInput;
import org.junit.jupiter.api.Test;
import vampirehandling.VampireHandler;

public class ReductionCfgeSatTest {

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();



    @Test
    public void  testReduction(){

        /*
        String solutionGrammarFive = "S -> 1 2\n" +
                "2 -> T 0\n" +
                "T -> 1 3\n" +
                "3 -> T 0\n" +
                "0 -> b\n" +
                "1 -> a\n" +
                "T -> b\n" +
                "T -> a";
        ContextFreeGrammar cfg = ContextFreeGrammar.parse("g", solutionGrammarFive);

        String cfg2 = "S -> A 0\n" +
                "S -> 1 A\n" +
                "A -> 1 2\n" +
                "2 -> A 0\n" +
                "0 -> b\n" +
                "1 -> a\n" +
                "S -> b\n" +
                "2 -> b\n" +
                "S -> a";
        ContextFreeGrammar cfgtwo = ContextFreeGrammar.parse("cfg2", cfg2);


        System.out.println("Rules:");
        for (Map.Entry<String, Set<List<String>>> entry : cfgtwo.getRules().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        //System.out.println( "Start vars: " + cfg.getStartVariables());

        /*

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

         */

        //ContextFreeGrammarEquivalenceProblem cfge = new ContextFreeGrammarEquivalenceProblem(cfg, cfgtwo);
        //System.out.println(cfge.reduceToTPTPFolSat());




        //System.out.println(reductionCfgeToFolSat.encodingWordStructure(grammar, grammar).writeToTPTP());
        //System.out.print(reductionCfgeToFolSat.subwordsLengthOne(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingGrammarInequivalence(grammar, grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.subwordsGreaterOne(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.encodingCYKTable(grammar).writeToTPTP());
        //System.out.println(reductionCfgeToFolSat.reduce(cfg, cfgtwo).writeToTPTP());

        VampireHandler vampireHandler = new VampireHandler();
        //vampireHandler.runVampire("3", Boolean.FALSE, "InputProblems", "Answers_3s_casc");

        FileFilter fileFilter = new FileFilter();
        //fileFilter.filterAndCopyTimeouts("Answers_3s_casc");


        /*String grammarString = "S -> a S b | A | B | ?\n" +
                    "A -> a A a | C\n" +
                    "B -> b B b | C\n" +
                    "C -> b C a | ?";
        */
        //CFGParser.parseGrammarString(grammarString).getAlphabet();

        SelectingCFGPairs selectingCFGPairs = new SelectingCFGPairs();
        //selectingCFGPairs.readCSVFile();

        TxtCFGPairsToVampInput txtCFGPairsToVampInput = new TxtCFGPairsToVampInput();
        txtCFGPairsToVampInput.parseAndWriteGrammars();

    }

}
