public class GUIClientThread extends Thread implements Runnable{
	
	GUIClient client = null;
	
	public GUIClientThread(GUIClient client) {
		super("GUIClient Thread");
		this.client = client;
		System.out.println("GUIClientThread: Connected");
	}
	
	public void run(){
			client.run();
	}
}