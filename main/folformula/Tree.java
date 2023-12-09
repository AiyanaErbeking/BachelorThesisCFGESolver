package folformula;

import java.util.ArrayList;

public abstract class Tree {

    protected ArrayList<Tree> children;


    public Tree(){
        children = new ArrayList<>();
    }

    public ArrayList<Tree> getChildren(){
        return children;
    }

    public Tree getChild(int position){
        assert children.size() >= position+1 : "there is no child at this position!";
        return children.get(position);
    }

    public void addChild(Tree child){
        this.children.add(child);
    }





}
