package grammarhandling;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides functionality for, given a single .csv file with many (think thousands) of student-given grammars,
 * selecting an equally-distributed grammar-subset and writing these to separate .txt files with the solution grammar.
 *
 * <p>This class writes files with naming convention: problemId_evaluation_number_frequency.txt, where:
 * <ul>
 *     <li>problemId is the name(number) of the Iltis question</li>
 *     <li>evaluation is the Iltis-provided result (EQUIVALENT, NOT_EQUIVALENT, UNKNOWN)</li>
 *     <li>number is unique for CFG's where question and result are the same</li>
 *     <li>frequency value is taken from the csv file (the number of student grammars isomorphic to current grammar)</li>
 * </ul>
 * </p>
 */
public class SelectingCFGPairs {

    private final String outputDirName = "TextCFGPairsWOUnknown";
    /**
     * change this path to the directory to which all grammar-pair .txt files should be written
     * */
    private final String outputDirPath = "/home/dev/Vampire/" + outputDirName;


    /**
     * this is the number of student grammars taken per key (problem ID, evaluation). Adjust this sample number as needed :)
     * This number is a maximum not a guarantee; it might be the case that there are fewer grammars available for some key.
     * */
    private final int numMaxCFGSamples = 10;


    // THE SOLUTION GRAMMARS FOR EACH PROBLEM ID (1 ... 7) =============================================================
    private final String solutionGrammarOne =
            "S → a S b | L | R\n" +
                    "L → b L b | B\n" +
                    "R → a R a | B\n" +
                    "B → b B a | ε";

    private final String solutionGrammarTwo =
            "S → X Y\n" +
                    "X → ε | a X b\n" +
                    "Y → ε | b Y a";

    private final String solutionGrammarThree =
            "S → a T b\n" +
                    "T → a T b | a | b";

    private final String solutionGrammarFour =
            "S → A X | Y C\n" +
                    "X → b B | c C | b X c\n" +
                    "Y → a A | b B | a Y b\n" +
                    "A → ε | a A\n" +
                    "B → ε | b B\n" +
                    "C → ε | c C";

    private final String solutionGrammarFive = "S → ε | S S | ( S ) | [ S ] | { S }";

    private final String solutionGrammarSix = "S → ε | S S | a S a S b | b S a S a | a S b S a";

    private final String solutionGrammarSeven = "S → a b | a S b";





    public void readCSVFile(){

        if (!isDirectoryEmpty(outputDirPath))
            throw new RuntimeException("the given output directory: " + outputDirName + " is not empty!");


        String csvFilePath = "/home/dev/Vampire/iltis-cfg-attempts.csv";

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFilePath)).withSkipLines(1).build()) {
            String[] nextRecord;

            // Map to store the count of rows for each combination of problem id and evaluation
            Map<String, Integer> rowCounts = new HashMap<>();

            // Read each record from the CSV file
            while ((nextRecord = csvReader.readNext()) != null) {
                // Extract relevant information from the record
                String problemId = nextRecord[0];
                String inputGrammar = nextRecord[1];
                String frequency = nextRecord[2];
                String evaluation = nextRecord[3];

                // Replace ? with ε in the input grammar
                inputGrammar = inputGrammar.replace("?", "ε");

                // Skip grammars with "UNKNOWN" evaluation
                if ("UNKNOWN".equals(evaluation)) {
                    continue;
                }

                // Form a key for the current combination of problem id and evaluation
                String key = problemId + "_" + evaluation;

                // Increment the count for the current combination
                int count = rowCounts.getOrDefault(key, 0) + 1;

                //
                if (count <= numMaxCFGSamples) {
                    rowCounts.put(key, count);

                    // Generate the unique file name
                    String fileName = outputDirPath + "/" + problemId + "_" + evaluation + "_" + count + "_" + frequency + ".txt";

                    // Write the input grammar to the file
                    try (FileWriter writer = new FileWriter(fileName)) {

                        // Write the associated solution grammar to file
                        writeSolutionGrammar(writer, problemId);

                        // Write the student grammar underneath
                        writer.write("\n" + inputGrammar);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isDirectoryEmpty(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

    private void writeSolutionGrammar(FileWriter writer, String problemId) throws IOException {
        switch (problemId) {
            case "I1":
                writer.write(solutionGrammarOne + "\n");
                break;
            case "I2":
                writer.write(solutionGrammarTwo + "\n");
                break;
            case "I3":
                writer.write(solutionGrammarThree + "\n");
                break;
            case "I4":
                writer.write(solutionGrammarFour + "\n");
                break;
            case "I5":
                writer.write(solutionGrammarFive + "\n");
                break;
            case "I6":
                writer.write(solutionGrammarSix + "\n");
                break;
            case "I7":
                writer.write(solutionGrammarSeven + "\n");
                break;
            default:
                throw new RuntimeException("problem id of current grammar unknown");
        }
    }
}
