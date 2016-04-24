
public class FailureEvent {
    public FailureEvent(int failId, int checkpoint, int previous) {
		// TODO Auto-generated constructor stub
    	pid = failId;
    	failAfter = checkpoint;
    	precedingEventId = previous;
	}
	public int pid;
    public int failAfter;
    public int precedingEventId;
    
}
