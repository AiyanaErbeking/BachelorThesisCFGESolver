import folformula.FOLFormula;
import folformula.writers.TPTPWriter;
import org.junit.Test;
import folformula.Variable;

public class FOLFormulaCreationTest {

    Variable X = new Variable("X");
    Variable Y = new Variable("Y");

    TPTPWriter tptpWriter = new TPTPWriter();

    @Test
    public void FOLFormulaConstructionTest(){
        FOLFormula simple = X.and(Y).and(X);

        System.out.println(tptpWriter.visitChildrenRecursivelyHelper(simple));

        FOLFormula existsStatement = X.exists(Y.exists(X.or(Y)));

    }

}
