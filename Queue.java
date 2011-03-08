
public class Queue{
	public final int MaxSize = 1024;
	public static int num = 0;
	public static boolean packetpop = false;
	
	public volatile ClientPacket[] queue = new ClientPacket[MaxSize];
	
	public synchronized void add(ClientPacket packet){

		if(packet.action == ClientPacket.ACK){
			for(int i=0;i<num;i++){
				// Find packet that ack corresponds to
				if(queue[i].vclock.equals(packet.vclock)){
					queue[i].count++;
					if(i==0 && queue[i].count==Mazewar.numClients){
						// Signal that packet at front of queue has all acks
						this.notifyAll();
						packetpop = true;
					}
					break;
					
				}
			}
		}
		else {
			insert(packet);
			num++;
		}
		
		if( queue[0] != null && queue[0].count==Mazewar.numClients/* && packetpop==false*/){
			synchronized (this) {
				this.notifyAll();
			}
			packetpop = true;
		}
		
		System.out.println(this.toString());
	}
	public synchronized ClientPacket peek(){
		return queue[0];
	}
	
	public synchronized void pop(){
		num--;
		for(int i=0;i<num;i++){
			queue[i]=queue[i+1];
		}
		
		if (num < 0){
			num = 0;
		}
		
		queue[num]=null;
		packetpop=false;

	}
	
	private synchronized void insert(ClientPacket packet){
		int i;
		//	Find postion in queue to insert packet
		for(i=0;i<MaxSize;i++){
			if(queue[i] == null || packet.vclock.lessThan(queue[i].vclock))
				break;
		}
		
		shiftRight(i);
		// Insert packet;
		if(queue[i]==null){
			queue[i]=new ClientPacket();
		}
		queue[i]=packet;
		Mazewar.broadcast.ack(packet.vclock);
	}
	
	private void shiftRight(int i) {
		queue[num+1] = new ClientPacket();
		for (int j = num; j >= i ; j--){
			queue[j+1]=queue[j];
		}
	}
	
	public String toString(){
		String s = new String();
		for (int i=0; i < MaxSize; i++){
			if (queue[i] == null)
				break;
			s+= "Packet " + i + ": \n" + queue[i] +  " ACKs: " + queue[i].count +"\n";
		}
		return s;
	}
	
	public void packetWait(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean morePackets(){
		if (queue[0] != null  /*queue[0].count==Mazewar.numClients/* && packetpop==false*/)
			return true;
		return false;
	}
}