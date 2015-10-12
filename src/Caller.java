import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class Caller {
	static int serial;
	static int setup;
	static int count;
	public static boolean flag;
	public static boolean isFirst;
	public static boolean Token;
	public static int pointTo;
	public static boolean first = true;

	
	public static void main(String[] args) throws InterruptedException,
	IOException {
		
		
		// Reading node number from user
	
//System.out.println("Enter name of the node");

//Scanner in = new Scanner(System.in);
//int s = in.nextInt();
int s = Integer.parseInt(args[0]);
System.out.println("I am Node "+s);
serial = s;

// Creating server class thread.
Server s1 = new Server();
Thread t1 = new Thread(s1);
t1.start();
// System.out.println("Successfully created server thread");

Thread.sleep(45000);

Application a1 = new Application();
Thread t3 = new Thread(a1);
t3.start();

Thread.sleep(45000);

// Calling client method1

Client c1 = new Client();
Thread t2 = new Thread(c1);
t2.start();

Thread.sleep(35000);




if(Listen.AckList.size() != 0){
	while(Listen.TreeNode.contains(Listen.AckList.get(0))){
		System.out.println("Before "+Listen.AckList);
		Listen.AckList.remove(Listen.AckList.get(0));
		System.out.println("After "+Listen.AckList);
		if(Listen.AckList.size() == 0){
			break;
		}
	}
	if(Listen.AckList.size() == 0){
System.out.println("I am the root node of Spanning Tree");
Token = true;
pointTo = Caller.serial;
}else{
	System.out.println("My parent is "+Listen.AckList.get(0));
	Token = false;
	pointTo = Listen.AckList.get(0);
}
}else{
	System.out.println("I am the root");
	Token = true;
	pointTo = Caller.serial;
}

Thread.sleep(5000);
System.out.println("The child neighbors are "+Listen.TreeNode);

Thread.sleep(15000);

Client.vector[serial - 1] = 0;

//Begining the Raymonds Algorithm
RaymondsTree.generateRequest(serial);

	}

}


