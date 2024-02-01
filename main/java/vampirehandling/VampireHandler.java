package vampirehandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VampireHandler {

    private final String localPathToVampire = "/home/dev/Vampire";
    private final String localPathToOutputDirectory = "/home/dev/Vampire";
    private final String localPathToInputDirectory = "/home/dev/Vampire";


    public VampireHandler(){}

    public String getLocalPathToVampire() { return localPathToVampire; }


    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        return "[" + now.format(formatter) + "] ";
    }


    private String invokeVampire(Path inputFilePath, String timeLimitSeconds, Boolean modeCascSat, String inputDirectoryName) throws IOException {
        // Get the file name from the Path object
        String inputFileName = inputFilePath.getFileName().toString();

        // Construct the relative path to the input file
        String relativePathToInputFile = inputDirectoryName + "/" + inputFileName;

        String vampModeOption = modeCascSat ? "--mode casc_sat" : "--mode casc";

        String vampireCommand = "./vampire";
        vampireCommand += " " + vampModeOption + " --cores 12 -t " + timeLimitSeconds + " " + relativePathToInputFile;

        LocalDateTime startTimestamp = LocalDateTime.now();

        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", vampireCommand);
        processBuilder.directory(new File(getLocalPathToVampire()));

        Process process = processBuilder.start();

        // convert Vampire output to String, which will be later written to the answer file
        StringBuilder outputStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputStringBuilder.append(line).append("\n");
            }
        }

        LocalDateTime endTimestamp = LocalDateTime.now();
        Duration duration = Duration.between(startTimestamp, endTimestamp);
        long seconds = duration.getSeconds();

        String timeElapsedRegex = "Success in time (\\d+\\.\\d+)";
        Pattern timeElapsedPattern = Pattern.compile(timeElapsedRegex);
        Matcher timeElapsedMatcher = timeElapsedPattern.matcher(outputStringBuilder);

        double timeElapsed = 0.0;

        if (timeElapsedMatcher.find()) {
            String timeElapsedStr = timeElapsedMatcher.group(1);
            timeElapsed = Double.parseDouble(timeElapsedStr);
        }

        try {
            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Process completed successfully, return the output
                return "successful solve. time limit: " + timeLimitSeconds + ", mode casc_sat: " + modeCascSat +
                        "\nTotal time spent grindin: " + timeElapsed + " seconds\n\n" + outputStringBuilder;
            } else {
                // Process failed, return an error message or handle accordingly
                return "Vampire failed to solve the problem. Exit code: " + exitCode + "\n\n" + outputStringBuilder;
            }
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
            return "Error while waiting for Vampire process to complete.";
        }
    }



    public void runVampire(String timeLimitSeconds, Boolean modeCascSat, String inputDirectoryName, String outputDirectoryName){

        // Create a Path object for the directory
        Path inputDirPath = Paths.get(localPathToInputDirectory + "/" + inputDirectoryName);
        Path outputDirPath = Paths.get(localPathToOutputDirectory + "/" + outputDirectoryName);

        // Create directories if they don't exist
        //createDirectoryIfNotExists(inputDirPath);
        createDirectoryIfNotExists(outputDirPath);

        int totalFiles = 0;
        try {
            totalFiles = (int) Files.walk(inputDirPath).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final int[] fileNumber = {0};

        try {
            // Walk through the directory and its subdirectories
            int finalTotalFiles = totalFiles;
            Files.walkFileTree(inputDirPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    if (file.getFileName().toString().contains("TriviallyUnequal")){
                        Path outputFile = outputDirPath.resolve(file.getFileName().toString().replace(".p", "_answer.txt"));
                        Files.write(outputFile, "Trivially Unequal".getBytes());
                    }

                    else if (Files.isRegularFile(file) && file.toString().endsWith(".p")) {
                        fileNumber[0] +=1;

                        // Log progress
                        System.out.println(getTimestamp() + "Grindin' file " + fileNumber[0] + " of " + finalTotalFiles + ": " + file.getFileName());

                        // Invoke Vampire and get the output
                        String vampireOutput = invokeVampire(file, timeLimitSeconds, modeCascSat, inputDirectoryName);

                        String regex = "TRYING \\[(\\d+)\\]";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(vampireOutput);

                        String result;
                        if (vampireOutput.contains("Termination reason: Satisfiable")) {

                            int lastMatchedNumber = -1;

                            while (matcher.find()) {
                                lastMatchedNumber = Integer.parseInt(matcher.group(1));
                            }
                            result = "SAT_ModelSize" + lastMatchedNumber;

                        } else if (vampireOutput.contains("status Unsatisfiable")) {
                            result = "UNSAT";
                        } else if (vampireOutput.contains("Parsing Error")) {
                            result = "VampParsingError";
                        } else {
                            result = "TIMEOUT";
                        }

                        // Write the output to a new file
                        Path outputFile = outputDirPath.resolve(file.getFileName().toString().replace(".p", "_" + result + "_answer.txt"));  // New output directory
                        Files.write(outputFile, vampireOutput.getBytes());

                        System.out.println(getTimestamp() + "Output " + result + " written to: " + outputFile.getFileName().toString() + "\n");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle the failure to visit a file
                    System.err.println("Error visiting file: " + file + " - " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }

    }

    private static boolean isDirectoryEmpty(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

    private static void createDirectoryIfNotExists(Path directoryPath) {
        if (Files.notExists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
