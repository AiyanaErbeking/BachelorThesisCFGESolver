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
    public static ContextFreeGrammar parseGrammarString(String name, String grammarString) {

        Set<Character> alphabetLettersInUse = extractLowercaseLetters(grammarString);
        Character unusedLetter = 'a';

        Map<String, String> nonAlphabetTerminalsToAlphabet = new HashMap<>();

        Map<String, Set<List<String>>> rules = new HashMap<>();

        // Split the grammar string into lines
        String[] lines = grammarString.split("\\n");

        for (String line : lines) {
            // Split each line into components
            String[] components = line.split("\\s*[->→]+\\s*");

            if (components.length == 2) {
                String variable = components[0].trim();
                if (Character.isDigit(variable.charAt(0))) variable = "Z" + variable;

                String[] alternatives = components[1].split("\\s*\\|\\s*");

                for (String alternative : alternatives) {
                    List<String> ruleList = new ArrayList<>();

                    // Split the alternative into variables and symbols
                    String[] parts = alternative.trim().split("\\s+");

                    for (String part : parts) {

                        if (part.equals("?") || part.equals("ε") || part.equals("ɛ")) {
                            ruleList.add(ContextFreeGrammar.epsilon);
                        }

                        // Check if the part starts with a number
                        else if (Character.isDigit(part.charAt(0))) {
                            part = "Z" + part;
                            ruleList.add(part);
                        }

                        else if (part.length() == 1 && !Character.isUpperCase(part.charAt(0)) && !alphabetLettersInUse.contains(part.charAt(0))) {

                            if (nonAlphabetTerminalsToAlphabet.containsKey(part))
                                ruleList.add(nonAlphabetTerminalsToAlphabet.get(part));

                            // terminals that are not part of the alphabet need to be mapped to an unused alphabet letter
                            else {
                                Character newLetter = generateNewLetter(unusedLetter, alphabetLettersInUse);
                                ruleList.add(newLetter.toString());
                                nonAlphabetTerminalsToAlphabet.put(part, newLetter.toString());
                                alphabetLettersInUse.add(newLetter);
                            }
                        }

                        else {
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

        return new ContextFreeGrammar(name, rules);
    }

    // Extract lowercase letters from a grammar string and add them to a set
    private static Set<Character> extractLowercaseLetters(String grammarString) {
        Set<Character> lowercaseLetters = new HashSet<>();

        for (char c : grammarString.toCharArray()) {
            if (Character.isLowerCase(c)) {
                lowercaseLetters.add(c);
            }
        }

        return lowercaseLetters;
    }

    private static Character generateNewLetter(Character unusedLetter, Set<Character> alphabetLettersInUse){
        while (alphabetLettersInUse.contains(unusedLetter)) {
            unusedLetter ++;
        }
        return unusedLetter;
    }
}