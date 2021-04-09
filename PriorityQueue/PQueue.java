//The general design of this class implementation was inspired by Amaninder Singh from geeksforgreeks.org. This format and logic comes from the webpage https://www.geeksforgeeks.org/max-heap-in-java/ 
// and was altered to fit this specific process implementation.
public class PQueue {
	MaxHeap heap;

	public PQueue() {
		heap = new MaxHeap(200);
	}

	public void enPQueue(Process p) {
		heap.insert(p);
	}

	public boolean isEmpty() {
		return heap.isEmpty();
	}

	public Process dePQueue() {
		return heap.extractMax();
	}

	public void update(int timeToIncrementLevel, int maxLevel) {
		for (int i = 1; i <= heap.size(); i++) {
			heap.getProcess(i).wasIgnored();
			if (heap.getProcess(i).getTimeNotProcessed() >= timeToIncrementLevel) {
				if (heap.getProcess(i).getPriority() < maxLevel) {
					heap.getProcess(i).upPriority();
					heap.getProcess(i).resetTimeNotProcessed();
				}
			}
			heap.heapUp(i);
		}
	}
}
