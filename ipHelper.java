import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ipHelper {
	
    public static  String getMyIP() {
    	String myIP = null;
    	try {
			myIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return myIP;
    }
    
    
}

class Location implements Serializable {
	
	/**
	 * Default UID
	 */
	private static final long serialVersionUID = 1L;
	
	public String  host;
	public int port;
	
	/* constructor */
	public Location(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/* printable output */
	public String toString() {
		return " HOST: " + host + " PORT: " + port; 
	}
	
	public boolean equals(Object o){
		Location target = (Location) o;
		if (target.host.equals(this.host) && target.port == this.port)
			return true;
		return false;
	}
}
