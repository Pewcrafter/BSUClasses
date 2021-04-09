/**
 * LinearNode represents a node in a linked list.
 *
 * @author Java Foundations, mvail
 * @version 4.0
 */
public class LinearNodeDLL<T> {
	private LinearNodeDLL<T> prev;
	private LinearNodeDLL<T> next;
	private T element;

	/**
  	 * Creates an empty node.
  	 */
	public LinearNodeDLL() {
		prev = null;
		next = null;
		element = null;
	}

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param elem
  	 *            the element to be stored within the new node
  	 */
	public LinearNodeDLL(T elem) {
		prev = null;
		next = null;
		element = elem;
	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public LinearNodeDLL<T> getNext() {
		return next;
	}

	/**
 	 * Returns the node previous to this one.
  	 *
  	 * @return the node that is previous to the current one
  	 */
	public LinearNodeDLL<T> getPrev() {
		return prev;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setNext(LinearNodeDLL<T> node) {
		next = node;
	}
	
	/**
 	 * Sets the node that is previous to this one.
 	 *
 	 * @param node
 	 *            the node to be set to be previous to the current one
 	 */
	public void setPrev(LinearNodeDLL<T> node) {
		prev = node;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	public T getElement() {
		return element;
	}

	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param elem
  	 *            the element to be stored in this node
  	 */
	public void setElement(T elem) {
		element = elem;
	}

	@Override
	public String toString() {
		return "Element: " + element.toString() + " Has next: " + (next != null);
	}
}
