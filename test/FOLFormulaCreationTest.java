import folformula.FOLFormula;
import org.junit.Test;
import folformula.Variable;

public class FOLFormulaCreationTest {

    Variable X = new Variable("X");
    Variable Y = new Variable("Y");

    @Test
    public void FOLFormulaConstructionTest(){
        FOLFormula simple = X.and(Y).and(X);

        FOLFormula existsStatement = X.exists(Y.exists(X.or(Y)));

    }

}
