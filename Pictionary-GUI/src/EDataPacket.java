import java.io.Serializable;

public class EDataPacket implements Serializable {

    private byte[] data;

    public EDataPacket(byte[] data){
        this.data = data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData(){
        return this.data;
    }
}
