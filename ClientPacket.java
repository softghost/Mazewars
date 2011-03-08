import java.io.Serializable;

/**
 * Definition of the protocol to communicate with a Matchmaking server.
 * 
 * @author Andres Rodriguez
 *
 */
public class ClientPacket implements Serializable{
	/**
	 * Default UID 
	 */
	private static final long serialVersionUID = 1L;

	/* define communication constants */
	public static final int NULL   			= 000;
	public static final int QUIT 			= 100;
	public static final int FORWARD			= 101;
	public static final int BACK			= 102;
	public static final int LEFT			= 103;
	public static final int RIGHT			= 104;
	public static final int FIRE			= 105;
	public static final int CONNECT			= 106;
	public static final int CONNECT_REPLY	= 107;
	public static final int SYNC			= 108;
	public static final int ACK				= 109;
	
	public ClientPacket(){
	}
	
	public ClientPacket(ClientPacket p){
		
		action = p.action;
		clientID = p.clientID;
		
		if (p.vclock != null)
			vclock = new VectorClock(p.vclock);
		if (p.name != null)
			name = p.name;
		if (p.myLocation != null)
			myLocation = p.myLocation;
		if (p.initialPoint != null)
			initialPoint= p.initialPoint;
		if (p.initialOrientation != null)
			initialOrientation = p.initialOrientation;
		
		count = p.count;
	}
	/*Packet structure defined here */
	
	/* Header */
	public int action = NULL;
	public int clientID; 
	public VectorClock vclock;
	public String name;
	public Location myLocation;
	public Point initialPoint;
	public Direction initialOrientation;
	public int count = 0;
	
	public String toString(){
		String s = new String();
		s += "Action:" + action + "\n";
		
		if (vclock != null)
			s += "vClock:" + vclock + "\n";
		
		if (name != null)
			s += "Name:" + name + "\n";
			
		return s;
	}
	
}



