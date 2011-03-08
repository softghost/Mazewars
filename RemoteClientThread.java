
public class RemoteClientThread extends Thread implements Runnable{
	
	RemoteClient client = null;
	
	public RemoteClientThread() {
		super("Remote Client Thread");
		client = new RemoteClient();
		System.out.println("RemoteClientThread: Connected");
	}
	
	public void run(){
			client.run();
	}
}
	
	

