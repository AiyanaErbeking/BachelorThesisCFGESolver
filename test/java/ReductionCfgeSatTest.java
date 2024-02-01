import cfg.ContextFreeGrammar;
import cfg.ContextFreeGrammarEquivalenceProblem;
import cfg.ReductionCfgeToFolSat;
import grammarhandling.FileFilter;
import grammarhandling.SelectingCFGPairs;
import grammarhandling.TxtCFGPairsToVampInput;
import org.junit.jupiter.api.Test;
import plotting.FindingAverageRuntime;
import vampirehandling.VampireHandler;

import java.util.ArrayList;
import java.util.List;

public class ReductionCfgeSatTest {

    public ReductionCfgeToFolSat reductionCfgeToFolSat = new ReductionCfgeToFolSat();



    @Test
    public void  testReduction(){

        String solutionGrammarFive = "S -> Xa Xb\n" +
                "S -> Xa S1\n" +
                "S1 -> S Xb\n" +
                "Xb -> b\n" +
                "Xa -> a\n";

        String regGramm = "S -> A B\n" +
                "A -> A Xa\n" +
                "B -> Xb B\n" +
                "Xb -> b\n" +
                "Xa -> a\n" +
                "B -> b\n" +
                "A -> a";

        String gee = "S -> A S\n" +
                "S -> a\n" +
                "A -> a\n";


        ContextFreeGrammar cfg = ContextFreeGrammar.parse("o", gee);
        String cfg2 = "S -> a\n";
        String cfg3 = "S -> b\n";
        ContextFreeGrammar cfgtwo = ContextFreeGrammar.parse("t", gee);

        ContextFreeGrammar ez = ContextFreeGrammar.parse("ez1", cfg2);
        ContextFreeGrammar ez2 = ContextFreeGrammar.parse("ez2", cfg3);

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

        ContextFreeGrammarEquivalenceProblem cfge = new ContextFreeGrammarEquivalenceProblem(cfg, cfgtwo);
        //System.out.println(cfge.reduceToLaTeXFolSat());

        //System.out.println(cfge.reduceToLaTeXFolSat());


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

        List<String> directoryNames = new ArrayList<>();
        directoryNames.add("Answers_NotE_CascSat_10000");
        directoryNames.add("Answers_NotE_CascSat_90000");
        directoryNames.add("Answers_NotE_CascSat_80000");

        FindingAverageRuntime findingAverageRuntime = new FindingAverageRuntime();
        //findingAverageRuntime.writeRuntimesFromAllDirectories(directoryNames);

        System.out.println(findingAverageRuntime.getListOfAverageRuntimes());
    }

}
