package folformula.writers;

import folformula.FOLFormula;
import folformula.terms.LEQ;
import folformula.terms.LetterAtPos;
import folformula.terms.Tableau;
import folformula.tree.Tree;
import folformula.tree.TreeVisitorCollector;

import java.util.HashSet;

public class CollectRelations extends TreeVisitorCollector {

    private HashSet<String> checkOutLetterAtPos(LetterAtPos tree, HashSet<String> subFormulae){

        subFormulae.add("tff(type_declaration, type, q_" + tree.getAssociatedLetter() + " : $int > $o). \n");
        return subFormulae;
    }

    private HashSet<String> checkOutTableau(Tableau tree, HashSet<String> subFormulae){

        String lowercaseVar = tree.getAssociatedVariable().toLowerCase();

        subFormulae.add("tff(type_declaration, type, " + tree.getAssociatedGrammarName() + "_tableau_" + lowercaseVar + " : ($int * $int) > $o). \n");

        return subFormulae;
    }

    private HashSet<String> checkOutLEQ(LEQ tree, HashSet<String> subFormulae){

        subFormulae.add("tff(type_declaration, type, leq : ($int * $int) > $o). \n");
        return subFormulae;
    }

    private HashSet<String> checkOutGeneral(FOLFormula tree, HashSet<String> subFormulae){

        return subFormulae;
    }

    @Override
    public HashSet<String> checkOut(Tree currentTree, HashSet<String> subFormulae) {
        if (currentTree instanceof LetterAtPos)
            return checkOutLetterAtPos((LetterAtPos) currentTree, subFormulae);
        else if (currentTree instanceof Tableau)
            return checkOutTableau((Tableau) currentTree, subFormulae);
        else if (currentTree instanceof LEQ)
            return checkOutLEQ((LEQ) currentTree, subFormulae);
        else return checkOutGeneral((FOLFormula) currentTree, subFormulae);
    }
}
