package fa.nfa;

import java.util.HashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.HashSet;

import fa.State;

public class NFAState extends State{
	
	private HashMap<Character, Set<NFAState>> transitions; //delta
	private boolean isFinal;//remembers its type
	private String label; //name
	
	/**
	 * Default constructor
	 * @param name the state name
	 */
	public NFAState(String label){
		initDefault(label);
		isFinal = false;
	}
	
	/**
	 * Overlaoded constructor that sets the state type
	 * @param name the state name
	 * @param isFinal the type of state: true - final, false - nonfinal.
	 */
	public NFAState(String label, boolean isFinal){
		initDefault(label);
		this.isFinal = isFinal;
	}
	
	private void initDefault(String label){
		this.label = label;
		transitions = new HashMap<Character, Set<NFAState>>();
	}
	
	/**
	 * Accessor for the state type
	 * @return true if final and false otherwise
	 */
	public boolean isFinal(){
		return isFinal;
	}
	
	public String getName() {
		return label;
	}

	/**
	 * Add the transition from <code> this </code> object
	 * @param onSymb the alphabet symbol
	 * @param toState to NFA state
	 */
	public void addTransition(char onSymb, NFAState toState){ 
		Set<NFAState> test = transitions.get(onSymb);
		if (test == null) {
			test = new HashSet<NFAState>();
			test.add(toState);
		} else {
			test.add(toState);
		}
		transitions.put(onSymb, test);
	}
	
	/**
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the new state 
	 */
	public Set<NFAState> getTo(char symb){
		Set<NFAState> ret = transitions.get(symb);
		if(ret == null){
			 System.out.println("WARNING: NFAState.getTo(char symb) returns FAILURE on " + symb + " from " + this.name);
			 Set<NFAState> empty = new HashSet<NFAState>();
			 return empty;
			}
		return transitions.get(symb);
	}
	
	@Override
	public String toString() {
		return label;
	}
}