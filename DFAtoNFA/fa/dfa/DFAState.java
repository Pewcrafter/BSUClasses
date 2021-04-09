package fa.dfa;
import fa.State;

/** Class that represents a DFAState within a DFA. Stores a name and whether the state is a final state or not.
 * 
 * @author Michael Stoneman & Joe Gibson
 *
 */
public class DFAState extends State {
	private Boolean fin = false; // F?
	private String name = "placeholder"; // label, obviously
	
	/**
	 * @return whether its final
	 */
	public Boolean isFin() {
		return fin;
	}
	
	/** Gets the name of the DFAState
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/** Sets whether state is final
	 * @param x
	 */
	public void setFin(Boolean x) {
		fin = x;
	}
	
	/** Sets the name to String s
	 * @param s
	 */
	public void setName(String s) {
		name = s;
	}
	
	/** Takes in a String to name the constructed DFAState
	 * @param title
	 */
	public DFAState(String title) {
		name = title;
		fin = false;
	}
	/** Returns the name of the DFAState
	 * @return name
	 */
	public String toString() {
		return name;
	}
}