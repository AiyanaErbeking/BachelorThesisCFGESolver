package FOLFormula;

import java.util.TreeSet;

public abstract class FOLFormula extends TreeSet<FOLFormula> implements Comparable<FOLFormula>{

    /**
     * the name of a FOL Formula is only != null if it is a Variable.
     * Variable name is only needed for comparison between Variables.
     * */
    protected String name = null;

    /**
     * As insertions into a TreeSet require comparison (does an instance of the inserting element already exist in the Set?),
     * the compareTo() method needs to be defined for FOL Formulae.
     * First, name is compared. Then, number of children is compared. If no difference is found here, the same is done
     * recursively for all children.
     *
     * compareTo() == 0 <=> objects are equal
     * x.compareTo(y) < 0 => x is less than y
     * */
    @Override
    public int compareTo(FOLFormula otherFolFormula) {

        int differentlyNamed = compareByName(otherFolFormula);
        if (differentlyNamed != 0) return differentlyNamed;

        int differentlySized = compareBySize(otherFolFormula);
        if (differentlySized != 0) return differentlySized;

        int differentlyClassed;



        return 0;
    }

    private int compareByName(FOLFormula otherFolFormula){

        if (this.name == null && otherFolFormula.name != null) return -1;

        if (this.name != null && otherFolFormula.name == null) return 1;

        if (this.name == null && otherFolFormula.name == null) return 0;

        return this.name.compareTo(otherFolFormula.name);
    }

    private int compareBySize(FOLFormula otherFolFormula){
        return size() - otherFolFormula.size();
    }

    private int compareByClass(FOLFormula otherFolFormula){
        int differentlyClassed = this.getClass()
    }

    private int compareByChildren(){}







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
