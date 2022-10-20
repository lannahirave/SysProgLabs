import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NFA {
    Set<Character> alphabet;

    Set<Integer> states;

    Integer initialState;

    Set<Integer> finalStates;

    Map<Integer, Map<Character, Set<Integer>>> transitions;

    private NFA() {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.initialState = 0;
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public static NFA createFromTxt(File file) throws FileNotFoundException {
        var nfa = new NFA();

        var scanner = new Scanner(file);

        var alphabetSize = scanner.nextInt();
        for (char c = 'a'; c <= 'z' && c < 'a' + alphabetSize; c++) {
            nfa.alphabet.add(c);
        }

        var statesCount = scanner.nextInt();
        for (int i = 0; i < statesCount; i++) {
            nfa.states.add(i);
        }
        nfa.initialState = scanner.nextInt();

        var finalStatesCount = scanner.nextInt();
        for (int i = 0; i < finalStatesCount; i++) {
            nfa.finalStates.add(scanner.nextInt());
        }

        for (var state : nfa.states) {
            nfa.transitions.put(state, new HashMap<>());
        }

        while (scanner.hasNext()) {
            var from = scanner.nextInt();
            var cond = scanner.next().charAt(0);
            var to = scanner.nextInt();

            var toStates = nfa.transitions.get(from)
                    .computeIfAbsent(cond, k -> new HashSet<>());
            toStates.add(to);
        }

        return nfa;
    }

    Set<Integer> processWord(String word) {
        var currStates = new HashSet<Integer>();
        currStates.add(initialState);
        for (var condition : word.toCharArray()) {
            var nextStates = new HashSet<Integer>();
            for (Integer from : currStates) {
                if (transitions.get(from).containsKey(condition)) {
                    nextStates.addAll(transitions.get(from).get(condition));
                }
            }
            currStates = new HashSet<>(nextStates);
        }
        return currStates;
    }
}
