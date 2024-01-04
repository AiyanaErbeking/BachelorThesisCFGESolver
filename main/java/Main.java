import grammarhandling.FileFilter;
import vampirehandling.VampireHandler;

public class Main {
    public static void main(String[] args) {

        String[] times = {"1", "30", "300", "600", "1200"};
        Boolean[] cascSat = {Boolean.TRUE, Boolean.FALSE};

        String standardOutputDirectoryNamePrefix = "Answers";
        String inputDirectoryName = "InputProblemsWOUnknown";
        String answerFileDirectoryName;

        VampireHandler vampireHandler = new VampireHandler();
        FileFilter fileFilter = new FileFilter();

        for (Boolean mode : cascSat) {
            for (String time : times) {

                answerFileDirectoryName = standardOutputDirectoryNamePrefix + vampMode(mode) + "_" + time;

                // handler creates new output directory with given name
                vampireHandler.runVampire(time, mode, inputDirectoryName, answerFileDirectoryName);

                fileFilter.filterAndCopyTimeouts(answerFileDirectoryName);

                inputDirectoryName = answerFileDirectoryName + "_TIMEOUTS";
            }
        }
    }

    public static String vampMode(Boolean cascSatMode){
        if (cascSatMode) return "_CascSat";
        else return "_Casc";
    }
}