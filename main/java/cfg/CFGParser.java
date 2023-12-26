package cfg;

import java.util.*;

/**
 * given a CFG as a Text/Unicode String, this class provides functionality for parsing such a string and
 * creating an equivalent CFG representation as a CFG class.
 *
 * THE PARSER ASSUMES THAT THE INPUT GRAMMAR STRING HAS WHITESPACES BTWN VARS & TERMINALS (TO ALLOW FOR VARS OF ARBITRARY LENGTH).
 *
 * check the symbols that are allowed to rep epsilon below.
 *
 * any string of length ==1 that doesn't start with a capital letter is considered part of the alphabet
 * */
public class CFGParser {

    // Parse a context-free grammar string and create an instance of ContextFreeGrammar
    public static ContextFreeGrammar parseGrammarString(String grammarString) {

        String nameCFG = "studentCFG";
        Map<String, Set<List<String>>> rules = new HashMap<>();

        // Split the grammar string into lines
        String[] lines = grammarString.split("\\n");

        for (String line : lines) {
            // Split each line into components
            String[] components = line.split("\\s*[-→]+\\s*");

            if (components.length == 2) {
                String variable = components[0].trim();
                String[] alternatives = components[1].split("\\s*\\|\\s*");

                // Add variable to rules map
                rules.put(variable, new HashSet<>());

                for (String alternative : alternatives) {
                    List<String> ruleList = new ArrayList<>();

                    // Split the alternative into variables and symbols
                    String[] parts = alternative.trim().split("\\s+");
                    for (String part : parts) {
                        if (part.equals("?") || part.equals("ε") || part.equals("ɛ")) {
                            ruleList.add(ContextFreeGrammar.epsilon);
                        } else {
                            ruleList.add(part);
                        }
                    }

                    // Add rule to the set associated with the variable
                    rules.computeIfAbsent(variable, k -> new HashSet<>()).add(ruleList);
                }

            } else {
                // Invalid grammar string format
                throw new IllegalArgumentException("Invalid grammar string format: " + line);
            }
        }

        return new ContextFreeGrammar(nameCFG, rules);
    }
}
