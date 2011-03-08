import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Information required for a client to join a game.
 * 
 * The first location specified in the clients list 
 * is considered the lock manager 
 * 
 * @author Andres Rodriguez
 *
 */
public class Game implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final int PLAYERS_MAX = 4;
	
	public List<Location> clients = new Vector<Location>();
	public int seed;
	
	public Game(Location _Location){
		clients.add(_Location);
		seed = 42;
	}

	public Game() {
		
	}

	public boolean isFull(){
		return (clients.size() == PLAYERS_MAX);
	}
	
	public boolean isEmpty(){
		return (clients.size() == 0);
	}
	
	public boolean contains(Location l){
		return clients.contains(l);
	}
	
	public int size(){
		return clients.size();
	}
	
	public void add(Location newClient){
		clients.add(newClient);
	}

	public void remove(Location clientLocation) {
		clients.remove(clientLocation);
	}
	
	@Override
	public String toString() {
		return "Game [clients=" + clients.toString() + ", seed=" + seed + "]";
	}
	
}



