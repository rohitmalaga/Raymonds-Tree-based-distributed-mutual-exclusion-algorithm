
public class Client implements Runnable{
	public static int[] vector;
	public void run() {	
		try {
			Utility.NodeEntries();
			Utility.AppEntries();
			vector = new int[Utility.num];
			Thread.sleep(15000);
			SpanningTree.Checkforneighbor();
			Thread.sleep(15000);
	} catch (Exception e) {
		e.printStackTrace();
	}
				}

}
