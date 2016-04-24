import java.util.Arrays;

public class State {
    boolean active;
	int maxNumber;
	int[] clock;
	int[] recievedMsgs;
	int[] sentMsgs;
	
	public State(boolean active, int maxNumber, int[] clock, int[] recievedMsgs, int[] sentMsgs) {
		super();
		this.active = active;
		this.maxNumber = maxNumber;
		this.clock = clock;
		this.recievedMsgs = recievedMsgs;
		this.sentMsgs = sentMsgs;
	}
	

	public State(boolean active, int maxNumber, int[] clock, int[] sentMsgs) {
		super();
		this.active = active;
		this.maxNumber = maxNumber;
		this.clock = clock;
		this.sentMsgs = sentMsgs;
	}
	
	


	public State( int[] recievedMsgs,boolean active, int maxNumber, int[] clock) {
		super();
		this.active = active;
		this.maxNumber = maxNumber;
		this.clock = clock;
		this.recievedMsgs = recievedMsgs;
	}


	@Override
	public String toString() {
		return "State [active=" + active + ", maxNumber=" + maxNumber + ", clock=" + Arrays.toString(clock)
				+ ", recievedMsgs=" + Arrays.toString(recievedMsgs) + ", sentMsgs=" + Arrays.toString(sentMsgs) + "]";
	}
	
	
	
	}
