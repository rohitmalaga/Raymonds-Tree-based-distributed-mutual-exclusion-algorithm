import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Application implements Runnable{

    public static String str,ipaddr;
	public static boolean inCS = true; 
	public static int p,max = 0;
	
	//This method is handles the execution of critical section
	public static void ExecuteCS(int serial)throws InterruptedException,IOException{
		if(max > 0){
			--max;
			System.out.println("max: "+max);
		inCS = false;
		//Client.vector[serial - 1]++;
		System.out.println("Node"+Caller.serial+"	CS-Enter");
		System.out.println("***********************************************************************************");
		//System.out.println("Vector Clock:");
		//Utility.printVector(Client.vector);
		System.out.println();
		for(int i=1 ; i<=3 ; i++)
		{
			//Client.vector[serial - 1]++;
			Thread.sleep(100);
		}
		
		System.out.println("Node"+Caller.serial+"	CS-Exit");
		//System.out.println("Vector Clock:");
		//Utility.printVector(Client.vector);
		System.out.println();
		inCS=true;
		}else{
			System.out.println("##########Reached Limit##########");
		}
		
		SendCSExit(serial);
		
	}
	
	public static void SendCSExit(int sender){
		try{
		Socket sock1 = new Socket(ipaddr, p);
		DataOutputStream dout;
  		
  			
  	    dout = new DataOutputStream(sock1.getOutputStream());
  		dout.writeUTF("From node " + sender +" "+Messge.Exit);
  		//System.out.println("CSExit Msg Sent\n");
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
	}
	

	@Override
	public void run() {
try {
	
	int port = 0;
	int node;
	String[] liner;
	String filename = "Configuration";
	FileReader inputFile = new FileReader(filename);
	BufferedReader bufferReader = new BufferedReader(inputFile);
	String line;
	
	while((line = bufferReader.readLine()) != null){
		
		liner = line.split(" ");
		if(liner[0].contentEquals("n")){
			max = Integer.parseInt(liner[3]);
			
		}else{
		node = Integer.parseInt(liner[0]);
		
		
		
		if(node == Caller.serial){
			ipaddr = liner[1];
			p = Integer.parseInt(liner[2]);
			port = Integer.parseInt(liner[3]);
			break;
		}
	}
	}
	bufferReader.close();
	inputFile.close();

	if (port == 0) {
		System.out
				.println("error occured while initializing port number");
		System.exit(1);
	}
	
	ServerSocket app = new ServerSocket(port);
	
			while (true) {
				
				Socket sock = app.accept();
				AppListen listener = new AppListen(sock);
				Thread l1 = new Thread(listener);
				l1.start();
			}
			
}catch(Exception e){
	e.printStackTrace();
}
		
	}
	
}
