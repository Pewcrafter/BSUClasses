import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Michael Stoneman
 *
 * A fully functional double-linked list with iterator and list iterator functionality.
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNodeDLL<T> head, tail;
	private int size;
	private int modCount;

	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
		if (isEmpty()) {
			head = nodeToAdd;
			tail = head;
		} else {
			head.setPrev(nodeToAdd);
			nodeToAdd.setNext(head);
			head = nodeToAdd;
		}
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
		if (isEmpty()) {
			head = nodeToAdd;
			tail = head;
		} else {
			tail.setNext(nodeToAdd);
			LinearNodeDLL<T> temp = tail;
			tail = nodeToAdd;
			nodeToAdd.setPrev(temp);
		}
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
		if (isEmpty()) {
			head = nodeToAdd;
			tail = head;
		} else {
			tail.setNext(nodeToAdd);
			LinearNodeDLL<T> temp = tail;
			tail = nodeToAdd;
			nodeToAdd.setPrev(temp);
		}
		size++;
		modCount++;
	}

	@Override
	public void addAfter(T element, T target) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
		LinearNodeDLL<T> current = head;
		boolean found = false;
		while (current != null && !found) {
			if (target.equals(current.getElement())) {
				found = true;
			} else {
				current = current.getNext();
			}
		}
		if (!found) {
			throw new NoSuchElementException();
		}
		if (current.equals(tail)) {
			current.setNext(nodeToAdd);
			nodeToAdd.setPrev(current);
			tail = nodeToAdd;
		} else {
			current.getNext().setPrev(nodeToAdd);
			nodeToAdd.setNext(current.getNext());
			current.setNext(nodeToAdd);
			nodeToAdd.setPrev(current);
		}
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
		LinearNodeDLL<T> current = head;
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int count = 1;
		while (count < index) {
			current = current.getNext();
			count++;
		}
		// empty list
		if (isEmpty()) {
			head = nodeToAdd;
			tail = head;
			size++;
			modCount++;
		}
		// index 0
		else if (index == 0) {
			current.setPrev(nodeToAdd);
			nodeToAdd.setNext(current);
			head = nodeToAdd;
			size++;
			modCount++;
		}
		// add to rear
		else if (index == size) {
			current.setNext(nodeToAdd);
			nodeToAdd.setPrev(current);
			tail = nodeToAdd;
			size++;
			modCount++;
		}
		// anywhere else
		else {
			current.getNext().setPrev(nodeToAdd);
			nodeToAdd.setNext(current.getNext());
			current.setNext(nodeToAdd);
			nodeToAdd.setPrev(current);
			size++;
			modCount++;
		}
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T value = head.getElement();
		// 1-element to empty
		if (size == 1) {
			head = tail = null;
		}
		// otherwise
		else {
			head = head.getNext();
			head.setPrev(null);
		}
		size--;
		modCount++;
		return value;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T value = tail.getElement();
		// 1-element to empty
		if (size == 1) {
			head = tail = null;
		}
		// otherwise
		else {
			tail = tail.getPrev();
			tail.setNext(null);
		}
		size--;
		modCount++;
		return value;
	}

	@Override
	public T remove(T element) {
		LinearNodeDLL<T> current = head;
		boolean found = false;
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		for (int i = 0; i < size; i++) {
			if (element.equals(current.getElement())) {
				found = true;
			}
			if (!found) {
				current = current.getNext();
			}
		}
		if (!found) {
			throw new NoSuchElementException();
		}
		// only node
		if (size == 1) {
			head = tail = null;
			size--;
			modCount++;
		}
		// last element
		else if (current == tail) {
			tail = tail.getPrev();
			current.getPrev().setNext(null);
			size--;
			modCount++;
		}
		// first element
		else if (current == head) {
			head = current.getNext();
			head.setPrev(null);
			size--;
			modCount++;
		}
		// anywhere else
		else {
			current.getNext().setPrev(current.getPrev());
			current.getPrev().setNext(current.getNext());
			size--;
			modCount++;
		}
		return element;
	}

	@Override
	public T remove(int index) {
		LinearNodeDLL<T> current = head;
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int count = 0;
		while (count < index) {
			current = current.getNext();
			count++;
		}
		T value = current.getElement();
		// only node
		if (size == 1) {
			head = tail = null;
			size--;
			modCount++;
		}
		// first node
		else if (index == 0) {
			head = head.getNext();
			head.setPrev(null);
			size--;
			modCount++;
		}
		// last node
		else if (current == tail) {
			tail = tail.getPrev();
			tail.setNext(null);
			size--;
			modCount++;
		}
		// anywhere else
		else {
			current.getNext().setPrev(current.getPrev());
			current.getPrev().setNext(current.getNext());
			size--;
			modCount++;
		}
		return value;
	}

	@Override
	public void set(int index, T element) {
		LinearNodeDLL<T> current = head;
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int count = 0;
		while (count < index) {
			current = current.getNext();
			count++;
		}
		current.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
		LinearNodeDLL<T> current = head;
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int count = 0;
		while (count < index) {
			current = current.getNext();
			count++;
		}
		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		int index = -1;
		boolean found = false;
		LinearNodeDLL<T> current = head;
		int count = 0;
		while (count < size && !found) {
			if (current.getElement().equals(element)) {
				found = true;
				index = count;
			} else {
				current = current.getNext();
				count++;
			}
		}
		return index;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		boolean found = false;
		LinearNodeDLL<T> current = head;
		int count = 0;
		while (count < size && !found) {
			if (current.getElement().equals(target)) {
				found = true;
			} else {
				current = current.getNext();
				count++;
			}
		}
		return found;
	}

	@Override
	public boolean isEmpty() {
		boolean empty = false;
		if (size == 0) {
			empty = true;
		}
		return empty;
	}

	@Override
	public int size() {
		return size;
	}

	public String toString() {
		String retStr;
		retStr = "[";
		LinearNodeDLL<T> current = head;
		if (isEmpty()) {
			retStr = "[]";
		} else
			while (current != tail) {
				retStr += current.getElement() + ", ";
				current = current.getNext();
			}
		if (current == tail && !isEmpty()) {
			retStr += current.getElement() + "]";
		}
		return retStr;
	}

	// List Iterator Jazz
	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

	private class DLLIterator implements ListIterator<T> {
		private int index;
		// nextOrPrev is 0 after a call to next, 1 after a call to previous
		private int nextOrPrev = 0;
		private T lastReturned;
		private LinearNodeDLL<T> recentNode = null;
		private LinearNodeDLL<T> nextNode;
		private int iterModCount;

		public DLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			index = 0;
		}

		public DLLIterator(int startingIndex) {
			if (startingIndex > size || startingIndex < 0) {
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;
			for (int i = 0; i < startingIndex; i++) {
				nextNode = nextNode.getNext();
			}
			iterModCount = modCount;
			index = startingIndex;
		}

		public void add(T element) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			LinearNodeDLL<T> nodeToAdd = new LinearNodeDLL<T>(element);
			if (size == 0) {
				head = nodeToAdd;
				tail = head;
				index++;
				size++;
			} else if (index == 0) {
				nodeToAdd.setNext(head);
				head.setPrev(nodeToAdd);
				head = nodeToAdd;
				index++;
				size++;
			} else if (index == size) {
				tail.setNext(nodeToAdd);
				nodeToAdd.setPrev(tail);
				tail = nodeToAdd;
				index++;
				size++;
			} else {
				nextNode.getPrev().setNext(nodeToAdd);
				nodeToAdd.setNext(nextNode);
				nodeToAdd.setPrev(nextNode.getPrev());
				nextNode.setPrev(nodeToAdd);
				index++;
				size++;
			}
			nextOrPrev = -1;
			iterModCount++;
			modCount++;
		}

		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			boolean hasPrev = false;
			if (index > 0) {
				hasPrev = true;
			}
			return hasPrev;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			boolean hasNext = false;
			if (nextNode != null) {
				hasNext = true;
			}
			return hasNext;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			recentNode = nextNode;
			nextNode = nextNode.getNext();
			lastReturned = recentNode.getElement();
			nextOrPrev = 0;
			index++;
			return lastReturned;
		}

		public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return index;
		}

		public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return index - 1;
		}

		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (index == 1) {
				lastReturned = head.getElement();
				recentNode = head;
				nextNode = head;
				index--;
			} else if (index == size) {
				recentNode = tail;
				lastReturned = recentNode.getElement();
				nextNode = recentNode;
				index--;
			} else {
				recentNode = nextNode.getPrev();
				lastReturned = recentNode.getElement();
				nextNode = recentNode;
				index--;
			}
			nextOrPrev = 1;
			return lastReturned;
		}

		public void set(T element) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (size == 0 || recentNode == null) {
				throw new IllegalStateException();
			}
			recentNode.setElement(element);
			iterModCount++;
			modCount++;
		}

		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (recentNode == null) {
				throw new IllegalStateException();
			}
			// only node
			if (size == 1) {
				head = tail = null;
				if (nextOrPrev == 0) {
					index--;
				}
				recentNode = null;
			}
			// first node
			else if (recentNode.equals(head)) {
				head = head.getNext();
				if (nextOrPrev == 0) {
					index--;
				}
				recentNode = null;
			}
			// last node
			else if (recentNode.equals(tail)) {
				tail = tail.getPrev();
				tail.setNext(null);
				if (nextOrPrev == 0) {
					index--;
				}
				recentNode = null;
			}
			// somewhere else
			else {
				recentNode.getPrev().setNext(recentNode.getNext());
				recentNode.getNext().setPrev(recentNode.getPrev());
				recentNode = null;
				if (nextOrPrev == 0) {
					index--;
				}
			}
			lastReturned = null;
			size--;
			modCount++;
			iterModCount++;
		}
	}

}
