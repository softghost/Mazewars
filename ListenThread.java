import java.io.IOException;
import java.net.ServerSocket;


public class ListenThread extends Thread implements Runnable{
	
	ServerSocket serverSocket = null;
	//private boolean GUIconnected = false;
	
	public ListenThread(int port) {
		super("Listen Thread");
		
        try {
        	serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("ERROR: Could not listen on port!");
            System.exit(-1);
        }
        
		System.out.println("Listen Thread: Ready");
	}
	
	public void run(){
		while(true){
			try {
				new ProducerThread(serverSocket.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
