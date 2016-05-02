import java.util.Arrays;

public class State {
    boolean active;
	int[] clock;
	int[] recievedMsgs;
	int[] sentMsgs;
	int sentCount;
	
	public State(boolean active, int[] clock, int[] recievedMsgs, int[] sentMsgs,int count) {
		super();
		this.active = active;
		this.clock = clock;
		this.recievedMsgs = recievedMsgs;
		this.sentMsgs = sentMsgs;
		this.sentCount = count;
	}
	

	@Override
	public String toString() {
		return "State [active=" + active + ", sentCount=" + sentCount + ", clock=" + Arrays.toString(clock)
				+ ", recievedMsgs=" + Arrays.toString(recievedMsgs) + ", sentMsgs=" + Arrays.toString(sentMsgs) + "]";
	}
	
	
	
	}
