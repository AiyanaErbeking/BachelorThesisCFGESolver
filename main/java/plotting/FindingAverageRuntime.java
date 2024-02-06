package plotting;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindingAverageRuntime {

    public String localPathToAllDirectories = "/home/dev/Vampire/";
    public String localPathToRuntimesListDirectory = localPathToAllDirectories + "RuntimesPerFile";

    public String localPathToModelSizeDirectory = localPathToAllDirectories + "Answers_NotE_ModelSizes";

    public List<Integer> getListOfModelSizes(){

        List<Integer> modelSizes = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(localPathToModelSizeDirectory))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    modelSizes.add(Integer.parseInt(extractValueFromFile(filePath, "fmb size ")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return modelSizes;
    }

    public List<Double> getListOfAverageRuntimes(){

        List<Double> avRuntimes = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(localPathToRuntimesListDirectory))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    avRuntimes.add(calculateAverageTime(String.valueOf(filePath)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return avRuntimes;
    }

    private static double calculateAverageTime(String filePath) {
        double sum = 0.0;
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                double time = extractTimeFromLine(line);
                if (time >= 0) {
                    sum += time;
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Avoid division by zero
        return count > 0 ? sum / count : 0.0;
    }

    private static double extractTimeFromLine(String line) {
        try {
            // Assuming the time is at the beginning of the line and ends with " s"
            String timeStr = line.split(" ")[0];
            return Double.parseDouble(timeStr);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle cases where the line does not contain a valid time
            return -1.0;
        }
    }

    public void writeRuntimesFromAllDirectories(List<String> directoryNames){
        for (String directory : directoryNames){
            processDirectory(localPathToAllDirectories + directory, localPathToRuntimesListDirectory, "Success in time ");
        }
    }

    private static void processDirectory(String inputDirectory, String outputDirectory, String valueIdentifier) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(inputDirectory))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    processFile(filePath, outputDirectory, valueIdentifier);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(Path filePath, String outputDirectory, String valueIdentifier) {
        try {
            String fileName = filePath.getFileName().toString();
            String outputFileName = Paths.get(outputDirectory, fileName).toString();

            // Check if the output file already exists
            boolean fileExists = Files.exists(Paths.get(outputFileName));

            // Read the particular value from the input file
            String extractedValue = extractValueFromFile(filePath, valueIdentifier);

            // Write the result to the output file
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName, true))) {
                if (!fileExists) {
                    // If the file doesn't exist, write the value to a new line
                    writer.println(extractedValue);
                } else {
                    writer.print(extractedValue + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractValueFromFile(Path filePath, String valueIdentifier) {
        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(valueIdentifier)) {
                    // Assuming the value is on the same line after the valueIdentifier
                    return line.substring(line.indexOf(valueIdentifier) + valueIdentifier.length()).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ""; // Return an empty string if the value is not found
    }

}
