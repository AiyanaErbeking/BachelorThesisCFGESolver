package cfg;

import folformula.FOLFormula;
import folformula.operators.*;
import folformula.terms.*;
import folformula.writers.TPTPWriter;

import java.util.*;
/**
 * INPUT GRAMMARS NEED TO BE IN CNF AND DO NOT CONTAIN EPSILON
 * */
public class ReductionCfgeToFolSat extends TPTPWriter {

    public FOLFormula reduce(ContextFreeGrammar C1, ContextFreeGrammar C2){

        ArrayList<FOLFormula> allSubformulae = new ArrayList<>();

        allSubformulae.add(encodingLEQ());
        allSubformulae.add(encodingWordStructure(C1, C2));
        allSubformulae.add(encodingCYKTable(C1));
        allSubformulae.add(encodingCYKTable(C2));
        allSubformulae.add(encodingGrammarInequivalence(C1, C2));

        return new Conjunction(allSubformulae);
    }


    /**
     * establishing leq() as a total-order, antisymmetric equivalence relation
     * */
    private FOLFormula encodingLEQ(){

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");
        Variable Z = new Variable("Z");

        FOLFormula totalOrder = new ForAll(X, new ForAll(Y, new Disjunction(new LEQ(X, Y), new LEQ(Y, X))));

        FOLFormula reflectiveAndAntisymm = new ForAll(X, new ForAll(Y, new Equivalence(new Equals(X, Y), new Conjunction(new LEQ(X, Y), new LEQ(Y, X)))));

        FOLFormula transitive = new ForAll(X, new ForAll(Y, new ForAll(Z, new Implication(new Conjunction(new LEQ(X, Y), new LEQ(Y, Z)), new LEQ(X, Z)))));

        return new Conjunction(totalOrder, reflectiveAndAntisymm, transitive);
    }


    /**
     * sub-formula: every position of the searched-for word contains exactly one letter
     * */
    public FOLFormula encodingWordStructure(ContextFreeGrammar C1, ContextFreeGrammar C2){

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

                conjLettersAtPos.add(new Negation(letterNotSigmaAtPos));
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
    public FOLFormula encodingCYKTable(ContextFreeGrammar CFG){

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");

        // because we throw an exception if the grammar var set is empty, it cannot be that both subformulae are null, so here both must be != null:
        return new ForAll(X, new ForAll(Y, new Conjunction(subwordsLengthOne(CFG), subwordsGreaterOne(CFG))));
    }

    /**
     * sub-formula: CYK Table entries where sub-word has length 1 ie. V =>* w iff V -> sigma
     * */
    public FOLFormula subwordsLengthOne(ContextFreeGrammar cfg){

        Set<String> variables = cfg.getVariables();
        Map<String, Set<List<String>>> rules = cfg.getRules();
        String grammarName = cfg.getName();


        Variable X = new Variable("X");
        Variable Y = new Variable("Y");

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        List<String> vars = new ArrayList<>(variables);

        ArrayList<FOLFormula> conjunctionList = new ArrayList<>();

        //a set of grammar variables which have terminal rules (map directly to a letter)
        Set<String> varWithTerminalRule = new HashSet<>();

        for (String var : vars) {
            // Get the rules associated with the current variable
            Set<List<String>> varRules = rules.get(var);

            // Check if any rule has length 1 and is part of the alphabet
            for (List<String> rule : varRules) {
                if (rule.size() == 1 && cfg.getAlphabet().contains(rule.get(0))) {
                    varWithTerminalRule.add(var);
                    break;  // No need to check further if a rule is found
                }
            }
        }


        //a set of grammar vars having non terminal rules
        Set<String> varWithoutTerminalRule = new HashSet<>(variables);
        varWithoutTerminalRule.removeAll(varWithTerminalRule);

        //conversion of set to indexable data structure
        List<String> varsWithTerminalRule = new ArrayList<>(varWithTerminalRule);
        List<String> varsWOTerminalRule = new ArrayList<>(varWithoutTerminalRule);

        for (String var : varsWithTerminalRule){

            Tableau varTableau = new Tableau(X, Y);
            varTableau.setAssociatedGrammarName(grammarName);
            varTableau.setAssociatedVariable(var);

            // a list containing all letters which can be generated from var
            List<String> terminalsFromVar = new ArrayList<>();
            for (List<String> varRule : rules.get(var)){
                if (varRule.size() == 1 && cfg.getAlphabet().contains(varRule.get(0))) {
                    terminalsFromVar.add(varRule.get(0));
                }
            }

            ArrayList<FOLFormula> disjunctionList = new ArrayList<>();

            for (String terminal : terminalsFromVar){

                LetterAtPos letterFromTerminalProductionsOfVar = new LetterAtPos(X);
                letterFromTerminalProductionsOfVar.setAssociatedLetter(terminal);

                disjunctionList.add(letterFromTerminalProductionsOfVar);
            }

            conjunctionList.add(new Equivalence(varTableau, new Disjunction(disjunctionList)));

            // in preparation for the next loop iterating over terminal production rules of a new grammar var
            terminalsFromVar.clear();

        }

        ArrayList<FOLFormula> conjOverNonTerminalVars = new ArrayList<>();

        for (String var : varsWOTerminalRule){

            Tableau varTableau = new Tableau(X, Y);
            varTableau.setAssociatedGrammarName(grammarName);
            varTableau.setAssociatedVariable(var);

            conjOverNonTerminalVars.add(new Equivalence(varTableau, new Bottom()));
        }

        conjunctionList.addAll(conjOverNonTerminalVars);

        return new Implication(new Equals(X, Y), new Conjunction(conjunctionList));
    }


    /**
     * sub-formula: CYK Table entries where sub-word has length > 1 ie. V =>* w iff V -> AB
     * */
    public FOLFormula subwordsGreaterOne(ContextFreeGrammar cfg){

        Set<String> variables = cfg.getVariables();
        Map<String, Set<List<String>>> rules = cfg.getRules();
        String grammarName = cfg.getName();

        if (variables.isEmpty()) throw new IllegalStateException("CFG variables is empty!");
        if (rules.isEmpty()) throw new IllegalStateException("CFG rules is empty!");

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");
        Variable K = new Variable("K");

        List<String> vars = new ArrayList<>(variables);

        Set<String> varWithNonTerminalRules = new HashSet<>();

        for (String var : vars) {
            // Get the rules associated with the current variable
            Set<List<String>> varRules = rules.get(var);

            // Check if any rule has length > 1
            for (List<String> rule : varRules) {
                if (rule.size() > 1) {
                    varWithNonTerminalRules.add(var);
                    break;  // No need to check further if a rule is found
                }
            }
        }

        Set<String> varsWONonTerminalRules = new HashSet<>(variables);
        varsWONonTerminalRules.removeAll(varWithNonTerminalRules);

        List<String> varsWithNonTerminalRules = new ArrayList<>(varWithNonTerminalRules);
        List<String> varsWOnonTerminalRules = new ArrayList<>(varsWONonTerminalRules);


        // the position k: X <= K < Y
        LEQ xLEQk = new LEQ(X, K);
        Conjunction kSMALERy = new Conjunction(new LEQ(K, Y), new Negation(new Equals(K, Y)));

        // there exists a position k+1 which is exactly one position greater than k (there does not exist another position in between k and k+1)
        Variable KPLUSONE = new Variable("KPLUSONE");
        Variable INBETWEEN = new Variable("INBETWEEN");

        // K < KPLUSONE :=  K <= KPLUSONE  &  K != KPLUSONE
        Conjunction kSMALERkplusone = new Conjunction(new LEQ(K, KPLUSONE), new Negation(new Equals(K, KPLUSONE)));

        // INBETWEEN <= KPLUSONE  &  INBETWEEN != KPLUSONE
        Conjunction inbetweenSMALERkplusone = new Conjunction(new LEQ(INBETWEEN, KPLUSONE), new Negation(new Equals(INBETWEEN, KPLUSONE)));

        // INBETWEEN != K
        Negation inbetweenNOTEQUALk = new Negation(new Equals(INBETWEEN, K));

        // NOT INBETWEEN < K
        Negation nOTinbetweenSMALLERk = new Negation(new Conjunction(new LEQ(INBETWEEN, K), new Negation(new Equals(INBETWEEN, K))));

        Exists eXISTSinbetween = new Exists(INBETWEEN, new Conjunction(inbetweenSMALERkplusone, inbetweenNOTEQUALk, nOTinbetweenSMALLERk));


        // conjunction over all grammar vars with non-terminal production rules
        ArrayList<FOLFormula> conjunctionList = new ArrayList<>();

        for (String var : varsWithNonTerminalRules) {

            Tableau xyTableau = new Tableau(X, Y);
            xyTableau.setAssociatedVariable(var);
            xyTableau.setAssociatedGrammarName(grammarName);


            Set<List<String>> nonTerminalProductionsFromVar = new HashSet<>();

            for (List<String> varRule : rules.get(var)){
                if (varRule.size() > 1) {
                    nonTerminalProductionsFromVar.add(varRule);
                }
            }

                ArrayList<FOLFormula> disjunctionList = new ArrayList<>();

                for (List<String> rule : nonTerminalProductionsFromVar) {

                    Tableau xkTableau = new Tableau(X, K);
                    xkTableau.setAssociatedGrammarName(grammarName);
                    xkTableau.setAssociatedVariable(rule.get(0));

                    Tableau kplusoneyTableau = new Tableau(KPLUSONE, Y);
                    kplusoneyTableau.setAssociatedVariable(rule.get(1));
                    kplusoneyTableau.setAssociatedGrammarName(grammarName);

                    // the first var from the non-term rule produces Tableau(X, K) and the second var from the rule produces Tableau(KPLUSONE, Y)
                    disjunctionList.add(new Conjunction(xkTableau, new Exists(KPLUSONE, new Conjunction(kSMALERkplusone, new Negation(eXISTSinbetween), kplusoneyTableau))));
                }

                Disjunction disjOverNonTerminalRules = new Disjunction(disjunctionList);

                // there exists a position k:
                Exists existsK = new Exists(K, new Conjunction(xLEQk, kSMALERy, disjOverNonTerminalRules));

                conjunctionList.add(new Equivalence(xyTableau, existsK));

                nonTerminalProductionsFromVar.clear();
            }

        ArrayList<FOLFormula> conjOverNonTermVars = new ArrayList<>();

        for (String var : varsWOnonTerminalRules){

            Tableau varsWOTableau = new Tableau(X, Y);
            varsWOTableau.setAssociatedVariable(var);
            varsWOTableau.setAssociatedGrammarName(grammarName);

            conjOverNonTermVars.add(new Equivalence(varsWOTableau, new Bottom()));

        }

        conjunctionList.addAll(conjOverNonTermVars);

        // position X < position Y
        Conjunction xSMALLERy = new Conjunction(new LEQ(X, Y), new Negation(new Equals(X, Y)));

        return new Implication(xSMALLERy, new Conjunction(conjunctionList));
    }


    /**
     * sub-formula: w is generated by C1 iff it is not generated by C2
     * */
    public FOLFormula encodingGrammarInequivalence(ContextFreeGrammar C1, ContextFreeGrammar C2){

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");
        Variable K = new Variable("K");
        Variable L = new Variable("L");

        // there does NOT exist a position K which is smaller than X
        Negation notExistKsmallerX = new Negation(new Exists(K, new Conjunction(new LEQ(K, X), new Negation(new LEQ(X, K)))));

        // there does NOT exist a position L which is greater than Y
        Negation notExistLgreaterY = new Negation(new Exists(L, new Conjunction(new LEQ(Y, L), new Negation(new LEQ(L, Y)))));

        return new Exists(X, new Exists(Y, new Conjunction(notExistKsmallerX, notExistLgreaterY, new Equivalence(wordIsGenerated(C1.getStartVariables(), C1.getName()), new Negation(wordIsGenerated(C2.getStartVariables(), C2.getName()))))));
    }


    /**
     * sub-formula: w is generated by CFG
     * ie. there exist position X, Y where X is the smallest and Y the greatest position in w,
     * and an S in CFG: S =>* w[X, Y]
     * */
    public FOLFormula wordIsGenerated(Set<String> cfgOneStartVariables, String cfgOneName){

        Variable X = new Variable("X");
        Variable Y = new Variable("Y");

        if (cfgOneStartVariables.isEmpty()) throw new IllegalStateException("CFG has no start variables!");

        List<String> startVars = new ArrayList<>(cfgOneStartVariables);

        ArrayList<FOLFormula> disjunctionList  = new ArrayList<>();

        // at least one Tableau entry from a Start var generates the subword w[X, Y]
        for (String start : startVars){

            Tableau sgenwTableau = new Tableau(X, Y);
            sgenwTableau.setAssociatedGrammarName(cfgOneName);
            sgenwTableau.setAssociatedVariable(start);

            disjunctionList.add(sgenwTableau);
        }

        return new Disjunction(disjunctionList);
    }

}
