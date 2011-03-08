import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProducerThread extends Thread implements Runnable{
	
	private Socket socket = null;
	RemoteClientThread client = null;
	
	public ProducerThread(Socket socket) {
		super("Producer Thread");
		this.socket = socket;
		client = new RemoteClientThread();
		client.start();
		System.out.println("Producer: Ready");
	}
	
	public void run(){
		
		try {
			/**
			 * Establish communication channels
			 */
			ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
			
			boolean disconnect = false;
			ClientPacket inPacket=null;
			
			while(!disconnect){
				
				/**
				 * Get a Packet
				 */
				try{
					inPacket = new ClientPacket((ClientPacket) fromClient.readObject());
				}catch(ClassCastException e){
					System.out.println("Cast error");
				}
				
				if(inPacket!=null)
					System.out.println("Received:" +inPacket);
				
				/**
				 * Update our clock
				 */
	        	if (inPacket.vclock != null){
	        		//System.out.println("Received:"+ inPacket.vclock);
	        		Mazewar.vClock.update(inPacket.vclock);
	        	}
	        	
	        	/**
	        	 * Check for disconnect
	        	 */
	        	if (inPacket.action == ClientPacket.QUIT)
	        		disconnect = true;
	        	
	        	/**
	        	 * check for connect
	        	 */
	        	if (inPacket.action == ClientPacket.CONNECT){
	      
	        		client.client.connect(inPacket);
	        		continue;
	        	}
	        	
	        	/**
	        	 * check for connect
	        	 */
	        	if (inPacket.action == ClientPacket.CONNECT_REPLY){
	        		
	        		client.client.connectReply(inPacket);
	        		continue;
	        	}
	        	
	        	/**
	        	 * Put the packet in the queue
	        	 */
	        	Mazewar.pQueue.add(inPacket);
	        	
	        	/**
	        	 * Flush excess packets from the queue
	        	 */
	        	while(Mazewar.pQueue.morePackets()){
	        		synchronized(Mazewar.pQueue){
						Mazewar.pQueue.notifyAll();
	        		}
	        	}
			}
			
			/**
			 * Close communication channels
			 */
			fromClient.close();
			socket.close();
			
			System.out.println("Producer: Connection Terminated");
		
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
		}
	}
}
	
	

