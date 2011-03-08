import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/*
Copyright (C) 2004 Geoffrey Alan Washburn
   
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
   
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
   
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
*/
  
/**
 * A skeleton for those {@link Client}s that correspond to clients on other computers.
 * @author Geoffrey Washburn &lt;<a href="mailto:geoffw@cis.upenn.edu">geoffw@cis.upenn.edu</a>&gt;
 * @version $Id: RemoteClient.java 342 2004-01-23 21:35:52Z geoffw $
 */

public class RemoteClient extends Client{
		
		public int localID = -1;
		
        /**
         * Create a remotely controlled {@link Client}.
         * @param name Name of this {@link RemoteClient}.
         */
        public RemoteClient() {
        		super("Connecting");
        		System.out.println("Remote Handler Thread: Ready");
        }
 
    	public void run(){
    			
    			boolean disconnect = false;
    			ClientPacket inPacket = new ClientPacket();
    			
    			while(!disconnect){
    				
    				/**
    				 * Get a Packet
    				 */
    				inPacket = getPacket();
    				
    				if (inPacket == null)
    					continue;
    				
    				System.out.println("Received:" +inPacket);
    				
    				/**
    				 * Update our clock
    				 */
    	        	if (inPacket.vclock != null){
    	        		
    	        		Mazewar.vClock.update(inPacket.vclock);
    	        	}
    	        	
    				/**
    				 * Process Packet
    				 */
    	        	switch (inPacket.action){
    	        	
    	        	case ClientPacket.CONNECT:
    	        		
    	        	case ClientPacket.CONNECT_REPLY:

    	        	case ClientPacket.FORWARD:
    	        		forward();
    	        		break;
    	        	case ClientPacket.BACK:
    	        		backup();
    	        		break;
    	        	case ClientPacket.LEFT:
    	        		turnLeft();
    	        		break;
    	        	case ClientPacket.RIGHT:
    	                turnRight();
    	                break;
    	        	case ClientPacket.FIRE:
    	        		fire();
    	        		break;
    	        	case ClientPacket.SYNC:
    	        		Mazewar.maze.moveClient(this, inPacket.initialPoint, inPacket.initialOrientation);
    	        		break;
    	        	case ClientPacket.QUIT:
    	        		disconnect = true;
    	        		maze.removeClient(this);
    	        		break;
    	        	default:
    	        		break;
    	        	}
    	    
    			}
    			
    			System.out.println("Remote Client: Done");
    		
    	}

		private ClientPacket getPacket() {
			
			while(true){
				//System.out.println("Waiting " + localID);
				try {
					synchronized(Mazewar.pQueue){
						Mazewar.pQueue.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//System.out.println("Woken " + localID);
				ClientPacket p = Mazewar.pQueue.peek();
				
				if (p != null && p.clientID == localID){
					//System.out.println("POP");
					Mazewar.pQueue.pop();
					return p;
				}
			}
		}

		public void setlocalID(int clientID) {
			localID = clientID;
		}
		
		public void connect(ClientPacket inPacket){
    		setName(inPacket.name);
    		localID = inPacket.clientID;
    		Mazewar.maze.addClient(this, inPacket.initialPoint, inPacket.initialOrientation);
    		Mazewar.broadcast.add(inPacket.myLocation);
		}
		public void connectReply(ClientPacket inPacket){
			setName(inPacket.name);
			localID = inPacket.clientID;
			Mazewar.maze.addClient(this, inPacket.initialPoint, inPacket.initialOrientation);
		}
}
