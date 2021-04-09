/**
 * Manages and implements an NFA to then change into a DFA if needed. 
 * @author Joe Gibson and Michael Stoneman
 * @date 11-8-2020
 */
package fa.nfa;


import java.util.Set;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Iterator;

import fa.State;
import fa.dfa.DFA;
import fa.nfa.NFAState;


public class NFA implements NFAInterface {

	private NFAState startState;
	private Set<NFAState> states;
	private Set<Character> alphy;
	private Set<NFAState> closedStates = new HashSet<NFAState>();
	private Set<Set<NFAState>> processed = new HashSet<Set<NFAState>>();
	
	/**
	 * Created an NFA
	 */
	public NFA() {
		states = new LinkedHashSet<NFAState>();
		alphy = new LinkedHashSet<Character>();
	}
	/**
	 * Adds a new start state
	 * @param the name of the start state
	 */
	@Override
	public void addStartState(String name) {
		NFAState toAdd = checkIfExists(name);
		if (toAdd == null) {
			toAdd = new NFAState(name);
			addState(toAdd);
		}
		startState = toAdd;
	}
	/**
	 * Adds a state to the NFA
	 * @param String name of the NFA
	 */
	@Override
	public void addState(String name) {
		NFAState toAdd = checkIfExists(name);
		if (toAdd == null) {
			toAdd = new NFAState(name);
			addState(toAdd);
		}
	}
	/**
	 * Adds a state with a given NFA state
	 * @param NFAState the desired NFA state
	 */
	private void addState(NFAState s){
		s.addTransition('e', s);
		states.add(s);
	}
	/**
	 * Adds a final state 
	 * @param String adds a final state with the given name
	 */
	@Override
	public void addFinalState(String name) {
		NFAState toAdd = checkIfExists(name);
		if (toAdd == null) {
			toAdd = new NFAState(name, true);
			addState(toAdd);
		}
	}
	/**
	 * Adds a transition
	 * @param String fromState dictates the initial state of the transition
	 * @param char onSymb dictates what symbol is being used to transition
	 * @param String toState dictates the receiving state of the transition
	 */
	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if(from == null){
			System.err.println("ERROR: No NFA state exists with name " + fromState);
			System.exit(2);
		} else if (to == null){
			System.err.println("ERROR: No NFA state exists with name " + toState);
			System.exit(2);
		}
		from.addTransition(onSymb, to);
		if (!alphy.contains(onSymb) && onSymb != 'e') {
			alphy.add(onSymb);
		}
	}
	/**
	 * checks to see if an NFAstate exists
	 * @param String Name of the NFAstate
	 * @return the NFAState that was being checked
	 */
	private NFAState checkIfExists(String name) {
		NFAState ret = null;
		for(NFAState s : states){
			if(s.getName().equals(name)){
				ret = s;
				break;
			}
		}
		return ret;
	}
	/**
	 * returns the set of states
	 * @return Set the set of states
	 */
	@Override
	public Set<? extends State> getStates() {
		return states;
	}
	/**
	 * returns the final states
	 * @return Set the final states
	 */
	@Override
	public Set<? extends State> getFinalStates() {
		Set<NFAState> temp = new LinkedHashSet<NFAState>();
		for (NFAState s : states) {
			if (s.isFinal()) {
				temp.add(s);
			}
		}
		return temp;
	}
	/**
	 * gets the start state
	 * @return State returns the start state
	 */
	@Override
	public State getStartState() {
		return startState;
	}
	/**
	 * returns the alphabet
	 * @return Set alphy 
	 */
	@Override
	public Set<Character> getABC() {
		return alphy;
	}
	/**
	 * creates a DFA from the NFA
	 * @return DFA the resultant DFA
	 */
	@Override
	public DFA getDFA() { 
		closedStates.clear();
		processed.clear();
		DFA dfa = new DFA();
		String newStateName = "[";
		Set<Set<NFAState>> queue = new LinkedHashSet<Set<NFAState>>();
		Set<NFAState> fromStates = eClosure(startState);
		for (NFAState s : fromStates) {
			newStateName += s.getName() + ", ";
		}
		if(newStateName.length() != 1)
		{
			newStateName = newStateName.substring(0, newStateName.length()-2);
		}
		newStateName += "]";
		dfa.addStartState(newStateName);
		queue.add(fromStates);
		
		while (!queue.isEmpty())
		{
			
			fromStates = queue.iterator().next();
			processed.add(fromStates);
			queue.remove(fromStates);
			String fromStateName = "[";
			for(NFAState q : fromStates) { 
				fromStateName += q.getName() + ", ";
			}	
			if(fromStateName.length() != 1) {
				fromStateName = fromStateName.substring(0, fromStateName.length()-2);}
			fromStateName += "]";
			for(Character z : alphy) //For each character in the alphabet
			{
				boolean isFinal = false;
				Set<NFAState> toStates = new HashSet<NFAState>();
				newStateName = "[";
				for(NFAState q : fromStates) { //Add each reachable state from character z
					toStates.addAll(q.getTo(z));
					
					for (NFAState s : toStates) { //Add each state reachable from epsilon after traversing with symbol z
						closedStates.clear();
						toStates.addAll(eClosure(s));
					}
				}
				if (!fromStates.isEmpty()) {
				}
				for (NFAState s : toStates) {
					if (s.isFinal()) {
						isFinal = true;
					}
					newStateName += s.getName() + ", ";
				}
				if (!toStates.isEmpty()) {
					newStateName = newStateName.substring(0, newStateName.length()-2);
					}
				newStateName += "]";
				if (isFinal)
				{
					dfa.addFinalState(newStateName);
					if(!processed.contains(toStates))
						queue.add(toStates);
				} else {
					dfa.addState(newStateName);
					if(!processed.contains(toStates))
						queue.add(toStates);
				}
				dfa.addTransition(fromStateName, z, newStateName);
			}
		}
		
		return dfa;
	}
	/**
	 * Gets to a state from an input symbol and NFAState
	 * @param NFAState the from state
	 * @param char the symbol of transition
	 */
	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTo(onSymb);
	}
	/**
	 * Manages states for DFA from NFAstates
	 * @param NFAState an NFAState
	 */
	@Override
	public Set<NFAState> eClosure(NFAState s) {
		Set<NFAState> closedSelf = s.getTo('e');
		Set<NFAState> reachableClosed = new HashSet<NFAState>();
		closedStates.add(s);
		reachableClosed.addAll(closedSelf);
		for (NFAState q : closedSelf) {
			if (closedStates.contains(q)) {
				//nothing happens, already called eClosure on q
			} else {
			closedStates.add(q); 
			reachableClosed.addAll(eClosure(q));
			}
		}
		return reachableClosed;
	}
	/**
	 * Returns a string representation of the NFA
	 * @return String a string representation of NFA
	 */
	@Override
	public String toString() {
		String s = "";
		s += "Q = { ";
		for (NFAState q : states) {
			s += q.getName() + ", ";
		}
		s = s.substring(0, s.length()-2) + " }/n";
		s += "Sigma = { ";
		for (Character n : alphy) {
			s += n + ", ";
		}
		s = s.substring(0, s.length()-2) + " }/n";
		s += "delta = \n"; 
		return s;
	}
}