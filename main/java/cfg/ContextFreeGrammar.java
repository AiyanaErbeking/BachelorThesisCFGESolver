package cfg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextFreeGrammar {

    private final String name;

    private Set<String> variables = findVariables();

    private Set<String> alphabet = findAlphabet();

    private Map<String, Set<String>> rules;

    private Set<String> startVariables = initStartVars();

    // to really check if grammar is clean, call the associated method
    private Boolean containsUnreachableVars = Boolean.TRUE;
    private Boolean containsNonGeneratingVars = Boolean.TRUE;


    public String epsilon = "";



    // HELPER METHODS FOR DETERMINING VALUE OF GRAMMAR ATTRIBUTES NOT INITIALISED IN CONSTRUCTOR =======================

    private Set<String> findVariables() { return getRules().keySet(); }

    private Set<String> findAlphabet(){
        Set<String> alphabet = new HashSet<>();

        // Iterate through the rules
        for (Set<String> productionRules : rules.values()) {
            for (String productionRule : productionRules) {
                // Split the rule into tokens
                String[] tokens = productionRule.split("\\s+");

                // Extract lowercase letters
                for (String token : tokens) {
                    if (token.length() == 1 && Character.isLowerCase(token.charAt(0))) {
                        alphabet.add(token);
                    }
                }
            }
        }
        return alphabet;
    }

    private Set<String> initStartVars(){
        Set<String> start = new HashSet<>();
        start.add("S");

        return start;
    }

    /**
     * helper method for iterating over all symbols (vars and terminals) in a given right side
     * */
    public String[] ruleSymbols(String rule){
        // Split the rule into tokens
        return rule.split("\\s+");
    }



    public ContextFreeGrammar(String name, Map<String, Set<String>> rules){

        this.name = name;
        this.rules = rules;

    }



    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Map<String, Set<String>> getRules() {
        return rules;
    }

    public void addRule(String variable, String rule) {
        rules.computeIfAbsent(variable, k -> new HashSet<>()).add(rule);
    }

    public Set<String> getStartVariables() {
        return startVariables;
    }

    public Set<String> getVariables(){ return variables; }

    public String getName() { return name; }


    /**
     * {@return a set of all string-rules from input var. These rules EXCLUDE the var itself}
     * */
    public Set<String> getRulesFromVar(String var){ return rules.getOrDefault(var, Collections.emptySet()); }


    /**
     * remove all occurrences of vars from which one cannot generate a word ie. they only lead to endless production rules.
     *
     * Note that because getAlphabet() always iterates through the rules and extracts all lowercase letters, after calling
     * {@link #removeNonGeneratingVars()}, the alphabet will be "updated" automatically
     * */
    public void removeNonGeneratingVars(){

        containsNonGeneratingVars = Boolean.FALSE;
    }

    /**
     * remove all occurrences of vars that are not reachable from the start var.
     * Alphabet is, as a by-product of current implementation, also updated.
     * */
    public void removeUnreachableVars(){

        containsUnreachableVars = Boolean.FALSE;
    }

    /**
     * a grammar is clean if all vars are reachable and generating
     * */
    public Boolean isGrammarClean(){

        return !(containsNonGeneratingVars | containsUnreachableVars);
    }

    /**
     * can the word epsilon be generated from the start var?
     * */
    public Boolean isEpsilonGenerated(){

        if (isGrammarClean() && getAlphabet().contains(epsilon))
            return Boolean.TRUE;

        return Boolean.FALSE;
    }


    // METHODS REQUIRED FOR CONVERSION TO CHOMSKY NORMAL FORM ==========================================================
    // AT THIS POINT GRAMMAR SHOULD BE CLEAN

    /**
     * create a new start symbol: S_0, and rule: S_0 -> S for each S in the old start vars
     *
     * Remove the old start vars S from the start var set, and replace with S_0
     * */
    public void removeStartVarFromRightSides(){

        Set<String> oldStartVars = startVariables;
        String newStartVar = "S0";

        for (String oldStart : oldStartVars)
            addRule(newStartVar, oldStart);

        startVariables.removeAll(oldStartVars);
        startVariables.add(newStartVar);
    }

    /**
     * terminals should only appear alone on right sides. For every rule where there appears a non-solitary terminal, check
     * if there exists a rule Var -> terminal, and if so, replace all instances of the terminal with Var (apart from the Var rule).
     * If such a terminal-generating rule doesn't exist, create one and do the same replacement.
     * */
    public void removeNonSolitaryTerminals(){}

    /**
     * For rules of the form: A -> X_1 ... X_n with n > 2, replace with the following new rules and new Vars A_i:
     * <ul>
     *   <li>A -> X_1 A_1</li>
     *   <li>A_1 -> X_2 A_2</li>
     *   <li>...</li>
     * </ul>
     */
    public void removeRulesWithRightSideGreaterTwo(){}

    /**
     * find the set of nullable vars (those vars from which epsilon can be generated). ie. either epsilon is directly
     * generated from this var, or this var has an alternative with ONLY nullable vars.
     *
     * for every alternative containing at least one nullable var, and for every nullable var in this alternative,
     * duplicate this alternative but remove an occurrence of the nullable var.
     * */
    public void removeEpsilonRules(){

    }

    /**
     * for rules of the form A -> B, remove this alternative from A and instead add all B alternatives to A
     * */
    public void removeUnitRules(){}

    public ContextFreeGrammar parse(String cfgString){

        return CFGParser.parseGrammarString(cfgString);
    }

}
