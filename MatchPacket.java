import java.io.Serializable;

/**
 * Definition of the protocol to communicate with a Matchmaking server.
 * 
 * @author Andres Rodriguez
 *
 */
public class MatchPacket implements Serializable{

	/**
	 * Default UID 
	 */
	private static final long serialVersionUID = 1L;

	/* define communication constants */
	public static final int MATCH_NULL	    = 000;
	public static final int MATCH_REPLY	    = 100;
	public static final int MATCH_ERROR		= 101;
	public static final int MATCH_REQUEST	= 102;
	public static final int MATCH_DISCONNECT= 103;
	public static final int MATCH_CLEANUP	= 104;
	
	
	/*Packet structure defined here */
	
	/* Header */
	public int type = MATCH_NULL;
	
	/* Data */
	public Location clientLocation;
	public Game toJoin;
	
}

