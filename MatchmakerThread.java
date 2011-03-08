import java.net.*;
import java.io.*;

public class MatchmakerThread extends Thread {
	
	private static MatchmakerData Games = new MatchmakerData();
	private Socket socket = null;
	
	public MatchmakerThread(Socket socket) {
		super("Matchmaker Thread");
		this.socket = socket;
		System.out.println("Matchmaker Thread: Connection Accepted");
	}

	public void run() {

		try {
			
			/**
			 * Establish communication channels
			 */
			ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
			
			boolean disconnect = false;
			MatchPacket inPacket;
			
			while(!disconnect){
				
				/**
				 * Get a Packet
				 */
				inPacket = (MatchPacket) fromClient.readObject();

				/**
				 * Process Packet and Reply
				 */
				MatchPacket outPacket = new MatchPacket();
				
				switch(inPacket.type){
				
				/* The client wants to find a game */
				case MatchPacket.MATCH_REQUEST:
					outPacket.type = MatchPacket.MATCH_REPLY;
					outPacket.toJoin = Games.getGame(inPacket.clientLocation);
					toClient.writeObject(outPacket);
					break;
					
				case  MatchPacket.MATCH_CLEANUP:
					Games.cleanup(inPacket.clientLocation);
					break;
					
				case  MatchPacket.MATCH_DISCONNECT: 
					disconnect = true;
					break;
					
				default:
					outPacket.type = MatchPacket.MATCH_ERROR;
					toClient.writeObject(outPacket);
				}
			}
			
			/**
			 * Close communication channels
			 */
			fromClient.close();
			toClient.close();
			socket.close();
			
			System.out.println("Matchmaker Thread: Connection Terminated");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}


