package folformula.tree;

import java.util.ArrayList;

/**
 * this class provides a means for recursively visiting all subtrees of a given tree.
 * this class is extended by FOL Formula writers, wherein the method inspect is overwritten for each type of tree (subFormula)
 * */
public abstract class TreeVisitor {

    public String visitChildrenRecursively(Tree currentTree){

        ArrayList<String> subFormulae = new ArrayList<>();

        for (Tree child : currentTree.getChildren()) {
            subFormulae.add(visitChildrenRecursively(child));
        }

        return inspect(currentTree, subFormulae);
    }

    public abstract String inspect(Tree currentTree, ArrayList<String> subFormulae);

}
