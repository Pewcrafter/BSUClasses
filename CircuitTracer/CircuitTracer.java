import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

/**
 * Search for shortest paths between start and end points on a circuit board as
 * read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to a
 * GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 * @author mstone
 */
public class CircuitTracer {
	private CircuitBoard board;
	private Storage<TraceState> stateStore;
	private ArrayList<TraceState> bestPaths;

	/**
	 * launch the program
	 * 
	 * @param args
	 *            three required arguments: first arg: -s for stack or -q for queue
	 *            second arg: -c for console output or -g for GUI output third arg:
	 *            input file name
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); // create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.println(
				"The correct format is: java CircuitTracer [-q (for queue storage) or -s (for stack storage)] [-c (for console output) or -g (for GUI output)] [filename]");
	}

	/**
	 * Set up the CircuitBoard and all other components based on command line
	 * arguments.
	 * 
	 * @param args
	 *            command line arguments passed through from main()
	 * @throws FileNotFoundException
	 */
	private CircuitTracer(String[] args) throws FileNotFoundException {
		String storeType = null;
		String output = null;
		bestPaths = new ArrayList<TraceState>();
		// Parses args for the correct commands
		if (!args[0].trim().equals("-q") && !args[0].trim().equals("-s")) {
			printUsage();
			System.exit(1);
		} else {
			storeType = args[0].trim();
		}
		if (!args[1].trim().equals("-c")) {
			printUsage();
			System.exit(1);
		} else if (args[1].trim().equals("-g")) {
			System.out.println("A GUI output is currently not supported but will be implemeneted in a future patch");
			printUsage();
			System.exit(1);
		} else {
			output = args[1].trim();
		}
		String filename = args[2].trim();
		// initializes stateStore to either a stack or a queue
		if (storeType.equals("-q")) {
			stateStore = stateStore.getQueueInstance();
		} else {
			stateStore = stateStore.getStackInstance();
		}
		board = new CircuitBoard(filename);
		int currentCol = (int) board.getStartingPoint().getX();
		int currentRow = (int) board.getStartingPoint().getY();
		// Following 4 "if" statements create the beginning paths around the starting
		// point
		if (board.isOpen(currentRow - 1, currentCol)) {
			stateStore.store(new TraceState(board, currentRow - 1, currentCol));
		}
		if (board.isOpen(currentRow, currentCol + 1)) {
			stateStore.store(new TraceState(board, currentRow, currentCol + 1));
		}
		if (board.isOpen(currentRow + 1, currentCol)) {
			stateStore.store(new TraceState(board, currentRow + 1, currentCol));
		}
		if (board.isOpen(currentRow, currentCol - 1)) {
			stateStore.store(new TraceState(board, currentRow, currentCol - 1));
		}
		// While loop generates every possible TraceState from the current TraceState
		// and adds them to stateStore
		while (!stateStore.isEmpty()) {
			TraceState currentState = stateStore.retrieve();
			currentCol = currentState.getRow();
			currentRow = currentState.getCol();
			if (currentState.isComplete()) {
				if (bestPaths.isEmpty()) {
					bestPaths.add(currentState);
				} else if (currentState.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(currentState);
				} else if (currentState.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(currentState);
				}
			} else {
				if (currentState.getBoard().isOpen(currentRow - 1, currentCol)) {
					stateStore.store(new TraceState(currentState, currentRow - 1, currentCol));
				}
				if (currentState.getBoard().isOpen(currentRow, currentCol + 1)) {
					stateStore.store(new TraceState(currentState, currentRow, currentCol + 1));
				}
				if (currentState.getBoard().isOpen(currentRow + 1, currentCol)) {
					stateStore.store(new TraceState(currentState, currentRow + 1, currentCol));
				}
				if (currentState.getBoard().isOpen(currentRow, currentCol - 1)) {
					stateStore.store(new TraceState(currentState, currentRow, currentCol - 1));
				}
			}
		}
		// Outputs to the console all of the best paths found
		if (output.equals("-c")) {
			String retStr = "";
			if (bestPaths.isEmpty())
			{
				
			}
			else {
			for (TraceState paths : bestPaths) {
				retStr += paths.getBoard().toString() + '\n';
			}
			retStr = retStr.substring(0, retStr.length() - 1);
			System.out.println(retStr);
			}
		}
	}

} // class CircuitTracer
