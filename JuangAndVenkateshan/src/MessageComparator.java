import java.util.Comparator;

public class MessageComparator implements Comparator<Message>
{
    @Override
    public int compare(Message x, Message y)
    {
        
        if (x.getClock() < y.getClock() || (x.getClock() == y.getClock() && x.getPID() < y.getPID()))
        {
            return -1;
        }
        return 1;
        
    }
}