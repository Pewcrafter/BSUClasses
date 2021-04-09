//The general design of this class implementation was inspired by Amaninder Singh from geeksforgreeks.org. This format and logic comes from the webpage https://www.geeksforgeeks.org/max-heap-in-java/ 
// and was altered to fit this specific process implementation.
public class MaxHeap {
	private Process[] heap;
	private int size;
	private int maxSize;

	public MaxHeap(int maxSize) {
		this.maxSize = maxSize;
		this.size = 0;
		heap = new Process[this.maxSize + 1];
		heap[0] = new Process(5, 5, -1);
	}

	// returns the index of the parent of the given index
	private Process parent(int index) {
		return heap[(int) Math.floor(index / 2)];
	}

	// Below two functions return left and
	// right children.
	// 2k and 2k+1 respectively
	private int leftChild(int index) {
		return 2 * index;
	}

	private int rightChild(int index) {
		return 2 * index + 1;
	}
	private void swap(int findex, int sindex) {
		Process tmp;
		tmp = heap[findex];
		heap[findex] = heap[sindex];
		heap[sindex] = tmp;
	}

	public Process getProcess(int index) {
		return heap[index];
	}

	// A recursive function to max heapify the given
	// subtree. This function assumes that the left and
	// right subtrees are already heapified, we only need
	// to fix the root.
	public void maxHeapify(int index) {
		int left = leftChild(index);
		int right = rightChild(index);
		int largest = index;
		if (left <= size && heap[left].compareTo(heap[right]) == 1)
		{
			largest = left;
		}
		if (right <= size && heap[right].compareTo(heap[largest]) == 1)
		{
			largest = right;
		}
		if (largest != index)
		{
			swap(index, largest);
			maxHeapify(largest);
		}
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	// Inserts a new element to max heap
	public void insert(Process element) {
		heap[++size] = element;
		heapUp(size);
	}

	public void print() {
		for (int i = 1; i <= size / 2; i++) {
			System.out.print(
					" PARENT : " + heap[i] + " LEFT CHILD : " + heap[2 * i] + " RIGHT CHILD :" + heap[2 * i + 1]);
			System.out.println();
		}
	}

	// Remove an element from max heap
	public Process extractMax() {
		Process popped = heap[1];
		heap[1] = heap[size--];
		maxHeapify(1);
		heap[size + 1] = null;
		return popped;
	}

	public void heapUp(int index) {
		int current = index;
		while (heap[current].compareTo(parent(current)) == 1 && current > 1) {
			swap(current, (current / 2));
			current = (current / 2);
		}
	}
}
