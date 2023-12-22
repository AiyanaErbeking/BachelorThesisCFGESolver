package vampirehandling;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class VampireHandler {

    private final String localPathToVampire = "/home/dev/Vampire";
    private final String localPathToInputDirectory = "/home/dev/Vampire/" + inputDirectoryName;
    private final String localPathToOutputDirectory = "/home/dev/Vampire/" + outputDirectoryName;

    /**
     * ensure that there exists a local folder named InputProblems
     * */
    public final static String inputDirectoryName = "InputProblems";
    public final static String outputDirectoryName= "Answers";


    public VampireHandler(){}


    public String getLocalPathToInputDirectory() { return localPathToInputDirectory; }
    public String getLocalPathToOutputDirectory() { return localPathToOutputDirectory; }
    public String getLocalPathToVampire() { return localPathToVampire; }



    private String invokeVampire(Path inputFilePath, String timeLimitSeconds, Boolean modeCascSat) throws IOException {

        // Get the file name from the Path object
        String fileName = inputFilePath.getFileName().toString();

        // Construct the relative path to the input file
        String relativePathToInputFile = inputDirectoryName + "/" + fileName;

        String vampModeOption = modeCascSat ? "--mode casc_sat" : "--mode casc";

        String vampireCommand = "./vampire";
        vampireCommand += " " + vampModeOption + " -t " + timeLimitSeconds + " " + relativePathToInputFile;

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

        try {
            // Wait for the process to complete
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Process completed successfully, return the output
                return "successful solve. time limit: " + timeLimitSeconds + ", mode casc_sat: " + modeCascSat + "\n\n" + outputStringBuilder.toString();
            } else {
                // Process failed, return an error message or handle accordingly
                return "Vampire failed to solve the problem. Exit code: " + exitCode + "\n\n" + outputStringBuilder.toString();
            }
        } catch (InterruptedException e) {
            // Handle the exception
            e.printStackTrace();
            return "Error while waiting for Vampire process to complete.";
        }
    }

    public void runVampire(String timeLimitSeconds, Boolean modeCascSat){

        // Specify the directory path
        String pathToInputDirectory = getLocalPathToInputDirectory();
        String pathToOutputDirectory = getLocalPathToOutputDirectory();

        // Create a Path object for the directory
        Path inputDirPath = Paths.get(pathToInputDirectory);
        Path outputDirPath = Paths.get(pathToOutputDirectory);

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
                    if (Files.isRegularFile(file) && file.toString().endsWith(".p")) {
                        fileNumber[0] +=1;

                        // Log progress
                        System.out.println("Grindin' file " + fileNumber[0] + " of " + finalTotalFiles + ": " + file.getFileName());

                        // Invoke Vampire and get the output
                        String vampireOutput = invokeVampire(file, timeLimitSeconds, modeCascSat);

                        // Write the output to a new file
                        Path outputFile = outputDirPath.resolve(file.getFileName() + "_answer.txt");  // New output directory
                        Files.write(outputFile, vampireOutput.getBytes());

                        String result;
                        if (vampireOutput.contains("Termination reason: Satisfiable")) {
                            result = "SAT";
                        } else if (vampireOutput.contains("status Unsatisfiable") || vampireOutput.contains("Termination reason: Refutation")) {
                            result = "UNSAT";
                        } else {
                            result = "UNKNOWN";
                        }

                        System.out.println("Output: " + result + " written to: " + outputFile.getFileName().toString() + "\n");
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


}
