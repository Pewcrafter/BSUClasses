import java.util.Comparator;
import java.util.ListIterator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator. As
 * written uses Quicksort algorithm.
 *
 * @author CS221
 */
public class Sort {
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. As
	 * configured, uses WrappedDLL. Must be changed if using your own
	 * IUDoubleLinkedList class.
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() {
		return new IUDoubleLinkedList<T>();
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface using
	 * compareTo() method defined by class of objects in list. DO NOT MODIFY THIS
	 * METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface
	 * @see IndexedUnsortedList
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) {
		quicksort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface using given
	 * Comparator. DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList
	 */
	public static <T> void sort(IndexedUnsortedList<T> list, Comparator<T> c) {
		quicksort(list, c);
	}

	/**
	 * Quicksort algorithm to sort objects in a list that implements the
	 * IndexedUnsortedList interface, using compareTo() method defined by class of
	 * objects in list. DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface
	 */
	private static <T extends Comparable<T>> void quicksort(IndexedUnsortedList<T> list) {
		if (list.size() > 1) {
			IndexedUnsortedList<T> lowList = newList();
			IndexedUnsortedList<T> highList = newList();
			ListIterator<T> listIter = list.listIterator();
			T pivot = list.first();
			listIter.next();
			listIter.remove();
			while (listIter.hasNext()) {
				T current = listIter.next();
				int result = current.compareTo(pivot);
				if (result == -1) {
					lowList.add(current);
					listIter.remove();
				} else {
					highList.add(current);
					listIter.remove();
				}
			}
			ListIterator<T> listIterLow = lowList.listIterator();
			ListIterator<T> listIterHigh = highList.listIterator();
			while (listIterLow.hasNext())
			{
				list.addToRear(listIterLow.next());
			}
			list.addToRear(pivot);
			while (listIterHigh.hasNext())
			{
				list.addToRear(listIterHigh.next());
			}
		}
	}

	/**
	 * Quicksort algorithm to sort objects in a list that implements the
	 * IndexedUnsortedList interface, using the given Comparator. DO NOT MODIFY THIS
	 * METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void quicksort(IndexedUnsortedList<T> list, Comparator<T> c) {
		if (list.size() > 1) {
			IndexedUnsortedList<T> lowList = newList();
			IndexedUnsortedList<T> highList = newList();
			ListIterator<T> listIter = list.listIterator();
			T pivot = list.first();
			listIter.next();
			listIter.remove();
			while (listIter.hasNext()) {
				T current = listIter.next();
				int result = c.compare(current, pivot);
				if (result == -1) {
					lowList.add(current);
					listIter.remove();
				} else {
					highList.add(current);
					listIter.remove();
				}
			}
			ListIterator<T> listIterLow = lowList.listIterator();
			ListIterator<T> listIterHigh = highList.listIterator();
			while (listIterLow.hasNext())
			{
				list.addToRear(listIterLow.next());
			}
			list.addToRear(pivot);
			while (listIterHigh.hasNext())
			{
				list.addToRear(listIterHigh.next());
			}
		}
	}
}