package folformula;

import folformula.operators.*;

import java.util.Iterator;
import java.util.TreeSet;

public abstract class FOLFormula extends TreeSet<FOLFormula> implements Comparable<FOLFormula>{

    /**
     * the name of a FOL Formula is only != null if it is a Variable.
     * Variable name is only needed for comparison between Variables.
     * */
    protected String name;

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

        int differentlyClassed = compareByClass(otherFolFormula);
        if (differentlyClassed != 0) return differentlyClassed;

        return compareByChildren(otherFolFormula);
    }

    /**
     * a name != null is always > a name == null
     * */
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
        int differentlyClassed = this.getClass().getName().compareTo(otherFolFormula.getClass().getName());
        return differentlyClassed;
    }

    private int compareByChildren(FOLFormula otherFolFormula){

        assert this.size() == otherFolFormula.size() : "comparing children despite tree sizes differing. something went wrong";

        Iterator<FOLFormula> this_itr = this.iterator();
        Iterator<FOLFormula> other_itr = otherFolFormula.iterator();

        //since the iterator only has method for returning NEXT and not current
        if (this_itr.hasNext() && other_itr.hasNext()){
            int lastKids = this.last().compareTo(otherFolFormula.last());
            if (lastKids != 0) return lastKids;
        }

        //iterator over elems in ascending order
        while (this_itr.hasNext() && other_itr.hasNext()){

            FOLFormula thisChild = this_itr.next();
            FOLFormula otherChild = other_itr.next();

            int differingKids = thisChild.compareTo(otherChild);

            if (differingKids != 0) return differingKids;

        }

        return 0;

    }







    public FOLFormula(String name){

        super(); //aka TreeSet()
        this.name = name;
    }

    public FOLFormula(FOLFormula subFormula){
        add(subFormula);
        this.name = null;
    }

    public FOLFormula(FOLFormula leftSubFormula, FOLFormula... rightSubformulae){
        add(leftSubFormula);
        for (FOLFormula rightSubformula : rightSubformulae){
            add(rightSubformula);
        }
        this.name = null;
    }

    public FOLFormula and(FOLFormula... rightSubformulae){ return new Conjunction(this, rightSubformulae); }

    public FOLFormula not(){ return new Negation(this); }

    public FOLFormula or(FOLFormula... rightSubformulae){ return new Disjunction(this, rightSubformulae); }

    public FOLFormula equivalent(FOLFormula rightSubformula){ return new Equivalence(this, rightSubformula); }

    public FOLFormula exists(FOLFormula subformula){
        assert this.getClass() == Variable.class : "trying to quantify something that is not a Variable!";
        return new Exists((Variable) this, subformula);
    }

    public FOLFormula forall(FOLFormula subformula){
        assert this.getClass() == Variable.class : "trying to quantify something that is not a Variable!";
        return new ForAll((Variable) this, subformula);
    }

    public FOLFormula implies(FOLFormula rightSubformula){ return new Implication(this, rightSubformula); }

}
