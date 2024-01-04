package grammarhandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * this class looks at all answer files from a given answer-file directory and checks which answers have TIMEOUT in
 * the name. For these files, the associated input-problem file is retrieved from the input problems directory and
 * is copied into a new output directory. The name of this output directory is the same as the answer-file directory
 * but with "TIMEOUTS" appended.
 * */
public class FileFilter {

    private final String localPathToAllDirectories = "/home/dev/Vampire/";
    private final String inputProblemFileDirectoryName = "InputProblems";

    public FileFilter(){}

    public void filterAndCopyTimeouts(String answerFileDirectoryName) {
        // Construct paths for answer and input directories
        Path answerDirectory = Paths.get(localPathToAllDirectories, answerFileDirectoryName);
        Path inputDirectory = Paths.get(localPathToAllDirectories, inputProblemFileDirectoryName);

        // Create a new directory for timeouts
        Path timeoutsDirectory = Paths.get(localPathToAllDirectories, answerFileDirectoryName + "_TIMEOUTS");
        try {
            Files.createDirectories(timeoutsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            // List all answer files with TIMEOUT in their names
            Files.walk(answerDirectory)
                    .filter(path -> path.toString().endsWith("TIMEOUT_answer.txt"))
                    .forEach(answerFilePath -> {
                        try {
                            // Extract the file name without the TIMEOUT suffix
                            String fileNameWithoutTimeout = answerFilePath.getFileName().toString().replace("_TIMEOUT_answer.txt", "");

                            // Find the corresponding input-problem file in the input directory
                            Path inputProblemFilePath = Files.walk(inputDirectory)
                                    .filter(inputPath -> inputPath.toString().endsWith(fileNameWithoutTimeout + ".p"))
                                    .findFirst()
                                    .orElse(null);

                            if (inputProblemFilePath == null)
                                throw new RuntimeException("FileFilter could not find an associated input-problem file for a given answer");
                            else{
                                // Copy the input-problem file to the timeouts directory
                                Path copiedFilePath = timeoutsDirectory.resolve(inputProblemFilePath.getFileName());
                                Files.copy(inputProblemFilePath, copiedFilePath, StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("FileFilter just copied the following file: " + copiedFilePath.toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
