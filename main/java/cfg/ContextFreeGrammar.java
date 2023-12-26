package cfg;

import java.util.*;

public class ContextFreeGrammar {

    // ATTRIBUTES ======================================================================================================

    private final String name;

    private Set<String> variables;

    private Set<String> alphabet;

    private Map<String, Set<List<String>>> rules;

    private Set<String> startVariables;


    // to really check if grammar is clean, call the associated method
    private Boolean containsUnreachableVars = Boolean.TRUE;
    private Boolean containsNonGeneratingVars = Boolean.TRUE;


    public static String epsilon = "";



    // HELPER METHODS FOR DETERMINING VALUE OF GRAMMAR ATTRIBUTES NOT INITIALISED IN CONSTRUCTOR =======================

    private Set<String> findVariables() { return rules.keySet(); }

    private Set<String> findAlphabet() {
        Set<String> alphabet = new HashSet<>();

        // Iterate through the rules
        for (Set<List<String>> productionRules : rules.values()) {
            for (List<String> productionRule : productionRules) {
                // Extract letters (including non-capitalized) from the productionRule
                for (String token : productionRule) {
                    // If the token doesn't start with a capital letter, add it to the alphabet
                    if (token.length() == 1 && !Character.isUpperCase(token.charAt(0))) {
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




    public ContextFreeGrammar(String name, Map<String, Set<List<String>>> rules){

        this.name = name;
        this.rules = rules;
        this.variables = rules.keySet();
        this.alphabet = findAlphabet();
        this.startVariables = initStartVars();

    }


    // GETTERS =========================================================================================================

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Map<String, Set<List<String>>> getRules() {
        return rules;
    }

    public Set<String> getStartVariables() {
        return startVariables;
    }

    public Set<String> getVariables(){ return variables; }

    public String getName() { return name; }



    // PUBLIC METHODS ==================================================================================================

    public void addRule(String variable, List<String> rule) {
        rules.computeIfAbsent(variable, k -> new HashSet<>()).add(rule);
    }

    /**
     * {@return a set of all right-sides from input var}
     * */
    public Set<List<String>> getRulesFromVar(String var){ return rules.getOrDefault(var, Collections.emptySet()); }


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
    // METHODS SHOULD BE RUN IN THE ORDER LISTED

    /**
     * If any of the current start vars appears on any right-hand side,
     * create a new start symbol: S_0, and rule: S_0 -> S
     *
     * Remove the old start vars S from the start var set, and replace with S_0
     */
    public void removeStartVarFromRightSides() {
        // Set to keep track of whether any old start variable appears on the right side
        boolean shouldAddNewStartVar = false;

        // Check if any old start variable appears on the right side
        for (String var : getVariables()) {
            for (String startVar : getStartVariables()) {
                for (List<String> rightSide : getRulesFromVar(var))
                    if (rightSide.contains(startVar)) {
                        shouldAddNewStartVar = true;
                        break;
                    }
            }
            if (shouldAddNewStartVar) {
                break;
            }
        }

        // Add new start variable only if needed
        if (shouldAddNewStartVar) {
            String newStartVar = "S0";
            Set<String> oldStarts = new HashSet<>(startVariables);
            startVariables.clear();
            startVariables.add(newStartVar);

            // Add the rule: S_0 -> S for each old start variable S
            for (String oldStart : oldStarts) {
                addRule(newStartVar, Collections.singletonList(oldStart));
            }
        }
    }


    /**
     * terminals should only appear alone on right sides.
     * For every rule where there appears a non-solitary terminal, check
     * if there exists a rule Var -> terminal, and if so, replace all instances of the terminal with Var (apart from the Var rule).
     * If such a terminal-generating rule doesn't exist, create one and do the same replacement.
     *
     * iterate through the key (vars) in rules. for each var, iterate through all its right sides.
     * */
    public void removeNonSolitaryTerminals() {
        for (String var : new HashSet<>(getVariables())) {  // Copy of the set to avoid concurrent modification
            Set<List<String>> varRightSides = new HashSet<>(rules.get(var));  // Copy of the set to avoid concurrent modification

            for (List<String> varRightSide : varRightSides) {
                List<String> updatedVarRightSide = new ArrayList<>(varRightSide);

                for (String symbol : varRightSide) {
                    // Replace each non-solitary terminal with a generating Var
                    if (alphabet.contains(symbol) && varRightSide.size() > 1) {
                        // terminalVar either already exists, or is created new along with an associated rule
                        String terminalVar = getVariableForTerminal(symbol);
                        updatedVarRightSide.set(varRightSide.indexOf(symbol), terminalVar);
                    }
                }

                // Update the right side in the rules map
                rules.get(var).remove(varRightSide);
                rules.get(var).add(updatedVarRightSide);
            }
        }
    }
    private String getVariableForTerminal(String terminal) {
        // Check if there exists a rule Var -> terminal
        for (String var : rules.keySet()) {
            if (rules.get(var).size() == 1 && rules.get(var).iterator().next().equals(Collections.singletonList(terminal))) {
                return var;
            }
        }

        // If no such rule exists, create a new variable and rule
        String newVar = "Z" + terminal;

        if (!rules.containsKey(newVar)) {
            addRule(newVar, Collections.singletonList(terminal));
            return newVar;
        }

        // If the variable already exists, try appending a unique index
        for (int i = 0; i <= 100; i++) {
            String indexedVar = newVar + i;
            if (!rules.containsKey(indexedVar)) {
                addRule(indexedVar, Collections.singletonList(terminal));
                return indexedVar;
            }
        }

        throw new RuntimeException("Cannot create a new variable Z + terminal + i for i < 100. Something is going wrong");
    }


    /**
     * For rules of the form: A -> X_0 ... X_n-1 with n > 2, replace with the following new rules and new Vars A_i:
     * <ul>
     *   <li>A -> X_0 A_0</li>
     *   <li>A_0 -> X_1 A_1</li>
     *   <li>...</li>
     * </ul>
     */
    public void removeRulesWithRightSideGreaterTwo() {
        Map<String, Set<List<String>>> updatedRules = new HashMap<>(rules);

        for (String var : new HashSet<>(getVariables())) {
            Set<List<String>> alternatives = new HashSet<>(rules.get(var));

            // Check for unit rules
            for (List<String> alternative : alternatives) {
                if (alternative.size() > 2) {
                    List<String> newVarNames = new ArrayList<>();

                    for (int i = 1; i < (alternative.size()); i++) {
                        String newVar = generateNewVar(var, i); // A1 ... An-1
                        newVarNames.add(newVar);
                    }

                    updatedRules.get(var).add(Arrays.asList(alternative.get(0), newVarNames.get(0)));

                    for (int j = 0; j < alternative.size(); j++) {
                        if ((j + 2) >= alternative.size()) {
                            updatedRules.computeIfAbsent(newVarNames.get(j), k -> new HashSet<>())
                                    .add(Arrays.asList(alternative.get(j), alternative.get(j + 1)));
                        } else {
                            updatedRules.computeIfAbsent(newVarNames.get(j), k -> new HashSet<>())
                                    .add(Arrays.asList(alternative.get(j + 1), newVarNames.get(j + 1)));
                        }
                    }

                }
            }
        }
        // Remove unwanted rules from updatedRules
        for (String key : updatedRules.keySet()) {
            Set<List<String>> rulesToBeRemoved = new HashSet<>();
            for (List<String> alternative : updatedRules.get(key)) {
                if (alternative.size() > 2) {
                    rulesToBeRemoved.add(alternative);
                }
            }
            updatedRules.get(key).removeAll(rulesToBeRemoved);
        }

        // Update the rules with updatedRules
        rules.putAll(updatedRules);
    }

    private String generateNewVar(String currentVar, int i){

        String newVar = currentVar + i;

        if (!rules.containsKey(newVar)) {
            return newVar;
        }

        // If the variable already exists, try appending a unique index
        for (int j = 1; j <= 100; j++) {
            String indexedVar = newVar + j;
            if (!rules.containsKey(indexedVar)) {
                return indexedVar;
            }
        }

        throw new RuntimeException("Cannot create a new variable" + currentVar + "i for i < 100. Something is going wrong");
    }


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
    public void removeUnitRules() {
        for (String var : new HashSet<>(getVariables())) {  // Copy of the set to avoid concurrent modification
            Set<List<String>> alternatives = new HashSet<>(rules.get(var));  // Copy of the set to avoid concurrent modification

            Set<List<String>> updatedAlternatives = new HashSet<>(alternatives);
            Set<List<String>> alternativesToBeRemoved = new HashSet<>();

            // Check for unit rules
            for (List<String> alternative : alternatives) {
                if (alternative.size() == 1 && getVariables().contains(alternative.get(0))) {
                    // Remove the unit rule
                    alternativesToBeRemoved.add(alternative);

                    // Add alternatives of B to A
                    Set<List<String>> unitVarAlternatives = rules.get(alternative.get(0));
                    updatedAlternatives.addAll(unitVarAlternatives);
                }
            }

            // Remove unwanted alternatives
            updatedAlternatives.removeAll(alternativesToBeRemoved);

            // Update the original set with the modified alternatives
            rules.put(var, updatedAlternatives);
        }
    }

/*
    public ContextFreeGrammar parse(String cfgString){

        return CFGParser.parseGrammarString(cfgString);
    }
*/
}
