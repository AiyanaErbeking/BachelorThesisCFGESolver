package FOLFormula;

import java.util.TreeSet;

public abstract class FOLFormula extends TreeSet<FOLFormula> implements Comparable<FOLFormula>{

    /**
     * the name of a FOL Formula is only != null if it is a Variable.
     * Variable name is actually only needed for comparison between Variables.
     * */
    protected String name = null;

    /**
     * As insertions into a TreeSet require comparison (does an instance of the inserting element already exist in the Set?),
     * the compareTo() method needs to be defined for FOL Formulae.
     * First, name is compared. Then, number of children is compared. If no difference is found here, the same is done
     * recursively for all children.
     * */
    @Override
    public int compareTo(FOLFormula OtherFolFormula) {

        int moreChildren = compareByChildren(OtherFolFormula);
        if (moreChildren != 0) return moreChildren;

        return 0;
    }

    private int compareByName(FOLFormula OtherFolFormula){
        return this.name.compareTo(OtherFolFormula.name);
    }

    private int compareByChildren(FOLFormula OtherFolFormula){
        return size() - OtherFolFormula.size();
    }

    public FOLFormula(String name){

        super(); //aka TreeSet()
        this.name = name;
    }

    public FOLFormula(FOLFormula subFormula){ add(subFormula); }

    public FOLFormula(FOLFormula leftSubFormula, FOLFormula... rightSubformulae){
        add(leftSubFormula);
        for (FOLFormula rightSubformula : rightSubformulae){
            add(rightSubformula);
        }
    }

    public FOLFormula and(FOLFormula... rightSubformulae){ return new Conjunction(this, rightSubformulae); }


}
