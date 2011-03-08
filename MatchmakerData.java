import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class MatchmakerData {
	
	private List<Game> games = new Vector<Game>();
	
	/**
	 * Fetch a game session that has open slots for joining.
	 * We must mark the slot as taken before we release the 
	 * monitor in order to mantain consistency.
	 * 
	 * @return {@link Game} Session to join 
	 */
	public Game getGame(Location newClient){
		
		System.out.println("Adding " + newClient);
		/**
		 * Search for games in progress with available slots
		 */
		for (Iterator<Game> i = games.iterator( ); i.hasNext( ); ){
			
			Game game = i.next();
			if (!game.isFull()){
				System.out.println("Matchmaker Data: client joining a game");
				game.add(newClient);
				return game;
			}
		}
		
		/**
		 * If there are no available games, create a new game session
		 */
		return newGame(newClient);
		
	}
	
	/**
	 * Remove the client specified by {@link Location} clientLocation from current Game sessions
	 *  
	 */
	public void cleanup(Location clientLocation){
		
		/**
		 * Search for games in progress that contain the client
		 */
		for (Iterator<Game> i = games.iterator( ); i.hasNext( ); ){
			Game game = i.next();
			if (game.clients.contains(clientLocation)){
				System.out.println("Matchmaker Data: client disconnecting from a game");
				game.remove(clientLocation);
			}
		}
	}
	private Game newGame(Location newClient)
	{
		System.out.println("Matchmaker Data: Creating new game session");
		Game game = new Game(newClient);
		games.add(game);
		return game;
	}
}

