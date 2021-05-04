import java.util.ArrayList;

public class EncryptionHandler {
	
	/**
	 * This method takes in the key as a string (the game name) and the data packet to be encrypted, 
	 * returning the encrypted byte array.
	 * @param key the game name
	 * @param packet the packet being sent out
	 * @return the encrypted DataPacket as a byte array
	 */
	public byte[] encode(String key, DataPacket packet) {
		//TODO do this
		return new byte[] {};
	}
	
	/**
	 * This method takes in the encrypted byte array, with the key supplied by the client, and 
	 * returns the DataPacket.
	 * @param key the game name
	 * @param encryptedData the data coming in from the server
	 * @return The decrypted byte array as a DataPacket
	 */
	public DataPacket decode(String key, byte[] encryptedData) {
		//TODO do this
		return new DataPacket(new ArrayList<DrawData>(), 0, new ArrayList<String>(), 0, "", "");
	}

}
