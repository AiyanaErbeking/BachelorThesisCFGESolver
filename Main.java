public class Main {
    public static void main(String[] args) {

        ContextfreeGrammar C1 = new ContextfreeGrammar();
        ContextfreeGrammar C2 = new ContextfreeGrammar();

        C1.alphabet.add("a");
        C2.alphabet.add("a");

        C1.rules.add("S, a");
        C2.rules.add("X, a");

        C1.startVariables.add("S");

        C1.variables.add("S");
        C2.variables.add("X");

        ReductionCfgeToFolSat cfgeToFol = new ReductionCfgeToFolSat();

        String folFormula = cfgeToFol.cfgeToFolSat(C1, C2);

        toProblemFileWriter problemFileWriter = new toProblemFileWriter();

        problemFileWriter.writeToFile(folFormula);

        // call vampire on problemFileWriter.pathToFile



    }
}