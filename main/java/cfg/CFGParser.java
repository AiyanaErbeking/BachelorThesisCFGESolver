package cfg;

import java.util.HashSet;
import java.util.Set;

/**
 * given a CFG as a Text/Unicode String, this class provides functionality for parsing such a string and
 * creating an equivalent CFG representation as a CFG class
 * */
public class CFGParser {

    // Parse a context-free grammar string and create an instance of ContextFreeGrammar
    public static ContextFreeGrammar parseGrammarString(String grammarString) {

        String nameCFG = "studentCFG";
        Set<String> variables = new HashSet<>();
        Set<String> alphabet = new HashSet<>();
        Set<String> rules = new HashSet<>();
        Set<String> startVariables = new HashSet<>();

        // Split the grammar string into lines
        String[] lines = grammarString.split("\\n");

        for (String line : lines) {
            // Split each line into components
            String[] components = line.split("\\s*->\\s*");

            if (components.length == 2) {
                String variable = components[0].trim();
                String[] alternatives = components[1].split("\\s*\\|\\s*");

                // Add variable to variables set
                variables.add(variable);

                for (String alternative : alternatives) {
                    // Remove non-letter characters and whitespace from the rule
                    String rule = alternative.trim().replaceAll("[^a-zA-Z]", "");

                    for (int i = 0; i < rule.length(); i++) {
                        char symbol = rule.charAt(i);
                        if (Character.isLowerCase(symbol)) {
                            alphabet.add(String.valueOf(symbol));
                        }
                    }

                    // Add all alternatives to the rule set with the variable from which they are generated.
                    // rule is only ever empty if the alternative was of the form: | ?
                    if (!rule.isEmpty()) rules.add(variable + rule);
                }

            } else {
                // Invalid grammar string format
                throw new IllegalArgumentException("Invalid grammar string format: " + line);
            }

            // Set the start variable to "S" (assuming it is always "S")
            startVariables.add("S");
        }

        return new ContextFreeGrammar(nameCFG, variables, alphabet, rules, startVariables);
    }

}
