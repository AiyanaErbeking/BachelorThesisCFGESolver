package folformula.tree;

import java.util.HashSet;

public abstract class TreeVisitorCollector {

    public HashSet<String> visitChildrenRecursively(Tree currentTree){

        HashSet<String> stringHashSet = new HashSet<>();

        for (Tree child : currentTree.getChildren()) {
            stringHashSet.addAll(visitChildrenRecursively(child));
        }

        return checkOut(currentTree, stringHashSet);
    }

    public abstract HashSet<String> checkOut(Tree currentTree, HashSet<String> stringHashSet);

}
