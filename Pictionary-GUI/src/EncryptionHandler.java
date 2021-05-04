import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class EncryptionHandler {
	
	/**
	 * This method takes in the key as a string (the game name) and the data packet to be encrypted, 
	 * returning the encrypted byte array.
	 * @param key the game name
	 * @param packet the packet being sent out
	 * @return the encrypted DataPacket as a byte array
	 * @throws IOException fails to create object output stream
	 */
	public byte[] encode(String key, DataPacket packet) throws IOException {
		//This section converts the packet into a byte array using streams.
		ByteArrayOutputStream packArr = new ByteArrayOutputStream();
		ObjectOutputStream packOut = new ObjectOutputStream(packArr);
		packOut.writeObject(packet);
		packOut.flush();
		byte[] packByte = packArr.toByteArray();
		
		//We XOR the key over its length, then use that result as the next key
		//and save it. 
		byte[] keyByte = key.getBytes();
		byte[] results = new byte[packByte.length];
		
		int its = (packByte.length/keyByte.length)+(packByte.length%keyByte.length);
		for(int q = 0; q < its; q++) {
			//Create an offset for proper array navigation
			int offset = q*keyByte.length;
			//Save the key into the holder
			byte[] holder = Arrays.copyOfRange(keyByte, 0, keyByte.length);
			for(int w = 0; w < keyByte.length; w++) {
				//Check to make sure that offset+w (w being remainders) is still in range
				if(offset+w>=results.length) break;
				//XOR the holder byte at w and the packByte at offset+w
				results[offset+w] = (byte) (holder[w] ^ packByte[offset+w]);
				//Save the result into the key for when it is used next. 
				keyByte[w] = results[offset+w];
			}
		}
		
		return results;
	}
	
	/**
	 * This method takes in the encrypted byte array, with the key supplied by the client, and 
	 * returns the DataPacket.
	 * @param key the game name
	 * @param encryptedData the data coming in from the server
	 * @return The decrypted byte array as a DataPacket
	 * @throws IOException fails to create object input stream
	 * @throws ClassNotFoundException fails to create DataPacket
	 */
	public DataPacket decode(String key, byte[] encryptedData) throws IOException, ClassNotFoundException {
		//This section reverses the XOR operation performed in encode
		byte[] keyByte = key.getBytes();
		byte[] packBytes = new byte[encryptedData.length];
		
		int its = (encryptedData.length/keyByte.length)
				+(encryptedData.length%keyByte.length);
		for(int q = 0; q < its; q++) {
			//Create an offset for proper array navigation
			int offset = q*keyByte.length;
			//Save the key into the holder
			byte[] holder = Arrays.copyOfRange(keyByte, 0, keyByte.length);
			for(int w = 0; w < keyByte.length; w++) {
				//Check to make sure that offset+w (w being remainders) is still in range
				if(offset+w>=packBytes.length) break;
				//XOR the holder byte at w and the encryptedData byte at offset+w
				packBytes[offset+w] = (byte) (holder[w] ^ encryptedData[offset+w]);
				//Save the result into the key for when it is used next. 
				keyByte[w] = encryptedData[offset+w];
			}
		}

		//This section creates the object from the decrypted byte array using streams
		ByteArrayInputStream byteArr = new ByteArrayInputStream(packBytes);
		ObjectInputStream packIn = new ObjectInputStream(byteArr);
		DataPacket packet = (DataPacket) packIn.readObject();
		
		return packet;
	}

}
