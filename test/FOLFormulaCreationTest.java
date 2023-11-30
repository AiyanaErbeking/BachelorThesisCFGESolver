import FOLFormula.FOLFormula;
import org.junit.Test;
import FOLFormula.Variable;

public class FOLFormulaCreationTest {

    Variable X = new Variable("X");
    Variable Y = new Variable("Y");

    @Test
    public void FOLFormulaConstructionTest(){
        FOLFormula simple = X.and(Y, X);
    }

}
