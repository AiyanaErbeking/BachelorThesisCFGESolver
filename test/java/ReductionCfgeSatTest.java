import cfg.ContextFreeGrammar;
import cfg.ContextFreeGrammarEquivalenceProblem;
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

        String solutionGrammarFive = "S -> A T\n" +
                "A -> a\n" +
                "T -> S B\n" +
                "B -> b\n";
        ContextFreeGrammar cfg = ContextFreeGrammar.parse("g", solutionGrammarFive);

        String cfg2 = "S -> a\n";
        ContextFreeGrammar cfgtwo = ContextFreeGrammar.parse("g2", solutionGrammarFive);

        ContextFreeGrammar ez = ContextFreeGrammar.parse("ez1", solutionGrammarFive);
        ContextFreeGrammar ez2 = ContextFreeGrammar.parse("ez2", solutionGrammarFive);

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

        ContextFreeGrammarEquivalenceProblem cfge = new ContextFreeGrammarEquivalenceProblem(ez, ez2);
        System.out.println(cfge.reduceToTPTPFolSat());

        System.out.println(cfge.reduceToTPTPFolSat());




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
        //txtCFGPairsToVampInput.parseAndWriteGrammars();

    }

}
