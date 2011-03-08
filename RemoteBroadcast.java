import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This Class handles the clientside operations related to the Matchmaking server.
 * @author Andres Rodriguez
 *
 */
public class RemoteBroadcast {
	
	private String name = null;
	private List<Socket> socket = null;
	private List<ObjectOutputStream> toClients = null;
	private int myPort;
	
	/**
	 * Basic constructor
	 * @param _name - name of the local user
	 */
	public RemoteBroadcast(String _name, int port){
		socket = new Vector<Socket>();
		toClients = new Vector<ObjectOutputStream>();
		name = _name;
		myPort = port;
	}
	
	/**
	 * Establish a connection with remote client
	 * @param client: {@link Location} of the remote client.
	 */
	public void add(Location client){
        try {
        	Socket _socket = new Socket(client.host, client.port);
        	socket.add(_socket);
			ObjectOutputStream o =new ObjectOutputStream(_socket.getOutputStream());
			toClients.add(o);
			
			//Mazewar.numClients++;
			
			ClientPacket p = new ClientPacket();
			p.myLocation = new Location(ipHelper.getMyIP(), myPort);
			p.action = ClientPacket.CONNECT_REPLY;
			p.name = name;
			p.initialPoint = GUIClient.point;
			p.initialOrientation = GUIClient.direction;
			p.clientID = Mazewar.clientID;
			p.vclock = Mazewar.vClock;
			o.writeObject(new ClientPacket(p));
			

		} catch (UnknownHostException e) {
			System.err.println("ERROR: Don't know where to connect!!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("ERROR: Couldn't get I/O for the connection.");
			System.exit(1);
		}
		
	}
	
	/**
	 * Establish a connection with remote client
	 * @param client: {@link Location} of the remote client.
	 */
	public void add(Game game){
        try {
        	
        	Mazewar.clientID = game.clients.indexOf(Mazewar.myLocation);
        	//System.out.println(Mazewar.clientID);
        	Mazewar.numClients = 1;//game.clients.size();
        	game.remove(Mazewar.myLocation);
        	
        	Iterator<Location> i = game.clients.iterator();
        	while(i.hasNext()){
        		Location client = i.next();
        		Socket _socket = new Socket(client.host, client.port);
        		socket.add(_socket);
        		toClients.add(new ObjectOutputStream(_socket.getOutputStream()));
        	}
		} catch (UnknownHostException e) {
			System.err.println("ERROR: Don't know where to connect!!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("ERROR: Couldn't get I/O for the connection.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Broadcast a packet
	 */
	public void broadcast(ClientPacket p){
		
		p.clientID = Mazewar.clientID;
		System.out.println("Broadcasting:" + p);
		try {
			Iterator<ObjectOutputStream> i = toClients.iterator();
			
			while(i.hasNext()) {
				ObjectOutputStream o = i.next();
				o.writeObject(new ClientPacket(p));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Disconnect cleanup sequence
	 */
	public void disconnect(){
		
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.QUIT;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
		
		try {
			
			Iterator<ObjectOutputStream> i = toClients.iterator();
			while(i.hasNext()){
				ObjectOutputStream o = i.next();
				o.close();
			}
			
			Iterator<Socket> j = socket.iterator();
			while(j.hasNext()){
				Socket s = j.next();
				s.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Broadcast common actions
	 */
	public void connect(){
		ClientPacket p = new ClientPacket();
		p.myLocation = new Location(ipHelper.getMyIP(), myPort);
		p.action = ClientPacket.CONNECT;
		p.name = name;
		p.initialPoint = GUIClient.point;
		p.initialOrientation = GUIClient.direction;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void forward(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.FORWARD;
		p.vclock = Mazewar.vClock;
		//System.out.println("Broadcasting:"+p.vclock);
		broadcast(new ClientPacket(p));
	}
	
	public void backup(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.BACK;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void left(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.LEFT;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void right(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.RIGHT;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void fire(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.FIRE;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void quit(){
		disconnect();
	}
	
	public void sync(){
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.SYNC;
		p.initialPoint = GUIClient.point;
		p.initialOrientation = GUIClient.direction;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}

	public void sync(Point point, Direction d) {
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.SYNC;
		p.initialPoint = point;
		p.initialOrientation = d;
		p.vclock = Mazewar.vClock;
		broadcast(new ClientPacket(p));
	}
	
	public void ack(VectorClock vclk) {
		ClientPacket p = new ClientPacket();
		p.action = ClientPacket.ACK;
		p.vclock = new VectorClock(vclk);
		broadcast(new ClientPacket(p));
	}
}
