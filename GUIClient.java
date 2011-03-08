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

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * An implementation of {@link LocalClient} that is controlled by the keyboard
 * of the computer on which the game is being run.  
 * @author Geoffrey Washburn &lt;<a href="mailto:geoffw@cis.upenn.edu">geoffw@cis.upenn.edu</a>&gt;
 * @version $Id: GUIClient.java 343 2004-01-24 03:43:45Z geoffw $
 */

public class GUIClient extends LocalClient implements KeyListener {

		public static Point point = new Point(0,0);
		public static Direction direction = new Direction(0);
		public static boolean needSync= true;
		private int localID;

		/**
         * Create a GUI controlled {@link LocalClient}.  
         */
        public GUIClient(String name) {
                super(name);
                localID = Mazewar.clientID;
        }
        
        /**
         * Handle a key press.
         * @param e The {@link KeyEvent} that occurred.
         */
        public void keyPressed(KeyEvent e) {
        		
        		Mazewar.vClock.increment();
        		
                // If the user pressed Q, invoke the cleanup code and quit. 
                if((e.getKeyChar() == 'q') || (e.getKeyChar() == 'Q')) {
                        Mazewar.broadcast.quit();
                		Mazewar.quit();
                // Up-arrow moves forward.
                } else if(e.getKeyCode() == KeyEvent.VK_UP) {
                		Mazewar.broadcast.forward();
                        forward();
                // Down-arrow moves backward.
                } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                		Mazewar.broadcast.backup();
                        backup();
                // Left-arrow turns left.
                } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                		Mazewar.broadcast.left();
                        turnLeft();
                // Right-arrow turns right.
                } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                		Mazewar.broadcast.right();
                        turnRight();
                // Spacebar fires.
                } else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                		Mazewar.broadcast.fire();
                		Mazewar.vClock.increment();
                		Mazewar.broadcast.sync();
                        fire();
                }
                
        		point = Mazewar.maze.getClientPoint(this);
        		direction = Mazewar.maze.getClientOrientation(this);
        		
        		if  (needSync){
        			Mazewar.vClock.increment();
        			Mazewar.broadcast.sync();
        			needSync = false;
        		}
        }
        
        /**
         * Handle a key release. Not needed by {@link GUIClient}.
         * @param e The {@link KeyEvent} that occurred.
         */
        public void keyReleased(KeyEvent e) {
        }
        
        /**
         * Handle a key being typed. Not needed by {@link GUIClient}.
         * @param e The {@link KeyEvent} that occurred.
         */
        public void keyTyped(KeyEvent e) {
        }

		public void run(){
    			
    			/*boolean disconnect = false;
    			ClientPacket inPacket = new ClientPacket();
    			
    			while(!disconnect){
    				
    				/**
    				 * Get a Packet
    				 *//*
    				inPacket = getPacket();
    				
    				if (inPacket == null)
    					continue;
    				
    				System.out.println("Received:" +inPacket);
    	        	
    				/**
    				 * Process Packet
    				 *//*
    	        	switch (inPacket.action){

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
    			
    			System.out.println("GUIClient: Done");*/
    	}
		
		private ClientPacket getPacket() {
			
			while(true){
				System.out.println("Waiting " + localID);
				try {
					synchronized(Mazewar.pQueue){
						Mazewar.pQueue.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Woken " + localID);
				ClientPacket p = Mazewar.pQueue.peek();
				System.out.println("I'm awake!!!!!");
				if (p != null && p.clientID == localID){
					System.out.println("POP");
					Mazewar.pQueue.pop();
					return p;
				}
			}
		}

}
