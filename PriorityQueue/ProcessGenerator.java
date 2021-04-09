import java.util.Random;

public class ProcessGenerator {
	double prob;
	Random random = new Random();

	public ProcessGenerator(double prob) {
		this.prob = prob;
	}

	public Process getNewProcess(int currentTime, int maxTimeToFin, int maxPLvl) {
		Process process = new Process(random.nextInt(maxPLvl) + 1, random.nextInt(maxTimeToFin) + 1, currentTime);
		return process;
	}

	public boolean query() {
		if (random.nextDouble() < prob) {
			return true;
		}
		return false;
	}
}
