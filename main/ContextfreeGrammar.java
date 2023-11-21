import java.util.Set;

public class ContextfreeGrammar {

    protected String name;

    protected Set<String> variables;

    protected Set<String> alphabet;

    protected Set<String> rules;

    protected Set<String> startVariables;

    public ContextfreeGrammar(String description){
        name = description;
    }

    ///////////
    // getters
    //////////

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Set<String> getRules() {
        return rules;
    }

    public Set<String> getStartVariables() {
        return startVariables;
    }

    public Set<String> getVariables() {
        return variables;
    }

    public String getName() {
        return name;
    }

    ///////////
    // setters
    //////////

    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }

    public void setRules(Set<String> rules) {
        this.rules = rules;
    }

    public void setStartVariables(Set<String> startVariables) {
        this.startVariables = startVariables;
    }

    public void setVariables(Set<String> variables) {
        this.variables = variables;
    }
}
