import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This Class handles the clientside operations related to the Matchmaking server.
 * @author Andres Rodriguez
 *
 */
public class MatchmakerClient {
	private Socket socket = null;
	private Location myLocation = null;
	private ObjectOutputStream toServer = null;
	private ObjectInputStream fromServer = null;
	private Location matchmaker = null;
	
	
	/**
	 * Establish a connection with the Matchmaking server
	 * @param Matchmaker: {@link Location} of the Matchmaking service.
	 */
	public void connect(Location Matchmaker){
        try {
        	
        	/**
			 * Establish communication channels
			 */
        	socket = new Socket(Matchmaker.host, Matchmaker.port);
			toServer = new ObjectOutputStream(socket.getOutputStream());
			fromServer = new ObjectInputStream(socket.getInputStream());
			
			matchmaker = Matchmaker;

		} catch (UnknownHostException e) {
			System.err.println("ERROR: Don't know where to connect!!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("ERROR: Couldn't get I/O for the connection.");
			System.exit(1);
		}
	}
	
	/**
	 * Request a Game to join from the Matchmaker. 
	 * @param myLocation {@link Location} containing the clients information
	 * @return {@link Location} of the game session we are assigned to join/
	 */
	public Game request(Location _myLocation){
		
		/**
		 * Prepare our Packet and send it
		 */
		myLocation = _myLocation;
		MatchPacket outPacket = new MatchPacket();
		outPacket.type = MatchPacket.MATCH_REQUEST;		
		outPacket.clientLocation = myLocation;
		send(outPacket);
				
		/**
		 * Wait for a reply
		 */
		MatchPacket inPacket = new MatchPacket();
		inPacket = receive();
		
		switch (inPacket.type){
		case MatchPacket.MATCH_REPLY:
			return inPacket.toJoin;
		default:
			return null;
		}
	}
	
	/**
	 * Let the Matchmaker know we are disconnecting so cleanup can be performed
	 * @param Packet
	 */
	public void disconnect(){
		MatchPacket outPacket = new MatchPacket();
		outPacket.type = MatchPacket.MATCH_DISCONNECT;	
		outPacket.clientLocation = myLocation;
		send(outPacket);
	}
	
	/**
	 * Let the Matchmaker know we are disconnecting so cleanup can be performed
	 * @param Packet
	 */
	public void cleanup(){
		connect(matchmaker);
		MatchPacket outPacket = new MatchPacket();
		outPacket.type = MatchPacket.MATCH_CLEANUP;	
		outPacket.clientLocation = myLocation;
		send(outPacket);
		disconnect();
	}
	
	/**
	 * Send the packet
	 */
	private void send (MatchPacket Packet){
		try {
			toServer.writeObject(Packet);
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Receive a packet
	 */
	private MatchPacket receive(){
		
		MatchPacket inPacket = new MatchPacket();
		
		try {
			inPacket = (MatchPacket) fromServer.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return inPacket;
		
	}
}
