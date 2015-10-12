import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;

public class Utility {
	
	public static HashMap<Integer, Socket> Nmap = new HashMap<Integer, Socket>();
	public static HashMap<Integer, Socket> Nodemap = new HashMap<Integer, Socket>();
	public static HashMap<Integer, Socket> Connection = new HashMap<Integer, Socket>();
	public static String[] tem;
	public static int num = 0;
	
	
	public static void NodeEntries(){
		
		try {
			int port,port2;
			String ipaddr;
			int node;
			String[] liner;
			String filename = "Configuration";

			FileReader inputFile = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;

			while ((line = bufferReader.readLine()) != null) {
				
				liner = line.split(" ");
				if(liner[0].contentEquals("n")){
				num = Integer.parseInt(liner[1]);
				RaymondsTree.tm = Integer.parseInt(liner[3]);
				RaymondsTree.cstimes = RaymondsTree.tm;
				node = 0;
				}else{
				node = Integer.parseInt(liner[0]);
				
				ipaddr = liner[1];
				port = Integer.parseInt(liner[2]);
				
					Socket socks = new Socket(ipaddr, port);
					Nodemap.put(node, socks);
					
					
			}
			}
			// Close the buffer reader
			bufferReader.close();
			inputFile.close();

			//SpanningTree.Checkforneighbor();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		
	}
	
public static void AppEntries(){
		
		try {
			int prt;
			String ipaddr;
			int node;
			String[] liner;
			String filename = "Configuration";

			FileReader inputFile = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;

			while ((line = bufferReader.readLine()) != null) {
				
				liner = line.split(" ");
				if(liner[0].contentEquals("n")){
				//num = Integer.parseInt(liner[1]);
				node = 0;
				}else{
				node = Integer.parseInt(liner[0]);
				
				ipaddr = liner[1];
				prt = Integer.parseInt(liner[3]);
				
					Socket soc = new Socket(ipaddr, prt);
					Connection.put(node, soc);
					
					
			}
			}
			// Close the buffer reader
			bufferReader.close();
			inputFile.close();

			//SpanningTree.Checkforneighbor();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		
	}
	
	public static void Getsocket(){

		try {
			int port;
			String ipaddr;
			int node;
			String[] liner;
			String filename = "Configuration";

			FileReader inputFile = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;

			while ((line = bufferReader.readLine()) != null) {
				
				liner = line.split(" ");
				if(liner[0].contentEquals("n")){
					
				}else{
				node = Integer.parseInt(liner[0]);
				for (int i = 0; i < SpanningTree.aList.size(); i++) {
					if(SpanningTree.aList.get(i) == node){
				ipaddr = liner[1];
				port = Integer.parseInt(liner[2]);
               	
					Socket sock = new Socket(ipaddr, port);
					
					Nmap.put(node, sock);

					}//if
				}//for
			}
			}
			// Close the buffer reader
			bufferReader.close();
			inputFile.close();

			//SpanningTree.Checkforneighbor();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}
	
	public static void uponCSExit(int serial){
		//On exiting the critical section if the FIFO queue is non-empty then
		Client.vector[serial - 1]++;
		System.out.print("Node"+Caller.serial+"	Vector	");
		printVector(Client.vector);
		System.out.println();
		
		//System.out.println("Inside uponCSExit\n");
				if(!RaymondsTree.RequestQ.isEmpty()){
					//System.out.println("/////Queue not empty////");
					int t = RaymondsTree.RequestQ.peek();
					if(Listen.TreeNode.contains(RaymondsTree.RequestQ.peek())){
						//System.out.println("///Req from Child///");
						int tok = RaymondsTree.RequestQ.peek();
						RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), serial);
						//System.out.println("1\n");
						//if the queue is still non-empty
						
						if(!RaymondsTree.RequestQ.isEmpty()){
							int toks = RaymondsTree.RequestQ.peek();
							//System.out.println("////If not empty after sending tok////");
							if(toks == Caller.serial){
								//System.out.println("///My own req///");
							//System.out.println("My own request");
								RaymondsTree.SendReq(Nodemap.get(tok), serial);
							//**RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
							}else{
								//System.out.println("///Not my own req///");
							RaymondsTree.SendReq(Nodemap.get(tok), serial);
							//System.out.println("2\n");
							}
						}else{
							//System.out.println("//Q empty after sending token to child//");
						}
					}else if(Listen.AckList.contains(RaymondsTree.RequestQ.peek())){
						int tok1 = RaymondsTree.RequestQ.peek();
						//System.out.println("////Parent is requesting////Token sent to "+RaymondsTree.RequestQ.peek());
						RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), serial);
						
						//if the queue is still non-empty
						if(!RaymondsTree.RequestQ.isEmpty()){
							int tok11 = RaymondsTree.RequestQ.peek();
							//System.out.println("////If not empty after sending tok////");
							if(tok11 == Caller.serial){
								//System.out.println("///1. My own req///");
							//System.out.println("My own request");
								RaymondsTree.SendReq(Nodemap.get(tok1), serial);
							//**RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
							}else{
								//System.out.println("///2. Not my own req///");
							RaymondsTree.SendReq(Nodemap.get(tok1), serial);
							//System.out.println("2\n");
							}
						}else{
							//System.out.println("//Q empty after sending token to parent//");
						}
						
						
					}else if(Caller.serial == t){
						//System.out.println("///**My req**///");
						//int tok2 = t;
						RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
						
						//if the queue is still non-empty
						if(!RaymondsTree.RequestQ.isEmpty()){
							//System.out.println("////If not empty after sending tok////");
							int tok2 = RaymondsTree.RequestQ.peek();
							if(tok2 == Caller.serial){
								//System.out.println("///3. My own req///");
							//System.out.println("My own request");
							RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
							}else{
								//System.out.println("///4. Not my own req///");
							RaymondsTree.SendReq(Nodemap.get(tok2), serial);
							//System.out.println("2\n");
							}
						}else{
							//System.out.println("//Q empty after sending token to myself//");
						}
						
					}else{
						//System.out.println("///no way///");
					}
					//Check for parent also needs to be implemented
					if(Listen.AckList.contains(RaymondsTree.RequestQ.peek())){
						//System.out.println("////Parent is requesting////");
						RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), serial);
					}else{
						//System.out.println("///inside else///");
					}
					
				}else{
					//System.out.println("////No req in queue///\n");
				}
	}
	
	public static void SendMsg(Socket sk,int sender){
		DataOutputStream dout;
		try {
			
	    dout = new DataOutputStream(sk.getOutputStream());
		dout.writeUTF("From node " + sender + " Positive "+Messge.ACK);
		//System.out.println("Sending Positive"+Messge.ACK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//In this method two vectors are combined ie the vector received along with token and current vector
		public static int[] CombineVector(String str){
			String[] m = str.split(",");
			
			int[] rcvVector = new int[Utility.num];
			for(int i = 0;i < Utility.num;i++){
				rcvVector[i] = Integer.parseInt(m[i]);
			}
			//Compare two vectors
			for(int i = 0;i < Utility.num;i++){
				if(rcvVector[i] > Client.vector[i]){
	              Client.vector[i] = rcvVector[i];				
				}
			}
			
			return Client.vector;
		}
	
	public static String ReceiveMsg(Socket sk) throws InterruptedException{
		
		String str = "";
		String[] temp1;
		try {
		DataInputStream din = new DataInputStream(sk.getInputStream());
		str = din.readUTF();
		temp1 = str.split(" ");
		tem = temp1;
		String rcvVector;
		
		if(str.contains("ACK")){
			
			
			Listen.TreeNode.add(Integer.parseInt(temp1[2]));
			
		
		}
		
		////////System.out.println("	>> Received msg: " + str);	
		
		
		if(str.contains("CS")){
			//System.out.println("Got msg");
			
			//System.out.println("Token Val "+Caller.Token);
			//System.out.println("CS Val "+RaymondsTree.inCS);
			if((Caller.Token == true) && (RaymondsTree.inCS == true)){
				
				if(!RaymondsTree.RequestQ.isEmpty()){
					Caller.Token = false;
					//System.out.println("Sending Token to queue element");
				    Caller.pointTo = RaymondsTree.RequestQ.peek();
					RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), Caller.serial);
				
			  }else{
				  //System.out.println("Its my own req\n"+Integer.parseInt(temp1[2]));
				  if(Caller.pointTo == Integer.parseInt(temp1[2])){
					  //System.out.println("poinTo Val "+Caller.pointTo);
					  RaymondsTree.SendCSReq(Connection.get(Caller.serial),Caller.serial);
				  }else{
					  Caller.Token = false;
				  //System.out.println("Sending Token to "+Integer.parseInt(temp1[2]));
				  Caller.pointTo = Integer.parseInt(temp1[2]);
				  //System.out.println("From Socket "+sk);
				  //System.out.println(Nodemap.get(sk));
				  RaymondsTree.SendToken(Nodemap.get(Integer.parseInt(temp1[2])), Caller.serial);
				  //System.out.println("Token sent");
				  }
			  }
			}else if(Caller.Token == true && RaymondsTree.inCS == false){
				//System.out.println("Request added");
				RaymondsTree.RequestQ.add(Integer.parseInt(temp1[2]));
			}else{
				if(RaymondsTree.RequestQ.isEmpty()){
					//System.out.println("Queue Empty");
					RaymondsTree.RequestQ.add(Integer.parseInt(temp1[2]));
					//System.out.println("My requestQ is "+RaymondsTree.RequestQ);
					//send req
					if(Listen.AckList.isEmpty()){
						//System.out.println("Sending request to "+Caller.pointTo);
						RaymondsTree.SendReq(Utility.Nodemap.get(Caller.pointTo), Caller.serial);
						//System.out.println("Request sent to "+Caller.pointTo);
						//System.out.println("Finally sent");
					}else{
						if(Caller.first == true){
					RaymondsTree.SendReq(Utility.Nodemap.get(Listen.AckList.get(0)), Caller.serial);
					//System.out.println("Sent the request to parent");
						}else{
							//Since token is being exchanged, we should send request to value in pointTo variable
							RaymondsTree.SendReq(Utility.Nodemap.get(Caller.pointTo), Caller.serial);
							//System.out.println("Request for token has been sent to "+Caller.pointTo);
						}
					}
					}else{
						//if the request queue is not empty
					//System.out.println("Adding to queue");
					//System.out.println(RaymondsTree.RequestQ);
					RaymondsTree.RequestQ.add(Integer.parseInt(temp1[2]));
				}
			}
		}
		
		if(str.contains("TOK")){
			//on receiving token
		     
			Client.vector = CombineVector(temp1[7]);
			Caller.Token = true;
			//System.out.println("Token received");
			/*
			if(!RaymondsTree.RequestQ.isEmpty()){
				Caller.pointTo = RaymondsTree.RequestQ.peek();
				if(Caller.pointTo == Caller.serial){
					RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
				}else{
				RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), Caller.serial);
				Caller.Token = false;
				System.out.println("Sent token to "+Caller.pointTo);
				}*/
				/*if(!RaymondsTree.RequestQ.isEmpty()){
					RaymondsTree.SendReq(Nodemap.get(Caller.pointTo), Caller.serial);
					}*/
			//}
			if(Listen.TreeNode.contains(RaymondsTree.RequestQ.peek())){
				Caller.pointTo = RaymondsTree.RequestQ.peek();
				RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), Caller.serial);
				Caller.Token = false;
				//System.out.println("Sent token to requesting child node");
				if(!RaymondsTree.RequestQ.isEmpty()){
					RaymondsTree.SendReq(Nodemap.get(Caller.pointTo), Caller.serial);
					
				}
			}else if(!RaymondsTree.RequestQ.isEmpty()){
				Caller.pointTo = RaymondsTree.RequestQ.peek();
				if(Caller.pointTo == Caller.serial){
					//System.out.println("********Came********");
					RaymondsTree.SendCSReq(Connection.get(RaymondsTree.RequestQ.poll()),Caller.serial);
				}else{
				RaymondsTree.SendToken(Nodemap.get(RaymondsTree.RequestQ.poll()), Caller.serial);
				Caller.Token = false;
				//System.out.println("Sent token to "+Caller.pointTo);
				}
				if(!RaymondsTree.RequestQ.isEmpty()){
					RaymondsTree.SendReq(Nodemap.get(Caller.pointTo), Caller.serial);
					}
			}
			else{
				//System.out.println("Executing Token");
				Caller.pointTo = Caller.serial;
				RaymondsTree.SendCSReq(Connection.get(Caller.serial),Caller.serial);
				
			}
		}
		
		if(str.contains("Exit")){
			//System.out.println("Received CSExit\n");
			uponCSExit(Caller.serial);
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(str);
	}

	public static void printHashMap() {
		HashMap<Integer, Socket> map = Nmap;
		System.out.println("* Printing hashmap objects *");
		System.out.println("Hashmap size - " + map.size());
		for (Map.Entry<Integer, Socket> entry : map.entrySet()) {
			System.out.println("============================= "
					+ entry.getKey() + " : " + entry.getValue());
		}
	}

	public static void printAckList() {
		System.out.println("* Printing Ack List *");
		for (Integer i : Listen.AckList) {
			System.out.println("Ack object : " + i);
		}
	}
	
	public static void printVector(int[] vector){
		for(int i = 0; i < num;i++){
			System.out.print(Client.vector[i]);
		}
	}

}
