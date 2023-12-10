package cfg;

import folformula.FOLFormula;
import folformula.operators.*;
import folformula.terms.LEQ;
import folformula.terms.LetterAtPos;
import folformula.terms.Tableau;
import folformula.terms.Variable;
import folformula.writers.TPTPWriter;

import java.util.*;

public class ReductionCfgeToFolSat extends TPTPWriter {

    public FOLFormula reduce(ContextfreeGrammar C1, ContextfreeGrammar C2){

        ArrayList<FOLFormula> allSubformulae = new ArrayList<>();

        allSubformulae.add(encodingWordStructure(C1, C2));
        allSubformulae.add(encodingCYKTable(C1));
        allSubformulae.add(encodingCYKTable(C2));
        allSubformulae.add(encodingGrammarInequivalence(C1, C2));

        return new Conjunction(allSubformulae);
    }

    /**
     * sub-formula: every position of the searched-for word contains exactly one letter
     * */
    public FOLFormula encodingWordStructure(ContextfreeGrammar C1, ContextfreeGrammar C2){

        FOLFormula forAllSubFormula;
        Variable X = new Variable("X");

        Set<String> alphabetC1 = C1.getAlphabet();

        // a set contains no element twice; all distinct letters from both alphabets are included
        alphabetC1.addAll(C2.getAlphabet());

        // conversion to list, so it's possible to question element index
        List<String> alphabet = new ArrayList<>(alphabetC1);

        if (alphabet.isEmpty()) throw new IllegalStateException("both CFG alphabets are empty!");

        ArrayList<FOLFormula> disjOfConj = new ArrayList<>();


        for (String sigma : alphabet){

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

            disjOfConj.add(lettersAtPos);

            alphabetWithoutSigma.add(sigma);
        }

        Disjunction disjOfConjunctions = new Disjunction(disjOfConj);

        forAllSubFormula = new ForAll(X, disjOfConjunctions);

        return forAllSubFormula;
    }

    /**
     * sub-formula: representing the CYK Table
     * */
    public FOLFormula encodingCYKTable(ContextfreeGrammar CFG){

        Set<String> variables = CFG.getVariables();
        Set<String> rules = CFG.getRules();
        String name = CFG.getName();

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");

        ForAll forALlY = new ForAll(Y, subwordsLengthOne(variables, rules, name).and(subwordsGreaterOne(variables, rules, name)));

        return new ForAll(X, forALlY);
    }

    /**
     * sub-formula: CYK Table entries where sub-word has length 1 ie. V =>* w iff V -> sigma
     * */
    public FOLFormula subwordsLengthOne(Set<String> variables, Set<String> rules, String grammarName){

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        List<String> vars = new ArrayList<>(variables);
        List<String> productions = new ArrayList<>(rules);

        ArrayList<FOLFormula> conjunctionList = new ArrayList<>();

        //a set of grammar variables which have terminal rules (map directly to a letter)
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

        //conversion of set to indexable data structure
        List<String> varsWithTerminalRule = new ArrayList<>(varWithTerminalRule);

        for (String var : varsWithTerminalRule){

            Tableau varTableau = new Tableau(X, Y);
            varTableau.setAssociatedGrammarName(grammarName);
            varTableau.setAssociatedVariable(var);

            List<String> terminalProductionsFromVar = new ArrayList<>();
            for (String rule : productions){
                if (rule.length() == 2){
                    if (Objects.equals(rule.substring(0, 1), var)){
                        terminalProductionsFromVar.add(rule);
                    }
                }
            }

            ArrayList<FOLFormula> disjunctionList = new ArrayList<>();

            for (String rule : terminalProductionsFromVar){

                LetterAtPos letterFromTerminalProductionsOfVar = new LetterAtPos(X);
                letterFromTerminalProductionsOfVar.setAssociatedLetter(rule.substring(1, 2));

                disjunctionList.add(letterFromTerminalProductionsOfVar);
            }

            conjunctionList.add(new Equivalence(varTableau, new Disjunction(disjunctionList)));

            // in preparation for the next loop iterating over terminal production rules of a new grammar var
            terminalProductionsFromVar.clear();


        }

        // position X == position Y ie. X <= Y AND Y <= X
        LEQ xLEQy = new LEQ(X, Y);
        LEQ yLEQx = new LEQ(Y, X);
        Conjunction xEQUALSy = new Conjunction(xLEQy, yLEQx);

        return new Implication(xEQUALSy, new Conjunction(conjunctionList));
    }

    /**
     * sub-formula: CYK Table entries where sub-word has length > 1 ie. V =>* w iff V -> AB
     * */
    public FOLFormula subwordsGreaterOne(Set<String> variables, Set<String> rules, String grammarName){

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        List<String> vars = new ArrayList<>(variables);
        List<String> productions = new ArrayList<>(rules);

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");
        Variable K = new Variable("K");

        // the position k: X <= K < Y
        LEQ xLEQk = new LEQ(X, K);
        Conjunction kSMALERky = new Conjunction(new LEQ(K, Y), new Negation(new LEQ(Y, K)));

        // there exists a position k+1 which is exactly one position greater than k (there does not exist another position in between k and k+1)
        Variable KPLUSONE = new Variable("KPLUSONE");
        Variable INBETWEEN = new Variable("INBETWEEN");

        Conjunction kSMALERkplusone = new Conjunction(new LEQ(K, KPLUSONE), new Negation(new LEQ(KPLUSONE, K)));
        Conjunction inbetweenSMALERkplusone = new Conjunction(new LEQ(INBETWEEN, KPLUSONE), new Negation(new LEQ(KPLUSONE, INBETWEEN)));
        Negation inbetweenNOTEQUALk = new Negation(new Conjunction(new LEQ(INBETWEEN, K), new LEQ(K, INBETWEEN)));
        Negation nOTinbetweenSMALLERk = new Negation(new Conjunction(new LEQ(INBETWEEN, K), new Negation(new LEQ(K, INBETWEEN))));
        Exists eXISTSinbetween = new Exists(INBETWEEN, new Conjunction(inbetweenSMALERkplusone, inbetweenNOTEQUALk, nOTinbetweenSMALLERk));




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

        // conjunction over all grammar vars with non-terminal production rules
        ArrayList<FOLFormula> conjunctionList = new ArrayList<>();

        for (String var : varsWithNonTerminalRules) {

            Tableau xyTableau = new Tableau(X, Y);
            xyTableau.setAssociatedVariable(var);
            xyTableau.setAssociatedGrammarName(grammarName);


                List<String> nonTerminalProductionsFromVar = new ArrayList<>();
                for (String rule : productions) {
                    if (rule.length() == 3) {
                        if (Objects.equals(rule.substring(0, 1), var)) {
                            nonTerminalProductionsFromVar.add(rule);
                        }
                    }
                }


                ArrayList<FOLFormula> disjunctionList = new ArrayList<>();

                for (String rule : nonTerminalProductionsFromVar) {

                    Tableau xkTableau = new Tableau(X, K);
                    xkTableau.setAssociatedGrammarName(grammarName);
                    xkTableau.setAssociatedVariable(rule.substring(1, 2));

                    Tableau kplusoneyTableau = new Tableau(KPLUSONE, Y);
                    kplusoneyTableau.setAssociatedVariable(rule.substring(2, 3));
                    kplusoneyTableau.setAssociatedGrammarName(grammarName);

                    // the first var from the non-term rule produces Tableau(X, K) and the second var from the rule produces Tableau(KPLUSONE, Y)
                    disjunctionList.add(new Conjunction(xkTableau, new Exists(KPLUSONE, new Conjunction(kSMALERkplusone, new Negation(eXISTSinbetween), kplusoneyTableau))));
                }

                Disjunction disjOverNonTerminalRules = new Disjunction(disjunctionList);

                // there exists a position k:
                Exists existsK = new Exists(K, new Conjunction(xLEQk, kSMALERky, disjOverNonTerminalRules));

                conjunctionList.add(new Equivalence(xyTableau, existsK));

                nonTerminalProductionsFromVar.clear();
            }

        // position X < position Y
        LEQ xLEQy = new LEQ(X, Y);
        Negation nOTyLEQx = new Negation(new LEQ(Y, X));
        Conjunction xSMALLERy = new Conjunction(xLEQy, nOTyLEQx);

        return new Implication(xSMALLERy, new Conjunction(conjunctionList));
    }

    /**
     * sub-formula: w is generated by C1 iff it is not generated by C2
     * */
    public FOLFormula encodingGrammarInequivalence(ContextfreeGrammar C1, ContextfreeGrammar C2){
        return "( " + wordIsGenerated(C1.getStartVariables(), C1.getName()) + getEquivalence() + getNegation() + wordIsGenerated(C2.getStartVariables(), C2.getName()) + " )";
    }

    /**
     * sub-formula: w is generated by CFG
     * ie. there exist position X, Y where X is the smallest and Y the greatest position in w,
     * and an S in CFG: S =>* w[X, Y]
     * */
    public FOLFormula wordIsGenerated(Set<String> startVariables, String name){

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
