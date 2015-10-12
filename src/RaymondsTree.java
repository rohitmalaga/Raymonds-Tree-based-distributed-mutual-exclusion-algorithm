import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class RaymondsTree {
    public static boolean inCS = true; 
    public static int tm = 0;
    public static int cstimes = 0;
    public static Queue<Integer> RequestQ = new LinkedList<Integer>();
    
    public static void generateRequest (int serial) throws InterruptedException {
    	//String val;
    	//int count = 7;
    	//System.out.println("Do you want to request for token? Yes/No");
   
    	//Scanner sn = new Scanner(System.in);
    	//val = sn.nextLine();
    	//if(val.contentEquals("Yes")){
    		while(tm > 0){
    			tm--;
    			//System.out.println("tm: "+tm);
    			SendReq(Utility.Nodemap.get(Caller.pointTo), Caller.serial);
    			
					Timer();
				
    		}
    		
    	//}else{
    		//System.out.println("I dont have the token");
    	//}
    }
    
    public static void Timer() throws InterruptedException {
		int min = 5 , max = 10 ;
		int time = 5 + (int)(Math.random() * ((max - min) + 1));
		//System.out.println("Entering random time "+time);
		//System.out.println("Time : ");
		for(int i =0 ;i < time ; i++)
		{
		Thread.sleep(100);
		//System.out.print(i+"\t");
		}
	}
    
    public static void SendToken(Socket sk,int sender){
		DataOutputStream dout;
		try {
			Caller.first = false;
			String sb = "";
			for(int i = 0;i < Utility.num;i++){
		        if(i < (Utility.num -1)){
		        	sb = sb + Client.vector[i] + ",";
		        }else{
		        	sb = sb + Client.vector[i];
		        }
				
			}
			
	    dout = new DataOutputStream(sk.getOutputStream());
		dout.writeUTF("From node " + sender + " Token transfer "+Messge.TOK+ " Vector "+sb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
  //This method is used to make request to enter critical section
  	public static void SendReq(Socket sk,int sender){
  		
  		DataOutputStream dout;
  		try {
  			
  				dout = new DataOutputStream(sk.getOutputStream());
  		  		dout.writeUTF("From node " + sender + " Critical Section Permission "+Messge.CS);	
  			
  	    
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	}
    
  	//CS-Enter Function
  	public static void SendCSReq(Socket sk,int sender){
  		
  		DataOutputStream dout;
  		try {
  			if(cstimes > 0){
  				--cstimes;
  				dout = new DataOutputStream(sk.getOutputStream());
  		  		dout.writeUTF("From node " + sender +" "+Messge.Enter);
  			}
  				
  			//}
  	    
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	}
  	
  	
  	//Not Used function
	public void raymondsAlgo()throws InterruptedException,IOException{
		if(Caller.Token == true){
			Application.ExecuteCS(Caller.serial);
		}else{
			//Insert request into FIFO Queue
			RequestQ.add(Integer.parseInt(Utility.tem[2]));
			//Send a request for token towards token holder
		    SendReq(Utility.Nodemap.get(Listen.AckList.get(0)), Caller.serial);
		}
	}
	
}
