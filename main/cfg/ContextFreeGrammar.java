package cfg;

import java.util.Set;

public class ContextFreeGrammar {

    private final String name;

    private final Set<String> variables;

    private final Set<String> alphabet;

    private final Set<String> rules;

    private final Set<String> startVariables;


    public ContextFreeGrammar(String name, Set<String> variables, Set<String> alphabet, Set<String> rules, Set<String> startVariables){

        this.name = name;
        this.variables = variables;
        this.alphabet = alphabet;
        this.rules = rules;
        this.startVariables = startVariables;

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

    public String getName() { return name; }


    public ContextFreeGrammar parse(String cfgString){

        return CFGParser.parseGrammarString(cfgString);
    }

}
