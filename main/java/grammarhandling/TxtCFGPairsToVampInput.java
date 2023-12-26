package grammarhandling;

import cfg.ContextFreeGrammar;
import cfg.ContextFreeGrammarEquivalenceProblem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * this class provides functionality for, given a directory with .txt files which include two CFG's, parsing these to
 * instances of the ContextFreeGrammar class, converting them to equiv CFG's in Chomsky NF, and reducing the problem
 * of their equivalence to a format of FOL SAT accepted by Vampire
 * */
public class TxtCFGPairsToVampInput {

    private ContextFreeGrammar C1;
    private ContextFreeGrammar C2;

    private final String inputDirName = "TextCFGPairs";
    private final String inputDirPath = "/home/dev/Vampire/" + inputDirName;
    private final String outputDirName = "InputProblems";
    private final String outputDirPath = "/home/dev/Vampire/" + outputDirName;

    public void parseAndWriteGrammars() {
        Path inputDir = Paths.get(inputDirPath);

        try {
            // Walk through the directory and obtain all .txt files
            Files.walk(inputDir, Integer.MAX_VALUE)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(this::processTxtFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTxtFile(Path txtFilePath) {
        try {
            // Read the content of the file
            String content = new String(Files.readAllBytes(txtFilePath), StandardCharsets.UTF_8);

            // Split the content into two grammars
            String[] grammars = content.split("\\n\\s*\\n");

            String nameOfCurrentFileRead = String.valueOf(txtFilePath.getFileName());

            C1 = ContextFreeGrammar.parse(grammars[0]);
            C2 = ContextFreeGrammar.parse(grammars[1]);

            C1.toChomskyNormalForm();
            C2.toChomskyNormalForm();

            ContextFreeGrammarEquivalenceProblem cfge = new ContextFreeGrammarEquivalenceProblem(C1, C2);

            String tptpReductionString = cfge.reduceToTPTPFolSat();

            writeToInputProbFile(tptpReductionString, nameOfCurrentFileRead);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method writes the input problem string tptpString to a new file in the given Output Dir. This file
     * has the same name as the file from which the two grammars came, but with the .p extension: nameOfGrammarFile.p
     * */
    private void writeToInputProbFile(String tptpString, String nameOfGrammarFile) {
        // Create the output directory if it doesn't exist
        Path outputDir = Paths.get(outputDirPath);
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectories(outputDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create the output file path with .p extension
        String outputFileName = nameOfGrammarFile.replace(".txt", ".p");
        Path outputFile = outputDir.resolve(outputFileName);

        try {
            // Write the TPTP string to the output file
            Files.write(outputFile, tptpString.getBytes(StandardCharsets.UTF_8));
            System.out.println("File written: " + outputFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

