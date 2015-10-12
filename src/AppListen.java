import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class AppListen implements Runnable {

Socket SOCK;
	static String str;
	public AppListen(Socket sock) {
		this.SOCK = sock;
	}
	
	public static void RCVMsg(Socket sk) throws InterruptedException{
		String[] temp;
		try {
		DataInputStream din = new DataInputStream(sk.getInputStream());
		str = din.readUTF();
		temp = str.split(" ");
		
		
		if(str.contains("Enter")){
			
			int num = Integer.parseInt(temp[2]);
			System.out.println("	>> Received msg: " + str);	
			Application.ExecuteCS(num);
		}
		
		
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
public void run() {
		
		try {
			
			while (true) {
	RCVMsg(SOCK);
				
			}
		}catch (Exception e) {
				e.printStackTrace();
			}
			
	}
	
}
