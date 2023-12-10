package cfg;

import folformula.FOLFormula;
import folformula.operators.Conjunction;
import folformula.operators.ForAll;
import folformula.terms.LetterAtPos;
import folformula.terms.Variable;
import folformula.writers.TPTPWriter;

import java.util.*;

public class ReductionCfgeToFolSat extends TPTPWriter {

    public String reduce(ContextfreeGrammar C1, ContextfreeGrammar C2){
        return "(" + encodingWordStructure(C1, C2) + getAnd() + encodingCYKTable(C1) + getAnd() + encodingCYKTable(C2) + getAnd() + encodingGrammarInequivalence(C1, C2) + ")";
    }

    /**
     * sub-formula: every position of the searched-for word contains exactly one letter
     * */
    protected FOLFormula encodingWordStructure(ContextfreeGrammar C1, ContextfreeGrammar C2){

        FOLFormula forAllSubFormula;
        FOLFormula disjunctionSubFormula;
        FOLFormula conjunctionSubFormula;
        Variable X = new Variable("X");

        Set<String> alphabetC1 = C1.getAlphabet();

        // a set contains no element twice; all distinct letters from both alphabets are included
        alphabetC1.addAll(C2.getAlphabet());

        // conversion to list, so it's possible to question element index
        List<String> alphabet = new ArrayList<>(alphabetC1);

        if (alphabet.isEmpty()) throw new IllegalStateException("both CFG alphabets are empty!");

        forAllSubFormula = new ForAll(X, disjunctionSubFormula);


        for (String sigma : alphabet){
            if (!Objects.equals(alphabet.get(0), sigma)){
                folFormula += getOr();
            }

            LetterAtPos letterAtPos = new LetterAtPos(X);
            letterAtPos.setAssociatedLetter(sigma);

            List<String> alphabetWithoutSigma = new ArrayList<>(alphabet);
            alphabetWithoutSigma.remove(sigma);

            ArrayList<FOLFormula> conjLettersAtPos = new ArrayList<>();

            conjLettersAtPos.add(letterAtPos);

            for (String letterNotSigma : alphabetWithoutSigma) {

                LetterAtPos letterNotSigmaAtPos = new LetterAtPos(X);
                letterNotSigmaAtPos.setAssociatedLetter(letterNotSigma);

                conjLettersAtPos.add(letterNotSigmaAtPos.not());
            }

            Conjunction lettersAtPos = new Conjunction(conjLettersAtPos);

            alphabetWithoutSigma.add(sigma);
        }
        folFormula += " )";
        return folFormula + " )";
    }

    /**
     * sub-formula: representing the CYK Table
     * */
    protected String encodingCYKTable(ContextfreeGrammar CFG){
        Set<String> variables = CFG.getVariables();
        Set<String> rules = CFG.getRules();
        String name = CFG.getName();

        return "(" + getForAll("X") + getForAll("Y") + "(" + subwordsLengthOne(variables, rules, name) + getAnd() + subwordsGreaterOne(variables, rules, name) + ") )";
    }

    /**
     * sub-formula: CYK Table entries where sub-word has length 1 ie. V =>* w iff V -> sigma
     * */
    public String subwordsLengthOne(Set<String> variables, Set<String> rules, String name){

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        List<String> vars = new ArrayList<>(variables);
        List<String> productions = new ArrayList<>(rules);

        // if position X == position Y
        String folFormula = "( ( leq(X, Y)" + getAnd() + "leq(Y, X)" + " )" + getImplication() + "( ";

        Set<String> varWithTerminalRule = new HashSet<>();
        for (String var : vars){
            for (String rule : productions) {
                if (rule.length() == 2) {
                    if (Objects.equals(rule.substring(0, 1), var)) {
                        varWithTerminalRule.add(var);
                    }
                }
            }
        }

        List<String> varsWithTerminalRule = new ArrayList<>(varWithTerminalRule);

        for (String var : varsWithTerminalRule){
            if (!Objects.equals(varsWithTerminalRule.get(0), var)){
                folFormula += getAnd();
            }

            folFormula += "( " + getTableau(name, var, "X", "Y") + getEquivalence();

            List<String> terminalProductionsFromVar = new ArrayList<>();
            for (String rule : productions){
                if (rule.length() == 2){
                    if (Objects.equals(rule.substring(0, 1), var)){
                        terminalProductionsFromVar.add(rule);
                    }
                }
            }

            folFormula += "( ";

            for (String rule : terminalProductionsFromVar){
                if (!Objects.equals(terminalProductionsFromVar.get(0), rule)){
                    folFormula += getOr();
                }
                folFormula += getLetterAtPos(rule.substring(1, 2), "X");
            }

            folFormula += " ) )";

            terminalProductionsFromVar.clear();

        }

        folFormula += " ) )";

        return folFormula;
    }

    /**
     * sub-formula: CYK Table entries where sub-word has length > 1 ie. V =>* w iff V -> AB
     * */
    public String subwordsGreaterOne(Set<String> variables, Set<String> rules, String name){

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        List<String> vars = new ArrayList<>(variables);
        List<String> productions = new ArrayList<>(rules);

        String positionX = "X";
        String positionY = "Y";
        String positionK = "K";

        // if position X < position Y
        String folFormula = "( " + getLessThan(positionX, positionY) + getImplication();

        Set<String> varWithNonTerminalRules = new HashSet<>();
        for (String var : vars){
            for (String rule : productions) {
                if (rule.length() == 3) {
                    if (Objects.equals(rule.substring(0, 1), var)) {
                        varWithNonTerminalRules.add(var);
                    }
                }
            }
        }

        List<String> varsWithNonTerminalRules = new ArrayList<>(varWithNonTerminalRules);

        for (String var : varsWithNonTerminalRules) {
                if (!Objects.equals(varsWithNonTerminalRules.get(0), var)) {
                    folFormula += getAnd();
                }

                folFormula += "( " + getTableau(name, var, positionX, positionY) + getEquivalence();

                folFormula += getExists(positionK) + "(" + getLeq(positionX, positionK) + getAnd() + getLessThan(positionK, positionY) + getAnd();

                List<String> nonTerminalProductionsFromVar = new ArrayList<>();
                for (String rule : productions) {
                    if (rule.length() == 3) {
                        if (Objects.equals(rule.substring(0, 1), var)) {
                            nonTerminalProductionsFromVar.add(rule);
                        }
                    }
                }

                folFormula += "( ";

                for (String rule : nonTerminalProductionsFromVar) {
                    if (!Objects.equals(nonTerminalProductionsFromVar.get(0), rule)) {
                        folFormula += getOr();
                    }
                    folFormula += "( " + getTableau(name, rule.substring(1, 2), positionX, positionK) + getAnd() + positionPlusOne(positionK) + getAnd() + getTableau(name, rule.substring(2, 3), "KPlusOne", positionY) + " ) )";
                }

                folFormula += " ) )";

                nonTerminalProductionsFromVar.clear();
            }
        return folFormula;
    }

    /**
     * sub-formula: w is generated by C1 iff it is not generated by C2
     * */
    protected String encodingGrammarInequivalence(ContextfreeGrammar C1, ContextfreeGrammar C2){
        return "( " + wordIsGenerated(C1.getStartVariables(), C1.getName()) + getEquivalence() + getNegation() + wordIsGenerated(C2.getStartVariables(), C2.getName()) + " )";
    }

    /**
     * sub-formula: w is generated by CFG
     * ie. there exist position X, Y where X is the smallest and Y the greatest position in w,
     * and an S in CFG: S =>* w[X, Y]
     * */
    private String wordIsGenerated(Set<String> startVariables, String name){

        String positionX = "X";
        String positionY = "Y";
        String positionK = "K";
        String positionL = "L";

        if (startVariables.isEmpty()) throw new IllegalStateException("CFG has no start variables!");

        // there exist positions X, Y
        String folFormula = "( " + getExists("X") + getExists("Y");

        // there does NOT exist a position K which is smaller than X
        folFormula += "( " + getNegation() + getExists("K") + "( leq(K, X)" + getAnd() + getNegation() + "leq(X, K) )";

        // there does NOT exist a position L which is greater than Y
        folFormula += getAnd() + getNegation() + getExists("L") + "( leq(Y, L)" + getAnd() + getNegation() + "leq(L, Y) )";

        folFormula += getAnd() + "( ";

        List<String> startVars = new ArrayList<>(startVariables);

        // and
        for (String start : startVars){
            if (!Objects.equals(startVars.get(0), start)){
                folFormula += getOr();
            }

            folFormula += getTableau(name, start, positionX, positionY);
        }

        folFormula += " ) ) )";

        return folFormula;
    }

}
