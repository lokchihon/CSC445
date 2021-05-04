import java.io.IOException;
import java.util.ArrayList;

public class EncryptionHandlerTester {
	
	static ArrayList<DrawData> theDs = new ArrayList<>();
	static ArrayList<String> theMs = new ArrayList<>();
	
	public static void main(String[] args) {
		
		for(int q = 0; q < 10; q++) {
			theDs.add(new DrawData(new int[] {q, q+1, q+2, q+3}, q+4, q+5));
			theMs.add("String number "+q+".");
		}
		
		DataPacket theD = new DataPacket(theDs, 30, theMs, 1, "This is a string.", "So is this, hello world!");
		EncryptionHandler theE = new EncryptionHandler();
		
		try {
			String key = "oogabooga";
			byte[] encrypted = theE.encode(key, theD);
			DataPacket theDReturns = theE.decode(key, encrypted);
			theDs = theDReturns.getDrawData();
			theMs = theDReturns.getMessages();
			for(int q = 0; q < 10; q++) {
				System.out.println("String "+q+": "+theMs.get(q));
				int[] points = theDs.get(q).getPoints();
				System.out.println("DrawData "+q+": "+points[0]+","+points[1]+","+points[2]+","+points[3]
						+"; "+theDs.get(q).getBrushSize()+"; "+theDs.get(q).getColor());
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
