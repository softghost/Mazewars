import java.net.*;
import java.io.*;


public class Matchmaker {
    
    public static void main(String[] args) throws IOException {
    	
        ServerSocket serverSocket = null;
        boolean listening = true;
       
        try {
        	if(/*args.length*/ 1 == 1) {
        		serverSocket = new ServerSocket(9000);
        	} else {
        		System.err.println("ERROR: Invalid arguments!");
        		System.exit(-1);
        	}
        } catch (IOException e) {
            System.err.println("ERROR: Could not listen on port!");
            System.exit(-1);
        }
        
        System.out.printf("Matchmaker: Listening on port %s\n", 9000);

        while (listening) {
        	new MatchmakerThread(serverSocket.accept()).start();
        }

        serverSocket.close();
    }
}
