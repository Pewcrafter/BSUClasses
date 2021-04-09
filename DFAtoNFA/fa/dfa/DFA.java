package fa.dfa;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import fa.State;

/** Class which constructs a DFA and it's complement from a given input file. Stores the states, start state, final states, and transitions and offers functionality
 * to test input strings against the constructed DFA to check if the input is within the language of the DFA. 
 * 
 * @author Michael Stoneman & Joe Gibson
 *
 */
public class DFA implements DFAInterface {
	
	DFAState startState; // q0
	Set<Character> alphabet = new LinkedHashSet<Character>(); // E
	Set<DFAState> finStates = new LinkedHashSet<DFAState>(); // F
	Set<DFAState> states = new LinkedHashSet<DFAState>(); // Q
	Map<Pair<Character, DFAState>,DFAState> transitions = new HashMap<Pair<Character, DFAState>, DFAState>(); // delta
		
	public DFA()
	{
	}
	
	/** Adds a start state to the DFA with the given name
	 * @param name
	 */
	@Override
	public void addStartState(String name) {
		startState = new DFAState(name);
		Boolean exists = false;
		
		// Iterate through all current states within the DFA to check if the state to be added already exists. If it does, set startState to this state, otherwise
		// add a new state to the DFA and set startState to the newly created state
		for (DFAState x : states)
		{
			//If a state with this name already exists, set exists to true and set startState to the already existing state
			if (x.getName().compareTo(startState.getName()) == 0) {
				exists = true;
				startState = x;
			}
		}
		//If the state doesn't already exist, add it
		if (!exists) {
			states.add(startState);
		}
	}

	
	/** Adds a state to the DFA with the given name
	 * @param name
	 */
	@Override
	public void addState(String name) {
		DFAState toAdd = new DFAState(name);
		Boolean exists = false;
		
		//Checks through all current states to see if the state to add already exists. If it doesn't, then we add it to the DFA. 
		for (DFAState x : states)
		{
			//If the state already exists, we just move on since it's already added
			if (x.getName().compareTo(toAdd.getName()) == 0) {
				exists = true;
			}
		}
		//Add the state to the DFA since it didn't exist already
		if (!exists) {
			states.add(toAdd);
		}
	}

	/** Adds a final state to the DFA with the given name
	 * @param name
	 */
	@Override
	public void addFinalState(String name) {
		DFAState toAdd = new DFAState(name);
		toAdd.setFin(true);
		Boolean finExists = false;
		Boolean exists = false;
		
		//Checks through all current final states to see if the state to add already exists as a final state.
		for (DFAState x : finStates)
		{
			//The state to add is already within final states, so we just move on.
			if (x.getName().compareTo(toAdd.getName()) == 0) {
				finExists = true;
				x.setFin(true);
			}
		}
		//Checks all current states to see if the state to add already exists. If it does, we move on. 
		for (DFAState x : states)
		{
			if (x.getName().compareTo(toAdd.getName()) == 0) {
				exists = true;
			}
		}
		//Adds the state to finStates since it wasn't already there
		if (!finExists) {
			finStates.add(toAdd);
		}
		//Adds the state to states since it wasn't already there
		if (!exists) {
			states.add(toAdd);
		}
	}

	/** Adds a transition to the DFA from 'fromState' to 'toState' with the symbol 'onSymb'
	 * @param fromState, onSymb, toState
	 */
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		DFAState from = new DFAState("pholder");
		DFAState to = new DFAState("pholder");
		
		Iterator<DFAState> iter = states.iterator();
		//Iterates through our states to find the state with the name "&fromState" and the state with the name "&toState"
		while(iter.hasNext())
		{
			DFAState temp = iter.next();
			if (fromState.compareTo(temp.getName()) == 0) {
				from = temp;
			}
			if (toState.compareTo(temp.getName()) == 0) {
				to = temp;
			}
		}
		//Creates a new pair which stores which symbol was found while on the state 'from'
		Pair<Character, DFAState> key = new Pair(onSymb, from);
		transitions.put(key, to);
		//Generates our alphabet by gathering all symbols found within transitions
		if (!alphabet.contains(onSymb)) {
			alphabet.add(onSymb);
		}
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getStates()
	 */
	@Override
	public Set<? extends DFAState> getStates() {
		return states;
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getFinalStates()
	 */
	@Override
	public Set<? extends DFAState> getFinalStates() {
		return finStates;
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getStartState()
	 */
	@Override
	public State getStartState() {
		return startState;
	}

	/* (non-Javadoc)
	 * @see fa.FAInterface#getABC()
	 */
	@Override
	public Set<Character> getABC() {
		return alphabet;
	}

	/**  Generates the complement to the DFA and returns it
	 * @return comp
	 */
	@Override
	public DFA complement() {
		DFA comp = new DFA();
		
		//Deep copies all states within the DFA into the complement DFA
		for (DFAState x : states) {
			//If the current state in the DFA is the start state, add it as the start state to our complement DFA
			if (x.getName().compareTo(startState.getName()) == 0) {
				comp.addStartState(x.getName());
				//If the start state is not a final state, make it a final state in our complement. If the DFA's start state is a final state, the complements start
				// state defaults to not being a final state, so no code is needed.
				if (!startState.isFin()) {
					comp.startState.setFin(true);
				}
			}
			//If the current state in the DFA is not a final state, make it a final state in the complement DFA, otherwise just add it to the complement
			//as it defaults to not being a final state.
			if (!x.isFin()) {
				comp.addFinalState(x.getName());
			} else {
				comp.addState(x.getName());
			}
		}
		
		//Iterate through all the states in the DFA as well as all of the characters in our alphabet. Check each state against each character in the alphabet to 
		//determine if a transition from that state with that character exists. If it does exist, add that same transition to our complement DFA.
		for (DFAState x : states) {
			for (Character z : alphabet) {
				Pair<Character, DFAState> temp = new Pair(z,x);
				if (transitions.containsKey(temp)) {
					comp.addTransition(x.getName(), z, transitions.get(temp).getName());
				}
			}
		}	
		return comp;
	}

	@Override
    /** 
     * Determines whether an input string is valid per the DFA. 
     * @param The input String 
     * @return Returns boolean expressing if string was accepted by DFA.
     *  */
    public boolean accepts(String s) {
        boolean accepted;
        DFAState currState = startState;
        
        //If the start state is final, then we are currently sitting in an accepting position
        if (startState.isFin()) {
            accepted = true;
        }
        else {
            accepted = false;
        }
        //Iterate through each character and update whether or not we're in an accepting position, once all characters are read we simply return
        //if we're in an accepting position or not
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != 'e') {
                currState = getToState(currState, s.charAt(i));
                if (currState.isFin()) {
                    accepted = true;
                }
                else {
                accepted = false;
                }
            }
        }
        return accepted;
    }

	/** Applies the transition of the state "from" on the symbol "onSymb" and returns the DFAState which the transition leads
	 * @param from, onSymb
	 * @return DFAState transitions.get(temp)
	 */
	@Override
	public DFAState getToState(DFAState from, char onSymb) {
		Pair<Character, DFAState> temp = new Pair(onSymb, from);
		return transitions.get(temp);
	}
	
	/** 
	 * 	Returns a string in the format below where:
	 *  Q = all states
	 *  Sigma = alphabet
	 *  delta = transitions
	 *  q0 = start state
	 *  F = final states
	 *  
	 * Q = { a b }
	 * Sigma = { 0 1 }
	 * delta =
	 *  	0	1	
	 *	a	a	b	
	 *	b	a	b	
	 * q0 = a
	 * F = { b }
	 * 
	 * @return str
	 */
	public String toString() {
		String str = "";
		
		//Handles Q
		str = str.concat("Q = { ");
		Iterator<DFAState> stateIter = states.iterator();
		while(stateIter.hasNext()) {
			DFAState temp = stateIter.next();
			str = str.concat(temp.getName() + " ");
		}
		str = str.concat("}\n");
		
		//Handles Sigma
		str = str.concat("Sigma = { ");
		Iterator<Character> alphIter = alphabet.iterator();
		while(alphIter.hasNext()) {
			Character curr = alphIter.next();
			str = str.concat(curr + " ");
		}
		str = str.concat("}\n");
		
		//Handles Delta
			//Row 1
		str = str.concat("delta = \n");
			//Row 2
		str = str.concat(" \t");
		for (Character x : alphabet){
			str = str.concat(x + "\t");
		}
		str = str.concat("\n");
			//Row 3 -> end
		for (DFAState x : states) {
			str = str.concat(" " + x.getName() + "\t");
			for (Character z : alphabet)
			{
			str = str.concat(getToState(x,z).getName() + "\t");
			}
			str = str.concat("\n");
		}
		//Handles q0
		str = str.concat("q0 = " + startState.getName() + "\n");
		
		//Handles F
		str = str.concat("F = { ");
		for (DFAState x : finStates) {
			str = str.concat(x.getName() + " ");
		}
		str = str.concat("}\n");
		
		return str;
	}

}