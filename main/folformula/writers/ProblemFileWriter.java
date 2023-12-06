package folformula.writers;

/**
 * writes a string to a TPTP file.
 * To always use the same problem file, this is written and deleted from with every new reduction
 * */
public class ProblemFileWriter {

    protected String pathToFile = "path/to/problemFile.txt";

    public void writeToFile(String folFormula){}

    public void deleteFromFile(){}
}
