package grammarhandling;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides functionality for, given a single .txt file with many student-given grammars, selecting
 * grammars from those and writing these to separate .txt files with the correct-answer grammar.
 *
 * <p>This class writes files with naming convention: question_frequency_result_number.txt, where:
 * <ul>
 *     <li>question is the name(number) of the Iltis question</li>
 *     <li>frequency value is taken from the csv file</li>
 *     <li>result is the Iltis-provided result (EQUIVALENT, NOT_EQUIVALENT, UNKNOWN)</li>
 *     <li>number is unique for CFG's where question and result are the same</li>
 * </ul>
 * </p>
 */
public class SelectingCFGPairs {

    private String outputDirPath = "/home/dev/Vampire/TextCFGPairs/";

    private String solutionGrammarOne =
                      """
                      S → aSb|L|R
                      L → bLb|B
                      R → aRa|B
                      B → bBa|ε
                      """;

    public void csvFileProcessing(){

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

                // Increment the count for the current combination
                int count = rowCounts.getOrDefault(problemId + evaluation, 0) + 1;
                rowCounts.put(problemId + evaluation, count);

                // Generate the file name
                String fileName = outputDirPath + problemId + "_" + frequency + "_" + evaluation + "_" + count + ".txt";

                // Write the input grammar to the file
                try (FileWriter writer = new FileWriter(fileName)) {
                    writer.write(inputGrammar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }
}
