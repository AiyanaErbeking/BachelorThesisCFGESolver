import folformula.FOLFormula;
import folformula.terms.LetterAtPos;
import folformula.writers.TPTPWriter;
import org.junit.Test;
import folformula.terms.Variable;

public class FOLFormulaCreationTest {

    Variable X = new Variable("X");
    Variable Y = new Variable("Y");

    TPTPWriter tptpWriter = new TPTPWriter();

    @Test
    public void FOLFormulaConstructionTest(){
        FOLFormula simple = X.and(Y).and(X);

        System.out.println(tptpWriter.visitChildrenRecursively(simple));

        FOLFormula existsStatement = X.exists(Y.exists(X.or(Y)));

        System.out.println(tptpWriter.visitChildrenRecursively(existsStatement));

        LetterAtPos letterAtPos = new LetterAtPos(X);
        letterAtPos.setAssociatedLetter("a");

        FOLFormula relationTest = X.exists(letterAtPos.and(X.equivalent(Y).not()));

        System.out.println(tptpWriter.visitChildrenRecursively(relationTest));

    }

}
