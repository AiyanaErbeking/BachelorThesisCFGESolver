package folformula;

import java.util.ArrayList;

public abstract class TreeVisitor {



    public String visitChildrenRecursivelyHelper(Tree currentTree){

        ArrayList<String> subFormulae = new ArrayList<>();

        for (Tree child : currentTree.getChildren()) {
            subFormulae.add(visitChildrenRecursivelyHelper(child));
        }

        return inspect(currentTree, subFormulae);
    }

    public abstract String inspect(Tree currentTree, ArrayList<String> subFormulae);

}
