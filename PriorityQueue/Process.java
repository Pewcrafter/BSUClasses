
public class Process implements Comparable<Process> {
	int pLvl;
	int timeToFin;
	int arrivalTime;
	int timeIgnored;

	public Process(int pLvl, int timeToFin, int arrivalTime) {
		this.pLvl = pLvl;
		this.timeToFin = timeToFin;
		this.arrivalTime = arrivalTime;
	}

	@Override
	public int compareTo(Process arg0) {
		if (this.pLvl > arg0.pLvl) {
			return 1;
		}
		if (this.pLvl < arg0.pLvl) {
			return -1;
		}
		if (this.arrivalTime < arg0.arrivalTime) {
			return 1;
		} else {
			return -1;
		}
	}
	public void upPriority()
	{
		pLvl++;
	}
	public void reduceTimeRemaining()
	{
		this.timeToFin--;
	}
	public boolean finish()
	{
		if (timeToFin == 0)
		{
			return true;
		}
		return false;
	}
	public void resetTimeNotProcessed()
	{
		timeIgnored = 0;
	}
	public int getTimeNotProcessed()
	{
		return timeIgnored;
	}
	public void wasIgnored()
	{
		this.timeIgnored++;
	}

	public int getPriority() {
		
		return pLvl;
	}

	public int getTimeRemaining() {
		
		return timeToFin;
	}

	public int getArrivalTime() {
		
		return arrivalTime;
	}
	public String toString()
	{
		String string = "Priority level: " + pLvl + "\nTime remaining until completion: " + timeToFin +"\nArrival time: " + arrivalTime;
		return string;
	}
	
}
