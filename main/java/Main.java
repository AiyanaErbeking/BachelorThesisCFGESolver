import grammarhandling.FileFilter;
import vampirehandling.VampireHandler;

public class Main {
    public static void main(String[] args) {

        String[] times = {"10000"};
        Boolean[] cascSat = {Boolean.TRUE};

        String standardOutputDirectoryNamePrefix = "Answers_NotE";
        String inputDirectoryName = "InputProbsNotE";
        String answerFileDirectoryName;

        VampireHandler vampireHandler = new VampireHandler();
        FileFilter fileFilter = new FileFilter();

        for (Boolean mode : cascSat) {
            for (String time : times) {

                answerFileDirectoryName = standardOutputDirectoryNamePrefix + vampMode(mode) + "_" + time;

                // handler creates new output directory with given name
                vampireHandler.runVampire(time, mode, inputDirectoryName, answerFileDirectoryName);

                //fileFilter.filterAndCopyTimeouts(answerFileDirectoryName);

                //inputDirectoryName = answerFileDirectoryName + "_TIMEOUTS";
            }

            // so that each casc mode gets the full list of problems, regardless of whether some were solved by the other mode
            inputDirectoryName = "InputProblems";
        }
    }

    public static String vampMode(Boolean cascSatMode){
        if (cascSatMode) return "_CascSat";
        else return "_Casc";
    }
}